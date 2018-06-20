package converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.stereotype.Component;

@Component
public class JsonConverter {

        public static String objectToJson(Object obj) throws JsonProcessingException {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));
            return mapper.writeValueAsString(obj);
        }
}
