package cool.o11y.clusternamer.internal;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.ai.openai.api.ResponseFormat.Type;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import cool.o11y.clusternamer.NamingSuggestions;
import cool.o11y.clusternamer.NamingTheme;

@Repository
public class ClusterNameRepository {

    private final ChatModel model;
    private final PromptTemplate systemPrompt;
    private final ObjectMapper objectMapper;

    static record LlmNamingResult(Collection<String> names) {
    }

    public ClusterNameRepository(ChatModel model,
            @Value("classpath:prompt-naming-system.txt") Resource system,
            ObjectMapper objectMapper) {
        this.model = model;
        this.systemPrompt = new SystemPromptTemplate(system);
        this.objectMapper = objectMapper;
    }

    public NamingSuggestionChunk nextChunk(NamingTheme theme, NamingSuggestions state) {
        Objects.requireNonNull(theme);
        Objects.requireNonNull(state);

        // build user prompt
        final Message queryPrompt = new UserMessage(theme.theme());

        // build prompt
        final Prompt prompt = new Prompt(
                List.of(systemPrompt.createMessage(Map.of("state", state2prompt(state))), queryPrompt),
                OpenAiChatOptions.builder()
                        .model(OpenRouterChatModels.GEMINI_FLASH_1_5.getName())
                        .temperature(0.8d)
                        .responseFormat(ResponseFormat.builder().type(Type.JSON_OBJECT).build())
                        .build());

        try {
            // call openai
            final ChatResponse response = this.model.call(prompt);

            // extract result
            LlmNamingResult suggestions = objectMapper.readValue(
                    response.getResult().getOutput().getContent(), LlmNamingResult.class);

            return new NamingSuggestionChunk(theme, NamingSuggestionChunk.sanitizeNames(suggestions.names()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String state2prompt(NamingSuggestions state) {
        if (state.getUsedSuggestions().isEmpty()) {
            return "";
        } else {
            final StringBuilder prompt = new StringBuilder();
            prompt.append("The results must not include the following:\n");
            prompt.append("\n");
            prompt.append(state.getUsedSuggestions().stream().map(e -> String.format("* %s", e)).collect(Collectors.joining("\n")));

            return prompt.toString();
        }
    }
}
