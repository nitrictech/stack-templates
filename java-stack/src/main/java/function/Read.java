package function;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.nitric.api.NotFoundException;
import io.nitric.api.document.Documents;
import io.nitric.faas.Faas;

import java.io.IOException;

public class Read {

    public static void main(String[] args) {
        new Faas()
            .http(context -> {
                var paths = context.getRequest().getPath().split("/");
                var id = paths[paths.length - 1];
        
                try {
                    var example = new Documents().collection("examples")
                        .doc(id)
                        .get();

                    var json = new ObjectMapper().writeValueAsString(example);

                    context.getResponse()
                        .contentType("application/json")
                        .text(json);
        
                } catch (NotFoundException nfe) {
                    context.getResponse()
                        .status(404)
                        .text("Document not found: %s", id);

                } catch (IOException ioe) {
                    context.getResponse()
                        .status(500)
                        .text("Error getting examples: %s", ioe);
                }
            
                return context;
            })
            .start();
    }
}