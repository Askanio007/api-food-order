package models.responseServer;

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
    private List errors = new ArrayList();
    private Object data;

    public ResponseServer(boolean success, Object data, List errors) {
        this.success = success;
        this.data = data;
        this.errors = errors;
    }

    public ResponseServer(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public static ResponseEntity<ResponseServer> OK(boolean success, Object data, List errors) {
        return new ResponseEntity<>(new ResponseServer(success, data, errors), HttpStatus.OK);
    }

    public static ResponseEntity<ResponseServer> OK(boolean success, Object data, String error) {
        return new ResponseEntity<>(new ResponseServer(success, data, Arrays.asList(error)), HttpStatus.OK);
    }

    public static ResponseEntity<ResponseServer> OK(boolean success, Object data) {
        return new ResponseEntity<>(new ResponseServer(success, data), HttpStatus.OK);
    }

    public static ResponseEntity<ResponseServer> unauthorized(String error) {
        return new ResponseEntity<>(new ResponseServer(false, null, Arrays.asList(error)), HttpStatus.UNAUTHORIZED);
    }

}
