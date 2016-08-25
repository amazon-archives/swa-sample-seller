# WARNING

* Deploy this application only to your local machine.
* Anyone who visits this web application may have access to confidential data about your seller account and customer accounts.
* This application is meant for testing/demonstration purposes only.

# Subscribe with Amazon (SWA) Sample Seller

Install JDK 8 from Oracle or OpenJDK:

* Oracle: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
* OpenJDK: http://openjdk.java.net/

Install the Maven build system from https://maven.apache.org/.

Build the JAR file:

```bash
cd path/to/repo  # Switch to the repo root directory.
mvn package      # Build the program with Maven.
```

Edit the configuration at `src/main/resources/config.json`.

Sign into the App Console at http://login.amazon.com/manageApps.

* Select the LWA application associated with your SWA subscription.
* Expand the "Web Settings" tab.
* Add `https://127.0.0.1:YOUR_PORT_NUMBER` to "Allowed JavaScript Origins" and "Allowed Return URLs".

Run the JAR file to start the web application:

```bash
java -jar target/swa_sample_seller-1.0-SNAPSHOT.jar   # Run the JAR file.
```

Visit the application at `https://127.0.0.1:YOUR_PORT_NUMBER`.
