package backendnovice.projectbookshop.board.controller;

import backendnovice.projectbookshop.board.dto.BoardDTO;
import backendnovice.projectbookshop.board.dto.SearchDTO;
import backendnovice.projectbookshop.board.service.BoardService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BoardController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MockBean(JpaMetamodelMappingContext.class) // Solving an issue with jpa auditing.
public class BoardControllerTests {
    @MockBean
    private BoardService boardService;

    @Autowired
    private MockMvc mockMvc;

    private Page<BoardDTO> fakePage;

    @BeforeAll
    void initialize() {
        List<BoardDTO> boardDTOs = new ArrayList<>();
        for(int i = 1; i <= 5; i++) {
            BoardDTO boardDTO = BoardDTO.builder()
                    .id((long) i)
                    .title("title #" + i)
                    .content("content #" + i)
                    .writer("writer #" + i)
                    .build();
            boardDTOs.add(boardDTO);
        }
        Pageable pageable = PageRequest.of(0, 10);
        fakePage = new PageImpl<>(boardDTOs, pageable, boardDTOs.size());
    }

    @Test
    @DisplayName("Write Page Test (GET)")
    void getWritePageTest() throws Exception {
        mockMvc.perform(get("/board/write"))
                .andExpect(status().isOk())
                .andExpect(view().name("board/write"));
    }

    @Test
    @DisplayName("Read Page Test (GET)")
    void getReadPageTest() throws Exception {
        // given
        Long id = 1L;
        BoardDTO boardDTO = BoardDTO.builder()
                .id(id)
                .title("title")
                .content("content")
                .writer("writer")
                .build();

        // when
        when(boardService.read(anyLong())).thenReturn(boardDTO);

        // then
        mockMvc.perform(get("/board/read")
                .param("id", String.valueOf(boardDTO.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("board/read"))
                .andExpect(model().attributeExists("dto"))
                .andExpect(model().attribute("dto", boardDTO));
    }

    @Test
    @DisplayName("Modify Page Test (GET)")
    void getModifyPageTest() throws Exception {
        // given
        Long id = 1L;
        BoardDTO boardDTO = BoardDTO.builder()
                .id(id)
                .title("title")
                .content("content")
                .writer("writer")
                .build();

        // when
        when(boardService.read(anyLong())).thenReturn(boardDTO);

        // then
        mockMvc.perform(get("/board/modify")
                .param("id", String.valueOf(boardDTO.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("board/modify"))
                .andExpect(model().attributeExists("dto"))
                .andExpect(model().attribute("dto", boardDTO));
    }

    @Test
    @DisplayName("List Page Test (GET)")
    void getListPageTest() throws Exception {
        // given
        SearchDTO searchDTO = new SearchDTO(fakePage);

        // when
        when(boardService.search(any(SearchDTO.class), any(Pageable.class))).thenReturn(fakePage);

        // then
        mockMvc.perform(get("/board/list")
                .param("tag", searchDTO.getTag())
                .param("keyword", searchDTO.getKeyword()))
                .andExpect(status().isOk())
                .andExpect(view().name("board/list"))
                .andExpect(model().attributeExists("dto"))
                .andExpect(model().attribute("dto", fakePage))
                .andExpect(model().attributeExists("search"))
                .andExpect(model().attribute("search", searchDTO));
    }

    @Test
    @DisplayName("Write Process Test (POST)")
    void writeProcessTest() throws Exception {
        // given
        Long id = 1L;
        BoardDTO boardDTO = BoardDTO.builder()
                .title("title")
                .content("content")
                .writer("writer")
                .build();

        // when
        when(boardService.write(any(BoardDTO.class))).thenReturn(id);

        // then
        mockMvc.perform(post("/board/write")
                .param("title", boardDTO.getTitle())
                .param("content", boardDTO.getContent())
                .param("writer", boardDTO.getWriter()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/board/read?id=" + id));
    }

    @Test
    @DisplayName("Modify Process Test (POST)")
    void modifyProcessTest() throws Exception {
        // given
        Long id = 1L;
        BoardDTO boardDTO = BoardDTO.builder()
                .title("modify title")
                .content("modify content")
                .build();

        // when
        when(boardService.modify(any(BoardDTO.class))).thenReturn(id);

        // then
        mockMvc.perform(post("/board/modify")
                .param("title", boardDTO.getTitle())
                .param("content", boardDTO.getContent()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/board/read?id=" + id));
    }

    @Test
    @DisplayName("Delete Process Test (POST)")
    void deleteProcessTest() throws Exception {
        // given
        Long id = 1L;

        // when
        doNothing().when(boardService).delete(id);

        // then
        mockMvc.perform(post("/board/delete")
                .param("id", String.valueOf(id)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/board/list"));
    }
}
