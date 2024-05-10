package SW_ET.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import SW_ET.entity.User;

@Data  // Lombok의 @Data 애너테이션을 사용하여 getter, setter 자동 생성
public class UserDto {
    @NotBlank(message = "사용자 이름은 필수입니다.")
    @Pattern(regexp = "[가-힣]+", message = "사용자 이름은 한글만 가능합니다.")
    private String userName;

    @NotBlank(message = "사용자 ID는 필수입니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String userPassword;

    private String confirmPassword; // 비밀번호 확인 필드

    @NotBlank(message = "이메일은 필수입니다.")
    private String userEmail;  // 이메일 필드

    // UserDto를 User 엔티티로 변환하는 메소드
    public User toUser() {
        User user = new User();
        user.setUserName(this.userName);
        user.setUserId(this.userId);
        user.setUserPassword(this.userPassword); // 실제 사용 시 비밀번호 암호화 고려
        user.setUserEmail(this.userEmail);
        // 추가적으로 User 클래스에 설정해야 할 다른 필드가 있다면 이곳에 추가
        return user;
    }
}
