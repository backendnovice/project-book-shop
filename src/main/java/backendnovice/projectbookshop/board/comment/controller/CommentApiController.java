/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-21
 * @desc      : An article-related controller class. that provide API when received requests from the view layer.
 * @changelog :
 * 2023-08-20 - backendnovice@gmail.com - create new file.
 * 2023-08-21 - backendnovice@gmail.com - add write, delete handle methods.
 * 2023-08-22 - backendnovice@gmail.com - apply exception handling.
 */

package backendnovice.projectbookshop.board.comment.controller;

import backendnovice.projectbookshop.board.comment.dto.CommentDTO;
import backendnovice.projectbookshop.board.comment.service.CommentService;
import backendnovice.projectbookshop.global.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/board/api/v1")
public class CommentApiController {
    private final CommentService commentService;

    public CommentApiController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Call comment service method to process register new comment.
     * @param commentDTO
     *      Comment data transfer object.
     * @return
     *      Response data transfer object. that includes result message.
     */
    @PostMapping("/write")
    public ResponseDTO<?> write(CommentDTO commentDTO) {
        String message;

        if(commentDTO == null) {
            message = "cannot register comment with null data.";
        }else {
            commentService.write(commentDTO);
            message = "comment registration succeeded.";
        }

        return ResponseDTO.of(HttpStatus.OK.value(), message, null);
    }

    /**
     * Call comment service method to process modify comment.
     * @param commentDTO
     *      Comment data transfer object.
     * @return
     *      Response data transfer object. that includes result message.
     */
    @PostMapping("/modify")
    public ResponseDTO<?> modify(CommentDTO commentDTO) {
        String message;

        if(commentDTO == null) {
            message = "cannot modify comment with null data.";
        }else {
            try {
                commentService.modify(commentDTO);
                message = "comment modification succeeded.";
            }catch(NoSuchElementException e) {
                message = "this comment does not exists.";
            }
        }

        return ResponseDTO.of(HttpStatus.OK.value(), message, null);
    }

    /**
     * Call comment service method to process delete comment by id.
     * @param commentId
     *      Comment id.
     * @exception NoSuchElementException
     *      Throwable exception when no comment found to delete.
     */
    @PostMapping("/delete")
    public ResponseDTO<?> delete(Long commentId) {
        String message;

        if(commentId == null) {
            message = "cannot delete comment with null id.";
        }else {
            try {
                commentService.delete(commentId);
                message = "comment deletion succeeded.";
            }catch (NoSuchElementException e) {
                message = "this comment does not exists.";
            }
        }

        return ResponseDTO.of(HttpStatus.OK.value(), message, null);
    }
}
