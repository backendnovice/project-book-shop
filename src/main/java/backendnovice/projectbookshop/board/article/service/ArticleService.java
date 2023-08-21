/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-22
 * @desc      : An article-related service interface. that define business logic for article.
 * @changelog :
 * 2023-07-25 - backendnovice@gmail.com - create new file.
 * 2023-07-26 - backendnovice@gmail.com - integrate search method.
 * 2023-07-29 - backendnovice@gmail.com - rename search method.
 * 2023-07-30 - backendnovice@gmail.com - add write, modify, delete method.
 * 2023-08-01 - backendnovice@gmail.com - add update view count method.
 * 2023-08-13 - backendnovice@gmail.com - change filename to ArticleService.
 * 2023-08-16 - backendnovice@gmail.com - add description annotation.
 * 2023-08-21 - backendnovice@gmail.com - add method description annotation.
 * 2023-08-22 - backendnovice@gmail.com - separate search method.
 */

package backendnovice.projectbookshop.board.article.service;

import backendnovice.projectbookshop.board.article.dto.ArticleDTO;
import backendnovice.projectbookshop.global.dto.PageDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.NoSuchElementException;

public interface ArticleService {
    /**
     * Execute article selection query without any options.
     * @param pageable
     *      Pageable object.
     * @return
     *      Page object that include found articles.
     * @exception NoSuchElementException
     *      Throwable exception when cannot found any articles.
     */
    Page<ArticleDTO> searchAll(Pageable pageable);

    /**
     * Select articles with search tag and keyword.
     * @param pageDTO
     *      Page data transfer object including tag and keyword.
     * @param pageable
     *      Pageable object.
     * @return
     *      Page object that includes the found articles and pagination.
     * @exception IllegalArgumentException
     *      Throwable exception when incorrect search tag detected.
     * @exception NoSuchElementException
     *      Throwable exception when cannot found any articles.
     */
    Page<ArticleDTO> searchByTags(PageDTO pageDTO, Pageable pageable);

    /**
     * Create article with DTO of parameter.
     * @param articleDTO
     *      Article data transfer object.
     * @return
     *      Created article id.
     * @exception IllegalArgumentException
     *      Throwable exception when empty article dto detected.
     */
    Long write(ArticleDTO articleDTO);

    /**
     * Modify article that matches id of dto.
     * @param articleDTO
     *      Article data transfer object including new data.
     * @return
     *      Modified article id.
     * @exception NoSuchElementException
     *      Throwable exception when no article found with id.
     */
    Long modify(ArticleDTO articleDTO);

    /**
     * Select article that matches id of parameter.
     * @param id
     *      Article id.
     * @return
     *      Found article data transfer object.
     * @exception NoSuchElementException
     *      Throwable exception when cannot found any article with id.
     */
    ArticleDTO read(Long id);

    /**
     * Delete article that matches id of parameter.
     * @param id
     *      Article id.
     * @exception NoSuchElementException
     *      Throwable exception when no article found to delete.
     */
    void delete(Long id);

    /**
     * Update view count after check cookies for prevent increasing views redundancy.
     * @param articleId
     *      Article id.
     * @param request
     *      Request cookies to check article visited today already.
     * @param response
     *      Response new cookie when request cookie not exists.
     */
    void updateView(Long articleId, HttpServletRequest request, HttpServletResponse response);
}
