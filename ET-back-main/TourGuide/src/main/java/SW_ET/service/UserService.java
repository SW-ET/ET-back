package SW_ET.service;

import SW_ET.dto.LoginDto;
import SW_ET.dto.UserDto;
import SW_ET.entity.User;

public interface UserService {
    String registerUser(UserDto userDto);
    boolean validateUser(LoginDto loginDto); // LoginDto를 사용하도록 변경
    boolean isUserIdExists(String userId);
    boolean isUserNickNameExists(String userNickName);
    User getUserByUserId(String userId);
    User getUserByUsername(String username);
}