/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-16
 * @desc      : An article-related repository interface. that manage and execute queries for Article table.
 * @changelog :
 * 2023-07-25 - backendnovice@gmail.com - create new file.
 *                                      - add select query methods.
 * 2023-08-01 - backendnovice@gmail.com - add update view count query method.
 * 2023-08-13 - backendnovice@gmail.com - change filename to ArticleRepository.
 * 2023-08-16 - backendnovice@gmail.com - add description annotation.
 */

package backendnovice.projectbookshop.board.article.repository;

import backendnovice.projectbookshop.board.article.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    /**
     * Search articles with title, case-insensitive.
     * @param title
     *      Article title
     * @param pageable
     *      Pageable object
     * @return
     *      Page object that include found articles
     */
    Page<Article> findAllByTitleContainsIgnoreCase(String title, Pageable pageable);

    /**
     * Search articles with content, case-insensitive.
     * @param content
     *      Article content
     * @param pageable
     *      Pageable object
     * @return
     *      Page object that include found articles
     */
    Page<Article> findAllByContentContainsIgnoreCase(String content, Pageable pageable);

    /**
     * Search articles with writer, case-insensitive.
     * @param writer
     *      Article writer
     * @param pageable
     *      Pageable object
     * @return
     *      Page object that include found articles
     */
    Page<Article> findAllByWriterContainsIgnoreCase(String writer, Pageable pageable);

    /**
     * Update articles views(view count) by id.
     * @param id
     *      Article id
     */
    @Modifying
    @Query("UPDATE Article b SET b.views = b.views + 1 WHERE b.id = :id")
    void updateViewById(@Param("id") Long id);
}
