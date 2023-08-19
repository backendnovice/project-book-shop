/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-20
 * @desc      : An article-related controller class. that provide API when received requests from the view layer.
 * @changelog :
 * 2023-08-20 - backendnovice@gmail.com - create new file.
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

    @PostMapping("/write")
    public void write(CommentDTO commentDTO) {
        commentService.write(commentDTO);
    }
}
