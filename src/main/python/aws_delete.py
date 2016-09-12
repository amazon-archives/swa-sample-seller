import aws_setup_utils

session = aws_setup_utils.create_session()
name = aws_setup_utils.read_config()['awsResourcesName']

ec2 = session.client('ec2')
iam = session.client('iam')
llambda = session.client('lambda')
apigateway = session.client('apigateway')
dynamodb = session.client('dynamodb')

def run_operation(message, aws_operation):
    """Runs aws_operation. Swallows and logs an exceptions.

    :param message: message to print before running the operation
    :param aws_operation: operation to run
    """

    print message,
    try:
        aws_operation()
    except Exception as err:
        print 'AWS error'
        print str(err)
    else:
        print 'done'

def delete_dynamo_db():
    dynamodb.delete_table(TableName=name)
def delete_apigateway():
    rest_apis = apigateway.get_rest_apis()
    rest_api_id = None
    for rest_api in rest_apis['items']:
        if rest_api['name'] == name:
            rest_api_id = rest_api['id']
    apigateway.delete_rest_api(restApiId=rest_api_id)
def delete_lambda():
    llambda.delete_function(FunctionName=name)
def detach_role():
    iam.detach_role_policy(RoleName=name, PolicyArn='arn:aws:iam::aws:policy/AmazonDynamoDBFullAccess')
def delete_role():
    iam.delete_role(RoleName=name)

run_operation("Deleting DynamoDB table...", delete_dynamo_db)
run_operation("Deleting API Gateway...", delete_apigateway)
run_operation("Deleting Lambda function...", delete_lambda)
run_operation("Detaching policy from role...", detach_role)
run_operation("Deleting role...", delete_role)
