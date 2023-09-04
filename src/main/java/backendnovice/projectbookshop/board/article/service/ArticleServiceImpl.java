/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-07-25
 * @modified : 2023-09-04
 * @desc     : An article-related service implementation class. that actually implement business logic for article.
 */

package backendnovice.projectbookshop.board.article.service;

import backendnovice.projectbookshop.board.article.domain.Article;
import backendnovice.projectbookshop.board.article.dto.ArticleDTO;
import backendnovice.projectbookshop.global.dto.PageDTO;
import backendnovice.projectbookshop.board.article.repository.ArticleRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final String COOKIE_NAME = "COOKIE_VIEWED";

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
    public Page<ArticleDTO> searchByTags(PageDTO pageDTO, Pageable pageable) {
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
    public void updateView(Long articleId, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        boolean isCookieExists = checkCookieExists(cookies, articleId);

        if(!isCookieExists) {
            Cookie newCookie = createDoubleHitPreventionCookie(articleId);
            response.addCookie(newCookie);
            articleRepository.updateViewsById(articleId);
        }
    }

    /**
     * Execute article selection query with title.
     * @param title
     *      Article Title.
     * @param pageable
     *      Pageable object.
     * @return
     *      Page object that include found articles.
     */
    private Page<ArticleDTO> searchWithTitle(String title, Pageable pageable) {
        Page<Object[]> result = articleRepository.findAllByTitleWithCommentCount(title, pageable);

        if(result.isEmpty()) {
            throw new NoSuchElementException();
        }

        return result.map(this::convertObjectToDTO);
    }

    /**
     * Execute article selection query with content.
     * @param content
     *      Article content.
     * @param pageable
     *      Pageable object.
     * @return
     *      Page object that include found articles.
     */
    private Page<ArticleDTO> searchWithContent(String content, Pageable pageable) {
        Page<Object[]> result = articleRepository.findAllByContentWithCommentCount(content, pageable);

        if(result.isEmpty()) {
            throw new NoSuchElementException();
        }

        return result.map(this::convertObjectToDTO);
    }

    /**
     * Execute article selection query with writer.
     * @param writer
     *      Article writer.
     * @param pageable
     *      Pageable object.
     * @return
     *      Page object that include found articles.
     */
    private Page<ArticleDTO> searchWithWriter(String writer, Pageable pageable) {
        Page<Object[]> result = articleRepository.findAllByWriterWithCommentCount(writer, pageable);

        if(result.isEmpty()) {
            throw new NoSuchElementException();
        }

        return result.map(this::convertObjectToDTO);
    }

    /**
     * Convert Article object to ArticleDTO object.
     * @param article
     *      Article object for convert.
     * @return
     *      Converted ArticleDTO object.
     */
    private ArticleDTO convertToDTO(Article article) {
        return ArticleDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .writer(article.getWriter())
                .date(article.getModifiedDate())
                .build();
    }

    /**
     * Convert Object[] including Article and commentCount to ArticleDTO.
     * @param object
     *      Object[] for convert.
     * @return
     *      Converted ArticleDTO object.
     */
    private ArticleDTO convertObjectToDTO(Object[] object) {
        Article article = (Article) object[0];
        int commentCount = (int) object[1];

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
     * Convert ArticleDTO object to Article object.
     * @param articleDTO
     *      ArticleDTO object for convert.
     * @return
     *      Converted Article object.
     */
    private Article convertToEntity(ArticleDTO articleDTO) {
        return Article.builder()
                .title(articleDTO.getTitle())
                .content(articleDTO.getContent())
                .writer(articleDTO.getWriter())
                .build();
    }

    /**
     * Check cookie exists with cookie name.
     * @param cookies
     *      Cookies array.
     * @param articleId
     *      Article id.
     * @return
     *      Checking result.
     */
    private boolean checkCookieExists(Cookie[] cookies, Long articleId) {
        if(cookies == null) {
            return false;
        }

        final String COOKIE_NAME_ID = COOKIE_NAME + articleId;
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(COOKIE_NAME_ID)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Create Double hit prevention Cookie expiring at tomorrow.
     * @param articleId
     *      Article id.
     * @return
     *      New cookie.
     */
    private Cookie createDoubleHitPreventionCookie(Long articleId) {
        Cookie cookie = new Cookie(COOKIE_NAME + articleId, String.valueOf(articleId));
        cookie.setMaxAge(getSecondsLeftTomorrow());
        cookie.setHttpOnly(true);

        return cookie;
    }

    /**
     * Measure times left until tomorrow by seconds.
     * @return
     *      Seconds left until tomorrow.
     */
    private int getSecondsLeftTomorrow() {
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime tomorrowTime = LocalDateTime.now().plusDays(1L).truncatedTo(ChronoUnit.DAYS);

        return (int) nowTime.until(tomorrowTime, ChronoUnit.SECONDS);
    }
}
