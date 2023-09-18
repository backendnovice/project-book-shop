/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-08-20
 * @modified : 2023-09-18
 * @desc     : 댓글 관련 로직을 정의하는 서비스 인터페이스.
 */

package backendnovice.projectbookshop.board.comment.service;

import backendnovice.projectbookshop.board.comment.dto.CommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.NoSuchElementException;

public interface CommentService {
    /**
     * 게시글 ID와 일치하는 댓글들을 검색한다.
     * @param articleId
     *      게시글 ID
     * @param pageable
     *      페이지네이션 객체
     * @return
     *      검색 결과
     */
    Page<CommentDTO> getComments(Long articleId, Pageable pageable);

    /**
     * 새로운 댓글을 등록한다.
     * @param commentDTO
     *      댓글 데이터 전달 객체
     */
    void write(CommentDTO commentDTO);

    /**
     * 댓글 ID와 일치하는 댓글을 수정한다.
     * @param commentDTO
     *      댓글 데이터 전달 객체
     * @exception NoSuchElementException
     *      댓글이 존재하지 않을 때 발생하는 예외
     */
    void modify(CommentDTO commentDTO);

    /**
     * 댓글 ID와 일치하는 댓글을 삭제한다.
     * @param commentId
     *      댓글 ID
     * @exception NoSuchElementException
     *      댓글이 존재하지 않을 때 발생하는 예외
     */
    void delete(Long commentId);
}
