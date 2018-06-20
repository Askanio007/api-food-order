package config.answerServer;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class SuccessAnswer extends AnswerServer{

    Object data;

    public SuccessAnswer(HttpStatus status, String message, Object data) {
        super(status, message);
        this.data = data;
    }

    public SuccessAnswer(HttpStatus status, String message) {
        super(status, message);
    }
}
