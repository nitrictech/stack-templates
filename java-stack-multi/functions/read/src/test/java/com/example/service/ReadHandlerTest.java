package com.example.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import io.nitric.api.NotFoundException;
import io.nitric.api.NitricException.Code;
import io.nitric.api.document.Documents;
import io.nitric.faas.http.HttpContext;

/**
 * Provides a ReadFunction unit test class.
 */
@ExtendWith(MockitoExtension.class)
public class ReadHandlerTest {

    @Test
    public void test_handle() {

        var documents = mock(Documents.class, Mockito.RETURNS_DEEP_STUBS); 

        var map = Map.of("name", "name", "description", "description");

        when(documents.collection(anyString())
                .doc(matches("e56b618e-bc13-41be-b935-8c280e37fcce"))
                .get()
            ).thenReturn(map);

        var context = HttpContext.newBuilder()
            .method("GET")
            .path("/examples/e56b618e-bc13-41be-b935-8c280e37fcce")
            .build();

        var function = new ReadHandler(documents);

        var ctx = function.handle(context);

        assertNotNull(ctx);
        assertEquals(200, ctx.getResponse().getStatus());
        assertTrue(ctx.getResponse().getDataAsText().contains("\"description\":\"description\""));
        assertTrue(ctx.getResponse().getDataAsText().contains("\"name\":\"name\""));
    }

    @Test
    public void test_not_found() {

        var documents = mock(Documents.class, Mockito.RETURNS_DEEP_STUBS); 

        when(documents.collection(anyString())
                .doc(anyString())
                .get()
            ).thenThrow(new NotFoundException(Code.NOT_FOUND, "", null, null));

        var context = HttpContext.newBuilder()
            .method("GET")
            .path("/examples/e56b618e-bc13-41be-b935-8c280e37fcce")
            .build();

        var function = new ReadHandler(documents);

        var ctx = function.handle(context);

        assertNotNull(ctx);
        assertEquals(404, ctx.getResponse().getStatus());
        assertEquals("Document not found: e56b618e-bc13-41be-b935-8c280e37fcce", ctx.getResponse().getDataAsText());
    }

}
