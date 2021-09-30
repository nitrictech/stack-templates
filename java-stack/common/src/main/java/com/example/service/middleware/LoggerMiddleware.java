package com.example.service.middleware;

import io.nitric.faas.http.HttpContext;
import io.nitric.faas.http.HttpMiddleware;

/**
 * Provides a performance logging middleware.
 */
public class LoggerMiddleware extends HttpMiddleware {

    @Override
    public HttpContext handle(HttpContext context, HttpMiddleware next) {

        var start = System.currentTimeMillis();

        var ctx = next.handle(context, next.getNext());

        var duration = System.currentTimeMillis() - start;
        System.out.printf("HTTP %s %s -> %s handled in %s ms\n",
                          ctx.getRequest().getMethod(),
                          ctx.getRequest().getPath(),
                          next.getClass().getSimpleName(),
                          duration);

        return ctx;
    }
    
}
