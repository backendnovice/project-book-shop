/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-07-25
 * @modified : 2023-09-04
 * @desc     : ArticleRepository test class.
 */

package backendnovice.projectbookshop.board.article.repository;

import backendnovice.projectbookshop.board.article.domain.Article;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest // Load jpa related settings, include in memory db, transactional.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArticleRepositoryTests {
    @Autowired
    private ArticleRepository articleRepository;

    /**
     * Initialize before all tests. initialize with 1 ~ 50 fake article.
     */
    @BeforeAll
    void initialize() {
        IntStream.rangeClosed(1, 50).forEach(i -> {
            Article article = Article.builder()
                    .title("Title #" + i)
                    .content("Content #" + i)
                    .writer("Writer")
                    .build();

            articleRepository.save(article);
        });
    }

    @Test
    void should_ReturnPageTypeObjectIncludingArticleAndCommentCount_When_FindAllWithCommentCountIsCalledAndSucceed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Object[]> result = articleRepository.findAllWithCommentCount(pageable);

        // then
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.getTotalPages()).isEqualTo(5);
        assertThat(result.getTotalElements()).isEqualTo(50);

    }

    /**
     * Test article select query method with title on succeed.
     */
    @Test
    void should_ReturnPageTypeObjectIncludingArticleAndCommentCount_When_FindAllByTitleWithCommentCountIsCalledAndSucceed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String title = "title";

        // when
        Page<Object[]> result = articleRepository.findAllByTitleWithCommentCount(title, pageable);

        // then
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.getTotalPages()).isEqualTo(5);
        assertThat(result.getTotalElements()).isEqualTo(50);
    }

    /**
     * Test article select query method with title on failure.
     */
    @Test
    void should_ReturnEmptyObject_When_FindAllByTitleWithCommentCountIsCalledAndFailed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String title = "wrong";

        // when
        Page<Object[]> result = articleRepository.findAllByTitleWithCommentCount(title, pageable);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    /**
     * Test article select query method with content on succeed.
     */
    @Test
    void should_ReturnPageTypeObjectIncludingArticleAndCommentCount_When_FindAllByContentWithCommentCountIsCalledAndSucceed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String content = "content";

        // when
        Page<Object[]> result = articleRepository.findAllByContentWithCommentCount(content, pageable);

        // then
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.getTotalPages()).isEqualTo(5);
        assertThat(result.getTotalElements()).isEqualTo(50);
    }

    /**
     * Test article select query method with content on failure.
     */
    @Test
    void should_ReturnEmptyObject_When_FindAllByContentWithCommentCountIsCalledAndFailed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String content = "wrong";

        // when
        Page<Object[]> result = articleRepository.findAllByContentWithCommentCount(content, pageable);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    /**
     * Test article select query method with writer on succeed.
     */
    @Test
    void should_ReturnPageTypeObjectIncludingArticleAndCommentCount_When_FindAllByWriterWithCommentCountIsCalledAndSucceed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String writer = "writer";

        // when
        Page<Object[]> result = articleRepository.findAllByWriterWithCommentCount(writer, pageable);

        // then
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.getTotalPages()).isEqualTo(5);
        assertThat(result.getTotalElements()).isEqualTo(50);
    }

    /**
     * Test article select query method with writer on failure.
     */
    @Test
    void should_ReturnEmptyObject_When_FindAllByWriterWithCommentCountIsCalledAndFailed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String writer = "wrong";

        // when
        Page<Object[]> result = articleRepository.findAllByWriterWithCommentCount(writer, pageable);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    /**
     * Test update view count query method with id.
     */
    @Test
    @DisplayName("Update View count Test with ID")
    void Should_IncreaseArticleViews_When_UpdateViewsByIdIsCalled() {
        // given
        Long id = 1L;

        // when
        articleRepository.updateViewsById(id);

        // then
        Article article = articleRepository.findById(id).get();
        assertThat(article.getViews()).isGreaterThan(0);
    }
}
