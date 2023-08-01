package backendnovice.projectbookshop.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Long id;

    private String title;

    private String content;

    private String writer;

    private int view;

    private LocalDateTime date;
}
