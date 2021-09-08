import { faas, documents } from "@nitric/sdk";
import { Example, path } from "../common";

// Start your function here
faas.start(
  async (
    request: faas.NitricTrigger<void>
  ): Promise<faas.Response<Example | string>> => {
    const ctx = request.context.asHttp();
    const response: faas.Response<Example | string> = request.defaultResponse();

    // get params from path
    const { id } = path.test(ctx.path);

    if (!id) {
      response.data = "Invalid Request";
      response.context.asHttp().status = 400;
    }

    try {
      console.log("getting doc id", id);
      const example = await documents()
        .collection<Example>("example")
        .doc(id)
        .get();
      response.data = example;
    } catch (e) {
      response.context.asHttp().status = 404;
      response.data = `Example not found!: ${e.message}`;
    }

    return response;
  }
);
