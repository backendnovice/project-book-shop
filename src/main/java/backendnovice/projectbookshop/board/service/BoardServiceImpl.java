package backendnovice.projectbookshop.board.service;

import backendnovice.projectbookshop.board.domain.Board;
import backendnovice.projectbookshop.board.dto.BoardDTO;
import backendnovice.projectbookshop.board.dto.SearchDTO;
import backendnovice.projectbookshop.board.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public Page<BoardDTO> search(SearchDTO searchDTO, Pageable pageable) {
        if(searchDTO.getTag() != null || searchDTO.getKeyword() != null) {
            if(searchDTO.getTag().equals("title")) {
                return searchWithTitle(searchDTO.getKeyword(), pageable);
            }
            if(searchDTO.getTag().equals("content")) {
                return searchWithContent(searchDTO.getKeyword(), pageable);
            }
            if(searchDTO.getTag().equals("writer")) {
                return searchWithWriter(searchDTO.getKeyword(), pageable);
            }
        }

        return searchAll(pageable);
    }

    @Override
    public BoardDTO write(BoardDTO boardDTO) {
        return convertToDTO(boardRepository.save(convertToEntity(boardDTO)));
    }

    @Override
    public BoardDTO read(Long id) {
        Optional<Board> result = boardRepository.findById(id);

        return convertToDTO(result.get());
    }

    private Page<BoardDTO> searchAll(Pageable pageable) {
        return boardRepository.findAll(pageable).map(this::convertToDTO);
    }

    private Page<BoardDTO> searchWithTitle(String title, Pageable pageable) {
        return boardRepository.findAllByTitleContainsIgnoreCase(title, pageable).map(this::convertToDTO);
    }

    private Page<BoardDTO> searchWithContent(String content, Pageable pageable) {
        return boardRepository.findAllByContentContainsIgnoreCase(content, pageable).map(this::convertToDTO);
    }

    private Page<BoardDTO> searchWithWriter(String writer, Pageable pageable) {
        return boardRepository.findAllByWriterContainsIgnoreCase(writer, pageable).map(this::convertToDTO);
    }

    private BoardDTO convertToDTO(Board board) {
        return BoardDTO.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .date(board.getModifiedDate())
                .build();
    }

    private Board convertToEntity(BoardDTO boardDTO) {
        return Board.builder()
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .build();
    }
}
