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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BoardServiceTests {
    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardServiceImpl boardService;

    private List<Board> fakeBoardList;

    private Page<Board> fakeBoardPage;

    @BeforeAll
    void initialize() {
        fakeBoardList = new ArrayList<>();
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
    @DisplayName("Search with Title Test")
    void searchWithTitleTest() {
        // when
        when(boardRepository.findAllByTitleContainsIgnoreCase(any(), any(PageRequest.class))).thenReturn(fakeBoardPage);
        Page<BoardDTO> result = boardService.searchWithTitle("title", PageRequest.of(0, 10));

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Search with Content Test")
    void searchWithContentTest() {
        // when
        when(boardRepository.findAllByContentContainsIgnoreCase(any(), any(PageRequest.class))).thenReturn(fakeBoardPage);
        Page<BoardDTO> result = boardService.searchWithContent("content", PageRequest.of(0, 10));

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Search with Writer Test")
    void searchWithWriterTest() {
        // when
        when(boardRepository.findAllByWriterContainsIgnoreCase(any(), any(PageRequest.class))).thenReturn(fakeBoardPage);
        Page<BoardDTO> result = boardService.searchWithWriter("writer", PageRequest.of(0, 10));

        // then
        assertThat(result).isNotNull();
    }
}
