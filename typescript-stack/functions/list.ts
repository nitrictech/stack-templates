import { faas, documents } from "@nitric/sdk";
import { Example } from "../common";

// Start your function here
faas.start(
  async (request: faas.NitricTrigger<void>): Promise<faas.Response<any>> => {
    const response: faas.Response<any> = request.defaultResponse();

    try {
      const examples = await documents()
        .collection<Example>("examples")
        .query()
        .fetch();
      const exampleResults = [];

      for (const example of examples.documents) {
        exampleResults.push(example.content);
      }
      response.data = exampleResults;
    } catch (e) {
      console.log(e);
      response.context.asHttp().status = 404;
      response.data = "Examples not found!";
    }

    return response;
  }
);
