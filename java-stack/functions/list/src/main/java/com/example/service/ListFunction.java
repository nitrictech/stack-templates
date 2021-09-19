package com.example.service;

import com.example.service.model.Example;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nitric.api.document.Documents;
import io.nitric.api.document.ResultDoc;
import io.nitric.faas2.Faas;
import io.nitric.faas2.http.HttpContext;
import io.nitric.faas2.http.HttpHandler;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides an example document List function.
 */
public class ListFunction implements HttpHandler {

    final Documents documents;

    public ListFunction(Documents documents) {
        this.documents = documents;
    }

    @Override
    public HttpContext handle(HttpContext context) {
        Stream<ResultDoc<Example>> stream = documents.collection("examples")
            .query(Example.class)
            .stream();

        List<Example> examples = stream
             .map(ResultDoc::getContent)
             .collect(Collectors.toUnmodifiableList());

        try {
            var json = new ObjectMapper().writeValueAsString(examples);

            context.getResponse()
                .status(200)
                .addHeader("Content-Type", "application/json")
                .data(json);

        } catch (IOException ioe) {
            context.getResponse()
                .status(500)
                .data("Error querying examples: " + ioe.toString());
        }

        return context;
    }

    public static void main(String[] args) {
        var list = new ListFunction(new Documents());
        new Faas().http(list).start();
    }

}