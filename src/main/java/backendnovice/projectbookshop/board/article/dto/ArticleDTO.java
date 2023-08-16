/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-16
 * @desc      : An article-related data transfer object class. that used to communicate between layers.
 * @changelog :
 * 2023-07-25 - backendnovice@gmail.com - create new file.
 * 2023-07-26 - backendnovice@gmail.com - add constructor annotation.
 * 2023-08-01 - backendnovice@gmail.com - add view count.
 * 2023-08-13 - backendnovice@gmail.com - change filename to ArticleDTO.
 * 2023-08-16 - backendnovice@gmail.com - add description annotation.
 */

package backendnovice.projectbookshop.board.article.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private LocalDateTime date;
}
