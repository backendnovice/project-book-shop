/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-08-20
 * @modified : 2023-09-04
 * @desc     : A comment-related entity class. that manage columns for Comment table.
 */

package backendnovice.projectbookshop.board.comment.domain;

import backendnovice.projectbookshop.board.article.domain.Article;
import backendnovice.projectbookshop.global.domain.Time;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @Setter
    @Column(name = "COMMENT_CONTENT")
    private String content;

    @Column(name = "COMMENT_WRITER")
    private String writer;

    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID")
    private Article article;

    @Builder
    public Comment(String content, String writer, Article article) {
        this.content = content;
        this.writer = writer;
        this.article = article;
    }
}
