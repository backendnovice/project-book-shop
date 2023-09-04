/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-08-20
 * @modified : 2023-09-04
 * @desc     : A comment-related data transfer object class. that used to communicate between layers.
 */

package backendnovice.projectbookshop.board.comment.dto;

import backendnovice.projectbookshop.board.article.domain.Article;
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

    private Article article;
}
