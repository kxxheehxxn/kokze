package org.ozea.ai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiTextSummaryClient implements TextSummaryClient {
    //private final AiSummaryService aiSummaryService;
    //서머리 서비스 생성 후 생성자 주입
    @Override
    public String summarize(String text, int targetLines) {
        /*
        * req, res에 맞춰서 호출 필요
        * */
        return null;
    }
}
