package common

import (
	"encoding/json"

	"github.com/nitrictech/go-sdk/api/errors"
	"github.com/nitrictech/go-sdk/api/errors/codes"
	"github.com/nitrictech/go-sdk/faas"
)

// HttpDecodeMiddleware - Middleware for decoding HTTP body as a struct
func Json(key string) faas.HttpMiddleware {
	return func(ctx *faas.HttpContext, next faas.HttpHandler) (*faas.HttpContext, error) {
		js := make(map[string]interface{})

		if err := json.Unmarshal(ctx.Request.Data(), &js); err != nil {
			return nil, errors.NewWithCause(
				codes.InvalidArgument,
				"HttpJsonMiddleware: Decoding Error",
				err,
			)
		}

		// decode into the copy of the struct
		ctx.Extras[key] = js

		return next(ctx)
	}
}
