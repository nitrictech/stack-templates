package com.example.service;

import com.example.service.middleware.LoggerMiddleware;
import com.example.service.model.Example;

import io.nitric.api.NotFoundException;
import io.nitric.api.document.Documents;
import io.nitric.faas.Faas;
import io.nitric.faas.http.HttpContext;
import io.nitric.faas.http.HttpHandler;

/**
 * Provides an example document ReadHandler function.
 */
public class ReadHandler implements HttpHandler {

    final Documents documents;

    public ReadHandler(Documents documents) {
        this.documents = documents;
    }

    @Override
    public HttpContext handle(HttpContext context) {

        var paths = context.getRequest().getPath().split("/");
        var id = paths[paths.length - 1];

        try {
            var json = documents.collection("examples")
                .doc(id, Example.class)
                .getJson();

            context.getResponse()
                .addHeader("Content-Type", "application/json")
                .data(json);

        } catch (NotFoundException nfe) {
            context.getResponse()
                .status(404)
                .data("Document not found: " + id);
        }

        return context;
    }

    public static void main(String[] args) {
        var loggerMiddleware = new LoggerMiddleware();
        var readHandler = new ReadHandler(new Documents());

        new Faas()
            .http(loggerMiddleware)
            .http(readHandler)
            .start();
    }
}