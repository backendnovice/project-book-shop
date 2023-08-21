/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-21
 * @desc      : An article-related controller class. that provide API when received requests from the view layer.
 * @changelog :
 * 2023-08-20 - backendnovice@gmail.com - create new file.
 * 2023-08-21 - backendnovice@gmail.com - add write, delete handle methods.
 */

package backendnovice.projectbookshop.board.comment.controller;

import backendnovice.projectbookshop.board.comment.dto.CommentDTO;
import backendnovice.projectbookshop.board.comment.service.CommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     */
    @PostMapping("/write")
    public void write(CommentDTO commentDTO) {
        commentService.write(commentDTO);
    }

    /**
     * Call comment service method to process delete comment by id.
     * @param commentId
     *      Comment id.
     */
    @PostMapping("/delete")
    public void delete(Long commentId) {
        commentService.delete(commentId);
    }
}
