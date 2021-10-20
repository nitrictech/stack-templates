package com.example.service;

import java.io.IOException;

import com.example.service.middleware.LoggerMiddleware;
import com.fasterxml.jackson.databind.ObjectMapper;

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
            var example = documents.collection("examples")
                .doc(id)
                .get();

            var json = new ObjectMapper().writeValueAsString(example);

            context.getResponse()
                .contentType("application/json")
                .text(json);

        } catch (NotFoundException nfe) {
            context.getResponse()
                .status(404)
                .text("Document not found: %s", id);

        } catch (IOException ioe) {
            context.getResponse()
                .status(500)
                .text("Error querying examples: %s", ioe);
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