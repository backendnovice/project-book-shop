/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-17
 * @desc      : ArticleService test class.
 * @changelog :
 * 2023-07-25 - backendnovice@gmail.com - create new file.
 * 2023-07-26 - backendnovice@gmail.com - add search, write tests.
 * 2023-07-29 - backendnovice@gmail.com - modify search tests.
 * 2023-07-30 - backendnovice@gmail.com - add modify, delete tests.
 * 2023-08-01 - backendnovice@gmail.com - add update view count test.
 * 2023-08-13 - backendnovice@gmail.com - change filename to ArticleRepositoryTests.
 * 2023-08-17 - backendnovice@gmail.com - add description annotation.
 */

package backendnovice.projectbookshop.board.service;

import backendnovice.projectbookshop.board.article.domain.Article;
import backendnovice.projectbookshop.board.article.dto.ArticleDTO;
import backendnovice.projectbookshop.global.dto.PageDTO;
import backendnovice.projectbookshop.board.article.repository.ArticleRepository;
import backendnovice.projectbookshop.board.article.service.ArticleServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArticleServiceTests {
    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleServiceImpl articleService;

    private Page<Article> fakeArticles;

    /**
     * Initialize before all tests. initialize "fakeArticles".
     */
    @BeforeAll
    void initialize() {
        List<Article> fakeArticleList = new ArrayList<>();
        fakeArticles = new PageImpl<>(fakeArticleList);
    }

    /**
     * Test search service method without any options.
     */
    @Test
    @DisplayName("Search test without any options")
    void searchTest() {
        // given
        PageDTO pageDTO = PageDTO.builder().build();

        // when
        when(articleRepository.findAll(any(PageRequest.class))).thenReturn(fakeArticles);
        Page<ArticleDTO> result = articleService.search(pageDTO, PageRequest.of(0, 10));

        // then
        assertThat(result).isNotNull();
    }

    /**
     * Test search service method with title.
     */
    @Test
    @DisplayName("Search test with title")
    void searchTestWithTitle() {
        // given
        PageDTO pageDTO = PageDTO.builder()
                .tag("title")
                .keyword("title")
                .build();

        // when
        when(articleRepository.findAllByTitleContainsIgnoreCase(any(), any(PageRequest.class))).thenReturn(fakeArticles);
        Page<ArticleDTO> result = articleService.search(pageDTO, PageRequest.of(0, 10));

        // then
        assertThat(result).isNotNull();
    }

    /**
     * Test search service method with content.
     */
    @Test
    @DisplayName("Search test with content")
    void searchWithOptionsTestWithContent() {
        // given
        PageDTO pageDTO = PageDTO.builder()
                .tag("content")
                .keyword("content")
                .build();

        // when
        when(articleRepository.findAllByContentContainsIgnoreCase(any(), any(PageRequest.class))).thenReturn(fakeArticles);
        Page<ArticleDTO> result = articleService.search(pageDTO, PageRequest.of(0, 10));

        // then
        assertThat(result).isNotNull();
    }

    /**
     * Test search service method with writer.
     */
    @Test
    @DisplayName("Search test with writer")
    void searchWithOptionsTestWithWriter() {
        // given
        PageDTO pageDTO = PageDTO.builder()
                .tag("writer")
                .keyword("writer")
                .build();

        // when
        when(articleRepository.findAllByWriterContainsIgnoreCase(any(), any(PageRequest.class))).thenReturn(fakeArticles);
        Page<ArticleDTO> result = articleService.search(pageDTO, PageRequest.of(0, 10));

        // then
        assertThat(result).isNotNull();
    }

    /**
     * Test write service method test.
     */
    @Test
    @DisplayName("Write Test")
    void writeTest() {
        // given
        Article article = mock(Article.class);
        ArticleDTO articleDTO = mock(ArticleDTO.class);

        // when
        when(articleRepository.save(any(Article.class))).thenReturn(article);
        long result = articleService.write(articleDTO);

        // then
        assertThat(result).isNotNull();
    }

    /**
     * Test read service method test.
     */
    @Test
    @DisplayName("Read Test")
    void readTest() {
        // given
        Article article = mock(Article.class);

        // when
        when(articleRepository.findById(anyLong())).thenReturn(Optional.ofNullable(article));
        ArticleDTO result = articleService.read(1L);

        // then
        assertThat(result).isNotNull();
    }

    /**
     * Test modify service method test.
     */
    @Test
    @DisplayName("Modify Test")
    void modifyTest() {
        // given
        ArticleDTO articleDTO = mock(ArticleDTO.class);
        Article article = mock(Article.class);

        // when
        when(articleRepository.findById(anyLong())).thenReturn(Optional.ofNullable(article));
        when(articleRepository.save(any(Article.class))).thenReturn(article);
        Long result = articleService.modify(articleDTO);

        // then
        assertThat(result).isNotNull();
    }

    /**
     * Test delete service method test.
     */
    @Test
    @DisplayName("Delete Test")
    void deleteTest() {
        // given
        Long id = 1L;

        // when
        doNothing().when(articleRepository).deleteById(anyLong());
        articleService.delete(id);

        // then
        verify(articleRepository, times(1)).deleteById(id);
    }

    /**
     * Test update view count service method test.
     */
    @Test
    @DisplayName("Update article view test")
    void updateViewByIdTest() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Article article = mock(Article.class);
        Long id = 1L;

        // when
        doNothing().when(articleRepository).updateViewById(anyLong());
        articleService.updateView(id, request, response);

        // then
        verify(articleRepository, times(1)).updateViewById(id);
    }
}
