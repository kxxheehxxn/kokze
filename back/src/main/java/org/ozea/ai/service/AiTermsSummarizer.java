package org.ozea.ai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiTermsSummarizer implements TermSummarizer {

    private final TextSummaryClient textSummaryClient;

    @Override
    public String summarizeTo3Lines(String rawTerms) {
        if (rawTerms == null || rawTerms.isBlank()) {
            return "";
        }
        return textSummaryClient.summarize(rawTerms, 3);
    }
}
