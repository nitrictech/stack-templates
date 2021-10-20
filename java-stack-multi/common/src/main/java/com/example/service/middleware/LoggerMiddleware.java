package com.example.service.middleware;

import io.nitric.faas.http.HttpContext;
import io.nitric.faas.http.HttpMiddleware;
import io.nitric.faas.TriggerProcessor.HttpMiddlewareWrapper;

/**
 * Provides a performance logging middleware.
 */
public class LoggerMiddleware extends HttpMiddleware {

    @Override
    public HttpContext handle(HttpContext context, HttpMiddleware next) {

        var start = System.currentTimeMillis();

        var ctx = next.handle(context, next.getNext());

        var handlerName = next.getClass().getSimpleName();
        if (next instanceof HttpMiddlewareWrapper) {
            var target = ((HttpMiddlewareWrapper) next).getTarget();
            if (target instanceof HandlerAdapter) {
                var adapter = (HandlerAdapter) target;
                handlerName = adapter.getHandler().getClass().getSimpleName();
            } else {
                handlerName = target.getClass().getSimpleName();
            }
        }

        var duration = System.currentTimeMillis() - start;
        System.out.printf("HTTP %s %s -> %s %s handled in %s ms\n",
                          ctx.getRequest().getMethod(),
                          ctx.getRequest().getPath(),
                          ctx.getResponse().getStatus(),
                          handlerName,
                          duration);

        return ctx;
    }
    
}
