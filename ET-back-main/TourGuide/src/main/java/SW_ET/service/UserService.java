package SW_ET.service;

import SW_ET.dto.LoginDto;
import SW_ET.dto.UserDto;

public interface UserService {
    String registerUser(UserDto userDto);  // 반환 타입을 void에서 String으로 변경
    boolean isUserIdExists(String userId);
    boolean validateUser(LoginDto loginDto);  // 로그인 유효성 검사 메소드도 String 반환으로 명시
    boolean isUserNickNameExists(String userNickName);
}
