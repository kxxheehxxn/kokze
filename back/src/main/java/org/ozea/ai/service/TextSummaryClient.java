package org.ozea.ai.service;

public interface TextSummaryClient {
    String summarize(String text, int targetLines);
}
