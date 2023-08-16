/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-16
 * @desc      : An article-related service implementation class. that actually implement business logic for article.
 * @changelog :
 * 2023-07-25 - backendnovice@gmail.com - create new file.
 * 2023-07-26 - backendnovice@gmail.com - integrate search method.
 * 2023-07-29 - backendnovice@gmail.com - rename search method.
 * 2023-07-30 - backendnovice@gmail.com - add write, modify, delete method.
 * 2023-08-01 - backendnovice@gmail.com - add update view count method.
 * 2023-08-13 - backendnovice@gmail.com - change filename to ArticleServiceImpl.
 *                                      - add prevent double hit cookie method.
 * 2023-08-16 - backendnovice@gmail.com - add description annotation.
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final String COOKIE_NAME = "COOKIE_VIEWED";
    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    /**
     * Get search tag and keyword and select article with that data.
     * @param pageDTO
     *      Page data transfer object including tag and keyword.
     * @param pageable
     *      Pageable object.
     * @return
     *      Page object that include found articles.
     */
    @Override
    public Page<ArticleDTO> search(PageDTO pageDTO, Pageable pageable) {
        if(pageDTO.getTag() != null || pageDTO.getKeyword() != null) {
            if(pageDTO.getTag().equals("title")) {
                return searchWithTitle(pageDTO.getKeyword(), pageable);
            }
            if(pageDTO.getTag().equals("content")) {
                return searchWithContent(pageDTO.getKeyword(), pageable);
            }
            if(pageDTO.getTag().equals("writer")) {
                return searchWithWriter(pageDTO.getKeyword(), pageable);
            }
        }

        return searchAll(pageable);
    }

    /**
     * Create article with DTO of parameter.
     * @param articleDTO
     *      Article data transfer object.
     * @return
     *      Created article id.
     */
    @Override
    public Long write(ArticleDTO articleDTO) {
        Article result = articleRepository.save(convertToEntity(articleDTO));

        return result.getId();
    }

    /**
     * Select article that matches id of parameter.
     * @param id
     *      Article id.
     * @return
     *      Found article data transfer object.
     */
    @Override
    public ArticleDTO read(Long id) {
        Optional<Article> result = articleRepository.findById(id);

        return convertToDTO(result.get());
    }

    /**
     * Modify article that matches id of dto.
     * @param articleDTO
     *      Article data transfer object including new data.
     * @return
     *      Modified article id.
     */
    @Override
    public Long modify(ArticleDTO articleDTO) {
        Article article = articleRepository.findById(articleDTO.getId()).get();
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        articleRepository.save(article);

        return article.getId();
    }

    /**
     * Delete article that matches id of parameter.
     * @param id
     *      Article id.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        articleRepository.deleteById(id);
    }

    /**
     * Update view count after check cookies for prevent increasing views redundancy.
     * @param articleId
     *      Article id.
     * @param request
     *      Request cookies to check article visited today already.
     * @param response
     *      Response new cookie when request cookie not exists.
     */
    @Override
    @Transactional
    public void updateView(Long articleId, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        boolean isCookieExists = false;

        if(cookies == null) {
            Cookie newCookie = createDoubleHitPreventionCookie(articleId);
            response.addCookie(newCookie);
            articleRepository.updateViewById(articleId);
        }else {
            final String COOKIE_NAME_ID = COOKIE_NAME + articleId;
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals(COOKIE_NAME_ID)) {
                    isCookieExists = true;
                    break;
                }
            }
            if(!isCookieExists) {
                Cookie newCookie = createDoubleHitPreventionCookie(articleId);
                response.addCookie(newCookie);
                articleRepository.updateViewById(articleId);
            }
        }
    }

    /**
     * Execute article selection query without any options.
     * @param pageable
     *      Pageable object.
     * @return
     *      Page object that include found articles.
     */
    private Page<ArticleDTO> searchAll(Pageable pageable) {
        return articleRepository.findAll(pageable).map(this::convertToDTO);
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
        return articleRepository.findAllByTitleContainsIgnoreCase(title, pageable).map(this::convertToDTO);
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
        return articleRepository.findAllByContentContainsIgnoreCase(content, pageable).map(this::convertToDTO);
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
        return articleRepository.findAllByWriterContainsIgnoreCase(writer, pageable).map(this::convertToDTO);
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
