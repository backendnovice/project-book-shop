/**
 * @author   : backendnovice@gmail.com
 * @created  : 2023-07-29
 * @modified : 2023-09-18
 * @desc     : 레이어 간의 통신을 담당하는 페이지 데이터 전송 객체.
 */

package backendnovice.projectbookshop.global.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDTO {
    private String tag;

    private String keyword;

    private int startPage;

    private int currentPage;

    private int finalPage;

    public PaginationDTO(Page<?> page) {
        Pageable pageable = page.getPageable();

        if(pageable.isPaged()) {
            this.currentPage = pageable.getPageNumber();
            this.startPage = Math.max(0, currentPage - pageable.getPageSize() + 1);
            this.finalPage = Math.min(currentPage + pageable.getPageSize(), page.getTotalPages() - 1);
        }else {
            this.currentPage = 0;
            this.startPage= 0;
            this.finalPage = 0;
        }
    }
}
