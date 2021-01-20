To run the Load Balancer make the following steps:

- Build: ./gradlew clean shadowJar

- Run: java -jar ./build/libs/loadbalancer-1.0-all.jar


By default, the app should be available on the http://127.0.0.1:5555/
You can change the IP address or port in the config file and rebuild.