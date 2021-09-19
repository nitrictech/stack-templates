package com.example.service;

import java.io.IOException;
import java.util.UUID;

import com.example.service.middleware.LoggerMiddleware;
import com.example.service.model.Example;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nitric.api.document.Documents;
import io.nitric.faas2.Faas;
import io.nitric.faas2.http.HttpContext;
import io.nitric.faas2.http.HttpHandler;

/**
 * Provides an example document Create function.
 */
public class CreateFunction implements HttpHandler {

    final Documents documents;

    public CreateFunction(Documents documents) {
        this.documents = documents;
    }

    @Override
    public HttpContext handle(HttpContext context) {
        try {
            var json = context.getRequest().getDataAsText();
            var example = new ObjectMapper().readValue(json, Example.class);
            var id = UUID.randomUUID().toString();

            documents.collection("examples").doc(id, Example.class).set(example);

            var msg = String.format("Created example with ID: %s", id);
            context.getResponse()
                .status(200)
                .data(msg);

        } catch (IOException ioe) {
            context.getResponse()
                .status(500)
                .data("error: " + ioe);
        }

        return context;
    }

    public static void main(String[] args) {
        var createFunction = new CreateFunction(new Documents());

        new Faas()
            .http(createFunction)
            .addMiddleware(new LoggerMiddleware())
            .start();
    }

}