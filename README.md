# SWA Sample Seller

Build the JAR file:

```
cd SWASampleSeller
mvn package
```

Edit configuration:

```
vi src/main/resources/config.json
```

Run the web server:

```
java -jar target/SWASampleSeller-1.0-SNAPSHOT.jar
```

# Warnings

This application is meant for testing/demonstration purposes only.

* Don't deploy this to production.
* Don't use `src/main/resources/key.jks`. Purchase your own SSL certificate.
