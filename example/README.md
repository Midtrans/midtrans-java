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

Config for MidtransCoreApi object, value false is environment type. True is production and false is SandBox

`~/midtrans-java/example/src/main/java/com/midtrans/sample/controller/CoreApiController.java`
```java
MidtransCoreApi coreApi = new ConfigFactory(new Config("YOU_SERVER_KEY","YOUR_CLIENT_KEY", false)).getCoreApi();
```
* [SnapController.java](https://github.com/Midtrans/midtrans-java/blob/master/example/src/main/java/com/midtrans/sample/controller/SnapController.java#L23)

Config for MidtransSnapApi object, value false is environment type. True is production and false is SandBox

`~/midtrans-java/example/src/main/java/com/midtrans/sample/controller/SnapController.java`
```java
MidtransSnapApi snapApi = new ConfigFactory(new Config("YOU_SERVER_KEY","YOUR_CLIENT_KEY", false)).getSnapApi();
```
* [HomeController.java](https://github.com/Midtrans/midtrans-java/blob/master/example/src/main/java/com/midtrans/sample/controller/HomeController.java#L17)

This clientKey value for `midtrans-new-3ds.min.js`

`~/midtrans-java/example/src/main/java/com/midtrans/sample/controller/HomeController.java`
```java
String clientKey = "YOUR-CLIENT-KEY";
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

> an important if you use ```MidtransCoreAPI``` for credit card transaction, you need to convert customer card credentials into token first before you can charge the transaction. If you use ```MidtransSnapAPI``` you must create snap token transaction before you can charge the transaction. This part will be explained more detail on javascript part below.

### 2.a Midtrans javascript (credit card) | CoreAPI
Credit card transaction have special handling compare than another payment type. For security reason, merchant will not recieve any customer card credentials and will be replaced with midtrans card token.

Midtrans card token can be obtained using javascript library that you need to setup on merchant web page. You can see detail overview about this javascript library on Midtrans credit card [documentation](https://api-docs.midtrans.com/#get-token). For example on sample store application, midtrans javascript file need to setup on merchant web page see [credit-card.html](https://github.com/Midtrans/midtrans-java/blob/master/example/src/main/resources/templates/coreapi/credit-card.html#L153)

[midtrans-new-3ds.min.js](https://api.midtrans.com/v2/assets/js/midtrans-new-3ds.min.js) file was used to trigger ```get_token``` transaction request to midtrans payment api. This request will translate customer card credentials into midtrans card token that can be use to charge a credit card transaction.


### 2.b Midtrans javascript (Snap Token) | SnapAPI
Snap frontend integration goal is to show Snap payment page within your site. You can see detail overview about [snap.js](https://app.sandbox.midtrans.com/snap/snap.js) library on [check-out.html](https://github.com/Midtrans/midtrans-java/blob/master/example/src/main/resources/templates/snap/check-out.html#L83) file. You can start payment process by calling snap.pay with SNAP_TOKEN acquired from ```MidtransSnapApi``` object with method ``createToken`` return tokenSnap as parameter. if you need more detail about snap.js you can see Snap.jS [documentation](https://snap-docs.midtrans.com/#frontend-integration) for detail.

example:
```javascript
snap.pay(your_snap_token, {
          // Optional
          onSuccess: function(result){
            /* You may add your own js here, this is just example */
          },
          // Optional
          onPending: function(result){
            /* You may add your own js here, this is just example */
          },
          // Optional
          onError: function(result){
            /* You may add your own js here, this is just example */
          }
```
## Get help

* [Midtrans Docs](https://docs.midtrans.com)
* [Midtrans Dashboard ](https://dashboard.midtrans.com/)
* [SNAP documentation](http://snap-docs.midtrans.com)
* [Core API documentation](http://api-docs.midtrans.com)
* Can't find answer you looking for? email to [support@midtrans.com](mailto:support@midtrans.com)
