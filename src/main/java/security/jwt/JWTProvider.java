package security.jwt;

import io.jsonwebtoken.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JWTProvider {

    @Value("${jwt.expire}")
    private Long jwtExpire;
    @Value("${jwt.refresh}")
    private Long jwtRefresh;
    @Value("${jwt.secret_key}")
    private String jwtSecret;
    public String generateJWT(String username) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtExpire))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public boolean validateJWT(String jwt) {
        try{
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
            return true;
        }catch (UnsupportedJwtException e){
            log.error("he thong khong duoc xac thuc voi JWT"+ e.getMessage());
        }catch (ExpiredJwtException e){
            log.error("chuoi JWT da het han"+ e.getMessage());
        }catch (MalformedJwtException e){
            log.error("chu ki khong dung dinh dang"+ e.getMessage());
        }catch (IllegalArgumentException e){
            log.error("tham so truyen JWT khong dung"+ e.getMessage());
        }
        return false;
    }

    public String getUsernameFromJWT(String jwt) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().getSubject();
    }

    public String refreshToken(String jwtOld, String username) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtRefresh))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

}
