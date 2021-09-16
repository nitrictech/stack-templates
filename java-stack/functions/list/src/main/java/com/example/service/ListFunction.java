package com.example.service;

import com.example.service.model.Example;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nitric.api.document.Documents;
import io.nitric.api.document.ResultDoc;
import io.nitric.faas.Faas;
import io.nitric.faas.Trigger;
import io.nitric.faas.NitricFunction;
import io.nitric.faas.Response;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListFunction implements NitricFunction {

    Documents documents = new Documents();

    @Override
    public Response handle(Trigger trigger) {
        Stream<ResultDoc<Example>> stream = documents.collection("examples").query(Example.class).stream();

        List<Example> examples = stream
             .map(ResultDoc::getContent)
             .collect(Collectors.toUnmodifiableList());

        try {
            var json = new ObjectMapper().writeValueAsString(examples);

            var response = trigger.buildResponse(json);
            response.getContext().asHttp().addHeader("Content-Type", "application/json");

            return response;

        } catch (IOException ioe) {
            var response = trigger.buildResponse("Error querying examples: " + ioe.toString());
            response.getContext().asHttp().setStatus(500);
            return response;
        }
    }

    public static void main(String[] args) {
        Faas.start(new ListFunction());
    }

}