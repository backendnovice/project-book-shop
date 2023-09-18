/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-07-25
 * @modified : 2023-09-18
 * @desc     : 게시글 테이블을 관리하는 엔티티 클래스.
 */

package backendnovice.projectbookshop.board.article.domain;

import backendnovice.projectbookshop.board.comment.domain.Comment;
import backendnovice.projectbookshop.global.domain.Time;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @Builder
    public Article(String title, String content, String writer, List<Comment> comments) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.comments = comments;
    }
}
