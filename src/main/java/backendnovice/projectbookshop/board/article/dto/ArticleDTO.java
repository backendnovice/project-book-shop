/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-07-25
 * @modified : 2023-09-04
 * @desc     : An article-related data transfer object class. that used to communicate between layers.
 */

package backendnovice.projectbookshop.board.article.dto;

import backendnovice.projectbookshop.board.comment.dto.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {
    private Long id;

    private String title;

    private String content;

    private String writer;

    private int views;

    private Page<CommentDTO> commentPages;

    private int commentCount;

    private LocalDateTime date;
}
