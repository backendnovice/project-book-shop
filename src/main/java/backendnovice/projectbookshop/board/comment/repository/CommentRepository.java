/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-21
 * @desc      : A comment-related repository interface. that manage and execute queries for Comment table.
 * @changelog :
 * 2023-08-20 - backendnovice@gmail.com - create new file.
 * 2023-08-21 - backendnovice@gmail.com - add select query methods.
 */

package backendnovice.projectbookshop.board.comment.repository;

import backendnovice.projectbookshop.board.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByArticleId(Long articleId, Pageable pageable);
}
