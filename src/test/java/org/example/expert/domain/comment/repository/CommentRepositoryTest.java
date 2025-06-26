package org.example.expert.domain.comment.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void 게시글_댓글들_조회() {
        User user1 = new User("test1@naver.com", "@Abc123456", UserRole.USER, "test1");
        User user2 = new User("test2@naver.com", "@Abc123456", UserRole.USER, "test2");
        User user3 = new User("test3@naver.com", "@Abc123456", UserRole.USER, "test3");
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);

        Todo todo = new Todo("title", "contents", "Glaze", user1);
        entityManager.persist(todo);

        Comment comment1 = new Comment("contents1", user1, todo);
        Comment comment2 = new Comment("contents2", user2, todo);
        Comment comment3 = new Comment("contents3", user3, todo);

        todo.getComments().add(comment1);
        todo.getComments().add(comment2);
        todo.getComments().add(comment3);

        entityManager.persist(comment1);
        entityManager.persist(comment2);
        entityManager.persist(comment3);

        entityManager.flush();
        entityManager.clear();

        List<Comment> comments = commentRepository.findByTodoIdWithUser(todo.getId());

        for(Comment comment : comments) {
            System.out.println(comment.getUser().getNickname());
        }

        Assertions.assertEquals(3, comments.size());
        Assertions.assertEquals(comment1.getContents(), comments.get(0).getContents());
        Assertions.assertEquals(comment2.getContents(), comments.get(1).getContents());
        Assertions.assertEquals(comment3.getContents(), comments.get(2).getContents());
    }
}
