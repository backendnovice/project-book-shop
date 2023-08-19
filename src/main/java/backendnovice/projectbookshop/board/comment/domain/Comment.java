/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-20
 * @desc      : A comment-related entity class. that manage columns for Comment table.
 * @changelog :
 * 2023-08-20 - backendnovice@gmail.com - create new file.
 */

package backendnovice.projectbookshop.board.comment.domain;

import backendnovice.projectbookshop.global.domain.Time;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String writer;

    @Builder
    public Comment(String content, String writer) {
        this.content = content;
        this.writer = writer;
    }
}
