package org.example.expert.domain.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.example.expert.QueryDslTestConfig;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StopWatch;

@DataJpaTest
@ActiveProfiles("test")
//@Transactional
@Import(QueryDslTestConfig.class)
public class UserUnitTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    List<User> users = new ArrayList<>();

    @Test
    public void findUsers() {
        Long startTime1 = System.nanoTime();
        int totalUsers = 200000;
        int batchSize = 1000;

        for(int i=0;i< totalUsers;i++) {
            User user = new User(i+"test@email.com", "!@#Test123", UserRole.USER, "test"+i);

            users.add(user);

            if(users.size() == batchSize) {
                userRepository.saveAll(users);
                userRepository.flush();
                users.clear();
            }
        }

        Long endTime1 = System.nanoTime();
        System.out.println(endTime1 - startTime1);

        StopWatch stopWatch = new StopWatch();

        for(int i=0;i<10;i++) {
            stopWatch.start();
            List<User> result = userRepository.findUsersByNickname("test7899"+i);
            System.out.println(result.size());
            stopWatch.stop();

            entityManager.clear();
        }
        System.out.println(stopWatch.prettyPrint());
    }
}
