/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-07-25
 * @modified : 2023-09-18
 * @desc     : 게시글 관련 POST, GET 요청을 핸들링하여 URI와 데이터를 제공하는 컨트롤러 클래스.
 */

package backendnovice.projectbookshop.board.article.controller;

import backendnovice.projectbookshop.board.article.dto.ArticleDTO;
import backendnovice.projectbookshop.global.dto.PaginationDTO;
import backendnovice.projectbookshop.board.article.service.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    private String message;

    /**
     * 게시글 등록 엔드포인트를 반환한다.
     * @return
     *      게시글 등록 URI
     */
    @GetMapping("/write")
    public String getWritePage() {
        return "article/write";
    }

    /**
     * 검색 옵션, 페이지네이션 데이터를 받아 게시글 목록 엔드포인트를 반환한다.
     * @param model
     *      게시글 DTO 또는 메시지를 포함하는 뷰 전달 객체
     * @param pageDTO
     *      검색 옵션을 담는 DTO 객체 (키워드, 태그)
     * @param pageable
     *      페이징 옵션을 담는 페이지네이션 객체
     * @return
     *      게시글 목록 URI
     */
    @GetMapping("/list")
    public String getListPage(Model model, PaginationDTO pageDTO, Pageable pageable) {
        final String TAG = pageDTO.getTag();
        final String KEYWORD = pageDTO.getKeyword();
        Page<ArticleDTO> data = null;
        PaginationDTO paginationDTO = new PaginationDTO();

        try {
            if(TAG == null || KEYWORD == null) {
                data = articleService.searchAll(pageable);
            }else {
                data = articleService.searchByTags(pageDTO, pageable);
            }
            paginationDTO = new PaginationDTO(data);
        } catch (NoSuchElementException e) {
            message = "게시글을 찾을 수 없습니다.";
            model.addAttribute("message", message);
        } catch (IllegalArgumentException e) {
            message = "올바르지 않은 요청입니다.";
            model.addAttribute("message", message);
        }

        model.addAttribute("data", data);
        model.addAttribute("pagination", paginationDTO);
        return "article/list";
    }

    /**
     * ID에 해당하는 게시글 조회 엔드포인트를 반환한다.
     * @param model
     *      게시글 DTO 또는 메시지를 포함하는 뷰 전달 객체
     * @param id
     *      게시글 ID
     * @return
     *      결과 URI
     */
    @GetMapping("/read")
    public String getReadPage(Model model, RedirectAttributes redirectAttributes
            , @RequestParam(value = "id", required = false) Long id) {
        if (id == null) {
            message = "올바르지 않은 요청입니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/article/list";
        }
        try {
            ArticleDTO result = articleService.read(id);
            articleService.updateViews(id);
            result.setViews(result.getViews() + 1);
            model.addAttribute("data", result);
            return "article/read";
        } catch (NoSuchElementException e) {
            message = "조회할 게시글이 존재하지 않습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/article/list";
        }
    }

    /**
     * ID에 해당하는 게시글 수정 엔드포인트를 반환한다.<br>
     * @param model
     *      게시글 DTO 또는 메시지를 포함하는 뷰 전달 객체
     * @param id
     *      게시글 ID
     * @return
     *      결과 URI
     */
    @GetMapping("/modify")
    public String getModifyPage(Model model, RedirectAttributes redirectAttributes, @RequestParam(value = "id", required = false) Long id) {
        if (id == null) {
            message = "올바르지 않은 요청입니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/article/list";
        }
        try {
            ArticleDTO result = articleService.read(id);
            model.addAttribute("data", result);
            return "article/modify";
        } catch (NoSuchElementException e) {
            message = "수정할 게시글이 존재하지 않습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/article/list";
        }
    }

    /**
     * 게시글 등록 서비스 메소드를 호출하고 엔드포인트와 메시지를 반환한다.<br>
     * @param articleDTO
     *      게시글 DTO
     * @param redirectAttributes
     *      결과 메시지
     * @return
     *      결과 URI
     */
    @PostMapping("/write")
    public String callWriteProcessService(RedirectAttributes redirectAttributes, ArticleDTO articleDTO) {
        if (articleDTO.getTitle() == null || articleDTO.getContent() == null || articleDTO.getWriter() == null) {
            message = "비어있는 게시글을 등록할 수 없습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/article/write";
        } else {
            long result = articleService.write(articleDTO);
            message = "게시글 등록이 완료되었습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/article/read?id=" + result;
        }
    }

    /**
     * 게시글 수정 서비스 메소드를 호출하고 엔드포인트와 메시지를 반환한다.
     * @param articleDTO
     *      게시글 DTO
     * @param redirectAttributes 
     *      결과 메시지
     * @return
     *      결과 URI
     */
    @PostMapping("/modify")
    public String callModifyProcessService(RedirectAttributes redirectAttributes, ArticleDTO articleDTO) {
        try {
            long result = articleService.modify(articleDTO);
            message = "게시글 수정이 완료되었습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/article/read?id=" + result;
        } catch (NoSuchElementException e) {
            message = "수정할 게시글을 찾지 못했습니다.";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/article/list";
        }
    }

    /**
     * 게시글 삭제 서비스 메소드를 호출하고 게시글 목록 엔드포인트와 메시지를 반환한다.
     * @param articleDTO
     *      게시글 DTO
     * @param redirectAttributes 
     *      결과 메시지
     * @return
     *      결과 URI
     */
    @PostMapping("/delete")
    public String callDeleteProcessService(RedirectAttributes redirectAttributes, ArticleDTO articleDTO) {
        try {
            articleService.delete(articleDTO.getId());
            message = "게시글 삭제가 완료되었습니다.";
        } catch (NoSuchElementException e) {
            message = "삭제할 게시글을 찾지 못했습니다.";
        }
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/article/list";
    }
}