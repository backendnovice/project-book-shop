/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-08-30
 * @modified : 2023-09-04
 * @desc     : CommentApiController test class.
 */

package backendnovice.projectbookshop.board.comment.controller;

import backendnovice.projectbookshop.board.comment.dto.CommentDTO;
import backendnovice.projectbookshop.board.comment.service.CommentService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentApiController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MockBean(JpaMetamodelMappingContext.class)
public class CommentApiControllerTests {
    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    private final String PREFIX = "/board/api/v1/comment/";

    private Page<CommentDTO> fakeComments;

    /**
     * Initialize before all test. initialize "fakeComments"
     */
    @BeforeAll
    void initialize() {
        List<CommentDTO> commentDTOList = new ArrayList<>();
        commentDTOList.add(new CommentDTO());
        commentDTOList.add(new CommentDTO());
        fakeComments = new PageImpl<>(commentDTOList);
    }

    /**
     * Test success case for POST request on write comment process.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnResponseDTOObject_When_WriteIsCalledAndSucceed() throws Exception {
        // given
        String uri = PREFIX + "write";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("content", "content");
        params.add("writer", "writer");

        // when
        doNothing().when(commentService).write(any(CommentDTO.class));

        // then
        mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).params(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("comment registration succeed."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Test failure case for POST request on write comment process.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnResponseDTOObject_When_WriteIsCalledAndFailed() throws Exception {
        // given
        String uri = PREFIX + "write";
        CommentDTO commentDTO = null;

        // when

        // then
        mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("cannot register comment with null data."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Test success case for GET request on read comment process.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnResponseDTOObject_When_ReadIsCalledAndSucceed() throws Exception {
        // given
        Long articleId = 1L;
        String uri = PREFIX + "read?id=" + articleId;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0");
        params.add("size", "10");

        // when
        when(commentService.getComments(anyLong(), any(Pageable.class))).thenReturn(fakeComments);

        // then
        mockMvc.perform(get(uri).params(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("comment selection succeed."))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    /**
     * Test failure case for GET request on read comment process.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnResponseDTOObject_When_ReadIsCalledAndFailed() throws Exception {
        // given
        String uri = PREFIX + "read";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0");
        params.add("size", "10");

        // when

        // then
        mockMvc.perform(get(uri).params(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("cannot read comment with null data."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Test success case for POST request on modify comment process.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnResponseDTOObject_When_ModifyIsCalledAndSucceed() throws Exception {
        // given
        String uri = PREFIX + "modify";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("content", "content");
        params.add("writer", "writer");

        // when
        doNothing().when(commentService).modify(any(CommentDTO.class));

        // then
        mockMvc.perform(post(uri).params(params).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("comment modification succeeded."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Test failure case for POST request on modify comment process when result is empty.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnResponseDTOObject_When_ModifyIsCalledAndFailedCauseResultIsEmpty() throws Exception {
        // given
        String uri = PREFIX + "modify";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("content", "content");
        params.add("writer", "writer");

        // when
        doThrow(NoSuchElementException.class).when(commentService).modify(any(CommentDTO.class));

        // then
        mockMvc.perform(post(uri).params(params).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("this comment does not exists."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Test failure case for POST request on modify comment process with empty data.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnResponseDTOObject_When_ModifyIsCalledWithEmptyAndFailed() throws Exception {
        // given
        String uri = PREFIX + "modify";

        // when
        doNothing().when(commentService).modify(any(CommentDTO.class));

        // then
        mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("cannot modify comment with null data."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Test success case for POST request on delete comment process.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnResponseDTOObject_When_DeleteIsCalledAndSucceed() throws Exception {
        // given
        Long commentId = 1L;
        String uri = PREFIX + "delete?id=" + commentId;

        // when
        doNothing().when(commentService).delete(commentId);

        // then
        mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("comment deletion succeeded."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Test failure case for POST request on modify comment process when result is empty.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnResponseDTOObject_When_DeleteIsCalledAndFailedCauseResultIsEmpty() throws Exception {
        // given
        Long commentId = 1L;
        String uri = PREFIX + "delete?id=" + commentId;

        // when
        doThrow(NoSuchElementException.class).when(commentService).delete(commentId);

        // then
        mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("this comment does not exists."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * Test failure case for POST request on delete comment process with empty data.
     * @throws Exception
     *      Throwable exception when using mockMvc.preform().
     */
    @Test
    void should_ReturnResponseDTOObject_When_DeleteIsCalledWithEmptyAndFailed() throws Exception {
        // given
        String uri = PREFIX + "delete";

        // when

        // then
        mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("cannot delete comment with null id."))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
