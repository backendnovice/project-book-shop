package backendnovice.projectbookshop.board.service;

import backendnovice.projectbookshop.board.dto.BoardDTO;
import backendnovice.projectbookshop.board.dto.SearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    Page<BoardDTO> search(SearchDTO boardDTO, Pageable pageable);

    BoardDTO write(BoardDTO boardDTO);

    BoardDTO read(Long id);
}
