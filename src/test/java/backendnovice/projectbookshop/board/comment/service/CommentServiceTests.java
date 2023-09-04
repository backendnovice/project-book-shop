/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-08-30
 * @modified : 2023-09-04
 * @desc     : CommentService test class.
 */

package backendnovice.projectbookshop.board.comment.service;

import backendnovice.projectbookshop.board.comment.domain.Comment;
import backendnovice.projectbookshop.board.comment.dto.CommentDTO;
import backendnovice.projectbookshop.board.comment.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTests {
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Page<Comment> fakeComments;

    private Page<Comment> emptyComments;

    /**
     * Initialize before each test. initialize "fakeComments" and "emptyComments".
     */
    @BeforeEach
    void initialize() {
        List<Comment> commentList = new ArrayList<>();
        commentList.add(new Comment());
        fakeComments = new PageImpl<>(commentList);
        emptyComments = Page.empty();
    }

    /**
     * Test success case for getComments() method.
     */
    @Test
    void should_ReturnCommentDTOTypePageObject_When_GetCommentsIsCalledAndSucceed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Long articleId = 1L;

        // when
        when(commentRepository.findAllByArticleId(anyLong(), any(Pageable.class))).thenReturn(fakeComments);

        // then
        Page<CommentDTO> result = commentService.getComments(articleId, pageable);
        assertThat(result).isNotNull();
    }

    /**
     * Test failure case for getComments() method.
     */
    @Test
    void should_ThrowNoSuchElementException_When_GetCommentsIsCalledAndFailed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Long articleId = 1L;

        // when
        when(commentRepository.findAllByArticleId(anyLong(), any(Pageable.class))).thenReturn(emptyComments);

        // then
        assertThatThrownBy(() -> {
            Page<CommentDTO> result = commentService.getComments(articleId, pageable);
        }).isInstanceOf(NoSuchElementException.class);
    }

    /**
     * Test success case for write() method.
     */
    @Test
    void should_CalledSaveOneTimes_When_WriteIsCalledAndSucceed() {
        // given
        CommentDTO commentDTO = mock(CommentDTO.class);
        Comment comment = mock(Comment.class);

        // when
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        commentService.write(commentDTO);

        // then
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    /**
     * Test success case for modify() method.
     */
    @Test
    void should_CalledSaveOneTimes_When_ModifyIsCalledAndSucceed() {
        // given
        CommentDTO commentDTO = mock(CommentDTO.class);
        Comment comment = mock(Comment.class);

        // when
        when(commentRepository.findById(anyLong())).thenReturn(Optional.ofNullable(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        commentService.modify(commentDTO);

        // then
        verify(commentRepository, times(1)).findById(anyLong());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    /**
     * Test failure case for modify() method.
     */
    @Test
    void should_ThrowNoSuchElementException_When_ModifyIsCalledAndFailed() {
        // given
        CommentDTO commentDTO = mock(CommentDTO.class);

        // when
        when(commentRepository.findById(anyLong())).thenThrow(NoSuchElementException.class);

        // then
        assertThatThrownBy(() -> {
            commentService.modify(commentDTO);
        }).isInstanceOf(NoSuchElementException.class);
    }

    /**
     * Test success case for delete() method.
     */
    @Test
    void should_CalledDeleteByIdOneTimes_When_DeleteIsCalledAndSucceed() {
        // given
        Long commentId = 1L;

        // when
        doNothing().when(commentRepository).deleteById(anyLong());
        commentService.delete(commentId);

        // then
        verify(commentRepository, times(1)).deleteById(commentId);
    }

    /**
     * Test failure case for delete() method.
     */
    @Test
    void should_ThrowNoSuchElementException_When_DeleteIsCalledAndFailed() {
        // given
        Long commentId = 1L;

        // when
        doThrow(EmptyResultDataAccessException.class).when(commentRepository).deleteById(commentId);

        // then
        assertThatThrownBy(() -> {
            commentService.delete(commentId);
        }).isInstanceOf(NoSuchElementException.class);
    }
}
