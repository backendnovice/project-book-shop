package backendnovice.projectbookshop.board.controller;

import backendnovice.projectbookshop.board.dto.BoardDTO;
import backendnovice.projectbookshop.board.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/list")
    public String getListPage(Model model, Pageable pageable) {
        Page<BoardDTO> result = boardService.searchAll(pageable);
        Pageable pageResult = result.getPageable();
        model.addAttribute("dto", result);
        model.addAttribute("page", pageResult);

        return "board/list";
    }

    @GetMapping("/list/search")
    public String getListSearchPage(Model model, BoardDTO boardDTO, Pageable pageable) {
        Page<BoardDTO> result = boardService.searchWithOptions(boardDTO, pageable);
        model.addAttribute("dto", result);

        return "board/list";
    }

    @GetMapping("/write")
    public String getWritePage() {
        return "board/write";
    }

    @PostMapping("/write")
    public String writeProcess(Model model, BoardDTO boardDTO) {
        BoardDTO result = boardService.write(boardDTO);
        model.addAttribute("id", result.getId());

        return "board/read";
    }

    @GetMapping("/read/{id}")
    public String getReadPage(Model model, @PathVariable("id") Long id) {
        BoardDTO result = boardService.read(id);
        model.addAttribute("dto", result);

        return "board/read";
    }
}