from nitric.api.exception import NitricServiceException
from nitric.faas import start, Trigger, Response
from nitric.api import Documents


async def handler(trigger: Trigger) -> Response:
    response = trigger.default_response()

    try:
        examples_query = Documents().collection("example").query()
        response.data = [doc.content async for doc in examples_query.stream()]
    except NitricServiceException:
        response.context.as_http().status = 500
        response.data = "An unexpected error occurred"

    return response


if __name__ == "__main__":
    start(handler)
