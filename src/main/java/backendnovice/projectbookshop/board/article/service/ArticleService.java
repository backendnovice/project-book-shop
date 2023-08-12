package backendnovice.projectbookshop.board.article.service;

import backendnovice.projectbookshop.board.article.dto.ArticleDTO;
import backendnovice.projectbookshop.global.dto.SearchDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {
    Page<ArticleDTO> search(SearchDTO searchDTO, Pageable pageable);

    Long write(ArticleDTO articleDTO);

    Long modify(ArticleDTO articleDTO);

    void delete(Long id);

    void updateView(Long articleId, HttpServletRequest request, HttpServletResponse response);

    ArticleDTO read(Long id);
}
