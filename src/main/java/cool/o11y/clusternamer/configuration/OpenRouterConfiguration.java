package cool.o11y.clusternamer.configuration;

import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class OpenRouterConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        final SimpleModule openrouter = new SimpleModule()
            .addDeserializer(OpenAiApi.ChatCompletionFinishReason.class,
                new FinishReasonDeserializer());

        return new ObjectMapper()
                .registerModules(new Jdk8Module(), new JavaTimeModule(), openrouter)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


}
