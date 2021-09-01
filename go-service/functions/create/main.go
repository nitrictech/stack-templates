package main

import (
	"fmt"

	"github.com/google/uuid"
	"github.com/nitrictech/go-sdk/api/documents"
	"github.com/nitrictech/go-sdk/faas"
	"nitric.io/example-service/common"
)

// NitricFunction - Handles individual function requests (http, events, etc.)
func NitricFunction(trigger *faas.NitricTrigger) (*faas.NitricResponse, error) {
	id := uuid.New().String()
	resp := trigger.DefaultResponse()

	example := &common.Example{}

	if err := trigger.GetDataAsStruct(example); err != nil {
		return nil, err
	}

	dc, err := documents.New()

	if err != nil {
		return nil, err
	}

	if err := dc.Collection("example").Doc(id).Set(example.Map()); err != nil {
		return nil, err
	}

	resp.SetData([]byte(fmt.Sprintf("Successfully created example: %s", id)))

	return resp, nil
}

func main() {
	if err := faas.Start(NitricFunction); err != nil {
		fmt.Println(err)
	}
}
