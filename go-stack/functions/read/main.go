package main

import (
	"encoding/json"
	"fmt"

	"github.com/nitrictech/go-sdk/api/documents"
	"github.com/nitrictech/go-sdk/faas"
	"nitric.io/example-service/common"
)

func handler(ctx *faas.HttpContext, next faas.HttpHandler) (*faas.HttpContext, error) {
	params, ok := ctx.Extras["params"].(map[string]string)

	if !ok || params == nil {
		return nil, fmt.Errorf("error retrieving path params")
	}

	id := params["id"]

	dc, err := documents.New()
	if err != nil {
		return nil, err
	}

	doc, err := dc.Collection("examples").Doc(id).Get()
	if err != nil {
		ctx.Response.Body = []byte("Error retrieving document")
		ctx.Response.Status = 404
	} else {
		b, err := json.Marshal(doc.Content())
		if err != nil {
			return nil, err
		}

		ctx.Response.Headers["Content-Type"] = []string{"application/json"}
		ctx.Response.Body = b
	}

	return next(ctx)
}

func main() {
	err := faas.New().Http(
		// Retrieve path parameters if available
		common.PathParser("/examples/:id"),
		// Actual Handler
		handler,
	).Start()

	if err != nil {
		fmt.Println(err)
	}
}
