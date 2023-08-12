package backendnovice.projectbookshop.board.article.service;

import backendnovice.projectbookshop.board.article.domain.Article;
import backendnovice.projectbookshop.board.article.dto.ArticleDTO;
import backendnovice.projectbookshop.global.dto.SearchDTO;
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

    @Override
    public Page<ArticleDTO> search(SearchDTO searchDTO, Pageable pageable) {
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
    public Long write(ArticleDTO articleDTO) {
        Article result = articleRepository.save(convertToEntity(articleDTO));

        return result.getId();
    }

    @Override
    public ArticleDTO read(Long id) {
        Optional<Article> result = articleRepository.findById(id);

        return convertToDTO(result.get());
    }

    @Override
    public Long modify(ArticleDTO articleDTO) {
        Article article = articleRepository.findById(articleDTO.getId()).get();
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        articleRepository.save(article);

        return article.getId();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        articleRepository.deleteById(id);
    }

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

    private Page<ArticleDTO> searchAll(Pageable pageable) {
        return articleRepository.findAll(pageable).map(this::convertToDTO);
    }

    private Page<ArticleDTO> searchWithTitle(String title, Pageable pageable) {
        return articleRepository.findAllByTitleContainsIgnoreCase(title, pageable).map(this::convertToDTO);
    }

    private Page<ArticleDTO> searchWithContent(String content, Pageable pageable) {
        return articleRepository.findAllByContentContainsIgnoreCase(content, pageable).map(this::convertToDTO);
    }

    private Page<ArticleDTO> searchWithWriter(String writer, Pageable pageable) {
        return articleRepository.findAllByWriterContainsIgnoreCase(writer, pageable).map(this::convertToDTO);
    }

    private ArticleDTO convertToDTO(Article article) {
        return ArticleDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .writer(article.getWriter())
                .date(article.getModifiedDate())
                .build();
    }

    private Article convertToEntity(ArticleDTO articleDTO) {
        return Article.builder()
                .title(articleDTO.getTitle())
                .content(articleDTO.getContent())
                .writer(articleDTO.getWriter())
                .build();
    }

    private Cookie createDoubleHitPreventionCookie(Long articleId) {
        Cookie cookie = new Cookie(COOKIE_NAME + articleId, String.valueOf(articleId));
        cookie.setMaxAge(getSecondsLeftTomorrow());
        cookie.setHttpOnly(true);

        return cookie;
    }

    private int getSecondsLeftTomorrow() {
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime tomorrowTime = LocalDateTime.now().plusDays(1L).truncatedTo(ChronoUnit.DAYS);

        return (int) nowTime.until(tomorrowTime, ChronoUnit.SECONDS);
    }
}
