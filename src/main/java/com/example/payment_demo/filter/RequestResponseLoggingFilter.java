package com.example.payment_demo.filter;

import com.example.payment_demo.constants.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);
    private static final String CORRELATION_ID_HEADER_NAME = Constants.CORRELATION_ID.toLowerCase();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        String correlationId = extractCorrelationId(request);
        long startTime = System.currentTimeMillis();

        try {
            MDC.put(Constants.CORRELATION_ID, correlationId);
            response.setHeader(Constants.CORRELATION_ID, correlationId);

            filterChain.doFilter(requestWrapper, responseWrapper);

            long timeTaken = System.currentTimeMillis() - startTime;
            logRequestResponse(requestWrapper, responseWrapper, timeTaken);
        } finally {
            MDC.clear();
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequestResponse(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response,
                                    long timeTaken) throws IOException {
        String requestBody = getStringValue(request.getContentAsByteArray(), request.getCharacterEncoding());
        String responseBody = getStringValue(response.getContentAsByteArray(), response.getCharacterEncoding());

        Map<String, String> headers = new HashMap<>();
        Collections.list(request.getHeaderNames())
                .forEach(headerName -> headers.put(headerName, request.getHeader(headerName)));
        MDC.put("logType", "REQUEST");
        MDC.put("method", request.getMethod());
        MDC.put("uri", request.getRequestURI());
        MDC.put("requestBody", requestBody);
        MDC.put("responseBody", responseBody);
        MDC.put("status", String.valueOf(response.getStatus()));
        MDC.put("timeTaken", String.valueOf(timeTaken));
        MDC.put("headers", objectMapper.writeValueAsString(headers));

        log.info("Request-Response processed");

        // Clear added MDC values, but keep correlationId
        String correlationId = MDC.get(Constants.CORRELATION_ID);
        MDC.clear();
        MDC.put(Constants.CORRELATION_ID, correlationId);
    }

    private String extractCorrelationId(HttpServletRequest request) {
        String correlationId = request.getHeader(CORRELATION_ID_HEADER_NAME);
        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = generateCorrelationId();
        }
        return correlationId;
    }

    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }

    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            return "Error getting string value: " + e.getMessage();
        }
    }
}
