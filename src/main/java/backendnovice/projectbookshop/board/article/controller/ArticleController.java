/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-07-25
 * @modified : 2023-09-04
 * @desc     : An article-related controller class. that maps URIs to handle requests from the view layer.
 */

package backendnovice.projectbookshop.board.article.controller;

import backendnovice.projectbookshop.board.article.dto.ArticleDTO;
import backendnovice.projectbookshop.global.dto.PageDTO;
import backendnovice.projectbookshop.board.article.service.ArticleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/board/article")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    private String message;

    /**
     * Get article write page URI.
     * @return
     *      Article write URI.
     */
    @GetMapping("/write")
    public String getWritePage() {
        return "article/write";
    }

    /**
     * Get article list page URI with pageable data.
     * @param model
     *      Contains data to transfer view layer. include pagination and articles data.
     * @param pageDTO
     *      Contains data related to search options. (tag, keyword)
     * @param pageable
     *      Pageable object. default page size is 10.
     * @return
     *      Article list URI.
     */
    @GetMapping("/list")
    public String getListPage(Model model, PageDTO pageDTO, Pageable pageable) {
        String tag = pageDTO.getTag();
        String keyword = pageDTO.getKeyword();
        Page<ArticleDTO> result = null;

        try {
            if(tag == null || keyword == null) {
                result = articleService.searchAll(pageable);
            }else {
                result = articleService.searchByTags(pageDTO, pageable);
            }
        }catch (NoSuchElementException e) {
            message = "Cannot found any articles.";
            result = articleService.searchAll(pageable);
        }catch (IllegalArgumentException e) {
            message = "Incorrect search tag detected.";
            result = articleService.searchAll(pageable);
        }

        PageDTO search = new PageDTO(result);
        model.addAttribute("dto", result);
        model.addAttribute("search", search);
        model.addAttribute("message", message);

        return "article/list";
    }

    /**
     * Get article read page URI by id. update views if user hasn't visited in a day.
     * @param model
     *      Contains article data to transfer View layer. (Read page)
     * @param articleId
     *      Article id.
     * @param request
     *      Request cookies to check article visited today already.
     * @param response
     *      Response new cookie when request cookie not exists.
     * @return
     *      If there is an article matches with id, will return read URI. if not, will return list redirection URI.
     */
    @GetMapping("/read")
    public String getReadPage(Model model, RedirectAttributes redirectAttributes, @RequestParam(value = "id", required = false) Long articleId
            , HttpServletRequest request, HttpServletResponse response) {
        if(articleId == null) {
            message = "Cannot read article with blank id.";
            redirectAttributes.addFlashAttribute("message", message);

            return "redirect:/article/list";
        }else {
            try {
                ArticleDTO result = articleService.read(articleId);
                articleService.updateView(articleId, request, response);
                result.setViews(result.getViews() + 1);
                model.addAttribute("dto", result);

                return "article/read";
            }catch (NoSuchElementException e) {
                message = "No article found with id.";
                redirectAttributes.addFlashAttribute("message", message);

                return "redirect:/article/list";
            }
        }
    }

    /**
     * Get article modify page URI by id.
     * @param model
     *      Contains article data to transfer View layer. (Modify page)
     * @param articleId
     *      Article id.
     * @return
     *      If there is an article matches with id, will return modify URI. if not, will return list redirection URI.
     */
    @GetMapping("/modify")
    public String getModifyPage(Model model, RedirectAttributes redirectAttributes, @RequestParam(value = "id", required = false) Long articleId) {
        if(articleId == null) {
            message = "Cannot modify article with blank id.";
            redirectAttributes.addFlashAttribute("message", message);

            return "redirect:/article/list";
        }else {
            try {
                ArticleDTO result = articleService.read(articleId);
                model.addAttribute("dto", result);

                return "article/modify";
            }catch (NoSuchElementException e) {
                message = "No article found with id.";
                redirectAttributes.addFlashAttribute("message", message);

                return "redirect:/article/list";
            }
        }
    }

    /**
     * Call article service method to process register new article. and return article read redirection URI.
     * @param articleDTO
     *      Article transfer data object.
     * @return
     *      If registration process succeeds, will return read URI. if process fails, will return write redirection URI.
     */
    @PostMapping("/write")
    public String callWriteProcessService(RedirectAttributes redirectAttributes, ArticleDTO articleDTO) {
        if(articleDTO.getTitle() == null || articleDTO.getContent() == null || articleDTO.getWriter() == null) {
            message = "cannot write an empty article.";
            redirectAttributes.addFlashAttribute("message", message);

            return "redirect:/article/write";
        }else {
            long result = articleService.write(articleDTO);
            message = "article registration succeed.";
            redirectAttributes.addFlashAttribute("message", message);

            return "redirect:/article/read?id=" + result;
        }
    }

    /**
     * Call article service method to process modify article. and return article read redirection URI.
     * @param articleDTO
     *      Article transfer data object.
     * @return
     *      If modification process succeeds, will return read URI. if process fails, will return list redirection URI.
     */
    @PostMapping("/modify")
    public String callModifyProcessService(RedirectAttributes redirectAttributes, ArticleDTO articleDTO) {
        try {
            long result = articleService.modify(articleDTO);
            message = "article modification succeed.";
            redirectAttributes.addFlashAttribute("message", message);

            return "redirect:/article/read?id=" + result;
        }catch (NoSuchElementException e) {
            message = "no article found with id.";
            redirectAttributes.addFlashAttribute("message", message);

            return "redirect:/article/list";
        }
    }

    /**
     * Call article service to process delete article. and return article list redirection URI.
     * @param articleDTO
     *      Article transfer data object.
     * @return
     *      Article list redirection URI.
     */
    @PostMapping("/delete")
    public String callDeleteProcessService(RedirectAttributes redirectAttributes, ArticleDTO articleDTO) {
        try {
            articleService.delete(articleDTO.getId());
            message = "article deletion succeed.";
        }catch(NoSuchElementException e) {
            message = "cannot found any article to delete.";
        }

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/article/list";
    }
}