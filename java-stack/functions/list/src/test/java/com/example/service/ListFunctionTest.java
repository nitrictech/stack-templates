package com.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import com.example.service.model.Example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import io.nitric.api.document.Documents;
import io.nitric.api.document.ResultDoc;
import io.nitric.mock.faas.MockTrigger;

@ExtendWith(MockitoExtension.class)
public class ListFunctionTest {

    @Test
    public void test_handle() {

        var function = new ListFunction();
        function.documents = mock(Documents.class, Mockito.RETURNS_DEEP_STUBS);

        List<ResultDoc<Example>> results = List.of(
            new ResultDoc<Example>(null, new Example("java-service", "Java Service Example")),
            new ResultDoc<Example>(null, new Example("python-service", "Pythyon Service Example")),
            new ResultDoc<Example>(null, new Example("typescript-service", "TypeScript Service Example"))
        );

        when(function.documents.collection(anyString())
                .query(eq(Example.class))
                .stream()
            ).thenReturn(results.stream());

        var trigger = MockTrigger.newHttpTriggerBuilder()
            .setMethod("GET")
            .build();

        var response = function.handle(trigger);

        assertEquals(200, response.getContext().asHttp().getStatus());
        assertTrue(response.getDataAsText().contains("java-service"));
        assertTrue(response.getDataAsText().contains("python-service"));
        assertTrue(response.getDataAsText().contains("typescript-service"));
    }
    
}