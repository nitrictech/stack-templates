package com.example.service.middleware;

import static org.mockito.AdditionalAnswers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.nitric.faas.http.HttpContext;
import io.nitric.faas.http.HttpHandler;
import io.nitric.faas.http.HttpMiddleware;

/**
 * Provides a LoggerMiddleware unit test.
 */
public class LoggerMiddlewareTest {

    @Test
    public void test_handle() {
        
        var ctx = HttpContext.newBuilder()
            .method("GET")
            .path("/examples")
            .build();
        
        HttpHandler mockHandler = mock(HttpHandler.class);
        when(mockHandler.handle(Mockito.any())).then(returnsFirstArg());

        var next = new HttpMiddleware.HandlerAdapter(mockHandler);
        
        var resultCtx = new LoggerMiddleware().handle(ctx, next);

        assertNotNull(resultCtx);

        verify(mockHandler, times(1)).handle(any());
    }

}
