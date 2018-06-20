package config.securityConfig;

import dto.UserDto;
import entity.User;
import enums.RoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenUtil {

    private final static String secret = "test";

    /**
     * Tries to parse specified String as a JWT token. If successful, returns User object with username, id and role prefilled (extracted from token).
     * If unsuccessful (token is invalid or not containing all required user properties), simply returns null.
     *
     * @param token the JWT token to parse
     * @return the User object extracted from specified token or null if a token is invalid.
     */
    public static UserDto parseToken(String token) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            UserDto u = new UserDto();
            u.setLogin(body.getSubject());
            u.setRole(RoleType.getRoleType((String)body.get("role")));
            u.setPassword((String)body.get("pass"));
            return u;
        } catch (JwtException | ClassCastException e) {
            return null;
        }
    }

    /**
     * Generates a JWT token containing username as subject, and userId and role as additional claims. These properties are taken from the specified
     * User object. Tokens validity is infinite.
     *
     * @param u the user for which the token will be generated
     * @return the JWT token
     */
    public static String generateToken(UserDto u) {
        return generateToken(u.getLogin(), u.getPassword(), u.getRole());
    }

    public static String generateToken(String login, String password, RoleType role) {
        Claims claims = Jwts.claims().setSubject(login);
        claims.put("pass",password);
        claims.put("role",role);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

}
