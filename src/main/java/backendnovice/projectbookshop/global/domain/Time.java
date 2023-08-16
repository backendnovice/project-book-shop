/**
 * @author    : backendnovice@gmail.com
 * @date      : 2023-08-16
 * @desc      : An common-related entity class. that provide time-related columns to multiple entity classes.
 * @changelog :
 * 2023-07-25 - backendnovice@gmail.com - create new file.
 * 2023-08-16 - backendnovice@gmail.com - add description annotation.
 */

package backendnovice.projectbookshop.global.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass                              // Set this common mapping entity.
@EntityListeners(AuditingEntityListener.class) // Include auditing features.
public class Time {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
