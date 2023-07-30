package backendnovice.projectbookshop.board.controller;

import backendnovice.projectbookshop.board.dto.BoardDTO;
import backendnovice.projectbookshop.board.dto.SearchDTO;
import backendnovice.projectbookshop.board.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/write")
    public String getWritePage() {
        return "board/write";
    }

    @PostMapping("/write")
    public String writeProcess(BoardDTO boardDTO) {
        long result = boardService.write(boardDTO);

        return "redirect:/board/read?id=" + result;
    }

    @GetMapping("/list")
    public String getListPage(Model model, SearchDTO searchDTO, @PageableDefault(size = 5) Pageable pageable) {
        Page<BoardDTO> result = boardService.search(searchDTO, pageable);
        SearchDTO search = new SearchDTO(result);

        model.addAttribute("dto", result);
        model.addAttribute("search", search);

        return "board/list";
    }

    @GetMapping("/read")
    public String getReadPage(Model model, @RequestParam("id") Long id) {
        BoardDTO result = boardService.read(id);
        model.addAttribute("dto", result);

        return "board/read";
    }

    @GetMapping("/modify")
    public String getModifyPage(Model model, @RequestParam("id") Long id) {
        BoardDTO result = boardService.read(id);
        model.addAttribute("dto", result);

        return "board/modify";
    }

    @PostMapping("/modify")
    public String modifyProcess(BoardDTO boardDTO) {
        long result = boardService.modify(boardDTO);

        return "redirect:/board/read?id=" + result;
    }

    @PostMapping("/delete")
    public String deleteProcess(BoardDTO boardDTO) {
        boardService.delete(boardDTO.getId());

        return "redirect:/board/list";
    }
}