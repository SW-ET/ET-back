package SW_ET.dto;

import SW_ET.entity.User;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class UserDto { // DTO for user registration
    @NotBlank(message = "사용자 ID는 필수입니다.")
    private String userId;

    @NotBlank(message = "닉네임은 필수입니다.")
    private String userNickName;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String userPassword;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String confirmPassword;

    private String role;

    // Convert this DTO into a User entity
    public User toUser() {
        User user = new User();
        user.setUserId(this.userId);
        user.setUserNickName(this.userNickName);
        user.setUserPassword(this.userPassword); // Consider encrypting the password here if not done elsewhere
        user.setUserRole(this.role);
        return user;
    }
}