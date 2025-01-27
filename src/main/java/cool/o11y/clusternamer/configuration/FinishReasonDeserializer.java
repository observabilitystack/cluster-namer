package cool.o11y.clusternamer.configuration;

import java.io.IOException;

import org.springframework.ai.openai.api.OpenAiApi;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @see https://github.com/spring-projects/spring-ai/issues/1522#issuecomment-2580012907
 */
public class FinishReasonDeserializer extends JsonDeserializer<OpenAiApi.ChatCompletionFinishReason> {
    @Override
    public OpenAiApi.ChatCompletionFinishReason deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException, JacksonException {
        String str = jsonParser.getCodec().readValue(jsonParser, String.class);
        for (OpenAiApi.ChatCompletionFinishReason reason : OpenAiApi.ChatCompletionFinishReason.values()) {
            if (reason.name().equalsIgnoreCase(str)) {
                return reason;
            }
        }
        return OpenAiApi.ChatCompletionFinishReason.STOP;
    }
}
