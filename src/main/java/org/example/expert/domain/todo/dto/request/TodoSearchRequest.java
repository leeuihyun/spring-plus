package org.example.expert.domain.todo.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodoSearchRequest {

    private String title;
    private String managerName;
    private LocalDate startDate;
    private LocalDate endDate;
}
