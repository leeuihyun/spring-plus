package org.example.expert.domain.comment.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import org.example.expert.domain.comment.dto.response.CommentResponse;
import org.example.expert.domain.comment.dto.response.CommentSaveResponse;
import org.example.expert.domain.comment.service.CommentService;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    void 게시글_댓글_조회_성공() throws Exception {
        long todoId = 1L;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER, "hyun");
        User user = User.fromAuthUser(authUser);
        UserResponse userResponse = new UserResponse(user.getId(), user.getEmail());
        List<CommentResponse> commentResponseList =
            List.of(new CommentResponse(1L, "contents1", userResponse),
                new CommentResponse(2L, "contents2", userResponse));

        when(commentService.getComments(todoId)).thenReturn(commentResponseList);

        // then
        mockMvc.perform(get("/todos/{todoId}/comments", todoId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].contents").value("contents1"))
            .andExpect(jsonPath("$[1].contents").value("contents2"));

    }
}
