/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-21
 * @desc      : A comment-related service implementation class. that actually implement business logic for comment.
 * @changelog :
 * 2023-08-20 - backendnovice@gmail.com - create new file.
 * 2023-08-21 - backendnovice@gmail.com - add write, delete method.
 * 2023-08-22 - backendnovice@gmail.com - modify exception handling.
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
        return commentRepository.findAllByArticleId(articleId, pageable).map(this::convertToDTO);
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
        }catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException();
        }
    }

    /**
     * Convert Comment object to CommentDTO object.
     * @param comment
     *      Comment object for convert.
     * @return
     *      Converted CommentDTO object.
     */
    private CommentDTO convertToDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .writer(comment.getWriter())
                .build();
    }

    /**
     * Convert CommentDTO object to Comment object.
     * @param commentDTO
     *      CommentDTO object for convert.
     * @return
     *      Converted Comment object.
     */
    private Comment convertToEntity(CommentDTO commentDTO) {
        return Comment.builder()
                .content(commentDTO.getContent())
                .writer(commentDTO.getWriter())
                .build();
    }

}
