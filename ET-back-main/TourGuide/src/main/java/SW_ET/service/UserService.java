package SW_ET.service;

import SW_ET.config.JwtTokenProvider;
import SW_ET.dto.LoginDto;
import SW_ET.dto.UserDto;
import SW_ET.entity.User;
import SW_ET.exceptions.UserServiceException;
import SW_ET.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService{

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    public String registerUser(UserDto userDto) {
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

    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId).orElse(null);
    }

    public boolean isUserIdExists(String userId) {
        return userRepository.existsByUserId(userId);
    }


    public User getUserByUserNickname(String userNickname) {
        return userRepository.findByUserNickName(userNickname).orElse(null);
    }

    public boolean isUserNickNameExists(String userNickName) {
        return userRepository.existsByUserNickName(userNickName);
    }

    public String login(LoginDto loginDto) {
        User user = userRepository.findByUserId(loginDto.getUserId()).orElse(null);
        if (user != null && passwordEncoder.matches(loginDto.getUserPassword(), user.getUserPassword())) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtTokenProvider.generateToken(authentication);
        } else {
            logger.error("Invalid credentials for user: {}", loginDto.getUserId());
            throw new UserServiceException("Invalid credentials");
        }
    }

}