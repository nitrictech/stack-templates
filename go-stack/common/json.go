package common

import (
	"encoding/json"

	"github.com/nitrictech/go-sdk/faas"
)

// Json - Middleware parsing http context data as map[string]interface{}
func Json(key string) faas.HttpMiddleware {
	return func(ctx *faas.HttpContext, next faas.HttpHandler) (*faas.HttpContext, error) {
		js := make(map[string]interface{})

		if err := json.Unmarshal(ctx.Request.Data(), &js); err != nil {
			ctx.Response.Body = []byte("Bad Request: Expected JSON body")
			ctx.Response.Status = 400

			return ctx, nil
		}

		// decode into the copy of the struct
		ctx.Extras[key] = js

		return next(ctx)
	}
}
