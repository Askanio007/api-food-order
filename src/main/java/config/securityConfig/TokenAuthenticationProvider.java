package config.securityConfig;

import dto.UserDto;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        TokenAuthentication authToken = (TokenAuthentication) authentication;
        return new UsernamePasswordAuthenticationToken(authToken.getPrincipal(), authToken.getCredentials(), Arrays.asList(new SimpleGrantedAuthority(authToken.getRole())));
    }

    @Override
    public boolean supports(Class authentication) {
         return authentication.equals(TokenAuthentication.class);
    }

}
