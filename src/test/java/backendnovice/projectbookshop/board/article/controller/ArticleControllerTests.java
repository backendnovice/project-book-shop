/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-08-01
 * @modified : 2023-09-19
 * @desc     : ArticleController 테스트 클래스.
 */

package backendnovice.projectbookshop.board.article.controller;

import backendnovice.projectbookshop.board.article.dto.ArticleDTO;
import backendnovice.projectbookshop.global.dto.PaginationDTO;
import backendnovice.projectbookshop.board.article.service.ArticleService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArticleController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MockBean(JpaMetamodelMappingContext.class) // JPA Auditing 이슈 해결
public class ArticleControllerTests {
    @MockBean
    private ArticleService articleService;

    @Autowired
    private MockMvc mockMvc;

    private final String PREFIX = "/article/";

    private Page<ArticleDTO> fakeArticles;

    /**
     * 모든 테스트 전 10개의 가짜 게시글 DTO 리스트를 생성한다.
     */
    @BeforeAll
    void initialize() {
        List<ArticleDTO> articleDTOS = new ArrayList<>();

        for(int i = 1; i <= 10; i++) {
            ArticleDTO articleDTO = ArticleDTO.builder()
                    .id((long) i)
                    .title("title #" + i)
                    .content("content #" + i)
                    .writer("writer #" + i)
                    .build();
            articleDTOS.add(articleDTO);
        }

        Pageable pageable = PageRequest.of(0, 10);
        fakeArticles = new PageImpl<>(articleDTOS, pageable, articleDTOS.size());
    }

    /**
     * 게시글 등록 URI에 대한 GET 요청이 성공하는지 테스트한다.
     * @throws Exception
     *      mockMvc.preform()에서 발생할 수 있는 예외
     */
    @Test
    void should_ReturnWritePage_When_GetWritePageIsCalled() throws Exception {
        // given
        String uri = PREFIX + "write";

        // when

        // then
        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(view().name("article/write"));
    }

    /**
     * 게시글 목록 URI에 대한 GET 요청이 성공하는지 테스트한다.
     * @throws Exception
     *      mockMvc.preform()에서 발생할 수 있는 예외
     */
    @Test
    void should_ReturnListPage_When_GetListPageIsCalledWithoutParam() throws Exception {
        // given
        String uri = PREFIX + "list";
        PaginationDTO fakePageDTO = new PaginationDTO(fakeArticles);

        // when
        when(articleService.searchAll(any(Pageable.class))).thenReturn(fakeArticles);

        // then
        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(view().name("article/list"))
                .andExpect(model().attributeExists("data"))
                .andExpect(model().attribute("data", fakeArticles))
                .andExpect(model().attributeExists("pagination"))
                .andExpect(model().attribute("pagination", fakePageDTO));
    }

    /**
     * 올바른 태그와 키워드를 파라미터로 게시글 목록 URI에 대한 GET 요청이 성공하는지 테스트한다.
     * @throws Exception
     *      mockMvc.preform()에서 발생할 수 있는 예외
     */
    @Test
    void should_ReturnListPage_When_GetListPageIsCalledWithGoodTagAndGoodKeyword() throws Exception {
        // given
        String uri = PREFIX + "list";
        PaginationDTO fakePageDTO = new PaginationDTO(fakeArticles);

        // when
        when(articleService.searchByTags(any(PaginationDTO.class), any(Pageable.class))).thenReturn(fakeArticles);

        // then
        mockMvc.perform(get(uri).param("tag", "title").param("keyword", "title"))
                .andExpect(view().name("article/list"))
                .andExpect(model().attributeExists("data"))
                .andExpect(model().attribute("data", fakeArticles))
                .andExpect(model().attributeExists("pagination"))
                .andExpect(model().attribute("pagination", fakePageDTO));
    }

    /**
     * 잘못된 태그를 파라미터로 게시글 목록 URI에 대한 GET 요청이 실패하는지 테스트한다.
     * @throws Exception
     *      mockMvc.preform()에서 발생할 수 있는 예외
     */
    @Test
    void should_ReturnListPage_When_GetListPageIsCalledWithBadTag() throws Exception {
        // given
        String uri = PREFIX + "list";
        PaginationDTO fakePageDTO = new PaginationDTO(fakeArticles);

        // when
        when(articleService.searchByTags(any(PaginationDTO.class), any(Pageable.class))).thenThrow(IllegalArgumentException.class);
        when(articleService.searchAll(any(Pageable.class))).thenReturn(fakeArticles);

        // then
        mockMvc.perform(get(uri).param("tag", "blank").param("keyword", "nothing"))
                .andExpect(view().name("article/list"))
                .andExpect(model().attributeExists("data"))
                .andExpect(model().attribute("data", fakeArticles))
                .andExpect(model().attributeExists("pagination"))
                .andExpect(model().attribute("pagination", fakePageDTO))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "올바르지 않은 요청입니다."));
    }

    /**
     * 잘못된 키워드를 파라미터로 게시글 목록 URI에 대한 GET 요청이 실패하는지 테스트한다.
     * @throws Exception
     *      mockMvc.preform()에서 발생할 수 있는 예외
     */
    @Test
    void should_ReturnListPage_When_GetListPageIsCalledWithBadKeyword() throws Exception {
        // given
        String uri = PREFIX + "list";
        PaginationDTO fakePageDTO = new PaginationDTO(fakeArticles);

        // when
        when(articleService.searchByTags(any(PaginationDTO.class), any(Pageable.class))).thenThrow(NoSuchElementException.class);
        when(articleService.searchAll(any(Pageable.class))).thenReturn(fakeArticles);

        // then
        mockMvc.perform(get(uri).param("tag", "title").param("keyword", "nothing"))
                .andExpect(view().name("article/list"))
                .andExpect(model().attributeExists("data"))
                .andExpect(model().attribute("data", fakeArticles))
                .andExpect(model().attributeExists("pagination"))
                .andExpect(model().attribute("pagination", fakePageDTO))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "게시글을 찾을 수 없습니다."));
    }

    /**
     * 올바른 ID를 파라미터로 게시글 조회 URI에 대한 GET 요청이 성공하는지 테스트한다.
     * @throws Exception
     *      mockMvc.preform()에서 발생할 수 있는 예외
     */
    @Test
    void should_ReturnReadPage_When_GetReadPageIsCalledWithGoodArticleId() throws Exception {
        // given
        String uri = PREFIX + "read";
        ArticleDTO mockDTO = mock(ArticleDTO.class);

        // when
        when(articleService.read(anyLong())).thenReturn(mockDTO);

        // then
        mockMvc.perform(get(uri).param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("article/read"))
                .andExpect(model().attributeExists("data"))
                .andExpect(model().attribute("data", mockDTO));
    }

    /**
     * 비어있는 ID를 파라미터로 게시글 조회 URI에 대한 GET 요청이 실패하는지 테스트한다.
     * @throws Exception
     *      mockMvc.preform()에서 발생할 수 있는 예외
     */
    @Test
    void should_ReturnListPage_When_GetReadPageIsCalledWithNullArticleId() throws Exception {
        // given
        String uri = PREFIX + "read";

        // when

        // then
        mockMvc.perform(get(uri))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/list"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "올바르지 않은 요청입니다."));
    }

    /**
     * 잘못된 ID를 파라미터로 게시글 조회 URI에 대한 GET 요청에서 예외가 발생하는지 테스트한다.
     * @throws Exception
     *      mockMvc.preform()에서 발생할 수 있는 예외
     */
    @Test
    void should_ReturnListPage_When_GetReadPageIsCalledWithBadArticleId() throws Exception {
        // given
        String uri = PREFIX + "read";

        // when
        when(articleService.read(anyLong())).thenThrow(NoSuchElementException.class);

        // then
        mockMvc.perform(get(uri).param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/list"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "조회할 게시글이 존재하지 않습니다."));
    }

    /**
     * 올바른 ID를 파라미터로 게시글 수정 URI에 대한 GET 요청이 성공하는지 테스트한다.
     * @throws Exception
     *      mockMvc.preform()에서 발생할 수 있는 예외
     */
    @Test
    void should_ReturnModifyPage_When_GetModifyPageIsCalledWithGoodArticleId() throws Exception {
        // given
        String uri = PREFIX + "modify";
        ArticleDTO mockDTO = mock(ArticleDTO.class);

        // when
        when(articleService.read(anyLong())).thenReturn(mockDTO);

        // then
        mockMvc.perform(get(uri).param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("article/modify"))
                .andExpect(model().attributeExists("data"))
                .andExpect(model().attribute("data", mockDTO));
    }

    /**
     * 비어있는 ID를 파라미터로 게시글 수정 URI에 대한 GET 요청이 실패하는지 테스트한다.
     * @throws Exception
     *      mockMvc.preform()에서 발생할 수 있는 예외
     */
    @Test
    void should_ReturnListPage_When_GetModifyPageIsCalledWithNullArticleId() throws Exception {
        // given
        String uri = PREFIX + "modify";

        // when

        // then
        mockMvc.perform(get(uri))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/list"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "올바르지 않은 요청입니다."));
    }

    /**
     * 잘못된 ID를 파라미터로 게시글 수정 URI에 대한 GET 요청에서 예외가 발생하는지 테스트한다.
     * @throws Exception
     *      mockMvc.preform()에서 발생할 수 있는 예외
     */
    @Test
    void should_ReturnListPage_When_GetModifyPageIsCalledWithBadArticleId() throws Exception {
        // given
        String uri = PREFIX + "modify";
        ArticleDTO mockDTO = mock(ArticleDTO.class);

        // when
        when(articleService.read(anyLong())).thenThrow(NoSuchElementException.class);

        // then
        mockMvc.perform(get(uri).param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/list"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "수정할 게시글이 존재하지 않습니다."));
    }

    /**
     * 게시글 등록 URI에 대한 POST 요청이 성공하는지 테스트한다.
     * @throws Exception
     *      mockMvc.preform()에서 발생할 수 있는 예외
     */
    @Test
    void should_ReturnReadPage_When_CallWriteProcessServiceIsCalledAndSucceed() throws Exception {
        // given
        String uri = PREFIX + "write";
        MultiValueMap<String, String> fakeParams = new LinkedMultiValueMap<>();
        fakeParams.add("title", "title");
        fakeParams.add("content", "content");
        fakeParams.add("writer", "writer");

        // when
        when(articleService.write(any(ArticleDTO.class))).thenReturn(1L);

        // then
        mockMvc.perform(post(uri).params(fakeParams))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/read?id=" + 1L))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "게시글 등록이 완료되었습니다."));
    }

    /**
     * 비어있는 데이터를 파라미터로 게시글 등록 URI에 대한 POST 요청이 성공하는지 테스트한다.
     * @throws Exception
     *      mockMvc.preform()에서 발생할 수 있는 예외
     */
    @Test
    void should_ReturnWritePage_When_CallWriteProcessServiceIsCalledAndFailed() throws Exception {
        // given
        String uri = PREFIX + "write";

        // then
        mockMvc.perform(post(uri))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/write"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "비어있는 게시글을 등록할 수 없습니다."));
    }

    /**
     * 올바른 데이터를 파라미터로 게시글 수정 URI에 대한 POST 요청이 성공하는지 테스트한다.
     * @throws Exception
     *      mockMvc.preform()에서 발생할 수 있는 예외
     */
    @Test
    void should_ReturnReadPage_When_CallModifyProcessServiceIsCalledAndSucceed() throws Exception {
        // given
        String uri = PREFIX + "modify";
        MultiValueMap<String, String> fakeParams = new LinkedMultiValueMap<>();
        fakeParams.add("title", "title");
        fakeParams.add("content", "content");
        fakeParams.add("writer", "writer");

        // when
        when(articleService.modify(any(ArticleDTO.class))).thenReturn(1L);

        // then
        mockMvc.perform(post(uri).params(fakeParams))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/read?id=" + 1L))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "게시글 수정이 완료되었습니다."));
    }

    /**
     * 잘못된 ID를 파라미터로 게시글 수정 URI에 대한 POST 요청이 실패하는지 테스트한다.
     * @throws Exception
     *      mockMvc.preform()에서 발생할 수 있는 예외
     */
    @Test
    void should_ReturnListPage_When_CallModifyProcessServiceIsCalledAndFailed() throws Exception {
        // given
        String uri = PREFIX + "modify";
        MultiValueMap<String, String> fakeParams = new LinkedMultiValueMap<>();
        fakeParams.add("title", "title");
        fakeParams.add("content", "content");
        fakeParams.add("writer", "writer");

        // when
        when(articleService.modify(any(ArticleDTO.class))).thenThrow(NoSuchElementException.class);

        // then
        mockMvc.perform(post(uri).params(fakeParams))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/list"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "수정할 게시글을 찾지 못했습니다."));
    }

    /**
     * 올바른 ID를 파라미터로 게시글 삭제 URI에 대한 POST 요청이 성공하는지 테스트한다.
     * @throws Exception
     *      mockMvc.preform()에서 발생할 수 있는 예외
     */
    @Test
    void should_ReturnListPage_When_CallDeleteProcessServiceIsCalledAndSucceed() throws Exception {
        // given
        String uri = PREFIX + "delete";

        // when
        doNothing().when(articleService).delete(anyLong());

        // then
        mockMvc.perform(post(uri).param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/list"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "게시글 삭제가 완료되었습니다."));
    }

    /**
     * 잘못된 ID를 파라미터로 게시글 삭제 URI에 대한 POST 요청이 실패하는지 테스트한다.
     * @throws Exception
     *      mockMvc.preform()에서 발생할 수 있는 예외
     */
    @Test
    void should_ReturnListPage_When_CallDeleteProcessServiceIsCalledAndFailed() throws Exception {
        // given
        String uri = PREFIX + "delete";

        // when
        doThrow(NoSuchElementException.class).when(articleService).delete(anyLong());

        // then
        mockMvc.perform(post(uri).param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/list"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "삭제할 게시글을 찾지 못했습니다."));
    }
}
