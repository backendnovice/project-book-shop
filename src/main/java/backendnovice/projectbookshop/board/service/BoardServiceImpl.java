package backendnovice.projectbookshop.board.service;

import backendnovice.projectbookshop.board.domain.Board;
import backendnovice.projectbookshop.board.dto.BoardDTO;
import backendnovice.projectbookshop.board.dto.SearchDTO;
import backendnovice.projectbookshop.board.repository.BoardRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Long write(BoardDTO boardDTO) {
        Board result = boardRepository.save(convertToEntity(boardDTO));

        return result.getId();
    }

    @Override
    public BoardDTO read(Long id) {
        Optional<Board> result = boardRepository.findById(id);

        return convertToDTO(result.get());
    }

    @Override
    public Long modify(BoardDTO boardDTO) {
        Board board = boardRepository.findById(boardDTO.getId()).get();
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        boardRepository.save(board);

        return board.getId();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateView(Long id) {
        boardRepository.updateViewById(id);
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
                .view(board.getView())
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
