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

    @Override
    public Response handle(Trigger trigger) {
        var id = trigger.getContext().asHttp().getPath();
        
        try {
            var example = Documents.collection("examples").doc(id, Example.class).get();

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
        Faas.start(new ReadFunction());
    }

}