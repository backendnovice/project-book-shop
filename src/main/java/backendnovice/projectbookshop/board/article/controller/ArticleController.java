package backendnovice.projectbookshop.board.article.controller;

import backendnovice.projectbookshop.board.article.dto.ArticleDTO;
import backendnovice.projectbookshop.global.dto.SearchDTO;
import backendnovice.projectbookshop.board.article.service.ArticleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/write")
    public String getWritePage() {
        return "article/write";
    }

    @GetMapping("/list")
    public String getListPage(Model model, SearchDTO searchDTO, @PageableDefault(size = 5) Pageable pageable) {
        Page<ArticleDTO> result = articleService.search(searchDTO, pageable);
        SearchDTO search = new SearchDTO(result);

        model.addAttribute("dto", result);
        model.addAttribute("search", search);

        return "article/list";
    }

    @GetMapping("/read")
    public String getReadPage(Model model, @RequestParam("id") Long articleId, HttpServletRequest request
            , HttpServletResponse response) {
        articleService.updateView(articleId, request, response);
        ArticleDTO result = articleService.read(articleId);
        model.addAttribute("dto", result);

        return "article/read";
    }

    @GetMapping("/modify")
    public String getModifyPage(Model model, @RequestParam("id") Long id) {
        ArticleDTO result = articleService.read(id);
        model.addAttribute("dto", result);

        return "article/modify";
    }

    @PostMapping("/write")
    public String writeProcess(ArticleDTO articleDTO) {
        long result = articleService.write(articleDTO);

        return "redirect:/article/read?id=" + result;
    }

    @PostMapping("/modify")
    public String modifyProcess(ArticleDTO articleDTO) {
        long result = articleService.modify(articleDTO);

        return "redirect:/article/read?id=" + result;
    }

    @PostMapping("/delete")
    public String deleteProcess(ArticleDTO articleDTO) {
        articleService.delete(articleDTO.getId());

        return "redirect:/article/list";
    }
}