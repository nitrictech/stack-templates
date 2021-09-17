package com.example.service;

import java.io.IOException;
import java.util.UUID;

import com.example.service.model.Example;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nitric.api.document.Documents;
import io.nitric.faas.Faas;
import io.nitric.faas.NitricFunction;
import io.nitric.faas.Response;
import io.nitric.faas.Trigger;

public class CreateFunction implements NitricFunction {

    final Documents documents;

    public CreateFunction(Documents documents) {
        this.documents = documents;
    }

    @Override
    public Response handle(Trigger trigger) {
        try {
            var json = trigger.getDataAsText();
            var example = new ObjectMapper().readValue(json, Example.class);
            var id = UUID.randomUUID().toString();

            documents.collection("examples").doc(id, Example.class).set(example);

            var msg = String.format("Created example with ID: %s", id);
            return trigger.buildResponse(msg);

        } catch (IOException ioe) {
            return trigger.buildResponse("error: " + ioe);
        }
    }

    public static void main(String[] args) {
        Faas.start(new CreateFunction(new Documents()));
    }

}