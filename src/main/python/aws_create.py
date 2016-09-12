import time
import uuid
import zipfile
import botocore
import sys
import aws_setup_utils

def main():
    session = aws_setup_utils.create_session()
    config = aws_setup_utils.read_config()

    ec2 = session.client('ec2')
    iam = session.client('iam')
    llambda = session.client('lambda')
    apigateway = session.client('apigateway')
    dynamodb = session.client('dynamodb')

    role_propagation_wait = 20
    name = config['awsResourcesName']
    region_name = config['awsRegionName']

    account_id = get_account_id(ec2)
    create_iam_role(iam, name)
    attach_policy(iam, name)
    print 'Waiting {} seconds for newly created role to propagate...'.format(role_propagation_wait),
    time.sleep(role_propagation_wait)
    print 'done'
    create_dynamodb_table(dynamodb, name)

    try:
        create_lambda_function(llambda, name, name, name, account_id)
    except botocore.exceptions.ClientError as err:
        print 'ERROR: Failed to create Lambda function.'
        print 'This may be because the role has not fully propagated.'
        print 'Run python src/main/python/aws_delete.py to delete resources.'
        print 'Increase the role_propagation_wait variable in this file.'
        print 'Run python src/main/python/aws_create.py to try again.'
        print 'Exception: ', str(err)
        sys.exit(1)

    rest_api_id = create_rest_api_gateway(apigateway, name, name, region_name, account_id)
    add_permission_to_rest_api_to_invoke_lambda(llambda, account_id, rest_api_id, name, region_name)
    deploy_api(apigateway, name, rest_api_id)

    print "Your push notification endpoint is https://{}.execute-api.{}.amazonaws.com/{}".format(rest_api_id, config['awsRegionName'], name)

def get_account_id(ec2):
    print 'Retrieving AWS Account ID...',
    account_id = ec2.describe_security_groups(GroupNames=['Default'])['SecurityGroups'][0]['OwnerId']
    print 'done'
    return account_id

def create_iam_role(iam, role_name):
    print 'Creating IAM role...',
    iam.create_role(RoleName=role_name, AssumeRolePolicyDocument="""{
        "Version": "2012-10-17",
        "Statement": [
            {
                "Effect": "Allow",
                "Principal": {
                    "Service": "lambda.amazonaws.com"
                },
                "Action": "sts:AssumeRole"
            }
        ]
    }""")
    print 'done'

def attach_policy(iam, role_name):
    print 'Attaching policy to IAM role...',
    iam.attach_role_policy(RoleName=role_name, PolicyArn='arn:aws:iam::aws:policy/AmazonDynamoDBFullAccess')
    print 'done'

def create_dynamodb_table(dynamodb, table_name):
    print 'Creating DynamoDB Table...',
    dynamodb.create_table(
        AttributeDefinitions=[
            {
                'AttributeName': 'uuid',
                'AttributeType': 'S'
            },
            {
                'AttributeName': 'timestamp',
                'AttributeType' : 'N'
            }
        ],
        TableName=table_name,
        KeySchema=[
            {
                'AttributeName': 'uuid',
                'KeyType' : 'HASH'
            },
            {
                'AttributeName': 'timestamp',
                'KeyType' : 'RANGE'
            }
        ],
        ProvisionedThroughput={
            "WriteCapacityUnits": 5,
            "ReadCapacityUnits": 5
        }
    )
    print 'done'

def create_lambda_function(llambda, lambda_function_name, role_name, dynamodb_table_name, account_id):
    print 'Creating Lambda function...',
    lambda_function = """
import json
import boto3
import uuid
import time

dynamodb = boto3.client('dynamodb')

print('Loading function')

def lambda_handler(event, context):
    print 'request body: ', event
    push_notification_version = str(event['pushNotificationVersion'])
    receipt_id = event['subscription']['receiptId']
    dynamodb.put_item(
        TableName='"""+dynamodb_table_name+"""',
        Item={
            'uuid':                    { 'S': str(uuid.uuid4()) },
            'timestamp':               { 'N': str(int(time.time())) },
            'receiptId':               { 'S': receipt_id },
            'pushNotificationVersion': { 'N': push_notification_version },
            'pushNotification':        { 'S': json.dumps(event) }
        }
    )
    return ''
    """

    file = open('lambda_function.py', 'w+')
    file.write(lambda_function)
    file.close()
    zip_file = zipfile.ZipFile('lambda_function.zip', mode='w')
    zip_file.write('lambda_function.py')
    zip_file.close()

    llambda.create_function(
        FunctionName=lambda_function_name,
        Runtime='python2.7',
        Role='arn:aws:iam::{}:role/{}'.format(account_id, role_name),
        Handler='lambda_function.lambda_handler',
        Code={'ZipFile': open('lambda_function.zip', 'rb').read()}
    )
    print 'done'

def create_rest_api_gateway(apigateway, rest_api_name, lambda_function_name, region_name, account_id):
    print 'Creating REST API Gateway...',
    rest_api_id = apigateway.create_rest_api(name=rest_api_name)['id']
    resource_id = apigateway.get_resources(restApiId=rest_api_id)['items'][0]['id']

    apigateway.put_method(
        restApiId=rest_api_id,
        resourceId=resource_id,
        httpMethod='POST',
        authorizationType='NONE',
        requestParameters={}
    )

    apigateway.put_method_response(
        restApiId=rest_api_id,
        resourceId=resource_id,
        httpMethod='POST',
        statusCode='200',
        responseModels={"application/json": "Empty"}
    )

    apigateway.put_integration(
        restApiId=rest_api_id,
        resourceId=resource_id,
        httpMethod='POST',
        type='AWS',

        # See "Uri" in http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-apitgateway-method-integration.html
        # for an explanation of this URI format.
        uri='arn:aws:apigateway:{}:lambda:path/2015-03-31/functions/arn:aws:lambda:{}:{}:function:{}/invocations'.format(region_name, region_name, account_id, lambda_function_name),

        integrationHttpMethod='POST'
    )

    apigateway.put_integration_response(
        restApiId=rest_api_id,
        resourceId=resource_id,
        httpMethod='POST',
        statusCode='200',
        responseTemplates={"application/json": "null"}
    )

    print 'done'
    return rest_api_id

def add_permission_to_rest_api_to_invoke_lambda(llambda, account_id, rest_api_id, lambda_function_name, region_name):
    print 'Adding permission to allow API Gateway to invoke Lambda...',

    rest_api_arn = 'arn:aws:execute-api:{}:{}:{}/*/POST/'.format(region_name, account_id, rest_api_id)

    llambda.add_permission(
        FunctionName=lambda_function_name,
        StatementId=str(uuid.uuid4()), # see https://en.wikipedia.org/wiki/Universally_unique_identifier
        Action='lambda:*',
        Principal='apigateway.amazonaws.com',
        SourceArn=rest_api_arn
    )

    print 'done'

def deploy_api(apigateway, stage_name, rest_api_id):
    print 'Deploying REST API Gateway...',
    apigateway.create_deployment(
        restApiId=rest_api_id,
        stageName=stage_name
    )
    print 'done'

if __name__ == '__main__':
    main()
