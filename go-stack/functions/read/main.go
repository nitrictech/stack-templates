package main

import (
	"encoding/json"
	"fmt"
	"path"

	"github.com/nitrictech/go-sdk/api/documents"
	"github.com/nitrictech/go-sdk/faas"
)

// NitricFunction - Handles individual function requests (http, events, etc.)
func NitricFunction(trigger *faas.NitricTrigger) (*faas.NitricResponse, error) {
	resp := trigger.DefaultResponse()

	id := path.Base(trigger.GetContext().AsHttp().Path)

	dc, err := documents.New()
	if err != nil {
		return nil, err
	}

	doc, err := dc.Collection("examples").Doc(id).Get()
	if err != nil {
		resp.SetData([]byte("Error retrieving document"))
		resp.GetContext().AsHttp().Status = 404
		return resp, nil
	}

	b, err := json.Marshal(doc.Content())
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
