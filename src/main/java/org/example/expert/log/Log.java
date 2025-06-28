package org.example.expert.log;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.entity.Timestamped;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "log")
public class Log extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long todoId;
    private Boolean success;
    private LogActions action;

    public Log(Long userId, Long todoId, Boolean success, LogActions action) {
        this.userId = userId;
        this.todoId = todoId;
        this.success = success;
        this.action = action;
    }
}
