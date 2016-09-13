import json
import boto3
import uuid
import time

dynamodb = boto3.client('dynamodb')

def lambda_handler(push_notification_json, context):
    """Store a push notification in a database.

    :param push_notification_json: dict representing the JSON of a push notification
    :param context: not used
    :return: '', indicating success
    """

    # Retrieve the push notification version.
    push_notification_version = push_notification_json['pushNotificationVersion']

    # Retrieve the subscription's receiptId.
    receipt_id = push_notification_json['subscription']['receiptId']

    # Do something with the push notification we received.
    # In this case, we store the notification as an item in a database.
    dynamodb.put_item(
        TableName=DYNAMO_DB_TABLE_NAME,
        Item={
            'uuid':                    { 'S': str(uuid.uuid4()) },
            'timestamp':               { 'N': str(int(time.time())) },
            'receiptId':               { 'S': receipt_id },
            'pushNotificationVersion': { 'N': str(push_notification_version) },
            'pushNotification':        { 'S': json.dumps(push_notification_json)}
        }
    )

    return ''
