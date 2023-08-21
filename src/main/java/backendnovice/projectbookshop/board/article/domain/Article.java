/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-21
 * @desc      : An article-related entity class. that manage columns for Article table.
 * @changelog :
 * 2023-07-25 - backendnovice@gmail.com - create new file.
 * 2023-07-30 - backendnovice@gmail.com - add setter annotation.
 * 2023-08-01 - backendnovice@gmail.com - add column annotation.
 *                                      - change setter annotation to method.
 * 2023-08-13 - backendnovice@gmail.com - change filename to Article.
 * 2023-08-16 - backendnovice@gmail.com - add description annotation.
 * 2023-08-21 - backendnovice@gmail.com - rename column by annotation.
 *                                      - add Comment entity relations.
 */

package backendnovice.projectbookshop.board.article.domain;

import backendnovice.projectbookshop.board.comment.domain.Comment;
import backendnovice.projectbookshop.global.domain.Time;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Article extends Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ARTICLE_ID")
    private Long id;

    @Setter
    @Column(name = "ARTICLE_TITLE", nullable = false)
    private String title;

    @Setter
    @Column(name = "ARTICLE_CONTENT", nullable = false)
    private String content;

    @Column(name = "ARTICLE_WRITER", nullable = false)
    private String writer;

    @Column(name = "ARTICLE_VIEWS", columnDefinition = "INTEGER DEFAULT 0", nullable = false)
    private int views;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Article(String title, String content, String writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }
}
