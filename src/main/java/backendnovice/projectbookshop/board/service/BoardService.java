package backendnovice.projectbookshop.board.service;

import backendnovice.projectbookshop.board.dto.BoardDTO;
import backendnovice.projectbookshop.board.dto.SearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    Page<BoardDTO> search(SearchDTO searchDTO, Pageable pageable);

    Long write(BoardDTO boardDTO);

    Long modify(BoardDTO boardDTO);

    void delete(Long id);

    void updateView(Long id);

    BoardDTO read(Long id);
}
