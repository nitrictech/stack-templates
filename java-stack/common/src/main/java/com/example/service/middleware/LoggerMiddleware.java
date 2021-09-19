package com.example.service.middleware;

import java.util.logging.Logger;

import io.nitric.faas2.http.HttpContext;
import io.nitric.faas2.http.HttpMiddleware;

/**
 * Provides a logger middleware.
 */
public class LoggerMiddleware extends HttpMiddleware {

    Logger logger = Logger.getLogger("LoggerMiddleware");

    @Override
    public HttpContext handle(HttpContext context, HttpMiddleware next) {

        var start = System.currentTimeMillis();

        var resultCtx = next.handle(context, next.getNext());

        var duration = System.currentTimeMillis() - start;
        var msg = String.format("Handled HTTP %s '%s' request in %s ms",
            context.getRequest().getMethod(),
            context.getRequest().getPath(),
            duration);

        logger.info(msg);

        return resultCtx;
    }
    
}
