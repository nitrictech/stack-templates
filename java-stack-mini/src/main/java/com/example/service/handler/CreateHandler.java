package com.example.service.handler;

import java.io.IOException;
import java.util.UUID;

import com.example.service.model.Example;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nitric.api.document.Documents;
import io.nitric.faas.http.HttpContext;
import io.nitric.faas.http.HttpHandler;

/**
 * Provides an example document CreateHandler function.
 */
public class CreateHandler implements HttpHandler {

    final Documents documents;

    public CreateHandler(Documents documents) {
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
            context.getResponse().data(msg);

        } catch (IOException ioe) {
            context.getResponse()
                .status(500)
                .data("error: " + ioe);
        }

        return context;
    }

}