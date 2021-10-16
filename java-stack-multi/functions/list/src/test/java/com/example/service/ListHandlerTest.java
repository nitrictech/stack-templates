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
import io.nitric.faas.http.HttpContext;

/**
 * Provides a ListFunction unit test class.
 */
@ExtendWith(MockitoExtension.class)
public class ListHandlerTest {

    @Test
    public void test_handle() {

        var documents = mock(Documents.class, Mockito.RETURNS_DEEP_STUBS);

        List<ResultDoc<Example>> results = List.of(
            new ResultDoc<Example>(new Example("java-service", "Java Service Example")),
            new ResultDoc<Example>(new Example("python-service", "Pythyon Service Example")),
            new ResultDoc<Example>(new Example("typescript-service", "TypeScript Service Example"))
        );

        when(documents.collection(anyString())
                .query(eq(Example.class))
                .stream()
            ).thenReturn(results.stream());

        var context = HttpContext.newBuilder()
            .method("GET")
            .build();

        var function = new ListHandler(documents);

        var ctx = function.handle(context);

        assertNotNull(ctx);
        assertEquals(200, ctx.getResponse().getStatus());

        var data = ctx.getResponse().getDataAsText();
        assertTrue(data.contains("java-service"));
        assertTrue(data.contains("python-service"));
        assertTrue(data.contains("typescript-service"));
    }
    
}