package com.example.util;

import com.example.dto.JwtDTO;
import com.example.enums.ProfileRole;
import com.example.exception.UnAuthorizedException;
import io.jsonwebtoken.*;

import java.util.Date;

public class JWTUtil {
    private static String secretKey="mazgi123";
    private static int tokenLiveTime=1000 * 3600 * 24*15;

    public static String encode(Integer id, String email, String role){
        JwtBuilder jwtBuilder= Jwts.builder();
        jwtBuilder.setIssuedAt(new Date());
        jwtBuilder.signWith(SignatureAlgorithm.HS512, secretKey);
        jwtBuilder.claim("id",id);
        jwtBuilder.claim("email",email);
        jwtBuilder.claim("role",role);

        jwtBuilder.setExpiration(new Date(System.currentTimeMillis()+(tokenLiveTime)));
        jwtBuilder.setIssuer("youtube by Sanjar");
        return jwtBuilder.compact();
    }
    public static JwtDTO decode(String token){
        try {
            JwtParser jwtParser=Jwts.parser();
            jwtParser.setSigningKey(secretKey);
            Jws<Claims>jws=jwtParser.parseClaimsJws(token);
            Claims claims= jws.getBody();
            String email=(String) claims.get("email");
            String role=(String) claims.get("role");
            Integer id=(Integer) claims.get("id");

            return new JwtDTO(id,email, ProfileRole.valueOf(role));
        }catch (ExpiredJwtException e){
            throw new UnAuthorizedException("your session is expired");
        }catch (JwtException e){
            throw new UnAuthorizedException(e.getMessage());
        }
    }
    public static String encodeForPasswordVerification(Integer id, String email, String newPassword){
        JwtBuilder jwtBuilder= Jwts.builder();
        jwtBuilder.setIssuedAt(new Date());
        jwtBuilder.signWith(SignatureAlgorithm.HS512, secretKey);
        jwtBuilder.claim("id",id);
        jwtBuilder.claim("email",email);
        jwtBuilder.claim("newPassword",newPassword);

        jwtBuilder.setExpiration(new Date(System.currentTimeMillis()+(tokenLiveTime)));
        jwtBuilder.setIssuer("youtube by Sanjar");
        return jwtBuilder.compact();
    }
    public static JwtDTO decodeForPasswordVerification(String token){
        try {
            JwtParser jwtParser=Jwts.parser();
            jwtParser.setSigningKey(secretKey);
            Jws<Claims>jws=jwtParser.parseClaimsJws(token);
            Claims claims= jws.getBody();
            String email=(String) claims.get("email");
            String newPassword=(String) claims.get("newPassword");
            Integer id=(Integer) claims.get("id");

            return new JwtDTO(id,email,newPassword);
        }catch (ExpiredJwtException e){
            throw new UnAuthorizedException("your session is expired");
        }catch (JwtException e){
            throw new UnAuthorizedException(e.getMessage());
        }
    }

}
