package org.example.expert.domain.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodoSearchResponse {

    private String title;
    private Long managersCount;
    private Long commentsCount;
}
