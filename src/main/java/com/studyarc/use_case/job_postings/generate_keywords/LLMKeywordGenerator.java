package com.studyarc.use_case.job_postings.generate_keywords;

import io.github.cdimascio.dotenv.Dotenv;

import com.studyarc.entity.job_postings.KeywordList;

import com.cohere.api.Cohere;
import com.cohere.api.requests.ChatRequest;
import com.cohere.api.types.ChatMessage;
import com.cohere.api.types.Message;
import com.cohere.api.types.NonStreamedChatResponse;

import java.util.List;

/***
 * Generates the keywords via an LLM prompt
 */
public class LLMKeywordGenerator implements KeywordGenerator {
    private static final Dotenv DOTENV = Dotenv.load();
    private static final String API_KEY = DOTENV.get("COHERE_API_KEY");

    @Override
    public KeywordList generate(String focus) {
        return null;
    }

    public static void main(String[] args) {
        Cohere cohere = Cohere.builder().token(API_KEY).clientName("snippet").build();

        NonStreamedChatResponse response = cohere.chat(
                ChatRequest.builder()
                        .message("Given this tech focus: AI, generate me a list of keywords that can be used to find jobs on job boards.")
                                .build());

        System.out.println(response);
    }
}
