package com.codingplatform.service;

import java.io.IOException;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import com.codingplatform.model.CodeSubmission;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Judge0Service {
    private static final String JUDGE0_URL = "https://judge0-ce.p.rapidapi.com/submissions?wait=true";
    private static final String API_KEY = ""; 

    public String submitCode(CodeSubmission submission) throws IOException {
        System.out.println("Inside submit code");
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(JUDGE0_URL);
            post.setHeader("Content-Type", "application/json");
            post.setHeader("x-rapidapi-key", API_KEY);
            post.setHeader("x-rapidapi-host", "judge0-ce.p.rapidapi.com");

            // Encode source code and stdin in Base64
            String encodedCode = submission.getSourceCode();
            String encodedStdin = submission.getStdin();

            // Create JSON payload
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(new Judge0Submission(encodedCode, submission.getLanguageId(), encodedStdin));

            System.out.println("json: " + json);
            post.setEntity(new StringEntity(json));

            return client.execute(post, response -> {
                String result = EntityUtils.toString(response.getEntity());
                System.out.println("result: " + result);
                return mapper.readTree(result).get("token").asText();
            });
        }
    }

    public String getSubmissionResult(String token) throws IOException {
        System.out.println("inside getSubmissionResult ");
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet get = new HttpGet("https://judge0-ce.p.rapidapi.com/submissions/" + token);
            get.setHeader("x-rapidapi-key", API_KEY);
            get.setHeader("x-rapidapi-host", "judge0-ce.p.rapidapi.com");

            return client.execute(get, response -> EntityUtils.toString(response.getEntity()));
        }
    }

    private static class Judge0Submission {
        public String source_code;
        public String language_id;
        public String stdin;

        public Judge0Submission(String sourceCode, String languageId, String stdin) {
            this.source_code = sourceCode;
            this.language_id = languageId;
            this.stdin = stdin;
        }
    }
}
