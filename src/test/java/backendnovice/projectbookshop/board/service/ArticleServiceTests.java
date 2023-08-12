package backendnovice.projectbookshop.board.service;

import backendnovice.projectbookshop.board.article.domain.Article;
import backendnovice.projectbookshop.board.article.dto.ArticleDTO;
import backendnovice.projectbookshop.global.dto.SearchDTO;
import backendnovice.projectbookshop.board.article.repository.ArticleRepository;
import backendnovice.projectbookshop.board.article.service.ArticleServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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

    private Page<Article> fakeBoardPage;

    @BeforeAll
    void initialize() {
        List<Article> fakeArticleList = new ArrayList<>();
        fakeBoardPage = new PageImpl<>(fakeArticleList);
    }

    @Test
    @DisplayName("Search test without any options")
    void searchTest() {
        // given
        SearchDTO searchDTO = SearchDTO.builder().build();

        // when
        when(articleRepository.findAll(any(PageRequest.class))).thenReturn(fakeBoardPage);
        Page<ArticleDTO> result = articleService.search(searchDTO, PageRequest.of(0, 10));

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
        when(articleRepository.findAllByTitleContainsIgnoreCase(any(), any(PageRequest.class))).thenReturn(fakeBoardPage);
        Page<ArticleDTO> result = articleService.search(searchDTO, PageRequest.of(0, 10));

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
        when(articleRepository.findAllByContentContainsIgnoreCase(any(), any(PageRequest.class))).thenReturn(fakeBoardPage);
        Page<ArticleDTO> result = articleService.search(searchDTO, PageRequest.of(0, 10));

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
        when(articleRepository.findAllByWriterContainsIgnoreCase(any(), any(PageRequest.class))).thenReturn(fakeBoardPage);
        Page<ArticleDTO> result = articleService.search(searchDTO, PageRequest.of(0, 10));

        // then
        assertThat(result).isNotNull();
    }

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

    @Test
    @DisplayName("Update Board View Test")
    void updateViewTest() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Long id = 1L;

        // when
        when(articleRepository.updateViewById(anyLong())).thenReturn(1);
        articleService.updateView(id, request, response);

        verify(articleRepository, times(1)).updateViewById(id);
    }
}
