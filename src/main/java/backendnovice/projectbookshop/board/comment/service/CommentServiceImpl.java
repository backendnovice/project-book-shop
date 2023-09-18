/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-08-20
 * @modified : 2023-09-18
 * @desc     : 댓글 관련 로직을 구현하는 서비스 클래스.
 */

package backendnovice.projectbookshop.board.comment.service;

import backendnovice.projectbookshop.board.comment.domain.Comment;
import backendnovice.projectbookshop.board.comment.dto.CommentDTO;
import backendnovice.projectbookshop.board.comment.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Page<CommentDTO> getComments(Long articleId, Pageable pageable) {
        Page<Comment> result = commentRepository.findAllByArticleId(articleId, pageable);
        if (result.isEmpty()) {
            throw new NoSuchElementException();
        }
        return result.map(this::convertToDTO);
    }

    @Override
    public void write(CommentDTO commentDTO) {
        commentRepository.save(convertToEntity(commentDTO));
    }

    @Override
    public void modify(CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentDTO.getId())
                .orElseThrow(NoSuchElementException::new);
        comment.setContent(commentDTO.getContent());
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void delete(Long commentId) {
        try {
            commentRepository.deleteById(commentId);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException();
        }
    }

    /**
     * Comment 데이터 전달 객체를 Comment 객체로 변환한다.
     * @param comment
     *      변환할 객체
     * @return
     *      변환된 객체
     */
    private CommentDTO convertToDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .writer(comment.getWriter())
                .build();
    }

    /**
     * Comment 객체를 데이터 전달 객체로 변환한다.
     * @param commentDTO
     *      변환할 객체
     * @return
     *      변환된 객체
     */
    private Comment convertToEntity(CommentDTO commentDTO) {
        return Comment.builder()
                .content(commentDTO.getContent())
                .writer(commentDTO.getWriter())
                .build();
    }

}
