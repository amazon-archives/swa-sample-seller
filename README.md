# SWA Sample Seller

Build the JAR file:

```
mvn package
```

Run the web server:

```
mvn package
java -jar target/SWASampleSeller-1.0-SNAPSHOT.jar key.jks 8443
```

# Warnings

This application is meant for testing/demonstration purposes only.

* Don't deploy this to production.
* Don't use key.jks. Purchase your own SSL certificate.
