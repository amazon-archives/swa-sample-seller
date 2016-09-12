# _WARNING_

* Deploy this application only to your local machine.
* Anyone who visits this web application will have access to confidential data about your seller account and customer accounts.

# Subscribe with Amazon (SWA) Sample Seller

## Refer to the documentation

This README will refer to the SWA Getting Started Guide at https://s3-us-west-2.amazonaws.com/swa-public-documents/integration-guides/SWA_SellerIntegrationGuide.pdf

For example, **_[4.1.1]_** refers to section 4.1.1 in the guide.

## Install dependencies

1. Install Java Development Kit (JDK) 8.
2. Verify installation by running `javac` from your terminal.
3. Install Apache Maven.
4. Verify installation by running `mvn` from your terminal.

## Configure Sample Seller

1. Edit the configuration at `src/main/resources/config.json`:
   * Retrieve your client ID and client secret from the Technical Profile of your product.
     * Set `YOUR_CLIENT_ID` to your client ID.
     * Set `YOUR_CLIENT_SECRET` to your client secret.
   * Set `YOUR_PORT` to an open port you want to serve Sample Seller on.
     * Ex: `8444`.
   * Set `YOUR_REDIRECT_URI` to `https://127.0.0.1/login:YOUR_PORT`
     * In Sample Seller, we want LWA to redirect the user to the `/login` path so Sample Seller can query and display the user's subscriptions information.
   * Set `YOUR_LOGOUT_REDIRECT_URL` to `https://127.0.0.1:YOUR_PORT`.
     * In Sample Seller, we want to redirect the user to the landing page.
2. Configure your LWA application. **_[2.4]_**
   * Sign into the App Console at http://login.amazon.com/manageApps.
   * Select the LWA application associated with your SWA subscription.
   * Expand the "Web Settings" tab.
   * Add `https://127.0.0.1:YOUR_PORT_NUMBER` to "Allowed JavaScript Origins"
   * Add `YOUR_REDIRECT_URI` to "Allowed Return URLs".

## Build Sample Seller

```bash
cd path/to/repo  # Switch to the repo root directory.
mvn package      # Build the program with Maven.
```

## Run Sample Seller

1. Run the JAR file to start the web application:

```bash
cd path/to/repo                                       # Switch to the repo root directory.
java -jar target/swa_sample_seller-1.0-SNAPSHOT.jar   # Run the JAR file.
```

2. Visit the application at `https://127.0.0.1:YOUR_PORT_NUMBER`.

## Take a tour of Sample Seller

1. Visit the index page at [https://127.0.0.1:YOUR_PORT](https://127.0.0.1:YOUR_PORT).
2. View code for the LWA button with [view-source:https://127.0.0.1:YOUR_PORT](view-source:https://127.0.0.1:YOUR_PORT) **_[3.1]_**
3. Click the LWA button and sign in with any Amazon account.
4. Check out the dump of API requests/responses on the current page.
5. View code for making the API requests:
    * In `src/main/java/com/github/amznlabs/swa_sample_seller/controllers/helpers/AmazonApiHelpers.java`:
      * Customer profile **_[4.1.1, Step 3]_**: See `// REQUEST CUSTOMER PROFILE`
      * Security API **_[4.2.1]_**: See `// REQUEST SELLER ACCESS TOKEN`
      * Subscriptions API **_[4.1.1, Step 3]_**, **_[4.2.5]_**: See `// REQUEST SUBSCRIPTIONS`
6. View code for handling SWA API response codes **_[4.2.3]_**:
   * In `src/main/java/com/github/amznlabs/swa_sample_seller/controllers/RequestController.java`
     * See `// HANDLE SWA API RESPONSE CODE`
7. View code for parsing an List Subscriptions API request **_[4.2.5]_**:
    * In `src/main/java/com/github/amznlabs/swa_sample_seller/controllers/RequestController.java`:
      * See `parseSubscriptionsResponse()`

## Capture real push notifications

Sample Push Notification Endpoint is a collection of services running on Amazon Web Services (AWS).

### Setup Python and AWS

* Install Python 2.7 from https://www.python.org/downloads/
* Create an AWS account at https://aws.amazon.com/
* Create an IAM user named `swa_sample_seller` with the `AdministratorAccess` permission.
  * See http://docs.aws.amazon.com/lambda/latest/dg/setting-up.html
  * **Make note of your access key ID and access key secret.**
* Run `pip install boto3` to install the Python `boto3` library.

### Edit `src/main/resources/config.json`

1. Set`awsAccessKeyId` to your user's access key ID.
2. Set `awsSecretAccessKey` to your user's secret access key.
3. [Optional] Change `awsResourcesName` to `YOUR_AWS_RESOURCES_NAME`.
    * All AWS resources will be created with this name.
    * Use a name that will be easy to find in your AWS console.
4. [Optional] Change `awsRegionName`.
    * See http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/using-regions-availability-zones.html

### Create Sample Push Notification Endpoint to catch push notifications

1. Run `python src/main/python/aws_create.py`.
2. Make note of the endpoint printed out by the script.
    * Ex: `Your push notification endpoint is YOUR_ENDPOINT`.

![Console output of running aws_create.py](aws_create.png)

### Send a test notification

1. Visit https://sellercentral.amazon.com/swa/dashboard.
2. Edit the **Technical Profile** of your product.
3. Continue to the **Push Notifications** page.
4. Set the push notications endpoint field to `YOUR_ENDPOINT`.
5. Click **Send Test Notification**.

### View the test notification

View the `YOUR_AWS_RESOURCES_NAME` table in your DynamoDB dashboard.

### View a real push notification

1. Purchase / Renew / Cancel your subscription.
2. View the `YOUR_AWS_RESOURCES_NAME` table in your DynamoDB dashboard.

### Delete the Sample Push Notification Endpoint

Run `python src/main/python/aws_delete.py`.

![Console output of running aws_delete.py](aws_delete.png)

# FAQ

* Is the AWS usage free?
  * If you are eligible for AWS Free Tier, then most likely yes. All AWS usage should be within free tier limits.
  * If you are not eligible for AWS Free Tier, then the expense will probably be minimal (less than $10).
* Why can't Sample Seller receive real push notifications?
  * Amazon only sends push notifications to a **publicly accessible IP** that serves a **trusted SSL certificate**.
  * Sample Seller should not be deployed to a publicly accessible IP.
  * Sample Seller uses a self-signed SSL certificate.
* Why do I need to use AWS to receive push notifications?
  * You do not need to use AWS.
  * You can use any service that serves a publicly accessible IP with a trusted SSL certificate.
* What SSL certificates are trusted by Amazon's push notification sender?
  * See https://docs.aws.amazon.com/sns/latest/dg/SendMessageToHttp.https.ca.html.
