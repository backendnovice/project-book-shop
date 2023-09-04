/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-07-25
 * @modified : 2023-09-04
 * @desc     : An article-related service interface. that define business logic for article.
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
