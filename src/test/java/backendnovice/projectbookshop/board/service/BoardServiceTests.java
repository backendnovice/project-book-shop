package backendnovice.projectbookshop.board.service;

import backendnovice.projectbookshop.board.domain.Board;
import backendnovice.projectbookshop.board.dto.BoardDTO;
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
    @DisplayName("Search All Test")
    void searchAllTest() {
        // when
        when(boardRepository.findAll(any(PageRequest.class))).thenReturn(fakeBoardPage);
        Page<BoardDTO> result = boardService.searchAll(PageRequest.of(0, 10));

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Search with Options Test (Title)")
    void searchWithOptionsTestWithTitle() {
        // given
        BoardDTO boardDTO = BoardDTO.builder()
                .title("title")
                .build();

        // when
        when(boardRepository.findAllByTitleContainsIgnoreCase(any(), any(PageRequest.class))).thenReturn(fakeBoardPage);
        Page<BoardDTO> result = boardService.searchWithOptions(boardDTO, PageRequest.of(0, 10));

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Search with Options Test (Content)")
    void searchWithOptionsTestWithContent() {
        // given
        BoardDTO boardDTO = BoardDTO.builder()
                .content("content")
                .build();

        // when
        when(boardRepository.findAllByContentContainsIgnoreCase(any(), any(PageRequest.class))).thenReturn(fakeBoardPage);
        Page<BoardDTO> result = boardService.searchWithOptions(boardDTO, PageRequest.of(0, 10));

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Search with Options Test (Writer)")
    void searchWithOptionsTestWithWriter() {
        // given
        BoardDTO boardDTO = BoardDTO.builder()
                .writer("writer")
                .build();

        // when
        when(boardRepository.findAllByWriterContainsIgnoreCase(any(), any(PageRequest.class))).thenReturn(fakeBoardPage);
        Page<BoardDTO> result = boardService.searchWithOptions(boardDTO, PageRequest.of(0, 10));

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Search with Options Test (Null)")
    void searchWithOptionsTestWithNull() {
        // given
        BoardDTO boardDTO = mock(BoardDTO.class);

        // when
        when(boardRepository.findAll(any(PageRequest.class))).thenReturn(fakeBoardPage);
        Page<BoardDTO> result = boardService.searchWithOptions(boardDTO, PageRequest.of(0, 10));

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
        BoardDTO result = boardService.write(boardDTO);

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
}
