package com.example.service.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ExampleTest {

    @Test
    public void test_toString() {
        var example = new Example();
        example.setName("Name");
        example.setDescription("Description");

        assertEquals("Example[name=Name, description=Description]", example.toString());
    }
    
}
