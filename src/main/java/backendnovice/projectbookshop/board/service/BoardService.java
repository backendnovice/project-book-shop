package backendnovice.projectbookshop.board.service;

import backendnovice.projectbookshop.board.dto.BoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    Page<BoardDTO> searchAll(Pageable pageable);

    Page<BoardDTO> searchWithOptions(BoardDTO boardDTO, Pageable pageable);

    BoardDTO write(BoardDTO boardDTO);

    BoardDTO read(Long id);
}
