package config.securityConfig;

import config.answerServer.ErrorAnswer;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static converter.JsonConverter.objectToJson;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.getWriter().append(objectToJson(new ErrorAnswer(HttpStatus.UNAUTHORIZED, "Not allowed", authException.getMessage())));
        response.setStatus(401);
    }
}