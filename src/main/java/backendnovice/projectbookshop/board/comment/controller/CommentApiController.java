/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-08-20
 * @modified : 2023-09-04
 * @desc     : An article-related controller class. that provide API when received requests from the view layer.
 */

package backendnovice.projectbookshop.board.comment.controller;

import backendnovice.projectbookshop.board.comment.dto.CommentDTO;
import backendnovice.projectbookshop.board.comment.service.CommentService;
import backendnovice.projectbookshop.global.dto.ResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/board/api/v1/comment")
public class CommentApiController {
    private final CommentService commentService;

    public CommentApiController(CommentService commentService) {
        this.commentService = commentService;
    }

    private String message;

    /**
     * Call comment service method to process register new comment.
     * @param commentDTO
     *      Comment data transfer object.
     * @return
     *      Response data transfer object, that includes result message.
     */
    @PostMapping("/write")
    public ResponseDTO<?> write(CommentDTO commentDTO) {
        if(commentDTO.getContent() == null || commentDTO.getWriter() == null) {
            message = "cannot register comment with null data.";
        }else {
            commentService.write(commentDTO);
            message = "comment registration succeed.";
        }

        return ResponseDTO.of(HttpStatus.OK.value(), message, null);
    }

    /**
     * Call comment service method to process select comments by article id.
     * @param articleId
     *      Article id.
     * @param pageable
     *      Comment pageable object.
     * @return
     *      Response data transfer object, that includes result message and data.
     */
    @GetMapping("/read")
    public ResponseDTO<?> read(@RequestParam(value = "id", required = false) Long articleId, Pageable pageable) {
        if(articleId == null) {
            message = "cannot read comment with null data.";
            return ResponseDTO.of(HttpStatus.OK.value(), message, null);
        }else {
            message = "comment selection succeed.";
            Page<CommentDTO> comments = commentService.getComments(articleId, pageable);
            return ResponseDTO.of(HttpStatus.OK.value(), message, comments);
        }
    }

    /**
     * Call comment service method to process modify comment.
     * @param commentDTO
     *      Comment data transfer object.
     * @return
     *      Response data transfer object, that includes result message.
     */
    @PostMapping("/modify")
    public ResponseDTO<?> modify(CommentDTO commentDTO) {
        if(commentDTO.getContent() == null || commentDTO.getWriter() == null) {
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
    public ResponseDTO<?> delete(@RequestParam(value = "id", required = false) Long commentId) {
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
