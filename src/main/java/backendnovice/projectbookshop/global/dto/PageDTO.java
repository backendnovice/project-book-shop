/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-16
 * @desc      : An page-related data transfer object class. that used to books and articles.
 * @changelog :
 * 2023-07-29 - backendnovice@gmail.com - create new file.
 * 2023-08-01 - backendnovice@gmail.com - add constructor paged condition check.
 * 2023-08-16 - backendnovice@gmail.com - add description annotation.
 *                                      - change filename to PageDTO.
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
public class PageDTO {
    private String tag;

    private String keyword;

    private int startPage;

    private int currentPage;

    private int finalPage;

    public PageDTO(Page<?> page) {
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
