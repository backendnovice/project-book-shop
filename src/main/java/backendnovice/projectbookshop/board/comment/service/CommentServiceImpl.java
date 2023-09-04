/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-08-20
 * @modified : 2023-09-04
 * @desc     : A comment-related service implementation class. that actually implement business logic for comment.
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

        if(result.isEmpty()) {
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
