/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-08-30
 * @modified : 2023-09-19
 * @desc     : CommentRepository 테스트 클래스.
 */

package backendnovice.projectbookshop.board.comment.repository;

import backendnovice.projectbookshop.board.article.domain.Article;
import backendnovice.projectbookshop.board.article.repository.ArticleRepository;
import backendnovice.projectbookshop.board.comment.domain.Comment;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
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
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    /**
     * 모든 테스트 전 150개의 가짜 댓글을 생성한다.
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
                        .article(saved)
                        .build();

                commentRepository.save(comment);
            });
        }
    }

    /**
     * 올바른 게시글 ID를 파라미터로 댓글 조회 쿼리 메소드를 실행하고 성공 케이스를 테스트한다.
     */
    @Test
    @Disabled
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
     * 잘못된 게시글 ID를 파라미터로 댓글 조회 쿼리 메소드를 실행하고 실패 케이스를 테스트한다.
     */
    @Test
    void should_ReturnEmptyObject_When_FindAllByArticleIdIsCalledAndFailed() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Long articleId = 4L;

        // when
        Page<Comment> result = commentRepository.findAllByArticleId(articleId, pageable);

        // then
        assertThat(result.isEmpty()).isTrue();
    }
}
