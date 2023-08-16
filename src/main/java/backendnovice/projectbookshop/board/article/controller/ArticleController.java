/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-16
 * @desc      : An article-related controller class. that maps URIs to handle requests from the view layer.
 * @changelog :
 * 2023-07-25 - backendnovice@gmail.com - create new file.
 * 2023-07-28 - backendnovice@gmail.com - add search, write handle method.
 * 2023-07-29 - backendnovice@gmail.com - modify search handle method.
 * 2023-07-30 - backendnovice@gmail.com - add modify, delete handle method.
 *                                      - modify write handle method.
 * 2023-08-01 - backendnovice@gmail.com - add update view in read handle method.
 * 2023-08-13 - backendnovice@gmail.com - change filename to ArticleController.
 * 2023-08-16 - backendnovice@gmail.com - add description annotation.
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

@Controller
@RequestMapping("/board/article")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

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
        Page<ArticleDTO> result = articleService.search(pageDTO, pageable);
        PageDTO search = new PageDTO(result);

        model.addAttribute("dto", result);
        model.addAttribute("search", search);

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
     *      Article read URI.
     */
    @GetMapping("/read")
    public String getReadPage(Model model, @RequestParam("id") Long articleId, HttpServletRequest request
            , HttpServletResponse response) {
        articleService.updateView(articleId, request, response);
        ArticleDTO result = articleService.read(articleId);
        model.addAttribute("dto", result);

        return "article/read";
    }

    /**
     * Get article modify page URI by id.
     * @param model
     *      Contains article data to transfer View layer. (Modify page)
     * @param articleId
     *      Article id.
     * @return
     *      Article modify URI.
     */
    @GetMapping("/modify")
    public String getModifyPage(Model model, @RequestParam("id") Long articleId) {
        ArticleDTO result = articleService.read(articleId);
        model.addAttribute("dto", result);

        return "article/modify";
    }

    /**
     * Call article service method to process register new article. and return article read redirection URI.
     * @param articleDTO
     *      Article transfer data object.
     * @return
     *      Article read URI. (Redirection)
     */
    @PostMapping("/write")
    public String callWriteProcessService(ArticleDTO articleDTO) {
        long result = articleService.write(articleDTO);

        return "redirect:/article/read?id=" + result;
    }

    /**
     * Call article service method to process modify article. and return article read redirection URI.
     * @param articleDTO
     *      Article transfer data object.
     * @return
     *      Article read URI. (Redirection)
     */
    @PostMapping("/modify")
    public String callModifyProcessService(ArticleDTO articleDTO) {
        long result = articleService.modify(articleDTO);

        return "redirect:/article/read?id=" + result;
    }

    /**
     * Call article service to process delete article. and return article list redirection URI.
     * @param articleDTO
     *      Article transfer data object.
     * @return
     *      Article list URI. (Redirection)
     */
    @PostMapping("/delete")
    public String callDeleteProcessService(ArticleDTO articleDTO) {
        articleService.delete(articleDTO.getId());

        return "redirect:/article/list";
    }
}