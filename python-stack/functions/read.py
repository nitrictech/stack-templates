from nitric.api.exception import NotFoundException, NitricServiceException
from nitric.faas import start, HttpContext
from nitric.api import Documents
import json


async def handler(ctx: HttpContext) -> HttpContext:
    try:
        doc_id = ctx.req.path.split("/")[-1]
    except IndexError:
        ctx.res.body = "Invalid request"
        ctx.res.status = 400
        return ctx

    try:
        example = await Documents().collection("examples").doc(doc_id).get()
        ctx.res.body = json.dumps(example.content)
    except NotFoundException:
        ctx.res.status = 404
        ctx.res.body = "Example not found"
    except NitricServiceException:
        ctx.res.status = 500
        ctx.res.body = "An unexpected error occurred"

    return ctx


if __name__ == "__main__":
    start(handler)
