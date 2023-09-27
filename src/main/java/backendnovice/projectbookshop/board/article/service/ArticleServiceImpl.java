/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-07-25
 * @modified : 2023-09-27
 * @desc     : 게시글 관련 로직을 구현하는 서비스 클래스.
 */

package backendnovice.projectbookshop.board.article.service;

import backendnovice.projectbookshop.board.article.domain.Article;
import backendnovice.projectbookshop.board.article.dto.ArticleDTO;
import backendnovice.projectbookshop.global.dto.PaginationDTO;
import backendnovice.projectbookshop.board.article.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Page<ArticleDTO> searchAll(Pageable pageable) {
        Page<Object[]> result = articleRepository.findAllWithCommentCount(pageable);

        if(result.isEmpty()) {
            throw new NoSuchElementException();
        }

        return result.map(this::convertObjectToDTO);
    }

    @Override
    public Page<ArticleDTO> searchByTags(PaginationDTO pageDTO, Pageable pageable) {
        String tag = pageDTO.getTag();
        String keyword = pageDTO.getKeyword();
        Page<ArticleDTO> result;

        if(tag.equals("title")) {
            result = searchWithTitle(keyword, pageable);
        }else if(tag.equals("content")) {
            result = searchWithContent(keyword, pageable);
        }else if(tag.equals("writer")) {
            result = searchWithWriter(keyword, pageable);
        }else {
            throw new IllegalArgumentException();
        }

        return result;
    }

    @Override
    public Long write(ArticleDTO articleDTO) {
        Article result = articleRepository.save(convertToEntity(articleDTO));

        return result.getId();
    }

    @Override
    public ArticleDTO read(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);

        return convertToDTO(article);
    }

    @Override
    public Optional<Article> getArticle(Long id) {
        return articleRepository.findById(id);
    }

    @Override
    @Transactional
    public Long modify(ArticleDTO articleDTO) {
        Article article = articleRepository.findById(articleDTO.getId())
                .orElseThrow(NoSuchElementException::new);
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        articleRepository.save(article);

        return article.getId();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            articleRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    @Transactional
    public void updateViews(Long id) {
        articleRepository.updateViewsById(id);
    }

    /**
     * 게시글 제목 검색 쿼리 메소드를 실행한다.
     * @param title
     *      게시글 제목
     * @param pageable
     *      페이지네이션 객체
     * @return
     *      검색 결과
     */
    private Page<ArticleDTO> searchWithTitle(String title, Pageable pageable) {
        Page<Object[]> result = articleRepository.findAllByTitleWithCommentCount(title, pageable);

        if(result.isEmpty()) {
            throw new NoSuchElementException();
        }

        return result.map(this::convertObjectToDTO);
    }

    /**
     * 게시글 내용 검색 쿼리 메소드를 실행한다.
     * @param content
     *      게시글 내용
     * @param pageable
     *      페이지네이션 객체
     * @return
     *      검색 결과
     */
    private Page<ArticleDTO> searchWithContent(String content, Pageable pageable) {
        Page<Object[]> result = articleRepository.findAllByContentWithCommentCount(content, pageable);

        if(result.isEmpty()) {
            throw new NoSuchElementException();
        }

        return result.map(this::convertObjectToDTO);
    }

    /**
     * 게시글 작성자 검색 쿼리 메소드를 실행한다.
     * @param writer
     *      게시글 작성자
     * @param pageable
     *      페이지네이션 객체
     * @return
     *      검색 결과
     */
    private Page<ArticleDTO> searchWithWriter(String writer, Pageable pageable) {
        Page<Object[]> result = articleRepository.findAllByWriterWithCommentCount(writer, pageable);

        if(result.isEmpty()) {
            throw new NoSuchElementException();
        }

        return result.map(this::convertObjectToDTO);
    }

    /**
     * Article 객체를 데이터 전달 객체로 변환한다.
     * @param article
     *      변환할 객체
     * @return
     *      변환된 객체
     */
    private ArticleDTO convertToDTO(Article article) {
        return ArticleDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .writer(article.getWriter())
                .date(article.getModifiedDate())
                .views(article.getViews())
                .build();
    }

    /**
     * 게시글과 조회수를 담고있는 Object[] 객체를 데이터 전달 객체로 변환한다.
     * @param object
     *      변환할 객체
     * @return
     *      변환된 객체
     */
    private ArticleDTO convertObjectToDTO(Object[] object) {
        Article article = (Article) object[0];
        long commentCount = (long) object[1];

        return ArticleDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .writer(article.getWriter())
                .views(article.getViews())
                .commentCount(commentCount)
                .date(article.getModifiedDate())
                .build();
    }

    /**
     * Article 데이터 전달 객체를 Article 객체로 변환한다.
     * @param articleDTO
     *      변환할 객체
     * @return
     *      변환된 객체
     */
    private Article convertToEntity(ArticleDTO articleDTO) {
        return Article.builder()
                .title(articleDTO.getTitle())
                .content(articleDTO.getContent())
                .writer(articleDTO.getWriter())
                .build();
    }
}
