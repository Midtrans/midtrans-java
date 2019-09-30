Midtrans Java Client Sample App
=====================================

Midtrans ❤️ Java! 

This is the Official Midtrans java-client sample store application. Visit [https://midtrans.com](https://midtrans.com). More information about the product and see documentation at [http://docs.midtrans.com](https://docs.midtrans.com) for more technical details.

## 1. How to build and run

You can get sample store source code from Midtrans Java Client repository on [Midtrans Github official](https://github.com/Midtrans/midtrans-java/tree/master/example).
This sample store application use [Spring Boot Framework](https://spring.io/projects/spring-boot)

Midtrans sample store use maven build tools for compile, build, and running the application.
Before build and run sample store application, you need to setup several config first such as merchant server-key and client-key which can be obtained from [Midtrans Admin Portal](https://account.midtrans.com/login).

### 1.a Setup config on Controller file:

* [CoreApiController.java](https://github.com/Midtrans/midtrans-java/blob/master/example/src/main/java/com/midtrans/sample/controller/CoreApiController.java#L24)
```java
~/midtrans-java/example/src/main/java/com/midtrans/sample/controller/CoreApiController.java
```
* [SnapController.java](https://github.com/Midtrans/midtrans-java/blob/master/example/src/main/java/com/midtrans/sample/controller/SnapController.java#L23)
```java
~/midtrans-java/example/src/main/java/com/midtrans/sample/controller/SnapController.java
```
* [HomeController.java](https://github.com/Midtrans/midtrans-java/blob/master/example/src/main/java/com/midtrans/sample/controller/HomeController.java#L17)
```java
~/midtrans-java/example/src/main/java/com/midtrans/sample/controller/HomeController.java
```

### 1.b Run and build apps:
To run the apps, you can just simply run this command on sample store project directory ```~/midtrans-java/example``` :

```bash
$ mvn spring-boot:run
```
you need maven installed on your machine to run app with ```mvn``` maven command.

After maven task build and run the apps, you can go to http://localhost:7777/ on your browser and try out the sample store application. 
On the sample store, you can try to make a transaction. After that you can check your transaction status on Midtrans Admin Portal.

## 2. Midtrans java client usage
Midtrans java-client library usage example can be shown on controller class. You need to construct ```MidtransCoreApi``` / ```MidtransSnapApi``` object with ConfigFactory object using several merchant config.
After that, you can use method api like ```chargeTransaction``` from ```MidtransCoreApi``` / ```MidtransSnapApi``` object which will be use to trigger the transaction. 
Before charge a transaction, you need to construct request params that will be used as BodyRequest MapObject charge parameter. 
For example, sample store application use ```DataMockup``` object for request parameter.

## Get help

* [Midtrans Docs](https://docs.midtrans.com)
* [Midtrans Dashboard ](https://dashboard.midtrans.com/)
* [SNAP documentation](http://snap-docs.midtrans.com)
* [Core API documentation](http://api-docs.midtrans.com)
* Can't find answer you looking for? email to [support@midtrans.com](mailto:support@midtrans.com)
