// src/main/java/io/github/alirostom1/logismart/dto/request/delivery/SearchDeliveryRequest.java
package io.github.alirostom1.logismart.dto.request.delivery;

import io.github.alirostom1.logismart.model.enums.DeliveryPriority;
import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
import io.github.alirostom1.logismart.util.ValidUUID;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchDeliveryRequest {

    private String searchTerm;

    @Pattern(regexp = "^(|CREATED|COLLECTED|IN_STOCK|IN_TRANSIT|DELIVERED)$",
            message = "Status must be one of: CREATED, COLLECTED, IN_STOCK, IN_TRANSIT, DELIVERED")
    private String status;

    @Pattern(regexp = "^(|LOW|MEDIUM|HIGH)$",
            message = "Priority must be one of: LOW, MEDIUM, HIGH")
    private String priority;

    @ValidUUID
    private String zoneId;

    @Pattern(regexp = "^[a-zA-Z\\sàâäèéêëîïôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ.-]{0,50}$",
            message = "City name must be alphabetic and less than 50 characters")
    private String city;

    @ValidUUID
    private String courierId;

    @ValidUUID
    private String senderId;

    @ValidUUID
    private String recipientId;

    @Min(value = 0, message = "Page must be greater than or equal to 0")
    private Integer page = 0;

    @Min(value = 1, message = "Size must be greater than or equal to 1")
    @Min(value = 100, message = "Size must be less than or equal to 100")
    private Integer size = 20;

    @Pattern(regexp = "^(createdAt|updatedAt|destinationCity|priority|status)$",
            message = "Sort by must be one of: createdAt, updatedAt, destinationCity, priority, status")
    private String sortBy = "createdAt";

    @Pattern(regexp = "^(ASC|DESC)$",
            message = "Sort direction must be ASC or DESC")
    private String sortDirection = "DESC";

    public Pageable getPageable() {
        return PageRequest.of(
                page != null ? page : 0,
                size != null ? size : 20,
                Sort.by(Sort.Direction.fromString(sortDirection != null ? sortDirection : "DESC"),
                        sortBy != null ? sortBy : "createdAt")
        );
    }
}