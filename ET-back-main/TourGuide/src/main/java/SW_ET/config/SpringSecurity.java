package SW_ET.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity(debug = true)  // Consider turning off debug in production
public class SpringSecurity {

    private final JwtTokenProvider jwtTokenProvider;

    public SpringSecurity(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()  // Disable CSRF as JWT is used which is inherently protected against CSRF
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // No session will be created or used by Spring Security
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs", "/users/check-userId", "/users/check-nickname").permitAll()
                .requestMatchers("/users/home", "/users/register", "/users/login_proc", "/resources/**", "/images/**", "/css/**", "/js/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll()  // 모든 사용자가 리뷰 조회를 할 수 있도록 허용
                .requestMatchers(HttpMethod.POST, "/api/reviews").hasAuthority("ROLE_USER")  // 리뷰 생성은 인증된 사용자만 가능
                .requestMatchers(HttpMethod.PUT, "/api/reviews/{id}").hasAuthority("ROLE_USER")  // 리뷰 수정은 인증된 사용자만 가능
                .requestMatchers(HttpMethod.DELETE, "/api/reviews/{id}").hasAuthority("ROLE_USER")  // 리뷰 삭제는 인증된 사용자만 가능
                .requestMatchers("/users/logout").authenticated()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/users/logout", "POST"))
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                });
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedOrigins(Arrays.asList("*"));  // Consider listing specific origins in production
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}