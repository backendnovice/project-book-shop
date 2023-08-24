/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-24
 * @desc      : ArticleRepository test class.
 * @changelog :
 * 2023-07-25 - backendnovice@gmail.com - create new file.
 * 2023-08-01 - backendnovice@gmail.com - add update view count test.
 * 2023-08-13 - backendnovice@gmail.com - change filename to ArticleRepositoryTests.
 * 2023-08-17 - backendnovice@gmail.com - add description annotation.
 * 2023-08-24 - backendnovice@gmail.com - apply method naming convention.
 */

package backendnovice.projectbookshop.board.repository;

import backendnovice.projectbookshop.board.article.domain.Article;
import backendnovice.projectbookshop.board.article.repository.ArticleRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest                                    // Load jpa related settings, include in memory db, transactional.
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Change lifecycle per class. to use @BeforeAll, @AfterAll correctly.
public class ArticleRepositoryTests {
    @Autowired
    private ArticleRepository articleRepository;

    /**
     * Initialize before all tests. initialize with 1 ~ 50 fake article.
     */
    @BeforeAll
    void initializeBeforeAllTests() {
        IntStream.rangeClosed(1, 50).forEach(i -> {
            Article article = Article.builder()
                    .title("Title #" + i)
                    .content("Content #" + i)
                    .writer("Writer")
                    .build();

            articleRepository.save(article);
        });
    }

    /**
     * finalize after all tests. delete all repository data.
     */
    @AfterAll
    void finalizeAfterAllTests() {
        articleRepository.deleteAll();
    }

    /**
     * Test article select query method with title on succeed.
     */
    @Test
    void should_ReturnArticleTypePageObject_When_FindAllByTitleContainsIgnoreCaseIsCalledAndSucceed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String title = "title";

        // when
        Page<Article> result = articleRepository.findAllByTitleContainsIgnoreCase(title, pageable);

        // then
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.getTotalPages()).isEqualTo(5);
        assertThat(result.getTotalElements()).isEqualTo(50);
    }

    /**
     * Test article select query method with title on failure.
     */
    @Test
    void should_ReturnArticleTypePageObject_When_FindAllByTitleContainsIgnoreCaseIsCalledAndFailed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String title = "wrong";

        // when
        Page<Article> result = articleRepository.findAllByTitleContainsIgnoreCase(title, pageable);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    /**
     * Test article select query method with content on succeed.
     */
    @Test
    void should_ReturnArticleTypePageObject_When_FindAllByContentContainsIgnoreCaseIsCalledAndSucceed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String content = "content";

        // when
        Page<Article> result = articleRepository.findAllByContentContainsIgnoreCase(content, pageable);

        // then
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.getTotalPages()).isEqualTo(5);
        assertThat(result.getTotalElements()).isEqualTo(50);
    }

    /**
     * Test article select query method with content on failure.
     */
    @Test
    void should_ReturnArticleTypePageObject_When_FindAllByContentContainsIgnoreCaseIsCalledAndFailed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String content = "wrong";

        // when
        Page<Article> result = articleRepository.findAllByContentContainsIgnoreCase(content, pageable);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    /**
     * Test article select query method with writer on succeed.
     */
    @Test
    void should_ReturnArticleTypePageObject_When_FindAllByWriterContainsIgnoreCaseIsCalledAndSucceed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String writer = "writer";

        // when
        Page<Article> result = articleRepository.findAllByWriterContainsIgnoreCase(writer, pageable);

        // then
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.getTotalPages()).isEqualTo(5);
        assertThat(result.getTotalElements()).isEqualTo(50);
    }

    /**
     * Test article select query method with writer on failure.
     */
    @Test
    void should_ReturnArticleTypePageObject_When_FindAllByWriterContainsIgnoreCaseIsCalledAndFailed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String writer = "wrong";

        // when
        Page<Article> result = articleRepository.findAllByWriterContainsIgnoreCase(writer, pageable);

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
        Article article = articleRepository.findById(id).orElseGet(null);
        assertThat(article.getViews()).isGreaterThan(0);
    }
}
