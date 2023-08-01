package backendnovice.projectbookshop.board.service;

import backendnovice.projectbookshop.board.domain.Board;
import backendnovice.projectbookshop.board.dto.BoardDTO;
import backendnovice.projectbookshop.board.dto.SearchDTO;
import backendnovice.projectbookshop.board.repository.BoardRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BoardServiceTests {
    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardServiceImpl boardService;

    private Page<Board> fakeBoardPage;

    @BeforeAll
    void initialize() {
        List<Board> fakeBoardList = new ArrayList<>();
        fakeBoardPage = new PageImpl<>(fakeBoardList);
    }

    @Test
    @DisplayName("Search test without any options")
    void searchTest() {
        // given
        SearchDTO searchDTO = SearchDTO.builder().build();

        // when
        when(boardRepository.findAll(any(PageRequest.class))).thenReturn(fakeBoardPage);
        Page<BoardDTO> result = boardService.search(searchDTO, PageRequest.of(0, 10));

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Search test with title")
    void searchTestWithTitle() {
        // given
        SearchDTO searchDTO = SearchDTO.builder()
                .tag("title")
                .keyword("title")
                .build();

        // when
        when(boardRepository.findAllByTitleContainsIgnoreCase(any(), any(PageRequest.class))).thenReturn(fakeBoardPage);
        Page<BoardDTO> result = boardService.search(searchDTO, PageRequest.of(0, 10));

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Search test with content")
    void searchWithOptionsTestWithContent() {
        // given
        SearchDTO searchDTO = SearchDTO.builder()
                .tag("content")
                .keyword("content")
                .build();

        // when
        when(boardRepository.findAllByContentContainsIgnoreCase(any(), any(PageRequest.class))).thenReturn(fakeBoardPage);
        Page<BoardDTO> result = boardService.search(searchDTO, PageRequest.of(0, 10));

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Search test with writer")
    void searchWithOptionsTestWithWriter() {
        // given
        SearchDTO searchDTO = SearchDTO.builder()
                .tag("writer")
                .keyword("writer")
                .build();

        // when
        when(boardRepository.findAllByWriterContainsIgnoreCase(any(), any(PageRequest.class))).thenReturn(fakeBoardPage);
        Page<BoardDTO> result = boardService.search(searchDTO, PageRequest.of(0, 10));

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Write Test")
    void writeTest() {
        // given
        Board board = mock(Board.class);
        BoardDTO boardDTO = mock(BoardDTO.class);

        // when
        when(boardRepository.save(any(Board.class))).thenReturn(board);
        long result = boardService.write(boardDTO);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Read Test")
    void readTest() {
        // given
        Board board = mock(Board.class);

        // when
        when(boardRepository.findById(anyLong())).thenReturn(Optional.ofNullable(board));
        BoardDTO result = boardService.read(1L);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Modify Test")
    void modifyTest() {
        // given
        BoardDTO boardDTO = mock(BoardDTO.class);
        Board board = mock(Board.class);

        // when
        when(boardRepository.findById(anyLong())).thenReturn(Optional.ofNullable(board));
        when(boardRepository.save(any(Board.class))).thenReturn(board);
        Long result = boardService.modify(boardDTO);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Delete Test")
    void deleteTest() {
        // given
        Long id = 1L;

        // when
        doNothing().when(boardRepository).deleteById(anyLong());
        boardService.delete(id);

        // then
        verify(boardRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Update Board View Test")
    void updateViewTest() {
        // given
        Long id = 1L;

        // when
        when(boardRepository.updateViewById(anyLong())).thenReturn(1);
        boardService.updateView(id);

        verify(boardRepository, times(1)).updateViewById(id);
    }
}
