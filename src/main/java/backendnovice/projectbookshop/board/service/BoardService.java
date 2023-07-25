package backendnovice.projectbookshop.board.service;

import backendnovice.projectbookshop.board.dto.BoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    Page<BoardDTO> searchAll(Pageable pageable);

    Page<BoardDTO> searchWithTitle(String title, Pageable pageable);

    Page<BoardDTO> searchWithContent(String content, Pageable pageable);

    Page<BoardDTO> searchWithWriter(String writer, Pageable pageable);
}
