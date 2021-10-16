package function;

import common.Example;

import io.nitric.api.NotFoundException;
import io.nitric.api.document.Documents;
import io.nitric.faas.Faas;

public class Read {

    public static void main(String[] args) {
        new Faas()
            .http(context -> {
                var paths = context.getRequest().getPath().split("/");
                var id = paths[paths.length - 1];
        
                try {
                    var json = new Documents().collection("examples")
                        .doc(id, Example.class)
                        .getJson();
        
                    context.getResponse()
                        .contentType("application/json")
                        .data(json);
        
                } catch (NotFoundException nfe) {
                    context.getResponse()
                        .status(404)
                        .data("Document not found: " + id);
                }
        
                return context;
            })
            .start();
    }
}