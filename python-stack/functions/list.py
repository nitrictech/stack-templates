import json

from nitric.api.exception import NitricServiceException
from nitric.faas import start, HttpContext
from nitric.api import Documents


async def handler(ctx: HttpContext) -> HttpContext:
    try:
        examples_query = Documents().collection("examples").query()
        results = await examples_query.fetch()
        ctx.res.body = json.dumps([doc.content for doc in results.documents])
    except NitricServiceException:
        ctx.res.status = 500
        ctx.res.body = "An unexpected error occurred"

    return ctx


if __name__ == "__main__":
    start(handler)
