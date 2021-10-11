package common

import (
	"strings"

	"github.com/nitrictech/go-sdk/faas"
)

const paramToken = ":"
const paramsKey = "params"

// PathParser - Middleware for parsing parameters from HTTP path
func PathParser(paramExpression string) faas.HttpMiddleware {
	pathParts := strings.Split(paramExpression, "/")
	parts := make(map[int]string)

	for i, s := range pathParts {
		if strings.HasPrefix(s, paramToken) {
			parts[i] = strings.Replace(s, paramToken, "", -1)
		}
	}

	return func(ctx *faas.HttpContext, next faas.HttpHandler) (*faas.HttpContext, error) {
		pathParts := strings.Split(ctx.Request.Path(), "/")

		params := make(map[string]string)

		for i, part := range pathParts {
			if parts[i] != "" {
				params[parts[i]] = part
			}
		}

		// decode into the copy of the struct
		ctx.Extras["params"] = params

		return next(ctx)
	}
}
