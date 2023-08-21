/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-22
 * @desc      : A comment-related entity class. that manage columns for Comment table.
 * @changelog :
 * 2023-08-20 - backendnovice@gmail.com - create new file.
 * 2023-08-21 - backendnovice@gmail.com - rename columns & add article relations.
 * 2023-08-22 - backendnovice@gmail.com - add setter annotation.
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
    public Comment(String content, String writer) {
        this.content = content;
        this.writer = writer;
    }
}
