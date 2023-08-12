package backendnovice.projectbookshop.board.article.repository;

import backendnovice.projectbookshop.board.article.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAllByTitleContainsIgnoreCase(String title, Pageable pageable);

    Page<Article> findAllByContentContainsIgnoreCase(String content, Pageable pageable);

    Page<Article> findAllByWriterContainsIgnoreCase(String writer, Pageable pageable);

    @Modifying
    @Query("UPDATE Article b SET b.views = b.views + 1 WHERE b.id = :id")
    int updateViewById(@Param("id") Long id);
}
