package edunhnil.project.forum.api.jwt;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import edunhnil.project.forum.api.dao.userRepository.User;
import edunhnil.project.forum.api.dao.userRepository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

    @Autowired
    private UserRepository userRepository;

    @Value("${spring.key.jwt}")
    protected String JWT_SECRET;

    public String generateToken(String id) {
        Date now = new Date();
        long JWT_EXPIRATION = 24 * 60 * 60 * 1000L; // expired in 1 days since login
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        System.out.println(JWT_SECRET);
        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public Optional<User> getUserInfoFromToken(String token) {
        String id = JwtUtils.getUserIdFromJwt(token, JWT_SECRET);
        return userRepository.getUserById(id);
    }

    public boolean validateToken(HttpServletRequest request) {
        String jwt = JwtUtils.getJwtFromRequest(request);
        if (StringUtils.hasText(jwt)) {
            try {
                Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwt);
                return true;
            } catch (MalformedJwtException e) {
                log.error("Invalid JWT Token");
            } catch (ExpiredJwtException e) {
                log.error("Expired JWT Token or Deprecated JWT Token");
            } catch (UnsupportedJwtException e) {
                log.error("Unsupported JWT Token");
            } catch (IllegalArgumentException e) {
                log.error("JWT claims is empty string");
            } catch (SignatureException e) {
                log.error("JWT signature does not match locally computed signature!");
            }
        }
        return false;
    }
}
