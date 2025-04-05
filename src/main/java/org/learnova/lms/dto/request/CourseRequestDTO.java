package org.learnova.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CourseRequestDTO(
        @NotEmpty(message = "name Course has some error  ")
        @NotBlank(message = "name Of Course is empty")
        String name,
        String description,
        @NotEmpty(message = "name startDate has some error  ")
        @NotBlank(message = "name Of startDate is empty")
        String startDate,
        @NotEmpty(message = "name startDate has some error  ")
        @NotBlank(message = "name Of startDate is empty")
        String endDate
) {
}
