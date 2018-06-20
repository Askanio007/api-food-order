package config.answerServer;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class AnswerServer {

    private HttpStatus status;
    private String message;

    public AnswerServer(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
