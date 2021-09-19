package com.example.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.service.model.Example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import io.nitric.api.document.Collection;
import io.nitric.api.document.DocumentRef;
import io.nitric.api.document.Documents;
import io.nitric.faas2.http.HttpContext;

/**
 * Provides a CreateFunction unit test class.
 */
@ExtendWith(MockitoExtension.class)
public class CreateFunctionTest {

    @Test
    public void test_handle() {

        Documents documents = mock(Documents.class);
        Collection collection = mock(Collection.class);
        DocumentRef<Example> documentRef = mock(DocumentRef.class);

        when(documents.collection(matches("examples"))).thenReturn(collection);
        when(collection.doc(anyString(), eq(Example.class))).thenReturn(documentRef);
        doNothing().when(documentRef).set(isA(Example.class));

        var data = "{ \"name\": \"java-service\", \"description\": \"Nitric Java Maven Service Example\" }";

        var context = HttpContext.newBuilder()
            .method("POST")
            .path("/")
            .data(data)
            .build();

        var function = new CreateFunction(documents);

        var ctx = function.handle(context);

        assertNotNull(ctx);
        assertEquals(200, ctx.getResponse().getStatus());
        assertTrue(ctx.getResponse().getDataAsText().startsWith("Created example with ID:"));

        verify(documentRef, times(1)).set(any());
    }
    
}
