package backendnovice.projectbookshop.board.repository;

import backendnovice.projectbookshop.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByTitleContainsIgnoreCase(String title, Pageable pageable);

    Page<Board> findAllByContentContainsIgnoreCase(String content, Pageable pageable);

    Page<Board> findAllByWriterContainsIgnoreCase(String writer, Pageable pageable);

    @Modifying
    @Query("UPDATE Board b SET b.view = b.view + 1 WHERE b.id = :id")
    int updateViewById(@Param("id") Long id);
}
