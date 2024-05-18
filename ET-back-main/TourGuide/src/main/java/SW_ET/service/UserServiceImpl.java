package SW_ET.service;

import SW_ET.dto.LoginDto;
import SW_ET.dto.UserDto;
import SW_ET.entity.User;
import SW_ET.exceptions.UserServiceException;
import SW_ET.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(SW_ET.service.UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String registerUser(UserDto userDto) {
        logger.debug("Registering user: {}", userDto.getUserId());
        if (userRepository.existsByUserId(userDto.getUserId())) {
            logger.error("User ID '{}' already exists.", userDto.getUserId());
            throw new UserServiceException("User ID '" + userDto.getUserId() + "' already exists.");
        }
        if (userRepository.existsByUserNickName(userDto.getUserNickName())) {
            logger.error("User Nickname '{}' already exists.", userDto.getUserNickName());
            throw new UserServiceException("User Nickname '" + userDto.getUserNickName() + "' already exists.");
        }
        if (!userDto.getUserPassword().equals(userDto.getConfirmPassword())) {
            logger.error("Passwords do not match for user '{}'.", userDto.getUserId());
            throw new UserServiceException("Passwords do not match.");
        }
        User newUser = userDto.toUser();
        newUser.setUserPassword(passwordEncoder.encode(userDto.getUserPassword()));
        userRepository.save(newUser);
        logger.info("User '{}' successfully registered.", newUser.getUserId());
        return "success";
    }


    @Override
    public boolean isUserIdExists(String userId) {
        return userRepository.existsByUserId(userId); // 문자열 userId로 존재 여부 확인
    }

    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId).orElse(null); // 문자열 userId로 유저 정보 조회
    }

    @Override
    public boolean isUserNickNameExists(String userNickName) {
        return userRepository.existsByUserNickName(userNickName); // 닉네임으로 존재 여부 확인
    }

    @Override
    public boolean validateUser(LoginDto loginDto) {
        User user = userRepository.findByUserId(loginDto.getUserId()).orElse(null);
        if (user == null) {
            logger.error("User not found for userId: {}", loginDto.getUserId());
            return false;
        }
        if (!passwordEncoder.matches(loginDto.getUserPassword(), user.getUserPassword())) {
            logger.error("Password mismatch for userId: {}", loginDto.getUserId());
            return false;
        }
        logger.info("User validated successfully for userId: {}", loginDto.getUserId());
        return true;
    }

}