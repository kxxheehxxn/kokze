package org.ozea.ai.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class SimpleTermSummarizer implements TermSummarizer {
    //변경 가능
    //백업
    @Override
    public String summarizeTo3Lines(String rawTerms) {
        if (rawTerms == null || rawTerms.isBlank()) {
            return "";
        }

        String[] lines = rawTerms.split("\\R");

        if(lines.length <= 3){
            return String.join("\n", lines);
        }

        return Arrays.stream(lines)
                .limit(3)
                .collect(Collectors.joining("\n"));
    }
}
