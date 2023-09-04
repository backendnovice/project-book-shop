/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-08-20
 * @modified : 2023-09-04
 * @desc     : A comment-related repository interface. that manage and execute queries for Comment table.
 */

package backendnovice.projectbookshop.board.comment.repository;

import backendnovice.projectbookshop.board.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByArticleId(Long articleId, Pageable pageable);
}
