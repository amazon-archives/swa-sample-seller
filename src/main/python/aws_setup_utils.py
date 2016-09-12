import json
import boto3

# Path to the config file.
CONFIG_PATH = 'src/main/resources/config.json'

def create_session():
    """Create a boto3 session by reading the config file at CONFIG_PATH.

    A boto3.Session(...) creates a session that represents an AWS user that can create/read/update/delete AWS resources.

    Ex:

        boto.Session(
            aws_access_key_id=YOUR_KEY_ID,
            aws_secret_access_key=YOUR_SECRET_ACCESS_KEY,
            region_name='us-west-2'
        )

    will create a session that can create resources in us-west-2
    based on the permissions associated the user with YOUR_KEY_ID
    and YOUR_SECRET_ACCESS_KEY.

    """
    with open(CONFIG_PATH) as config_file:
        config_json = json.load(config_file)
        return boto3.Session(
            aws_access_key_id=config_json['awsAccessKeyId'],
            aws_secret_access_key= config_json['awsSecretAccessKey'],
            region_name=config_json['awsRegionName']
        )

def read_config():
    """Return the JSON config at CONFIG_PATH."""
    with open(CONFIG_PATH) as config_file:
        return json.load(config_file)
