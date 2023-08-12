package backendnovice.projectbookshop.board.controller;

import backendnovice.projectbookshop.board.article.controller.ArticleController;
import backendnovice.projectbookshop.board.article.dto.ArticleDTO;
import backendnovice.projectbookshop.global.dto.SearchDTO;
import backendnovice.projectbookshop.board.article.service.ArticleService;
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

@WebMvcTest(ArticleController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MockBean(JpaMetamodelMappingContext.class) // Solving an issue with jpa auditing.
public class ArticleControllerTests {
    @MockBean
    private ArticleService articleService;

    @Autowired
    private MockMvc mockMvc;

    private Page<ArticleDTO> fakePage;

    @BeforeAll
    void initialize() {
        List<ArticleDTO> articleDTOS = new ArrayList<>();
        for(int i = 1; i <= 5; i++) {
            ArticleDTO articleDTO = ArticleDTO.builder()
                    .id((long) i)
                    .title("title #" + i)
                    .content("content #" + i)
                    .writer("writer #" + i)
                    .build();
            articleDTOS.add(articleDTO);
        }
        Pageable pageable = PageRequest.of(0, 10);
        fakePage = new PageImpl<>(articleDTOS, pageable, articleDTOS.size());
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
        ArticleDTO articleDTO = ArticleDTO.builder()
                .id(id)
                .title("title")
                .content("content")
                .writer("writer")
                .build();

        // when
        when(articleService.read(anyLong())).thenReturn(articleDTO);

        // then
        mockMvc.perform(get("/board/read")
                .param("id", String.valueOf(articleDTO.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("board/read"))
                .andExpect(model().attributeExists("dto"))
                .andExpect(model().attribute("dto", articleDTO));
    }

    @Test
    @DisplayName("Modify Page Test (GET)")
    void getModifyPageTest() throws Exception {
        // given
        Long id = 1L;
        ArticleDTO articleDTO = ArticleDTO.builder()
                .id(id)
                .title("title")
                .content("content")
                .writer("writer")
                .build();

        // when
        when(articleService.read(anyLong())).thenReturn(articleDTO);

        // then
        mockMvc.perform(get("/board/modify")
                .param("id", String.valueOf(articleDTO.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("board/modify"))
                .andExpect(model().attributeExists("dto"))
                .andExpect(model().attribute("dto", articleDTO));
    }

    @Test
    @DisplayName("List Page Test (GET)")
    void getListPageTest() throws Exception {
        // given
        SearchDTO searchDTO = new SearchDTO(fakePage);

        // when
        when(articleService.search(any(SearchDTO.class), any(Pageable.class))).thenReturn(fakePage);

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
        ArticleDTO articleDTO = ArticleDTO.builder()
                .title("title")
                .content("content")
                .writer("writer")
                .build();

        // when
        when(articleService.write(any(ArticleDTO.class))).thenReturn(id);

        // then
        mockMvc.perform(post("/board/write")
                .param("title", articleDTO.getTitle())
                .param("content", articleDTO.getContent())
                .param("writer", articleDTO.getWriter()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/board/read?id=" + id));
    }

    @Test
    @DisplayName("Modify Process Test (POST)")
    void modifyProcessTest() throws Exception {
        // given
        Long id = 1L;
        ArticleDTO articleDTO = ArticleDTO.builder()
                .title("modify title")
                .content("modify content")
                .build();

        // when
        when(articleService.modify(any(ArticleDTO.class))).thenReturn(id);

        // then
        mockMvc.perform(post("/board/modify")
                .param("title", articleDTO.getTitle())
                .param("content", articleDTO.getContent()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/board/read?id=" + id));
    }

    @Test
    @DisplayName("Delete Process Test (POST)")
    void deleteProcessTest() throws Exception {
        // given
        Long id = 1L;

        // when
        doNothing().when(articleService).delete(id);

        // then
        mockMvc.perform(post("/board/delete")
                .param("id", String.valueOf(id)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/board/list"));
    }
}
