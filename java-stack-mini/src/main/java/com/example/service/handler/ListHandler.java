package com.example.service.handler;

import com.example.service.model.Example;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nitric.api.document.Documents;
import io.nitric.api.document.ResultDoc;
import io.nitric.faas.http.HttpContext;
import io.nitric.faas.http.HttpHandler;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides an example document ListHandler function.
 */
public class ListHandler implements HttpHandler {

    final Documents documents;

    public ListHandler(Documents documents) {
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
                .contentType("application/json")
                .data(json);

        } catch (IOException ioe) {
            context.getResponse()
                .status(500)
                .data("Error querying examples: " + ioe.toString());
        }

        return context;
    }

}