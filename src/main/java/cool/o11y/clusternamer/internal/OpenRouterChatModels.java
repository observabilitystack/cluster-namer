package cool.o11y.clusternamer.internal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OpenRouterChatModels {

    // Google Gemini models
    GEMINI_FLASH_2_0("google/gemini-2.0-flash-exp:free"),
    GEMINI_FLASH_1_5("google/gemini-flash-1.5"),
    GEMINI_PRO_1_5("google/gemini-pro-1.5"),

    // openAI models
    GPT_4O("openai/gpt-4o-2024-11-20"),
    GPT_4O_MINI("openai/gpt-4o-mini")

    ;

    @Getter
    private final String name;

}
