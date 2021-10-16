package function;

import java.io.IOException;
import java.util.UUID;

import common.Example;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nitric.api.document.Documents;
import io.nitric.faas.Faas;

public class Create {

    public static void main(String[] args) {
        new Faas()
            .http(context -> {
                try {
                    var json = context.getRequest().getDataAsText();
                    var example = new ObjectMapper().readValue(json, Example.class);
                    var id = UUID.randomUUID().toString();
        
                    new Documents().collection("examples").doc(id, Example.class).set(example);
        
                    var msg = String.format("Created example with ID: %s", id);
                    context.getResponse().data(msg);
        
                } catch (IOException ioe) {
                    context.getResponse()
                        .status(500)
                        .data("error: " + ioe);
                }
        
                return context;
            })
            .start();
    }

}