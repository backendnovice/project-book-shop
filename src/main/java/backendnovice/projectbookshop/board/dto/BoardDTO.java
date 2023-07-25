package backendnovice.projectbookshop.board.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardDTO {
    private Long id;

    private String title;

    private String content;

    private String writer;

    private LocalDateTime date;
}
