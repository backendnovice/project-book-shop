/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-16
 * @desc      : An article-related service interface. that define business logic for article.
 * @changelog :
 * 2023-07-25 - backendnovice@gmail.com - create new file.
 * 2023-07-26 - backendnovice@gmail.com - integrate search method.
 * 2023-07-29 - backendnovice@gmail.com - rename search method.
 * 2023-07-30 - backendnovice@gmail.com - add write, modify, delete method.
 * 2023-08-01 - backendnovice@gmail.com - add update view count method.
 * 2023-08-13 - backendnovice@gmail.com - change filename to ArticleService.
 * 2023-08-16 - backendnovice@gmail.com - add description annotation.
 */

package backendnovice.projectbookshop.board.article.service;

import backendnovice.projectbookshop.board.article.dto.ArticleDTO;
import backendnovice.projectbookshop.global.dto.PageDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {
    Page<ArticleDTO> search(PageDTO pageDTO, Pageable pageable);

    Long write(ArticleDTO articleDTO);

    Long modify(ArticleDTO articleDTO);

    void delete(Long id);

    void updateView(Long articleId, HttpServletRequest request, HttpServletResponse response);

    ArticleDTO read(Long id);
}
