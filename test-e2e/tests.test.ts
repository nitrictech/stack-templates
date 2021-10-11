// Copyright 2021, Nitric Technologies Pty Ltd.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
import * as request from "supertest";

const api = request.agent("http://localhost:49152");

const newExample = {
  name: "test",
  description: "test",
};

const newExample2 = {
  name: "test2",
  description: "test2",
};

describe("Stack Template API Tests", () => {
  let id: string;

  test("Should get empty examples list - /examples", async () => {
    const res = await api.get("/examples");

    expect(res.statusCode).toBe(200);
    expect(JSON.parse(res.text)).toEqual([]);
  });

  test("Should Set an example - /examples", async () => {
    const res = await api.post("/examples").send(newExample);

    expect(res.statusCode).toBe(200);

    // set id
    id = res.text.split("Created example with ID: ")[1];

    // Ensure ID is defined
    expect(id).toBeDefined();

    expect(res.text).toContain("Created example with ID: ");
  });

  test("Should get example - /examples/{id}", async () => {
    const res = await api.get(`/examples/${id}`);

    expect(res.statusCode).toBe(200);
    expect(JSON.parse(res.text)).toEqual(newExample);
  });

  test("Should list new example - /examples", async () => {
    const res = await api.get("/examples");

    expect(res.statusCode).toBe(200);
    expect(JSON.parse(res.text)).toEqual([newExample]);
  });
});
