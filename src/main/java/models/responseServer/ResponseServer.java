package models.responseServer;

import config.securityConfig.TokenUtil;
import dto.UserDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class ResponseServer {

    private boolean success;
    private String token;
    private List errors = new ArrayList();
    private String message;
    private Object data;

    public ResponseServer(boolean success, String message, Object data, List errors) {
        this.success = success;
        this.data = data;
        this.errors = errors;
        this.message = message;
    }

    public ResponseServer(boolean success, String message, Object data, String token) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.token = token;
    }

    public ResponseServer(boolean success, String message, Object data) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public ResponseServer(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ResponseServer(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public static ResponseEntity<ResponseServer> OK(boolean success, String message, Object data, List errors) {
        return new ResponseEntity<>(new ResponseServer(success, message, data, errors), HttpStatus.OK);
    }

    public static ResponseEntity<ResponseServer> OK(boolean success, String message, Object data, String error) {
        return new ResponseEntity<>(new ResponseServer(success, message, data, Arrays.asList(error)), HttpStatus.OK);
    }

    public static ResponseEntity<ResponseServer> OK(boolean success, String message, Object data) {
        return new ResponseEntity<>(new ResponseServer(success, message, data), HttpStatus.OK);
    }

    public static ResponseEntity<ResponseServer> OK(boolean success, String message) {
        return new ResponseEntity<>(new ResponseServer(success, message), HttpStatus.OK);
    }

    public static ResponseEntity<ResponseServer> OK(boolean success, Object data) {
        return new ResponseEntity<>(new ResponseServer(success, data), HttpStatus.OK);
    }

    public static ResponseEntity<ResponseServer> unauthorized(String error) {
        return new ResponseEntity<>(new ResponseServer(false, null, Arrays.asList(error)), HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<ResponseServer> authorized(UserDto user) {
        return new ResponseEntity<>(new ResponseServer(true, "login successful", user, TokenUtil.generateToken(user)), HttpStatus.OK);
    }

}
