from nitric.api.exception import NotFoundException, NitricServiceException
from nitric.faas import start, Trigger, Response
from nitric.api import Documents


async def handler(trigger: Trigger) -> Response:
    ctx = trigger.context.as_http()
    response = trigger.default_response()

    # get id param from HTTP request path
    try:
        doc_id = ctx.path.split("/")[-1]
    except IndexError:
        response.data = "Invalid request"
        response.context.as_http().status = 400
        return response

    try:
        example = await Documents().collection("examples").doc(doc_id).get()
        response.data = example.content
    except NotFoundException:
        response.context.as_http().status = 404
        response.data = "Example not found"
    except NitricServiceException:
        response.context.as_http().status = 500
        response.data = "An unexpected error occurred"

    return response


if __name__ == "__main__":
    start(handler)
