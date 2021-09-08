from nitric.faas import start, Trigger
from nitric.api import Documents

from common.example import generate_id


async def handler(trigger: Trigger) -> str:
    example = trigger.get_object()

    doc_id = generate_id()

    await Documents().collection("examples").doc(doc_id).set(example)

    return f'created example with ID: {doc_id}'


if __name__ == "__main__":
    start(handler)
