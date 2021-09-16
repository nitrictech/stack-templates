package com.example.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.service.model.Example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import io.nitric.api.document.Documents;
import io.nitric.mock.faas.MockTrigger;

@ExtendWith(MockitoExtension.class)
public class ReadFunctionTest {

    @Test
    public void test_handle() {

        var function = new ReadFunction();
        function.documents = mock(Documents.class, Mockito.RETURNS_DEEP_STUBS);

        var example = new Example("name", "description");

        when(function.documents.collection(anyString())
                .doc(matches("123"), eq(Example.class))
                .get()
            ).thenReturn(example);

        var trigger = MockTrigger.newHttpTriggerBuilder()
            .setMethod("GET")
            .setPath("123")
            .build();

        var response = function.handle(trigger);

        assertEquals(200, response.getContext().asHttp().getStatus());
        assertEquals("{\"name\":\"name\",\"description\":\"description\"}", response.getDataAsText());
    }

}
