package SW_ET.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  // 스프링 시큐리티를 활성화하고, 웹 보안을 구성하기 위한 애너테이션
public class SpringSecurity {

    // 비밀번호 암호화를 위한 PasswordEncoder Bean을 정의합니다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt 해싱 알고리즘을 사용
    }

    // HTTP 보안 구성을 정의하는 SecurityFilterChain Bean을 정의합니다.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // "/login", "/users/register", "/resources/**" 경로에 대한 접근은 인증 없이 허용합니다.
                        .requestMatchers("/login", "/users/register", "/resources/**").permitAll()
                        // 그 외 모든 요청은 인증된 사용자만 접근 가능하도록 요구합니다.
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // 사용자 정의 로그인 페이지의 URL을 설정합니다.
                        .loginPage("/login")
                        .permitAll()
                        // 로그인 폼이 제출될 URL을 설정합니다.
                        .loginProcessingUrl("/login_proc")
                        // 로그인 성공 시 사용자를 리디렉션할 기본 URL을 설정합니다.
                        .defaultSuccessUrl("/dashboard", true)
                        // 로그인 실패 시 리디렉션할 URL을 설정합니다.
                        .failureUrl("/login?error=true")
                )
                .logout(logout -> logout
                        // 로그아웃 성공 시 리디렉션할 URL을 설정합니다.
                        .logoutSuccessUrl("/login")
                        .permitAll()
                )
                // CSRF 보호 기능을 비활성화합니다.
                .csrf().disable();

        return http.build();
    }
}
