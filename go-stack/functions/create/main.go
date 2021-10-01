package main

import (
	"fmt"

	"github.com/google/uuid"
	"github.com/nitrictech/go-sdk/api/documents"
	"github.com/nitrictech/go-sdk/faas"
	"nitric.io/example-service/common"
)

const exampleKey = "example"

func handler(ctx *faas.HttpContext, next faas.HttpHandler) (*faas.HttpContext, error) {
	id := uuid.New().String()
	example, ok := ctx.Extras[exampleKey].(map[string]interface{})

	if !ok || example == nil {
		return nil, fmt.Errorf("unable to retrieve decoded example")
	}

	dc, err := documents.New()

	if err != nil {
		return nil, err
	}

	if err := dc.Collection("examples").Doc(id).Set(example); err != nil {
		return nil, err
	}

	ctx.Response.Status = 201
	ctx.Response.Body = []byte(fmt.Sprintf("Created example with ID: %s", id))

	return next(ctx)
}

func main() {
	err := faas.New().Http(
		// Decoding middleware
		common.Json(exampleKey),
		// Actual Handler
		handler,
	).Start()

	if err != nil {
		fmt.Println(err)
	}
}
