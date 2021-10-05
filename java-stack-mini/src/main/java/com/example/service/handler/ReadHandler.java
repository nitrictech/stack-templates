package com.example.service.handler;

import com.example.service.model.Example;

import io.nitric.api.NotFoundException;
import io.nitric.api.document.Documents;
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

        var id = context.getRequest().getQueryParam("exampleId");

        try {
            var json = documents.collection("examples")
                .doc(id, Example.class)
                .getJson();

            context.getResponse()
                .contentType("application/json")
                .data(json);

        } catch (NotFoundException nfe) {
            context.getResponse()
                .status(404)
                .data("Document not found: " + id);
        }

        return context;
    }
}