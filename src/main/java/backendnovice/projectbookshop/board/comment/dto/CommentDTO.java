/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-20
 * @desc      : A comment-related data transfer object class. that used to communicate between layers.
 * @changelog :
 * 2023-08-20 - backendnovice@gmail.com - create new file.
 */

package backendnovice.projectbookshop.board.comment.dto;

import backendnovice.projectbookshop.board.article.domain.Article;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;

    private String content;

    private String writer;

    @ManyToOne
    @JoinColumn(name = "id")
    private Article article;
}
