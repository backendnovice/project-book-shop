/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-20
 * @desc      : A comment-related service implementation class. that actually implement business logic for comment.
 * @changelog :
 * 2023-08-20 - backendnovice@gmail.com - create new file.
 */

package backendnovice.projectbookshop.board.comment.service;

import backendnovice.projectbookshop.board.comment.domain.Comment;
import backendnovice.projectbookshop.board.comment.dto.CommentDTO;
import backendnovice.projectbookshop.board.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * Create comment with DTO of parameter.
     * @param commentDTO
     *      CommentDTO object.
     * @exception IllegalArgumentException
     *      Throwable exception when empty comment dto detected.
     */
    @Override
    public void write(CommentDTO commentDTO) {
        if(commentDTO == null) {
            throw new IllegalArgumentException("Unable to write an empty comment.");
        }

        commentRepository.save(convertToEntity(commentDTO));
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
