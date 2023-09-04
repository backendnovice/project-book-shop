/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-07-25
 * @modified : 2023-09-04
 * @desc     : ArticleService test class.
 */

package backendnovice.projectbookshop.board.article.service;

import backendnovice.projectbookshop.board.article.domain.Article;
import backendnovice.projectbookshop.board.article.dto.ArticleDTO;
import backendnovice.projectbookshop.global.dto.PageDTO;
import backendnovice.projectbookshop.board.article.repository.ArticleRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTests {
    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleServiceImpl articleService;

    private Page<Object[]> fakeResult;

    private Page<Object[]> emptyResult;

    /**
     * Initialize before each test. initialize "fakeArticles".
     */
    @BeforeEach
    void initialize() {
        // Initialize fake result.
        List<Object[]> fakeArticles = new ArrayList<>();
        Object[] fakeData = new Object[]{new Article(), 1};
        fakeArticles.add(fakeData);
        fakeResult = new PageImpl<>(fakeArticles);

        // Initialize empty result.
        emptyResult = Page.empty();
    }

    /**
     * Test success case for searchAll() method.
     */
    @Test
    void should_ReturnArticleDTOTypePageObject_When_SearchAllIsCalledAndSucceed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when
        when(articleRepository.findAllWithCommentCount(any(Pageable.class))).thenReturn(fakeResult);

        // then
        Page<ArticleDTO> result = articleService.searchAll(pageable);
        assertThat(result).isNotNull();
    }

    /**
     * Test failure case for searchAll() method.
     */
    @Test
    void should_ThrowNoSuchElementException_When_SearchAllIsCalledAndFailed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when
        when(articleRepository.findAllWithCommentCount(any(Pageable.class))).thenReturn(emptyResult);

        // then
        assertThatThrownBy(() -> {
            Page<ArticleDTO> result = articleService.searchAll(pageable);
        }).isInstanceOf(NoSuchElementException.class);
    }

    /**
     * Test success case for searchByTags() method with title tag.
     */
    @Test
    void should_ReturnArticleDTOTypePageObject_When_SearchByTagsIsCalledWithTitleTagAndSucceed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        PageDTO pageDTO = PageDTO.builder().tag("title").keyword("keyword").build();

        // when
        when(articleRepository.findAllByTitleWithCommentCount(anyString(), any(Pageable.class))).thenReturn(fakeResult);

        // then
        Page<ArticleDTO> result = articleService.searchByTags(pageDTO, pageable);
        assertThat(result).isNotNull();
    }

    /**
     * Test success case for searchByTags() method with content tag.
     */
    @Test
    void should_ReturnArticleDTOTypePageObject_When_SearchByTagsIsCalledWithContentTagAndSucceed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        PageDTO pageDTO = PageDTO.builder().tag("content").keyword("keyword").build();

        // when
        when(articleRepository.findAllByContentWithCommentCount(anyString(), any(Pageable.class))).thenReturn(fakeResult);

        // then
        Page<ArticleDTO> result = articleService.searchByTags(pageDTO, pageable);
        assertThat(result).isNotNull();
    }

    /**
     * Test success case for searchByTags() method with writer tag.
     */
    @Test
    void should_ReturnArticleDTOTypePageObject_When_SearchByTagsIsCalledWithWriterTagAndSucceed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        PageDTO pageDTO = PageDTO.builder().tag("writer").keyword("keyword").build();

        // when
        when(articleRepository.findAllByWriterWithCommentCount(anyString(), any(Pageable.class))).thenReturn(fakeResult);

        // then
        Page<ArticleDTO> result = articleService.searchByTags(pageDTO, pageable);
        assertThat(result).isNotNull();
    }

    /**
     * Test failure case for searchByTags() method with wrong tag.
     */
    @Test
    void should_ThrowIllegalArgumentException_When_SearchByTagsIsCalledWithWrongTagAndFailed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        PageDTO pageDTO = PageDTO.builder().tag("wrong").keyword("keyword").build();

        // when

        // then
        assertThatThrownBy(() -> {
            Page<ArticleDTO> result = articleService.searchByTags(pageDTO, pageable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Test failure case for searchByTags() method expecting empty result.
     */
    @Test
    void should_ThrowNoSuchElementException_When_SearchByTagsIsCalledExpectEmpty() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        PageDTO pageDTO = PageDTO.builder().tag("title").keyword("keyword").build();

        // when
        when(articleRepository.findAllByTitleWithCommentCount(anyString(), any(Pageable.class))).thenReturn(emptyResult);

        // then
        assertThatThrownBy(() -> {
            Page<ArticleDTO> result = articleService.searchByTags(pageDTO, pageable);
        }).isInstanceOf(NoSuchElementException.class);
    }

    /**
     * Test success case for write() method.
     */
    @Test
    void should_ReturnArticleId_When_WriteIsCalledWithArticleDTOAndSucceed() {
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
     * Test success case for read() method.
     */
    @Test
    void should_ReturnArticleDTO_When_ReadIsCalledWithArticleIdAndSucceed() {
        // given
        Article article = mock(Article.class);

        // when
        when(articleRepository.findById(anyLong())).thenReturn(Optional.ofNullable(article));
        ArticleDTO result = articleService.read(1L);

        // then
        assertThat(result).isNotNull();
    }

    /**
     * Test failure case for read() method.
     */
    @Test
    void should_ThrowNoSuchElementException_When_ReadIsCalledWithArticleIdAndFailed() {
        // given
        Article article = mock(Article.class);

        // when
        when(articleRepository.findById(anyLong())).thenThrow(NoSuchElementException.class);

        // then
        assertThatThrownBy(() -> {
            ArticleDTO result = articleService.read(1L);
        }).isInstanceOf(NoSuchElementException.class);
    }

    /**
     * Test success case for modify() method.
     */
    @Test
    void should_ReturnArticleId_When_ModifyIsCalledWithArticleDTOAndSucceed() {
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
     * Test failure case for modify() method.
     */
    @Test
    void should_ThrowNoSuchElementException_When_ModifyIsCalledWithArticleDTOAndFailed() {
        // given
        ArticleDTO articleDTO = mock(ArticleDTO.class);

        // when
        when(articleRepository.findById(anyLong())).thenThrow(NoSuchElementException.class);

        // then
        assertThatThrownBy(() -> {
            Long result = articleService.modify(articleDTO);
        }).isInstanceOf(NoSuchElementException.class);
    }

    /**
     * Test success case for delete() method.
     */
    @Test
    void should_ReturnArticleId_When_DeleteIsCalledWithArticleIdAndSucceed() {
        // given
        Long id = 1L;

        // when
        doNothing().when(articleRepository).deleteById(anyLong());
        articleService.delete(id);

        // then
        verify(articleRepository, times(1)).deleteById(id);
    }

    /**
     * Test failure case for delete() method.
     */
    @Test
    void should_ThrowNoSuchElementException_When_DeleteIsCalledWithArticleIdAndFailed() {
        // given
        Long id = 1L;

        // when
        doThrow(EmptyResultDataAccessException.class).when(articleRepository).deleteById(id);

        // then
        assertThatThrownBy(() -> {
            articleService.delete(id);
        }).isInstanceOf(NoSuchElementException.class);
    }

    /**
     * Test success case for updateView() method.
     */
    @Test
    void should_CalledUpdateViewByIdOneTimes_When_UpdateViewIsCalledAndSucceed() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Article article = mock(Article.class);
        Long id = 1L;

        // when
        doNothing().when(articleRepository).updateViewsById(anyLong());
        articleService.updateView(id, request, response);

        // then
        verify(articleRepository, times(1)).updateViewsById(id);
    }
}
