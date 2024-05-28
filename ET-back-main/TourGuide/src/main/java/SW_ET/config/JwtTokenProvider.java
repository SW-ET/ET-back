package SW_ET.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKeyString;

    @Value("${security.jwt.token.expire-length:3600000}") // 1h
    private long validityInMilliseconds;

    private SecretKey secretKey;
    private Set<String> tokenBlacklist = ConcurrentHashMap.newKeySet();

    @PostConstruct
    protected void init() {
        if (secretKeyString.length() < 32) {
            this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            log.warn("Provided secret key is too short. A secure key has been generated.");
        } else {
            this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
        }
        log.info("JWT Secret key initialized.");
    }

    public String generateToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(((UserDetails) authentication.getPrincipal()).getUsername());
        claims.put("auth", authentication.getAuthorities());

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        log.info("Generated token for user: {}", claims.getSubject());
        return token;
    }

    public boolean validateToken(String token) {
        if (tokenBlacklist.contains(token)) {
            log.error("Token is blacklisted: {}", token);
            return false;
        }
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            log.debug("Token is valid.");
            return !isTokenExpired(token);
        } catch (Exception e) {
            log.error("Invalid token or token expired: {}", e.getMessage());
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        log.debug("Authenticated user: {}", username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getExpiration();
        boolean expired = expiration.before(new Date());
        if (expired) {
            log.info("Token has expired.");
        }
        return expired;
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public void invalidateToken(String token) {
        tokenBlacklist.add(token);
        log.info("Token invalidated: {}", token);
    }
}
