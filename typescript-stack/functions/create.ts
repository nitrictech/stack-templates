import { faas, documents } from "@nitric/sdk";
import { Example } from "../common";
import { uuid } from "uuidv4";

// Start your function here
faas.start(async (request: faas.NitricTrigger<Example>): Promise<string> => {
  const example = request.dataAsObject();

  // generate a new uuid
  const id = uuid();

  // Create a new example document
  await documents().collection("example").doc(id).set(example);

  return `Created example with ID: ${id}`;
});
