package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import com.example.service.model.Example;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import io.grpc.Status;
import io.nitric.mock.api.document.MockDocuments;
import io.nitric.mock.faas.MockTrigger;

public class ReadFunctionTest {

    @Test
    public void test_handle_ok() throws IOException {

        var trigger = MockTrigger.newHttpTriggerBuilder()
            .setMethod("GET")
            .setPath("123")
            .build();

        var example = new Example("name", "description");
        new MockDocuments().whenGet(example);

        var function = new ReadFunction();

        var response = function.handle(trigger);

        var json = new ObjectMapper().writeValueAsString(example);
        assertEquals(json, response.getDataAsText());

        var context = response.getContext().asHttp();
        assertEquals(200, context.getStatus());
        assertEquals("application/json", context.getHeaders().get("Content-Type"));
    }

    @Test
    public void test_handle_not_found() {

        var trigger = MockTrigger.newHttpTriggerBuilder()
            .setMethod("GET")
            .setPath("123")
            .build();

        new MockDocuments().whenGetError(Status.NOT_FOUND);

        var function = new ReadFunction();

        var response = function.handle(trigger);

        assertTrue(response.getDataAsText().startsWith("Error retrieving document: "));

        var context = response.getContext().asHttp();
        assertEquals(404, context.getStatus());
    }

}