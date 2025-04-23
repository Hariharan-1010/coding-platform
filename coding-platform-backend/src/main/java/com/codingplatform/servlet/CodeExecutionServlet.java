package com.codingplatform.servlet;

import java.io.IOException;

import com.codingplatform.model.CodeSubmission;
import com.codingplatform.service.Judge0Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CodeExecutionServlet extends HttpServlet {
    private final Judge0Service judge0Service = new Judge0Service();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            sb.append(line);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            CodeSubmission submission = mapper.readValue(sb.toString(), CodeSubmission.class);

            System.out.println("submission: " + submission.toString());

            String token = judge0Service.submitCode(submission);
            System.out.println("token: " + token);
            String result = judge0Service.getSubmissionResult(token);
            System.out.println("result: " + result);

            response.getWriter().write(result);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Judge0 API error\"}");
        }
    }
}