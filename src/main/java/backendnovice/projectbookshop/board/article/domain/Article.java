/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-16
 * @desc      : An article-related entity class. that manage columns for Article table.
 * @changelog :
 * 2023-07-25 - backendnovice@gmail.com - create new file.
 * 2023-07-30 - backendnovice@gmail.com - add setter annotation.
 * 2023-08-01 - backendnovice@gmail.com - add column annotation.
 *                                      - change setter annotation to method.
 * 2023-08-13 - backendnovice@gmail.com - change filename to Article.
 * 2023-08-16 - backendnovice@gmail.com - add description annotation.
 */

package backendnovice.projectbookshop.board.article.domain;

import backendnovice.projectbookshop.global.domain.Time;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Article extends Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String writer;

    @Column(columnDefinition = "INTEGER DEFAULT 0", nullable = false)
    private int views;

    @Builder
    public Article(String title, String content, String writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
