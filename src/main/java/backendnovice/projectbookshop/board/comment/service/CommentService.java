/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-21
 * @desc      : A comment-related service interface. that define business logic for comment.
 * @changelog :
 * 2023-08-20 - backendnovice@gmail.com - create new file.
 * 2023-08-21 - backendnovice@gmail.com - add write, delete method.
 *                                      - add method description annotation.
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
     * @exception IllegalArgumentException
     *      Throwable exception when empty comment dto detected.
     */
    void write(CommentDTO commentDTO);

    /**
     * Delete comment that matches id of parameter.
     * @param commentId
     *      Comment id.
     * @exception NoSuchElementException
     *      Throwable exception when no comment found to delete.
     */
    void delete(Long commentId);
}
