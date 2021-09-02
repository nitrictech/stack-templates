package main

import (
	"encoding/json"
	"fmt"
	"io"

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

	docIter, err := dc.Collection("example").Query().Stream()
	if err != nil {
		return nil, err
	}

	docs := make([]map[string]interface{}, 0)

	for d, err := docIter.Next(); err != io.EOF; d, err = docIter.Next() {
		if err != nil {
			return nil, err
		}

		docs = append(docs, d.Content())
	}

	b, err := json.Marshal(docs)
	if err != nil {
		return nil, err
	}

	resp.SetData(b)
	resp.GetContext().AsHttp().Headers["Content-Type"] = "application/json"

	return resp, nil
}

func main() {
	if err := faas.Start(NitricFunction); err != nil {
		fmt.Println(err)
	}
}
