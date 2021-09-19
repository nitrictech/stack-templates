package com.example.service;

import io.nitric.api.NotFoundException;
import io.nitric.api.document.Documents;
import io.nitric.faas2.Faas;
import io.nitric.faas2.http.HttpContext;
import io.nitric.faas2.http.HttpHandler;

/**
 * Provides an example document Read function.
 */
public class ReadFunction implements HttpHandler {

    final Documents documents;

    public ReadFunction(Documents documents) {
        this.documents = documents;
    }

    @Override
    public HttpContext handle(HttpContext context) {

        var paths = context.getRequest().getPath().split("/");
        var id = paths[paths.length - 1];

        try {
            var json = documents.collection("examples")
                .doc(id)
                .getJson();

            context.getResponse()
                .status(200)
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
        var read = new ReadFunction(new Documents());
        new Faas().http(read).start();
    }
}