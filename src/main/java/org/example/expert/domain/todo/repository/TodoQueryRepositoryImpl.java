package org.example.expert.domain.todo.repository;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.request.TodoSearchRequest;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TodoQueryRepositoryImpl implements TodoQueryRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(todo)
            .leftJoin(todo.user, user).fetchJoin()
            .where(todo.id.eq(todoId))
            .fetchOne());
    }

//    @Override
//    public Page<TodoSearchResponse> findTodos(TodoSearchRequest request, Pageable pageable) {
//        String title = "%" + request.getTitle().trim() + "%";
//        String managerName = "%" + request.getManagerName().trim() + "%";
//        LocalDate startDate = Optional.ofNullable(request.getStartDate())
//            .orElse(LocalDate.of(1, 1, 1));
//        LocalDate endDate = Optional.ofNullable(request.getEndDate())
//            .orElse(LocalDate.now());
//
//        BooleanExpression titleWhere = todo.title.like(title);
//        BooleanExpression managerNameWhere = manager.user.nickname.like(managerName);
//        BooleanExpression dateWhere =
//            todo.createdAt.between(startDate.atStartOfDay(), endDate.atTime(23,59,59));
//
//        List<TodoSearchResponse> todoList = jpaQueryFactory
//            .select(
//                Projections.constructor(TodoSearchResponse.class,
//                    todo.title,
//                    manager.id.countDistinct().coalesce(0L),
//                    comment.id.countDistinct().coalesce(0L)
//            ))
//            .from(todo)
//            .leftJoin(manager).on(todo.id.eq(manager.todo.id))
//            .leftJoin(manager.user, user)
//            .leftJoin(comment).on(todo.id.eq(comment.todo.id))
//            .where(titleWhere, managerNameWhere, dateWhere)
//            .groupBy(todo.id)
//            .orderBy(todo.createdAt.desc())
//            .offset(pageable.getOffset())
//            .limit(pageable.getPageSize())
//            .fetch();
//
//        Long totalCount = Optional.ofNullable(jpaQueryFactory
//            .select(todo.count())
//            .from(todo)
//            .leftJoin(manager).on(todo.id.eq(manager.todo.id))
//            .leftJoin(manager.user, user)
//            .where(titleWhere, managerNameWhere, dateWhere)
//            .fetchOne()).orElse(0L);
//
//        return new PageImpl<>(todoList, pageable, totalCount);
//    }

    @Override
    public Page<TodoSearchResponse> findTodos(TodoSearchRequest request, Pageable pageable) {
        String title = "%" + request.getTitle().trim() + "%";
        String managerName = "%" + request.getManagerName().trim() + "%";
        LocalDate startDate = Optional.ofNullable(request.getStartDate())
            .orElse(LocalDate.of(1, 1, 1));
        LocalDate endDate = Optional.ofNullable(request.getEndDate())
            .orElse(LocalDate.now());

        BooleanBuilder builder = new BooleanBuilder();
        if(!request.getTitle().trim().isEmpty()){
            builder.and(todo.title.like(title));
        }

        builder.and(todo.createdAt.between(startDate.atStartOfDay(), endDate.atTime(23,59,59)));

        if(!request.getManagerName().trim().isEmpty()) {
            builder.and(todo.id.in(
                JPAExpressions.select(manager.todo.id).from(manager)
                    .join(manager.user, user)
                    .where(user.nickname.like(managerName))
            ));
        }

        List<TodoSearchResponse> todoList = jpaQueryFactory
            .select(
                Projections.constructor(TodoSearchResponse.class,
                    todo.title,
                    JPAExpressions.select(manager.id.countDistinct()).from(manager).where(manager.todo.id.eq(todo.id)),
                    JPAExpressions.select(comment.id.countDistinct()).from(comment).where(comment.todo.id.eq(todo.id))
                ))
            .from(todo)
            .where(builder)
            .orderBy(todo.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long totalCount = Optional.ofNullable(jpaQueryFactory
            .select(todo.count())
            .from(todo)
            .where(builder)
            .fetchOne()).orElse(0L);

        return new PageImpl<>(todoList, pageable, totalCount);
    }
}
