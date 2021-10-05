package com.example.service;

import com.example.service.handler.CreateHandler;
import com.example.service.handler.ListHandler;
import com.example.service.handler.ReadHandler;

import io.nitric.api.document.Documents;
import io.nitric.faas.Faas;
import io.nitric.faas.http.HttpRouter;

/**
 * Provides an ExampleService 'minilinth'.
 */
public class ExampleService {
    
    /**
     * Provides the application entry point.
     */
    public static void main(String[] args) {        

        var documents = new Documents();

        var router = new HttpRouter()
            .post("/examples", new CreateHandler(documents))
            .get("/examples", new ListHandler(documents))
            .get("/examples/{exampleId}", new ReadHandler(documents))
            .logger(new SysLogger());

        new Faas()
            .http(router)
            .start();
    }

}
