/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-08-20
 * @modified : 2023-09-27
 * @desc     : 댓글 관련 POST, GET 요청을 핸들링하여 데이터를 제공하는 컨트롤러 클래스.
 */

package backendnovice.projectbookshop.board.comment.controller;

import backendnovice.projectbookshop.board.comment.dto.CommentDTO;
import backendnovice.projectbookshop.board.comment.service.CommentService;
import backendnovice.projectbookshop.global.dto.ResponseDTO;
import backendnovice.projectbookshop.global.exception.CallErrorPageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentApiController {
    private final CommentService commentService;

    public CommentApiController(CommentService commentService) {
        this.commentService = commentService;
    }

    private String message;

    /**
     * 댓글 등록 서비스 메소드를 호출하고 결과 메시지를 반환한다.
     * @param commentDTO
     *      댓글 데이터 전달 객체
     * @return
     *      결과 메시지
     */
    @PostMapping("/write")
    public ResponseDTO<?> write(@RequestBody CommentDTO commentDTO) {
        System.out.println(commentDTO.getId());
        System.out.println(commentDTO.getContent());
        System.out.println(commentDTO.getWriter());
        if (commentDTO.getContent() == null || commentDTO.getWriter() == null) {
            message = "올바르지 않은 요청입니다.";
        } else {
            try {
                commentService.write(commentDTO);
                message = "댓글 등록이 완료되었습니다.";
            } catch (NoSuchElementException e) {
                message = "게시글이 존재하지 않습니다.";
            }
        }

        return ResponseDTO.of(HttpStatus.OK.value(), message, null);
    }

    /**
     * 댓글 조회 서비스 메소드를 호출하고 결과 데이터 및 메시지를 반환한다.
     * @param articleId
     *      게시글 ID
     * @param pageable
     *      페이지네이션 객체
     * @return
     *      결과 댓글 및 메시지
     */
    @GetMapping("/read")
    public ResponseDTO<?> read(@RequestParam(value = "id", required = false) Long articleId, Pageable pageable) {
        if (articleId == null) {
            throw new CallErrorPageException("올바르지 않은 요청입니다.");
        }
        try {
            Page<CommentDTO> comments = commentService.getComments(articleId, pageable);
            message = "댓글 조회가 완료되었습니다.";
            return ResponseDTO.of(HttpStatus.OK.value(), message, comments);
        }catch (NoSuchElementException e) {
            message = "댓글이 존재하지 않습니다.";
            return ResponseDTO.of(HttpStatus.OK.value(), message, null);
        }
    }

    /**
     * 댓글 수정 서비스 메소드를 호출하고 결과 메시지를 반환한다.
     * @param commentDTO
     *      댓글 데이터 전달 객체
     * @return
     *      결과 메시지를 포함하는 Response 객체
     */
    @PostMapping("/modify")
    public ResponseDTO<?> modify(CommentDTO commentDTO) {
        if (commentDTO.getContent() == null || commentDTO.getWriter() == null) {
            message = "비어있는 댓글을 등록할 수 없습니다.";
        }
        try {
            commentService.modify(commentDTO);
            message = "댓글 수정이 완료되었습니다.";
        } catch (NoSuchElementException e) {
            message = "수정할 댓글이 존재하지 않습니다.";
        }
        return ResponseDTO.of(HttpStatus.OK.value(), message, null);
    }

    /**
     * 댓글 삭제 서비스 메소드를 호출하고 결과 메시지를 반환한다.
     * @param commentId
     *      댓글 ID
     * @exception NoSuchElementException
     *      댓글이 존재하지 않을 때 발생하는 예외
     */
    @PostMapping("/delete")
    public ResponseDTO<?> delete(@RequestParam(value = "id", required = false) Long commentId) {
        if (commentId == null) {
            message = "올바르지 않은 요청입니다.";
        }
        try {
            commentService.delete(commentId);
            message = "댓글 삭제가 완료되었습니다.";
        } catch (NoSuchElementException e) {
            message = "삭제할 댓글이 존재하지 않습니다.";
        }
        return ResponseDTO.of(HttpStatus.OK.value(), message, null);
    }
}
