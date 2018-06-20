package config.securityConfig;

import dto.UserDto;
import enums.RoleType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.*;

public class TokenAuthentication extends AbstractAuthenticationToken {

    private final String token;
    private final UserDto userDto;

    protected TokenAuthentication(String token, UserDto userDto, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        this.userDto = userDto;
    }

    protected TokenAuthentication(String token, UserDto userDto) {
        super(null);
        this.token = token;
        this.userDto = userDto;
    }

    protected TokenAuthentication(UserDto userDto) {
        super(null);
        this.token = TokenUtil.generateToken(userDto);
        this.userDto = userDto;
    }

    @Override
    public String getCredentials() {
        return userDto.getPassword();
    }

    @Override
    public String getPrincipal() {
        return userDto.getLogin();
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return userDto.getRole().name();
    }

}
