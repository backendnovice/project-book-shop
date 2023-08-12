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

    @AfterAll
    void finalizeAfterAllTests() {
        articleRepository.deleteAll();
    }

    @Test
    @DisplayName("Select Board Test with Title (Success)")
    void findAllByTitleContainsIgnoreCaseTest_Success() {
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

    @Test
    @DisplayName("Select Board Test with Title (Failure)")
    void findAllByTitleContainsIgnoreCaseTest_Failure() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String title = "wrong";

        // when
        Page<Article> result = articleRepository.findAllByTitleContainsIgnoreCase(title, pageable);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Select Board Test with Content (Success)")
    void findAllByContentContainsIgnoreCaseTest_Success() {
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

    @Test
    @DisplayName("Select Board Test with Content (Failure)")
    void findAllByContentContainsIgnoreCaseTest_Failure() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String content = "wrong";

        // when
        Page<Article> result = articleRepository.findAllByContentContainsIgnoreCase(content, pageable);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Select Board Test with Writer (Success)")
    void findAllByWriterContainsIgnoreCaseTest_Success() {
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

    @Test
    @DisplayName("Select Board Test with Writer (Failure)")
    void findAllByWriterContainsIgnoreCaseTest_Failure() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String writer = "wrong";

        // when
        Page<Article> result = articleRepository.findAllByWriterContainsIgnoreCase(writer, pageable);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Update View count Test with ID")
    void updateViewByIdTest() {
        // given
        Long id = 1L;

        // when
        int result = articleRepository.updateViewById(id);

        // then
        assertThat(result).isGreaterThan(0);
    }
}
