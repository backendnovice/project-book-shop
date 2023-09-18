/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-07-25
 * @modified : 2023-09-18
 * @desc     : 게시글 관련 로직을 정의하는 서비스 인터페이스.
 */

package backendnovice.projectbookshop.board.article.service;

import backendnovice.projectbookshop.board.article.dto.ArticleDTO;
import backendnovice.projectbookshop.global.dto.PaginationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.NoSuchElementException;

public interface ArticleService {
    /**
     * 페이지를 파라미터로 전체 게시글을 검색한다.
     * @param pageable
     *      페이지네이션 객체
     * @return
     *      검색 결과
     * @exception NoSuchElementException
     *      게시글이 존재하지 않을 때 발생하는 예외
     */
    Page<ArticleDTO> searchAll(Pageable pageable);

    /**
     * 태그와 키워드, 페이지를 파라미터로 게시글을 검색한다.
     * @param paginationDTO
     *      페이지 데이터 전달 객체
     * @param pageable
     *      페이지네이션 객체
     * @return
     *      검색 결과
     * @exception IllegalArgumentException
     *      태그가 올바르지 않을 때 발생하는 예외
     * @exception NoSuchElementException
     *      게시글이 존재하지 않을 때 발생하는 예외
     */
    Page<ArticleDTO> searchByTags(PaginationDTO paginationDTO, Pageable pageable);

    /**
     * 새로운 게시글을 등록한다.
     * @param articleDTO
     *      게시글 데이터 전달 객체
     * @return
     *      등록된 게시글 ID
     * @exception IllegalArgumentException
     *      데이터가 비어있을 때 발생하는 예외
     */
    Long write(ArticleDTO articleDTO);

    /**
     * ID의 일치하는 기존 게시글을 수정한다.
     * @param articleDTO
     *      게시글 데이터 전달 객체
     * @return
     *      수정된 게시글 ID
     * @exception IllegalArgumentException
     *      데이터가 비어있을 때 발생하는 예외
     * @exception NoSuchElementException
     *      ID와 일치하는 게시글이 존재하지 않을 때 발생하는 예외
     */
    Long modify(ArticleDTO articleDTO);

    /**
     * ID와 일치하는 게시글을 조회한다.
     * @param id
     *      게시글 ID
     * @return
     *      검색 결과
     * @exception NoSuchElementException
     *      ID와 일치하는 게시글이 존재하지 않을 때 발생하는 예외
     */
    ArticleDTO read(Long id);

    /**
     * ID와 일치하는 게시글을 삭제한다.
     * @param id
     *      게시글 ID
     * @exception NoSuchElementException
     *      ID와 일치하는 게시글이 존재하지 않을 때 발생하는 예외
     */
    void delete(Long id);

    /**
     * 게시글의 조회수를 증가시킨다.
     * @param id
     *      게시글 ID
     */
    void updateViews(Long id);
}
