Midtrans Java Client Sample App
=====================================
​
Midtrans ❤️ Java! 
​
This is the Official [Midtrans](https://midtrans.com) java-client sample store Spring Boot web application. For more information about the product and documentation please refer to [http://docs.midtrans.com](https://docs.midtrans.com) and also you can try visit [demo app](https://midtrans-java.herokuapp.com/).
​
## 1. How to build and run
​
You can get the sample store source code from Midtrans Java Client repository on [Midtrans Github official](https://github.com/Midtrans/midtrans-java/tree/master/example), by cloning or downloading the repo.
This sample store web application is using [Spring Boot Framework](https://spring.io/projects/spring-boot).
​
This web app use Maven build tools to compile, build, and running the application.
Before building and running sample store application, you need to setup several config first such as merchant server-key and client-key which can be obtained from [Midtrans Admin Portal](https://account.midtrans.com/login).
​
### 1.1 Setup config on Controller file:
​
* [CoreApiController.java](https://github.com/Midtrans/midtrans-java/blob/master/example/src/main/java/com/midtrans/sample/controller/CoreApiController.java#L24)
​
Config for `MidtransCoreApi` object, the 3rd param (`false`) is environment type. Available value are: `true` for Production and `false` for Sandbox mode.
​
`~/midtrans-java/example/src/main/java/com/midtrans/sample/controller/CoreApiController.java`
```java
MidtransCoreApi coreApi = new ConfigFactory(new Config("YOU_SERVER_KEY","YOUR_CLIENT_KEY", false)).getCoreApi();
```
* [SnapController.java](https://github.com/Midtrans/midtrans-java/blob/master/example/src/main/java/com/midtrans/sample/controller/SnapController.java#L23)
​
Config for `MidtransSnapApi` object, the 3rd param (`false`) is environment type. Available value are: `true` for Production and `false` for Sandbox mode.
​
`~/midtrans-java/example/src/main/java/com/midtrans/sample/controller/SnapController.java`
```java
MidtransSnapApi snapApi = new ConfigFactory(new Config("YOU_SERVER_KEY","YOUR_CLIENT_KEY", false)).getSnapApi();
```
* [HomeController.java](https://github.com/Midtrans/midtrans-java/blob/master/example/src/main/java/com/midtrans/sample/controller/HomeController.java#L17)
​
The clientKey will be used for `midtrans-new-3ds.min.js`
​
`~/midtrans-java/example/src/main/java/com/midtrans/sample/controller/HomeController.java`
```java
String clientKey = "YOUR-CLIENT-KEY";
```
​
### 1.2 Run and build apps:
To run the app, you can simply run this command inside sample store project directory ```~/midtrans-java/example``` :
​
```bash
$ mvn spring-boot:run
```
you need Maven installed on your machine to run app with ```mvn``` Maven command.
​
After  building and running the apps via maven, you can visit http://localhost:7777/ on your browser and try out the sample store application. 
On the sample store, you can try to do transactions. Then you can check your transaction status on Midtrans Admin Portal.
​
## 2. Midtrans java client usage explanation
Midtrans java-client library usage example can be seen on controller classes. ```MidtransCoreApi``` / ```MidtransSnapApi```  object is constructed with ConfigFactory object, and few configs as input.
After that, API method like ```chargeTransaction``` from ```MidtransCoreApi``` / ```MidtransSnapApi``` object is used to create the transaction. 
Before charging a transaction, request params is constructed, that will be used as BodyRequest MapObject charge parameter. 
For example, sample store application use ```DataMockup``` object for request parameter.
​
> Important note: if you use ```MidtransCoreAPI``` for credit card transaction, you need to convert customer card credentials into token first before you can charge the transaction. If you use ```MidtransSnapAPI``` you just need to create snap token to create transaction. This part will be explained more detail on javascript part below.
​
### 2.a Midtrans javascript (credit card) | CoreAPI
Credit card transaction need special handling compared to other payment type. For security reason, merchant backend should not transmit any customer card credentials, instead the card credentials need to be exchanged with midtrans card token.
​
Midtrans card token can be obtained using javascript library that needs to be included on payment page. You can see the overview about this javascript library on Midtrans credit card [documentation](https://api-docs.midtrans.com/#get-token). For example on sample store application, midtrans javascript library is used on merchant payment page see [credit-card.html](https://github.com/Midtrans/midtrans-java/blob/master/example/src/main/resources/templates/coreapi/credit-card.html#L153)
​
[midtrans-new-3ds.min.js](https://api.midtrans.com/v2/assets/js/midtrans-new-3ds.min.js) library is used to trigger ```get_token``` transaction request to Midtrans api. This request will exchange customer card credentials into midtrans card token that can be use to charge a credit card transaction.
​
​
### 2.b Snap javascript (Show Payment Page) | SnapAPI
Snap frontend integration goal is to show Snap payment page within the site. You can see the overview about [snap.js](https://app.sandbox.midtrans.com/snap/snap.js) library on [check-out.html](https://github.com/Midtrans/midtrans-java/blob/master/example/src/main/resources/templates/snap/check-out.html#L83) file. Payment page is initiated by calling `snap.pay` with `SNAP_TOKEN` acquired from ```MidtransSnapApi``` method ``createToken`` which will return tokenSnap. if you need more detail about snap.js you can refer to Snap.jS [documentation](https://snap-docs.midtrans.com/#frontend-integration) for detail.
​
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
​
* [Midtrans Docs](https://docs.midtrans.com)
* [Midtrans Dashboard ](https://dashboard.midtrans.com/)
* [SNAP documentation](http://snap-docs.midtrans.com)
* [Core API documentation](http://api-docs.midtrans.com)
* Can't find answer you looking for? email to [support@midtrans.com](mailto:support@midtrans.com)
