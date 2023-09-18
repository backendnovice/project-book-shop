/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-08-20
 * @modified : 2023-09-18
 * @desc     : 댓글 관련 쿼리를 관리하는 레포지토리 인터페이스.
 */

package backendnovice.projectbookshop.board.comment.repository;

import backendnovice.projectbookshop.board.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * 게시글 ID와 일치하는 댓글들을 조회하고 페이지 객체를 반환한다.
     * @param articleId
     *      게시글 ID
     * @param pageable
     *      페이지네이션 객체
     * @return
     *      검색 결과
     */
    Page<Comment> findAllByArticleId(Long articleId, Pageable pageable);
}
