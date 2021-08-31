package com.example.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.nitric.mock.api.document.MockDocuments;
import io.nitric.mock.faas.MockTrigger;
import org.mockito.Mockito;

public class CreateFunctionTest {

    @Test
    public void test_handle() {
        var data = "{ \"name\": \"java-service\", \"description\": \"Nitric Java Maven Service Example\" }";

        var trigger = MockTrigger.newHttpTriggerBuilder()
            .setMethod("POST")
            .setPath("/")
            .setDataAsText(data)
            .build();

        var mock = new MockDocuments().whenSet();

        var function = new CreateFunction();

        var response = function.handle(trigger);

        assertTrue(response.getDataAsText().startsWith("Created example with ID:"));

        Mockito.verify(mock.getMock(), Mockito.times(1)).set(Mockito.any());
    }

}