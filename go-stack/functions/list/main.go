package main

import (
	"encoding/json"
	"fmt"

	"github.com/nitrictech/go-sdk/api/documents"
	"github.com/nitrictech/go-sdk/faas"
)

// NitricFunction - Handles individual function requests (http, events, etc.)
func NitricFunction(trigger *faas.NitricTrigger) (*faas.NitricResponse, error) {
	resp := trigger.DefaultResponse()

	dc, err := documents.New()
	if err != nil {
		return nil, err
	}

	query := dc.Collection("example").Query()
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

	resp.SetData(b)
	resp.GetContext().AsHttp().Headers["Content-Type"] = []string{"application/json"}

	return resp, nil
}

func main() {
	if err := faas.Start(NitricFunction); err != nil {
		fmt.Println(err)
	}
}
