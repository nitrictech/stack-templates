import { faas, documents } from "@nitric/sdk";
import { Example, path } from "../common";

// Start your function here
faas
  .http(async (ctx: faas.HttpContext): Promise<faas.HttpContext> => {
    // get params from path
    const { id } = path.test(ctx.req.path);

    if (!id) {
      ctx.res.body = new TextEncoder().encode("Invalid Request");
      ctx.res.status = 400;
    }

    try {
      console.log("getting doc id", id);
      const example = await documents()
        .collection<Example>("examples")
        .doc(id)
        .get();

      ctx.res.json(example);
    } catch (e) {
      ctx.res.status = 404;
      ctx.res.body = new TextEncoder().encode(
        `Example not found!: ${e.message}`
      );
    }

    return ctx;
  })
  .start();
