package main

import (
	"encoding/json"
	"fmt"

	"github.com/nitrictech/go-sdk/api/documents"
	"github.com/nitrictech/go-sdk/faas"
)

func handler(ctx *faas.HttpContext, next faas.HttpHandler) (*faas.HttpContext, error) {
	dc, err := documents.New()
	if err != nil {
		return nil, err
	}

	query := dc.Collection("examples").Query()
	results, err := query.Fetch()

	if err != nil {
		return nil, err
	}

	docs := make([]map[string]interface{}, 0)

	for _, doc := range results.Documents {
		// handle documents
		docs = append(docs, doc.Content())
	}

	b, err := json.Marshal(docs)
	if err != nil {
		return nil, err
	}

	ctx.Response.Body = b
	ctx.Response.Headers["Content-Type"] = []string{"application/json"}

	return next(ctx)
}

func main() {
	err := faas.New().Http(
		// Actual Handler
		handler,
	).Start()

	if err != nil {
		fmt.Println(err)
	}
}
