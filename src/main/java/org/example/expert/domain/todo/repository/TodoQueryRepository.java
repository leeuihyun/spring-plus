package org.example.expert.domain.todo.repository;

import java.util.Optional;
import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoQueryRepository {
    Optional<Todo> findByIdWithUser(Long todoId);

    Page<TodoSearchResponse> findTodos(TodoSearchRequest request, Pageable pageable);
}
