package SW_ET.dto;

import SW_ET.entity.User;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
public class UserDto {
    @NotBlank(message = "사용자 ID는 필수입니다.")
    private String userId;

    @NotBlank(message = "닉네임은 필수입니다.")
    private String userNickName;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String userPassword;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String confirmPassword;

/*    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    private String userEmail;*/

    public User toUser() {
        User user = new User();
        user.setUserId(this.userId);
        user.setUserNickName(this.userNickName);
        user.setUserPassword(this.userPassword); // 실제 사용 시 비밀번호 암호화 고려
/*        user.setUserEmail(this.userEmail);*/
        return user;
    }
}
