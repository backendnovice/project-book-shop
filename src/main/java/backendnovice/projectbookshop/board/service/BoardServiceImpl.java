package backendnovice.projectbookshop.board.service;

import backendnovice.projectbookshop.board.domain.Board;
import backendnovice.projectbookshop.board.dto.BoardDTO;
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
    public Page<BoardDTO> searchAll(Pageable pageable) {
        return boardRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Override
    public Page<BoardDTO> searchWithOptions(BoardDTO boardDTO, Pageable pageable) {
        if(boardDTO.getTitle() != null) {
            return searchWithTitle(boardDTO.getTitle(), pageable);
        }
        if(boardDTO.getContent() != null) {
            return searchWithContent(boardDTO.getContent(), pageable);
        }
        if(boardDTO.getWriter() != null) {
            return searchWithWriter(boardDTO.getWriter(), pageable);
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
