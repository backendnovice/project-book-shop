/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-07-25
 * @modified : 2023-09-04
 * @desc     : An article-related repository interface. that manage and execute queries for Article table.
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
     * Find articles and comment counts without any options.
     * @param pageable
     *      Pageable object for pagination data.
     * @return
     *      Page object that include founding result.
     */
    @Query("SELECT a, COUNT(c) FROM Article a LEFT JOIN a.comments c GROUP BY a")
    Page<Object[]> findAllWithCommentCount(Pageable pageable);

    /**
     * Find articles and comment counts with title.
     * @param title
     *      Article title.
     * @param pageable
     *      Pageable object for pagination data.
     * @return
     *      Page object that include founding result.
     */
    @Query("SELECT a, COUNT(c) FROM Article a LEFT JOIN a.comments c WHERE LOWER(a.title) LIKE %:title% GROUP BY a.id")
    Page<Object[]> findAllByTitleWithCommentCount(@Param("title") String title, Pageable pageable);

    /**
     * Find articles and comment counts with title.
     * @param content
     *      Article content.
     * @param pageable
     *      Pageable object for pagination data.
     * @return
     *      Page object that include founding result.
     */
    @Query("SELECT a, COUNT(c) FROM Article a LEFT JOIN a.comments c WHERE LOWER(a.content) LIKE %:content% GROUP BY a.id")
    Page<Object[]> findAllByContentWithCommentCount(@Param("content") String content, Pageable pageable);

    /**
     * Find articles and comment counts with title.
     * @param writer
     *      Article writer.
     * @param pageable
     *      Pageable object for pagination data.
     * @return
     *      Page object that include founding result.
     */
    @Query("SELECT a, COUNT(c) FROM Article a LEFT JOIN a.comments c WHERE LOWER(a.writer) LIKE %:writer% GROUP BY a.id")
    Page<Object[]> findAllByWriterWithCommentCount(@Param("writer") String writer, Pageable pageable);

    /**
     * Update articles views(view count) by id.
     * @param id
     *      Article id
     */
    @Modifying
    @Query("UPDATE Article b SET b.views = b.views + 1 WHERE b.id = :id")
    void updateViewsById(@Param("id") Long id);
}
