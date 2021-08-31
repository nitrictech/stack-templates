package com.example.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;

import com.example.service.model.Example;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import io.nitric.mock.faas.MockTrigger;
import io.nitric.mock.api.document.MockDocuments;

public class ListFunctionTest {

    @Test
    public void test_handle() throws IOException {
        var results = new ArrayList<Example>();
        results.add(new Example("java-service", "Java Service Example"));
        results.add(new Example("python-service", "Pythyon Service Example"));
        results.add(new Example("typescript-service", "TypeScript Service Example"));

        new MockDocuments().whenQuery("example", results);

        var trigger = MockTrigger.newHttpTriggerBuilder()
            .setMethod("GET")
            .setPath("/")
            .build();

        var function = new ListFunction();

        var response = function.handle(trigger);

        var json = new ObjectMapper().writeValueAsString(results);
        assertEquals(json, response.getDataAsText());

        var context = response.getContext().asHttp();
        assertEquals(200, context.getStatus());
        assertEquals("application/json", context.getHeaders().get("Content-Type"));
    }

}