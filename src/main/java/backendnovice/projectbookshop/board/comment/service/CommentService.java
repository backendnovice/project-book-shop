/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-08-20
 * @modified : 2023-09-04
 * @desc     : A comment-related service interface. that define business logic for comment.
 */

package backendnovice.projectbookshop.board.comment.service;

import backendnovice.projectbookshop.board.comment.dto.CommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.NoSuchElementException;

public interface CommentService {
    /**
     * Select comments with article id.
     * @param articleId
     *      Article id.
     * @param pageable
     *      Pageable object.
     * @return
     *      Page object that includes the found articles and pagination.
     */
    Page<CommentDTO> getComments(Long articleId, Pageable pageable);

    /**
     * Create comment with DTO of parameter.
     * @param commentDTO
     *      CommentDTO object.
     */
    void write(CommentDTO commentDTO);

    /**
     * Modify comment that matches id of dto.
     * @param commentDTO
     *      Comment data transfer object including new content.
     * @exception NoSuchElementException
     *      Throwable exception when no comment found to modify.
     */
    void modify(CommentDTO commentDTO);

    /**
     * Delete comment that matches id of parameter.
     * @param commentId
     *      Comment id.
     * @exception NoSuchElementException
     *      Throwable exception when no comment found to delete.
     */
    void delete(Long commentId);
}
