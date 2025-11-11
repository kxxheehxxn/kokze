package org.ozea.ai.service;

import org.springframework.stereotype.Service;

@Service
public class AiSummaryServiceImpl implements AiSummaryService {
    @Override
    public String summarizeTo3Lines(String text) {
        // TODO: LLM 연동
        return "";
    }
}
