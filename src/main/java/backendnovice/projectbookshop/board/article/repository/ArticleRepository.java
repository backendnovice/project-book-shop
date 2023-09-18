/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-07-25
 * @modified : 2023-09-18
 * @desc     : 게시글 관련 쿼리를 관리하는 레포지토리 인터페이스.
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
     * 전체 게시글과 댓글 개수를 검색하고 페이지 타입 객체로 반환한다.
     * @param pageable
     *      페이지네이션 객체
     * @return
     *      검색 결과
     */
    @Query("SELECT a, COUNT(c) FROM Article a LEFT JOIN a.comments c GROUP BY a")
    Page<Object[]> findAllWithCommentCount(Pageable pageable);

    /**
     * 제목을 포함하는 게시글과 댓글 개수를 검색하고 페이지 타입 객체로 반환한다.
     * @param title
     *      게시글 제목
     * @param pageable
     *      페이지네이션 객체
     * @return
     *      검색 결과
     */
    @Query("SELECT a, COUNT(c) FROM Article a LEFT JOIN a.comments c WHERE LOWER(a.title) LIKE %:title% GROUP BY a.id")
    Page<Object[]> findAllByTitleWithCommentCount(@Param("title") String title, Pageable pageable);

    /**
     * 내용을 포함하는 게시글과 댓글 개수를 검색하고 페이지 타입 객체로 반환한다.
     * @param content
     *      게시글 내용
     * @param pageable
     *      페이지네이션 객체
     * @return
     *      검색 결과
     */
    @Query("SELECT a, COUNT(c) FROM Article a LEFT JOIN a.comments c WHERE LOWER(a.content) LIKE %:content% GROUP BY a.id")
    Page<Object[]> findAllByContentWithCommentCount(@Param("content") String content, Pageable pageable);

    /**
     * 작성자명을 포함하는 게시글과 댓글 개수를 검색하고 페이지 타입 객체로 반환한다.
     * @param writer
     *      게시글 작성자
     * @param pageable
     *      페이지네이션 객체
     * @return
     *      검색 결과
     */
    @Query("SELECT a, COUNT(c) FROM Article a LEFT JOIN a.comments c WHERE LOWER(a.writer) LIKE %:writer% GROUP BY a.id")
    Page<Object[]> findAllByWriterWithCommentCount(@Param("writer") String writer, Pageable pageable);

    /**
     * ID와 일치하는 게시글의 조회수(views)를 증가시킨다.
     * @param id
     *      게시글 ID
     */
    @Modifying
    @Query("UPDATE Article b SET b.views = b.views + 1 WHERE b.id = :id")
    void updateViewsById(@Param("id") Long id);
}
