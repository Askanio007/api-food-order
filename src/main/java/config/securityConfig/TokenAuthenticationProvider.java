package config.securityConfig;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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
