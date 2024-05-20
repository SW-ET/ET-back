package SW_ET.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class LoginDto {
    @NotBlank(message = "사용자 ID는 필수입니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String userPassword;

    // 필요하다면 여기에 로그인 처리에 필요한 추가적인 메소드나 필드를 정의할 수 있습니다.
}