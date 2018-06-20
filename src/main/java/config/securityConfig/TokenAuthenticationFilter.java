package config.securityConfig;

import dto.UserDto;
import models.responseServer.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;
import service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static converter.JsonConverter.objectToJson;

@Component
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private UserService userService;

    public TokenAuthenticationFilter() {
        super("/api/**");
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return super.requiresAuthentication(request, response);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String token = request.getHeader("Authorization");
        if (token != null) {
            UserDto u = TokenUtil.parseToken(token);
            if (u == null) {
                response.getWriter().append(bodyToJson("incorrect token"));
                response.setStatus(401);
                return null;
            } else {
                if (userService.userIsActive(u.getLogin())) {
                    TokenAuthentication authRequest = new TokenAuthentication(token, u);
                    return getAuthenticationManager().authenticate(authRequest);
                }
            }
        }
        response.getWriter().append(bodyToJson("not token"));
        response.setStatus(401);
        return null;
    }

    private String bodyToJson(String message) throws IOException {
        return objectToJson(ResponseServer.unauthorized(message));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }


    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
}
