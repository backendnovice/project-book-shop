/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-17
 * @desc      : ArticleController test class.
 * @changelog :
 * 2023-08-01 - backendnovice@gmail.com - create new file.
 * 2023-08-13 - backendnovice@gmail.com - change filename to ArticleControllerTests.
 * 2023-08-17 - backendnovice@gmail.com - add description annotation.
 */

package backendnovice.projectbookshop.board.controller;

import backendnovice.projectbookshop.board.article.controller.ArticleController;
import backendnovice.projectbookshop.board.article.dto.ArticleDTO;
import backendnovice.projectbookshop.global.dto.PageDTO;
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

    private Page<ArticleDTO> fakeArticles;

    /**
     * Initialize before all tests. initialize "fakeArticles" with 10 fake article.
     */
    @BeforeAll
    void initialize() {
        List<ArticleDTO> articleDTOS = new ArrayList<>();
        for(int i = 1; i <= 10; i++) {
            ArticleDTO articleDTO = ArticleDTO.builder()
                    .id((long) i)
                    .title("title #" + i)
                    .content("content #" + i)
                    .writer("writer #" + i)
                    .build();
            articleDTOS.add(articleDTO);
        }
        Pageable pageable = PageRequest.of(0, 10);
        fakeArticles = new PageImpl<>(articleDTOS, pageable, articleDTOS.size());
    }

    /**
     * Test GET request for article write page.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    @DisplayName("Write Page Test (GET)")
    void getWritePageTest() throws Exception {
        mockMvc.perform(get("/board/write"))
                .andExpect(status().isOk())
                .andExpect(view().name("board/write"));
    }

    /**
     * Test GET request for article read page with mock id.
     * @throws Exception
     *      Throwable exception when using mockMvc.perform().
     */
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

    /**
     * Test GET request for modify page with mock id.
     * @throws Exception
     *      Throwable exception when using mockMvc.perform().
     */
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

    /**
     * Test GET request for list page with mock articles.
     * @throws Exception
     *      Throwable exception when using mockMvc.perform().
     */
    @Test
    @DisplayName("List Page Test (GET)")
    void getListPageTest() throws Exception {
        // given
        PageDTO pageDTO = new PageDTO(fakeArticles);

        // when
        when(articleService.search(any(PageDTO.class), any(Pageable.class))).thenReturn(fakeArticles);

        // then
        mockMvc.perform(get("/board/list")
                .param("tag", pageDTO.getTag())
                .param("keyword", pageDTO.getKeyword()))
                .andExpect(status().isOk())
                .andExpect(view().name("board/list"))
                .andExpect(model().attributeExists("dto"))
                .andExpect(model().attribute("dto", fakeArticles))
                .andExpect(model().attributeExists("search"))
                .andExpect(model().attribute("search", pageDTO));
    }

    /**
     * Test POST request for write article process.
     * @throws Exception
     *      Throwable exception when using mockMvc.perform().
     */
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

    /**
     * Test POST request for modify article process.
     * @throws Exception
     *      Throwable exception when using mockMvc.perform().
     */
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

    /**
     * Test POST request for delete article process.
     * @throws Exception
     *      Throwable exception when using mockMvc.perform().
     */
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
