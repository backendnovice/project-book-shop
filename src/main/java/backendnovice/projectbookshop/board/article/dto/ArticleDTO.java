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
