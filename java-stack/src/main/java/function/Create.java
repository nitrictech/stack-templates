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
        
                    context.getResponse().text("Created example with ID: %s", id);
        
                } catch (IOException ioe) {
                    context.getResponse()
                        .status(500)
                        .text("error: %s", ioe);
                }
        
                return context;
            })
            .start();
    }

}