/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-30
 * @desc      : CommentRepository test class.
 * @changelog :
 * 2023-08-30 - backendnovice@gmail.com - create new file.
 */

package backendnovice.projectbookshop.board.comment.repository;

import backendnovice.projectbookshop.board.article.domain.Article;
import backendnovice.projectbookshop.board.article.repository.ArticleRepository;
import backendnovice.projectbookshop.board.comment.domain.Comment;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommentRepositoryTests {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    /**
     * Initialize before all test. initialize with 150 fake comments
     */
    @BeforeAll
    void initialize() {
        for(int i = 1; i <= 3; i++) {
            Article article = Article.builder()
                    .title("Title #" + i)
                    .content("Content #" + i)
                    .writer("Writer")
                    .build();

            Article saved = articleRepository.save(article);

            IntStream.rangeClosed(1, 50).forEach(j -> {
                Comment comment = Comment.builder()
                        .content("Comment #" + j)
                        .writer("Writer #" + j)
                        .article(article)
                        .build();

                commentRepository.save(comment);
            });
        }
    }

    /**
     * Test comment select query method with title on succeed.
     */
    @Test
    void should_ReturnCommentTypePageObject_When_FindAllByArticleIdIsCalledAndSucceed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Long articleId = 1L;

        // when
        Page<Comment> result = commentRepository.findAllByArticleId(articleId, pageable);

        // then
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.getTotalPages()).isEqualTo(5);
        assertThat(result.getTotalElements()).isEqualTo(50);
    }

    /**
     * Test comment select query method with title on failed.
     */
    @Test
    void should_ReturnEmptyObject_When_FindAllByArticleIdIsCalledAndFailed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Long articleId = 4L;

        // when
        Page<Comment> result = commentRepository.findAllByArticleId(articleId, pageable);

        // then
        assertThat(result.isEmpty());
    }
}
