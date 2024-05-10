package SW_ET.controllerTest;

import SW_ET.controller.UserController;
import SW_ET.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void whenEmptyInput_thenBadRequest() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("userName", "")  // 비어 있는 입력값
                        .param("userEmail", "test@example.com")
                        .param("userPassword", "password123")
                        .with(csrf()))  // CSRF 토큰 포함
                .andExpect(status().isBadRequest());  // 400 상태 코드 기대
    }

    @Test
    @WithMockUser
    public void whenNonKoreanInput_thenBadRequest() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("userName", "JohnDoe")
                        .param("userEmail", "john@example.com")
                        .param("userPassword", "password")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("사용자 이름은 한글만 가능합니다.")));
    }

    @Test
    @WithMockUser
    public void whenValidKoreanInput_thenSuccess() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("userName", "홍길동")
                        .param("userEmail", "hong@example.com")
                        .param("userPassword", "password")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User registered successfully")));
    }

    @Test
    @WithMockUser
    public void whenInvalidInput_thenBadRequest() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("userName", "John123") // 유효하지 않은 입력
                        .param("userEmail", "john@example.com")
                        .param("userPassword", "password")
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("사용자 이름은 한글만 가능합니다.")));
    }
}