package config.answerServer;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class ErrorAnswer extends AnswerServer {

    private List errors;

    public ErrorAnswer(HttpStatus status, String message, List errors) {
        super(status, message);
        this.errors = errors;
    }

    public ErrorAnswer(HttpStatus status, String message, String error) {
        super(status, message);
        errors = Arrays.asList(error);
    }


}
