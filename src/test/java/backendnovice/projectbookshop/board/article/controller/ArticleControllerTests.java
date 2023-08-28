/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-23
 * @desc      : ArticleController test class.
 * @changelog :
 * 2023-08-01 - backendnovice@gmail.com - create new file.
 * 2023-08-13 - backendnovice@gmail.com - change filename to ArticleControllerTests.
 * 2023-08-17 - backendnovice@gmail.com - add description annotation.
 * 2023-08-23 - backendnovice@gmail.com - fix all test.
 */

package backendnovice.projectbookshop.board.article.controller;

import backendnovice.projectbookshop.board.article.controller.ArticleController;
import backendnovice.projectbookshop.board.article.dto.ArticleDTO;
import backendnovice.projectbookshop.global.dto.PageDTO;
import backendnovice.projectbookshop.board.article.service.ArticleService;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

    private final String PREFIX = "/board/article/";

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
     * Test for GET request on write page.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnWritePage_When_GetWritePageIsCalled() throws Exception {
        // given
        String uri = PREFIX + "write";

        // when

        // then
        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(view().name("article/write"));
    }

    /**
     * Test success case for GET request on list page without any options.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnListPage_When_GetListPageIsCalledWithoutParam() throws Exception {
        // given
        String uri = PREFIX + "list";
        PageDTO fakePageDTO = new PageDTO(fakeArticles);

        // when
        when(articleService.searchAll(any(Pageable.class))).thenReturn(fakeArticles);

        // then
        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(view().name("article/list"))
                .andExpect(model().attributeExists("dto"))
                .andExpect(model().attribute("dto", fakeArticles))
                .andExpect(model().attributeExists("search"))
                .andExpect(model().attribute("search", fakePageDTO));
    }

    /**
     * Test success case for GET request on list page with both good tag and keyword.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnListPage_When_GetListPageIsCalledWithGoodTagAndGoodKeyword() throws Exception {
        // given
        String uri = PREFIX + "list";
        PageDTO fakePageDTO = new PageDTO(fakeArticles);

        // when
        when(articleService.searchByTags(any(PageDTO.class), any(Pageable.class))).thenReturn(fakeArticles);

        // then
        mockMvc.perform(get(uri).param("tag", "title").param("keyword", "title"))
                .andExpect(view().name("article/list"))
                .andExpect(model().attributeExists("dto"))
                .andExpect(model().attribute("dto", fakeArticles))
                .andExpect(model().attributeExists("search"))
                .andExpect(model().attribute("search", fakePageDTO));
    }

    /**
     * Test failure case for GET request on list page with bad tag.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnListPage_When_GetListPageIsCalledWithBadTag() throws Exception {
        // given
        String uri = PREFIX + "list";
        PageDTO fakePageDTO = new PageDTO(fakeArticles);

        // when
        when(articleService.searchByTags(any(PageDTO.class), any(Pageable.class))).thenThrow(IllegalArgumentException.class);
        when(articleService.searchAll(any(Pageable.class))).thenReturn(fakeArticles);

        // then
        mockMvc.perform(get(uri).param("tag", "blank").param("keyword", "nothing"))
                .andExpect(view().name("article/list"))
                .andExpect(model().attributeExists("dto"))
                .andExpect(model().attribute("dto", fakeArticles))
                .andExpect(model().attributeExists("search"))
                .andExpect(model().attribute("search", fakePageDTO))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Incorrect search tag detected."));
    }

    /**
     * Test failure case for GET request on list page with bad keyword.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnListPage_When_GetListPageIsCalledWithBadKeyword() throws Exception {
        // given
        String uri = PREFIX + "list";
        PageDTO fakePageDTO = new PageDTO(fakeArticles);

        // when
        when(articleService.searchByTags(any(PageDTO.class), any(Pageable.class))).thenThrow(NoSuchElementException.class);
        when(articleService.searchAll(any(Pageable.class))).thenReturn(fakeArticles);

        // then
        mockMvc.perform(get(uri).param("tag", "title").param("keyword", "nothing"))
                .andExpect(view().name("article/list"))
                .andExpect(model().attributeExists("dto"))
                .andExpect(model().attribute("dto", fakeArticles))
                .andExpect(model().attributeExists("search"))
                .andExpect(model().attribute("search", fakePageDTO))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Cannot found any articles."));
    }

    /**
     * Test success case for GET request on read page with good article id.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnReadPage_When_GetReadPageIsCalledWithGoodArticleId() throws Exception {
        // given
        String uri = PREFIX + "read";
        ArticleDTO mockDTO = mock(ArticleDTO.class);

        // when
        when(articleService.read(anyLong())).thenReturn(mockDTO);

        // then
        mockMvc.perform(get(uri).param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("article/read"))
                .andExpect(model().attributeExists("dto"))
                .andExpect(model().attribute("dto", mockDTO));
    }

    /**
     * Test failure case for GET request on read page with null article id.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnListPage_When_GetReadPageIsCalledWithNullArticleId() throws Exception {
        // given
        String uri = PREFIX + "read";

        // when

        // then
        mockMvc.perform(get(uri))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/list"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "Cannot read article with blank id."));
    }

    /**
     * Test failure case for GET request on read page with bad article id.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnListPage_When_GetReadPageIsCalledWithBadArticleId() throws Exception {
        // given
        String uri = PREFIX + "read";

        // when
        when(articleService.read(anyLong())).thenThrow(NoSuchElementException.class);

        // then
        mockMvc.perform(get(uri).param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/list"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "No article found with id."));
    }

    /**
     * Test success case for GET request on modify page with good article id.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnModifyPage_When_GetModifyPageIsCalledWithGoodArticleId() throws Exception {
        // given
        String uri = PREFIX + "modify";
        ArticleDTO mockDTO = mock(ArticleDTO.class);

        // when
        when(articleService.read(anyLong())).thenReturn(mockDTO);

        // then
        mockMvc.perform(get(uri).param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("article/modify"))
                .andExpect(model().attributeExists("dto"))
                .andExpect(model().attribute("dto", mockDTO));
    }

    /**
     * Test failure case for GET request on modify page with null article id.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnListPage_When_GetModifyPageIsCalledWithNullArticleId() throws Exception {
        // given
        String uri = PREFIX + "modify";

        // when

        // then
        mockMvc.perform(get(uri))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/list"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "Cannot modify article with blank id."));
    }

    /**
     * Test failure case for GET request on modify page with bad article id.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnListPage_When_GetModifyPageIsCalledWithBadArticleId() throws Exception {
        // given
        String uri = PREFIX + "modify";
        ArticleDTO mockDTO = mock(ArticleDTO.class);

        // when
        when(articleService.read(anyLong())).thenThrow(NoSuchElementException.class);

        // then
        mockMvc.perform(get(uri).param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/list"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "No article found with id."));
    }

    /**
     * Test success case for POST request on write article process.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnReadPage_When_CallWriteProcessServiceIsCalledAndSucceed() throws Exception {
        // given
        String uri = PREFIX + "write";
        MultiValueMap<String, String> fakeParams = new LinkedMultiValueMap<>();
        fakeParams.add("title", "title");
        fakeParams.add("content", "content");
        fakeParams.add("writer", "writer");

        // when
        when(articleService.write(any(ArticleDTO.class))).thenReturn(1L);

        // then
        mockMvc.perform(post(uri).params(fakeParams))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/read?id=" + 1L))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "article registration succeed."));
    }

    /**
     * Test failure case for POST request on write article process.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnWritePage_When_CallWriteProcessServiceIsCalledAndFailed() throws Exception {
        // given
        String uri = PREFIX + "write";

        // then
        mockMvc.perform(post(uri))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/write"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "cannot write an empty article."));
    }

    /**
     * Test success case for POST request on modify article process.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnReadPage_When_CallModifyProcessServiceIsCalledAndSucceed() throws Exception {
        // given
        String uri = PREFIX + "modify";
        MultiValueMap<String, String> fakeParams = new LinkedMultiValueMap<>();
        fakeParams.add("title", "title");
        fakeParams.add("content", "content");
        fakeParams.add("writer", "writer");

        // when
        when(articleService.modify(any(ArticleDTO.class))).thenReturn(1L);

        // then
        mockMvc.perform(post(uri).params(fakeParams))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/read?id=" + 1L))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "article modification succeed."));
    }

    /**
     * Test failure case for POST request on modify article process.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnListPage_When_CallModifyProcessServiceIsCalledAndFailed() throws Exception {
        // given
        String uri = PREFIX + "modify";
        MultiValueMap<String, String> fakeParams = new LinkedMultiValueMap<>();
        fakeParams.add("title", "title");
        fakeParams.add("content", "content");
        fakeParams.add("writer", "writer");

        // when
        when(articleService.modify(any(ArticleDTO.class))).thenThrow(NoSuchElementException.class);

        // then
        mockMvc.perform(post(uri).params(fakeParams))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/list"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "no article found with id."));
    }

    /**
     * Test success case for POST request on delete article process.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnListPage_When_CallDeleteProcessServiceIsCalledAndSucceed() throws Exception {
        // given
        String uri = PREFIX + "delete";

        // when
        doNothing().when(articleService).delete(anyLong());

        // then
        mockMvc.perform(post(uri).param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/list"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "article deletion succeed."));
    }

    /**
     * Test failure case for POST request on delete article process.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnListPage_When_CallDeleteProcessServiceIsCalledAndFailed() throws Exception {
        // given
        String uri = PREFIX + "delete";

        // when
        doThrow(NoSuchElementException.class).when(articleService).delete(anyLong());

        // then
        mockMvc.perform(post(uri).param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/list"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "cannot found any article to delete."));
    }
}
