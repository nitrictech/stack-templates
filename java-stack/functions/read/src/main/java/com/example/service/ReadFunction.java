package com.example.service;

import java.io.IOException;

import com.example.service.model.Example;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nitric.api.document.Documents;
import io.nitric.faas.Faas;
import io.nitric.faas.Trigger;
import io.nitric.faas.NitricFunction;
import io.nitric.faas.Response;

public class ReadFunction implements NitricFunction {

    final Documents documents;

    public ReadFunction(Documents documents) {
        this.documents = documents;
    }

    @Override
    public Response handle(Trigger trigger) {
        var paths = trigger.getContext().asHttp().getPath().split("/");
        var id = paths[paths.length - 1];

        try {
            var example = documents.collection("examples").doc(id, Example.class).get();

            var json = new ObjectMapper().writeValueAsString(example);

            var response = trigger.buildResponse(json);
            response.getContext().asHttp().addHeader("Content-Type", "application/json");
            response.getContext().asHttp().setStatus(200);
            return response;

        } catch (IOException ioe) {
            var response = trigger.buildResponse("Error retrieving document: ");
            response.getContext().asHttp().setStatus(500);
            return response;
        }
    }

    public static void main(String[] args) {
        Faas.start(new ReadFunction(new Documents()));
    }

}