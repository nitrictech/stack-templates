import { faas, documents } from "@nitric/sdk";
import { Example } from "../common";

// Start your function here
faas
  .http(async (ctx: faas.HttpContext): Promise<faas.HttpContext> => {
    try {
      const examples = await documents()
        .collection<Example>("examples")
        .query()
        .fetch();

      const exampleResults = [];

      for (const example of examples.documents) {
        exampleResults.push(example.content);
      }
      ctx.res.json(exampleResults);
    } catch (e) {
      ctx.res.status = 500;
      ctx.res.body = new TextEncoder().encode("An unexpected error occurred");
    }

    return ctx;
  })
  .start();
