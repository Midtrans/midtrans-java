# Midtrans Client - Java
[![Download](https://maven-badges.herokuapp.com/maven-central/com.midtrans/java-library/badge.svg)](https://search.maven.org/artifact/com.midtrans/java-library/)
[![Build Status](https://travis-ci.org/Xaxxis/midtrans-java.svg?branch=master)](https://travis-ci.org/Xaxxis/midtrans-java)
[![Demo apps](https://img.shields.io/badge/Go%20to-Demo%20Apps-green)](https://sample-demo-dot-midtrans-support-tools.et.r.appspot.com/)

Midtrans :heart: Java, This is the Official Java API client/library for Midtrans Payment API. Visit [https://midtrans.com](https://midtrans.com). More information about the product and see documentation at [http://docs.midtrans.com](https://beta-docs.midtrans.com/) for more technical details. This library used java version 1.8

## 1. Installation

### 1.a Using Maven or Gradle
If you're using Maven as the build tools for your project, please add the following dependency to your project's build definition (pom.xml):
Maven:
```xml
<dependency>
    <groupId>com.midtrans</groupId>
    <artifactId>java-library</artifactId>
    <version>3.2.0</version>
</dependency>
```
Gradle:
If you're using Gradle as the build tools for your project, please add jcenter repository to your build script then add the following dependency to your project's build definition (build.gradle):
```Gradle
dependencies {
	implementation 'com.midtrans:java-library:3.2.0'
}
```
> **IMPORTANT NOTE**: Since April 13, 2021 We already migrate the repository from jcenter/bintray repository to [Maven central](https://search.maven.org/artifact/com.midtrans/java-library).

### 1.b Using JAR File

If you are not using project build management like Maven, Gradle or Ant you can use manual jar library download JAR Library on [here](https://search.maven.org/remotecontent?filepath=com/midtrans/java-library/3.2.0/java-library-3.2.0.jar)

## 2. Usage

### 2.1 Choose Product/Method

We have [3 different products](https://beta-docs.midtrans.com/) that you can use:
- [Snap](#22A-snap) - Customizable payment popup will appear on **your web/app** (no redirection). [doc ref](https://snap-docs.midtrans.com/)
- [Snap Redirect](#22B-snap-redirect) - Customer need to be redirected to payment url **hosted by midtrans**. [doc ref](https://snap-docs.midtrans.com/)
- [Core API (VT-Direct)](#22C-core-api-vt-direct) - Basic backend implementation, you can customize the frontend embedded on **your web/app** as you like (no redirection). [doc ref](https://api-docs.midtrans.com/)
- [Iris Disbursement](#22D-iris-api) - Iris is Midtrans’ cash management solution that allows you to disburse payments to any bank accounts in Indonesia securely and easily. [doc ref](https://iris-docs.midtrans.com/)

Choose one that you think best for your unique needs.

### 2.2 Client Initialization and Configuration

Get your client key and server key from [Midtrans Dashboard](https://dashboard.midtrans.com)

Create API client object, Select one from any of methods below that you most prefer. You can also check the [project's functional tests](library/src/test/java/com/midtrans/java) for more examples.

Set a config with globally, except iris api
```java
import com.midtrans.Midtrans;
import com.midtrans.httpclient.CoreApi;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import org.json.JSONObject;

Midtrans.serverKey = "YOUR_SERVER_KEY";
Midtrans.clientKey = "YOUR_CLIENT_KEY";
Midtrans.isProduction = false;

// CoreApi Request with global config
JSONObject result = CoreApi.chargeTransaction(param);

// SnapApi request with global config
JSONObject result = SnapApi.createTransaction(param);
```
#### Per-request Configuration
It is also possible if on each of individual requests you want to set a unique/different configuration; like idempotency key, 
proxy, override/append notification url, or multiple account API keys via Config options and set as param on static method e.g: `CoreApi.chargeTransaction(param, configOptions)` method available on `CoreApi.class`, `SnapApi.class`, `TransactionApi.class` or `IrisApi.class`. Please follow the steps given below.

>Note: This method is expected to be thread-safe, you should prefer this if you are implementing multi-threading/concurrency in your code.
```java
import com.midtrans.Config;
import com.midtrans.Midtrans;
import com.midtrans.httpclient.CoreApi;
import com.midtrans.httpclient.error.MidtransError;
import org.json.JSONObject;

Config coreApiConfigOptions = Config.builder()
        .setServerKey("YOUR_SERVER_KEY")
        .setClientKey("YOUR_CLIENT_KEY")
        .setIrisIdempotencyKey("UNIQUE_ID")
        .setPaymentIdempotencyKey("UNIQUE_ID")
        .setProxyConfig(PROXY_CONFIG)
        .setPaymentOverrideNotification("WEBHOOK_ENDPOINT")
        .setIsProduction(false)
        .build();

// CoreApi request with config options
JSONObject result = CoreApi.chargeTransaction(param, coreApiConfigOptions);

Config snapConfigOptions = Config.builder()
        .setServerKey("YOUR_SERVER_KEY")
        .setClientKey("YOUR_CLIENT_KEY")
        .setIrisIdempotencyKey("UNIQUE_ID")
        .setPaymentIdempotencyKey("UNIQUE_ID")
        .setProxyConfig(PROXY_CONFIG)
        .setPaymentOverrideNotification("WEBHOOK_ENDPOINT")
        .setIsProduction(false)
        .build();

// SnapApi request with config options
JSONObject result = SnapApi.createTransaction(param, snapConfigOptions);
```

In case you are using a single account API key, but you need to set the config options value dynamically from the config object. Please follow the steps given below.
```java
import com.midtrans.Midtrans;
import com.midtrans.httpclient.CoreApi;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import org.json.JSONObject;

//1. Set credentials key globally
Midtrans.serverKey = "YOUR_SERVER_KEY";
Midtrans.clientKey = "YOUR_CLIENT_KEY";
Midtrans.isProduction = false;

//2. Set config options for core-api charge request
Config configOptions = Config.builder()
     .setPaymentIdempotencyKey("UNIQUE_ID")
     .setPaymentOverrideNotification("DYNAMIC_WEBHOOK_ENDPOINT")
     .build();

//3. CoreApi request with config options
JSONObject result = CoreApi.chargeTransaction(param, configOptions);

//4. Set config options for Snap charge request
Config snapConfigOptions = Config.builder()
     .setPaymentOverrideNotification("DYNAMIC_WEBHOOK_ENDPOINT")
     .build();

//5. SnapApi request with config options
JSONObject result = SnapApi.createTransaction(param, snapConfigOptions);
```

#### Alternative way to initialize
```java
import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.service.MidtransCoreApi;

Config coreApiConfigOptions = Config.builder()
        .setServerKey("YOUR_SERVER_KEY")
        .setClientKey("YOUR_CLIENT_KEY")
        .setIsProduction(false)
        .build();

MidtransCoreApi coreApi = new ConfigFactory(coreApiConfigOptions).getCoreApi();

//You can set Config.class with
`YOUR_SERVER_KEY`
`YOUR_CLIENT_KEY`
`isProduction`
```

```java
import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.service.MidtransSnapApi;

Config snapConfigOptions = Config.builder()
        .setServerKey("YOUR_SERVER_KEY")
        .setClientKey("YOUR_CLIENT_KEY")
        .setIsProduction(false)
        .build();

MidtransSnapApi snapApi = new ConfigFactory(snapConfigOptions).getSnapApi();

//You can set Config.class with
`YOUR_SERVER_KEY`
`YOUR_CLIENT_KEY`
`isProduction`
```

```java
import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.service.MidtransIrisApi;

Config irisConfigOptions = Config.builder()
        .setServerKey("IRIS-CREDENTIAL")
        .setIsProduction(false)
        .build();

MidtransIrisApi irisApi = new ConfigFactory(irisConfigOptions).getIrisApi();

//You can set Config.class with
`IRIS-CREDNTIALS`
`null`
`isProduction`
```

You can also re-set config using `apiConfig()` method on MidtransCoreApi.Class, MidtransSnapApi.Class or MidtransIrisApi.class like `coreApi.apiConfig().set( ... )`
> Please note that if you are using multi-thread concurrency, this method is not thread-safe. For that, you should use the method of creating & setting individual Config option instances per request. [Refer to this section](README.md#per-request-configuration)

example:

```java
// Create Snap API instance, empty config
coreApi.apiConfig().setProduction(false);
coreApi.apiConfig().setClientKey("YOUR_CLIENT_KEY");
coreApi.apiConfig().setServerKey("YOUR_SERVER_KEY");

// You don't have to re-set using all the options, 
// i.e. set serverKey only
coreApi.apiConfig().setServerKey("YOUR_SERVER_KEY");

// For Iris Disbursement can set creator & approver credentials with apiConfig()
irisApi.apiConfig().setServerKey("IRIS-CREDENTIALS");
```

In production environment, LOG is by default turned off, you can enable by `setEnabledLog`, e.g:

```java
coreApi.apiConfig().setEnabledLog(true);

//or using
        
config.setEnabledLog(true);
```

Using internal proxy, you can set ProxyConfig object with ProxyConfigBuilder to set hostname, port, username, and password. and also connectionTimeout, readTimeout, and writeTimeout. But if you not define network configuration like
ProxyConfig, connectionTimeout, readTimeout and writeTimeout, default value is 10 second and the connection no proxy configuration.

example:
```java
import com.midtrans.proxy.ProxyConfig;
import com.midtrans.service.MidtransCoreApi;

ProxyConfig proxyConfig = ProxyConfig.builder()
        .setHost(PROXY_HOTS)
        .setPort(PROXY_PORT)
        .setUsername(PROXY_USERNAME)
        .setPassword(PROXY_PASSWORD)
        .build();

//Initialize MidtransCoreApi config with proxy and network configuration
private MidtransCoreApi coreApi = new ConfigFactory(new Config("YOU_SERVER_KEY","YOUR_CLIENT_KEY", false, 10, 10, 10, proxyConfig)).getCoreApi();

// Alternative, initialize proxy in Global Config
Midtrans.setProxyConfig(proxyConfig);
```
add new 4 params to use http proxy and network config,
- connectionTimeout - default value 10s
- readTimeout - default values 10s
- writeTimeout - default value 10s
- proxyConfig
- maxConnectionPool - default value 16
- keepAliveDuration - default value 300s

and also you can set value for connectionTimeout or etc with configuration class, default value for TimeUnit is SECONDS, you can change the TimeUnit with config class.

example: 
```java
// set as globally
Midtrans.setConnectTimeout(10000);
Midtrans.setReadTimeout(10000);
Midtrans.setWriteTimeout(10000);
Midtrans.setMaxConnectionPool(16);
Midtrans.setKeepAliveDuration(300000);
Midtrans.setHttpClientTimeUnit(TimeUnit.MILLISECONDS);

// set connection timeout with Config class
config.setConnectionTimeout(10000, TimeUnit.MILLISECONDS);
config.setReadTimeout(10000, TimeUnit.MILLISECONDS);
config.setWriteTimeout(10000, TimeUnit.MILLISECONDS);
config.setKeepAliveDuration(300000, TimeUnit.MILLISECONDS);
config.setMaxConnectionPool(16);

// Set connection timeout from initiate api object
coreApi.apiConfig().setConnectionTimeout(10000, TimeUnit.MILLISECONDS);
coreApi.apiConfig().setReadTimeout(10000, TimeUnit.MILLISECONDS);
coreApi.apiConfig().setWriteTimeout(10000, TimeUnit.MILLISECONDS);
coreApi.apiConfig().setKeepAliveDuration(300000, TimeUnit.MILLISECONDS);
coreApi.apiConfig().setMaxConnectionPool(16);
```

#### Override Notification Url
Merchant can opt to change or add custom notification urls on every transaction. It can be achieved by adding additional HTTP headers into charge request.

There are two headers we provide:

1. `X-Append-Notification`: to add new notification url(s) alongside the settings on dashboard
2. `X-Override-Notification`: to use new notification url(s) disregarding the settings on dashboard
Both header can only receive up to maximum of 3 urls.

[More details](https://api-docs.midtrans.com/#override-notification-url)

```java
//X-Append-Notification
Config configOptions = Config.builder()
     .setPaymentAppendNotification("https://example.com/test1,https://example.com/test")
     .build();
        
CoreApi.chargeTransaction(param, configOptions);

//X-Override-Notification
Config configOptions = Config.builder()
     .setPaymentOverrideNotification("https://example.com/test1,https://example.com/test")
     .build();;

CoreApi.chargeTransaction(param, configOptions);
```
When both `X-Append-Notification` and `X-Override-Notification` are used together then only override will be used.

### Handling Error / Exception
When using function that result in Midtrans API call e.g: CoreApi.chargeTransaction(...) or SnapApi.createTransaction(...) there's a chance it may throw error (MidtransError object), the error object will contains below properties that can be used as information to your error handling logic:
```java
try {
    JSONObject result = CoreApi.chargeTransaction(param);
} catch (MidtransError e) {
    e.printStackTrace();
    e.getStatusCode(); // basic error message string
    e.getMessage(); // HTTP status code e.g: 400, 401, etc.
    e.getResponseBody(); // API response body
    e.getResponse(); // raw OkHttp response object
}
```
#### CoreAPI Simple Sample Usage
```java
import com.midtrans.httpclient.CoreApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.Midtrans;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.json.JSONObject;


public class MidtransExample {

    public static void main(String[] args) throws MidtransError {
        Midtrans.serverKey = "YOUR_SERVER_KEY";
        Midtrans.isProduction = false;

        UUID idRand = UUID.randomUUID();
        Map<String, Object> chargeParams = new HashMap<>();

        Map<String, String> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", idRand.toString());
        transactionDetails.put("gross_amount", "265000");

        chargeParams.put("transaction_details", transactionDetails);
        chargeParams.put("payment_type", "gopay");
        
            JSONObject result = CoreApi.chargeTransaction(chargeParams);
            System.out.println(result);
    }
}
```

### 2.2.A Snap
You can see Snap example [here](example/src/main/java/com/midtrans/sample/controller/SnapController.java).

Available methods for `MidtransSnapApi` class
```java
//1. To get snap transaction with return json raw object
JSONObject createTransaction(Map<String, Object> params);

//2. To get snap token with return String token
String createTransactionToken(Map<String, Object> params);

//3. To get snap redirect url with return String redirect url
String createTransactionRedirectUrl(Map<String, Object> params);
```
`params` is Map Object or String of JSON of [SNAP Parameter](https://snap-docs.midtrans.com/#json-objects)


#### Get Snap Token

```java
// Create new Object SnapAPI
MidtransSnapApi snapApi = new ConfigFactory(new Config("YOU_SERVER_KEY","YOUR_CLIENT_KEY", false)).getSnapApi();

// Create params JSON Raw Object request
public Map<String, Object> requestBody() {
    UUID idRand = UUID.randomUUID();
    Map<String, Object> params = new HashMap<>();
    
    Map<String, String> transactionDetails = new HashMap<>();
    transactionDetails.put("order_id", idRand);
    transactionDetails.put("gross_amount", "265000");
    
    Map<String, String> creditCard = new HashMap<>();
    creditCard.put("secure", "true");
    
    params.put("transaction_details", transactionDetails);
    params.put("credit_card", creditCard);
    
    return params;
}

// Create Token and then you can send token variable to FrontEnd,
// to initialize Snap JS when customer click pay button
String transactionToken = snapApi.createTransactionToken(requestBody())

//you can use Model object in springboot controller to send token to FrontEnd
model.addAttribute("transactionToken", transactionToken);
```


#### Initialize Snap JS when customer click pay button

On frontend / html:
Replace `PUT_TRANSACTION_TOKEN_HERE` with `transactionToken` acquired above, you can use java template engine like Thymeleaf to parse `transactionToken` to frontEnd like [this](https://github.com/Xaxxis/midtrans-java/blob/master/example/src/main/resources/templates/snap/check-out.html#L90) `[[${transactionToken}]]`
```html
<html>
  <body>
    <button id="pay-button">Pay!</button>
    <pre><div id="result-json">JSON result will appear here after payment:<br></div></pre> 

<!-- TODO: Remove ".sandbox" from script src URL for production environment. Also input your client key in "data-client-key" -->
    <script src="https://app.sandbox.midtrans.com/snap/snap.js" data-client-key="<Set your ClientKey here>"></script>
    <script type="text/javascript">
      document.getElementById('pay-button').onclick = function(){
        // SnapToken acquired from previous step
        snap.pay('PUT_TRANSACTION_TOKEN_HERE', {
          // Optional
          onSuccess: function(result){
            /* You may add your own js here, this is just example */ document.getElementById('result-json').innerHTML += JSON.stringify(result, null, 2);
          },
          // Optional
          onPending: function(result){
            /* You may add your own js here, this is just example */ document.getElementById('result-json').innerHTML += JSON.stringify(result, null, 2);
          },
          // Optional
          onError: function(result){
            /* You may add your own js here, this is just example */ document.getElementById('result-json').innerHTML += JSON.stringify(result, null, 2);
          }
        });
      };
    </script>
  </body>
</html>
```

#### Implement Notification Handler
[Refer to this section](#23-handle-http-notification)

### 2.2.B Snap Redirect

Also available as examples [here](example/src/main/java/com/midtrans/sample/controller/SnapController.java).

#### Get Redirection URL of a Payment Page

```java
// Create new Object SnapAPI
MidtransSnapApi snapApi = new ConfigFactory(new Config("YOU_SERVER_KEY","YOUR_CLIENT_KEY", false)).getSnapApi();

// Create params JSON Raw Object request
public Map<String, Object> requestBody() {
    UUID idRand = UUID.randomUUID();
    Map<String, Object> params = new HashMap<>();
    
    Map<String, String> transactionDetails = new HashMap<>();
    transactionDetails.put("order_id", idRand);
    transactionDetails.put("gross_amount", "265000");
    
    Map<String, String> creditCard = new HashMap<>();
    creditCard.put("secure", "true");
    
    params.put("transaction_details", transactionDetails);
    params.put("credit_card", creditCard);
    
    return params;
}

// Create Token and then you can send token variable to FrontEnd,
// to initialize Snap JS when customer click pay button
String redirectURL = snapApi.createTransactionRedirectUrl(requestBody())

//you can return to redirectURL on springboot controller
return "redirect:" +redirectURL;
```
#### Implement Notification Handler
[Refer to this section](#23-handle-http-notification)

### 2.2.C Core API (VT-Direct)

You can see Core API examples [here](example/src/main/java/com/midtrans/sample/controller/CoreApiController.java).

Available methods for `MidtransCoreApi` class
```java
    /**
     * Do re-set config Class like clientKey, serverKey, isProduction
     * @return {Config class}
     */
    Config apiConfig();

    /**
    * Do `/charge` API request to Core API
    * @param  {Map Object} parameter - object of Core API JSON body as parameter, will be converted to JSON (more params detail refer to: https://api-docs.midtrans.com)
    * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
    */
    JSONObject chargeTransaction(Map<String, Object> params);

    /**
     * Do `/<orderId>/status` API request to Core API
     * @param  {String} orderId - orderId of the transaction (more detail refer to: https://api-docs.midtrans.com)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     */
    JSONObject checkTransaction(String orderId);

    /**
     * Do `/<orderId>/approve` API request to Core API
     * @param  {String} orderId - orderId of the transaction (more detail refer to: https://api-docs.midtrans.com)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     */
    JSONObject approveTransaction(String orderId);

    /**
     * Do `/<orderId>/cancel` API request to Core API
     * @param  {String} orderId - orderId of the transaction (more detail refer to: https://api-docs.midtrans.com)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     */
    JSONObject cancelTransaction(String orderId);

    /**
     * Do `/<orderId>/expire` API request to Core API
     * @param  {String} orderId - orderId of the transaction (more detail refer to: https://api-docs.midtrans.com)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     */
    JSONObject expireTransaction(String orderId);

    /**
     * Do `/<orderId>/refund` API request to Core API
     * @param  {String} orderId - orderId of the transaction (more detail refer to: https://api-docs.midtrans.com)
     * @param  {Map Object} parameter - object of Core API JSON body as parameter, will be converted to JSON (more params detail refer to: https://api-docs.midtrans.com)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     */
    JSONObject refundTransaction(String orderId, Map<String, String> params);

    /**
     * Do `/token` API request to Core API
     * @param  {Map Object} parameter - object of Core API JSON body as parameter, will be converted to JSON (more params detail refer to: https://api-docs.midtrans.com)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     */
    JSONObject cardToken(Map<String, String> params);

    /**
     * Do `/card/register` API request to Core API
     * @param  {Map Object} parameter - object of Core API JSON body as parameter, will be converted to JSON (more params detail refer to: https://api-docs.midtrans.com)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     */
    JSONObject registerCard(Map<String, String> params);

    /**
     * Do `/point_inquiry/<tokenId>` API request to Core API
     * @param  {String} tokenId - tokenId of credit card (more detail refer to: https://api-docs.midtrans.com)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     */
    JSONObject cardPointInquiry(String tokenId);
    
    /**
     * Do `/<orderId>/deny` API request to Core API
     * @param {String} orderId - orderId of the transaction (more detail refer to: https://api-docs.midtrans.com/#deny-transaction)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     */
    JSONObject denyTransaction(String orderId);
    
    /**
     * Do `v1/bins/{bin}` API request to Core API
     * @param binNumber {String} of the transaction (more detail refer to: https://api-docs.midtrans.com/#bin-api)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject getBIN(String binNumber) throws MidtransError;
```
`params` is Map Object or String of JSON of [Core API Parameter](https://api-docs.midtrans.com/#json-objects)

#### Credit Card Get Token

Get token should be handled on  Frontend please refer to [API docs](https://api-docs.midtrans.com)

#### Credit Card Charge

```java
MidtransCoreApi coreApi = new ConfigFactory(new Config("YOU_SERVER_KEY","YOUR_CLIENT_KEY", false)).getCoreApi();


// Create Function JSON Raw Object
public Map<String, Object> requestBody() {
    UUID idRand = UUID.randomUUID();
    Map<String, Object> params = new HashMap<>();
    
    Map<String, String> transactionDetails = new HashMap<>();
    transactionDetails.put("order_id", idRand);
    transactionDetails.put("gross_amount", "265000");
    
    Map<String, String> creditCard = new HashMap<>();
    creditCard.put("token_id", YOUR_TOKEN_ID);
    creditCard.put("authentication", "true");
    
    params.put("transaction_details", transactionDetails);
    params.put("credit_card", creditCard);
    
    return params;
}

// charge transaction
JSONObject result = coreApi.chargeTransaction(requestBody());
System.out.println(result);
```

#### Credit Card 3DS Authentication

The credit card charge result may contains `redirect_url` for 3DS authentication. 3DS Authentication should be handled on Frontend please refer to [API docs](https://api-docs.midtrans.com/#card-features-3d-secure)

For full example on Credit Card 3DS transaction refer to:
- [App examples](example/src/main/java/com/midtrans/sample) that implement Snap & Core Api

### 2.3 Handle HTTP Notification

> **IMPORTANT NOTE**: To update transaction status on your backend/database, **DO NOT** solely rely on frontend callbacks! For security reason to make sure the status is authentically coming from Midtrans, only update transaction status based on HTTP Notification or API Get Status.

Create separated web endpoint (notification url) to receive HTTP POST notification callback/webhook. 
HTTP notification will be sent whenever transaction status is changed.
Example also available [here](example/src/main/java/com/midtrans/sample/controller/CoreApiController.java)

```java
@PostMapping(value = "/notification", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<String> handleNotification(@RequestBody Map<String, Object> response) throws MidtransError {
        String notifResponse = null;
        if (!(response.isEmpty())) {
            //Get Order ID from notification body
            String orderId = (String) response.get("order_id");

            // Get status transaction to api with order id
            JSONObject transactionResult = coreApi.checkTransaction(orderId);

            String transactionStatus = (String) transactionResult.get("transaction_status");
            String fraudStatus = (String) transactionResult.get("fraud_status");

            notifResponse = "Transaction notification received. Order ID: " + orderId + ". Transaction status: " + transactionStatus + ". Fraud status: " + fraudStatus;
            System.out.println(notifResponse);

            if (transactionStatus.equals("capture")) {
                if (fraudStatus.equals("challenge")) {
                    // TODO set transaction status on your database to 'challenge' e.g: 'Payment status challenged. Please take action on your Merchant Administration Portal
                } else if (fraudStatus.equals("accept")) {
                    // TODO set transaction status on your database to 'success'
                }
            } else if (transactionStatus.equals("cancel") || transactionStatus.equals("deny") || transactionStatus.equals("expire")) {
                // TODO set transaction status on your database to 'failure'
            } else if (transactionStatus.equals("pending")) {
                // TODO set transaction status on your database to 'pending' / waiting payment
            }
        }
        return new ResponseEntity<>(notifResponse, HttpStatus.OK);
    }
```

### 2.4 Transaction Action
Also available as examples [here](example/src/main/java/com/midtrans/sample/controller/CoreApiController.java)
#### Get Status
```java
// get status of transaction that already recorded on midtrans (already `charge`-ed) 
JSONObject result = checkTransaction("YOUR_ORDER_ID OR TRANSACTION_ID");
    //do something with `result` JSON Raw Object
```
#### Get Status B2B
```java
// get transaction status of VA b2b transaction
JSONObject result = getTransactionStatusB2B("YOUR_ORDER_ID OR TRANSACTION_ID");
    //do something with `result` JSON Raw Object
```
#### Approve Transaction
```java
// approve a credit card transaction with `challenge` fraud status
JSONObject result = approveTransaction("YOUR_ORDER_ID OR TRANSACTION_ID");
    //do something with `result` JSON Raw Object
```
#### Deny Transaction
```java
// deny a credit card transaction with `challenge` fraud status
JSONObject result = denyTransaction("YOUR_ORDER_ID OR TRANSACTION_ID");
    //do something with `result` JSON Raw Object
```
#### Cancel Transaction
```java
JSONObject result = cancelTransaction("YOUR_ORDER_ID OR TRANSACTION_ID");
    //do something with `result` JSON Raw Object
```
#### Expire Transaction
```java
JSONObject result = expireTransaction("YOUR_ORDER_ID OR TRANSACTION_ID");
    //do something with `result` JSON Raw Object
```
#### Refund Transaction
```java
//Make params with Map Object
private Map<String,String> params() {
    params.put("amount", "5000");
    params.put("reason", "Item out of stock");
    return params;
}

JSONObject refundTransaction("YOUR_ORDER_ID OR TRANSACTION_ID", params());
    //do something with `result` JSON Raw Object
```
### 2.2.D Iris API (Disbursement)
You can see Iris API examples [here](example/src/main/java/com/midtrans/sample/controller/IrisController.java).

#### IrisAPI Simple Sample Usage
```java

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.service.MidtransIrisApi;
import com.midtrans.httpclient.error.MidtransError;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.json.JSONObject;

public class MidtransIrisExample {

    public static void main(String[] args) throws MidtransError {
        MidtransIrisApi irisApi = new ConfigFactory(new Config("IRIS-CREDENTIALS", null, false)).getIrisApi();
        
        Map<String, String> beneficiary = new HashMap<>();
            beneficiary.put("bank", "bca");
            beneficiary.put("name", "PT Jon Snow");
            beneficiary.put("alias_name", "ptjonsnow");
            beneficiary.put("account", "1145532134");
            beneficiary.put("email", "jonsnow@mail.com");

        // Optional - Use idempotency key
        irisApi.apiConfig().setIrisIdempotencyKey("Iris-Idempotency-Key");
        
        // Request create beneficiary to Iris API
        JSONObject result = irisApi.createBeneficiaries(beneficiary);
        System.out.println(result);
    }
}
```

Available methods for `MidtransIrisApi` class
```java
    /**
     * Do re-set config Class iris-credential, IrisIdempotencyKey
     *
     * @return {Config class}
     */
    Config apiConfig();

    /**
     * Do `/ping` Returns pong message for monitoring purpose
     *
     * @return {String} - with value Pong
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    String ping() throws MidtransError;

    /**
     * Do get `/balance` API request to Use get current balance information. For Aggregator Partner you need to top up to Iris’ bank account.
     *
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response refer to: https://iris-docs.midtrans.com/#check-balance-aggregator
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject getBalance() throws MidtransError;

    /**
     * Do create `/beneficiaries` Use this API to create a new beneficiary information for quick access on the payout page in Iris Portal.
     *
     * @param params Map Object parameter, object of Core API JSON body as parameter, will be converted to JSON (more params detail refer to: https://iris-docs.midtrans.com/#create-beneficiaries)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject createBeneficiaries(Map<String, String> params) throws MidtransError;

    /**
     * Do update `/beneficiaries/{alias_name}` Use this API to update an existing beneficiary identified by it's `alias_name`.
     *
     * @param aliasName Alias name used by the Beneficiary. Length should be less than or equal to 20 characters only alphanumeric characters are allowed
     * @param params    Map Object parameter, object of Core API JSON body as parameter, will be converted to JSON (more params detail refer to: https://iris-docs.midtrans.com/#update-beneficiaries)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject updateBeneficiaries(String aliasName, Map<String, String> params) throws MidtransError;

    /**
     * Do get `/beneficiaries` Use this API to fetch list of all beneficiaries saved in Iris Portal.
     *
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response refer to: https://iris-docs.midtrans.com/#list-beneficiaries
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONArray getBeneficiaries() throws MidtransError;

    /**
     * Do create `/payouts` This API is for Creator to create a payout. It can be used for single payout and also multiple payouts.
     *
     * @param params Map Object parameter, object of Core API JSON body as parameter, will be converted to JSON (more params detail refer to: https://iris-docs.midtrans.com/#create-payouts)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject createPayouts(Map<String, Object> params) throws MidtransError;

    /**
     * Do approve `/payouts/approve` Use this API for Apporver to approve multiple payout request.
     *
     * @param params Map Object parameter, object of Core API JSON body as parameter, will be converted to JSON (more params detail refer to: https://iris-docs.midtrans.com/#approve-payouts)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject approvePayouts(Map<String, Object> params) throws MidtransError;

    /**
     * Do reject `/payouts/reject` Use this API for Apporver to reject multiple payout request.
     *
     * @param params Map Object parameter, object of Core API JSON body as parameter, will be converted to JSON (more params detail refer to: https://iris-docs.midtrans.com/#reject-payouts)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject rejectPayouts(Map<String, Object> params) throws MidtransError;

    /**
     * Do get `/payouts/{reference_no}` Use this API for get details of a single payout
     *
     * @param referenceNo String parameter, unique reference no of a payout (more params detail refer to: https://iris-docs.midtrans.com/#get-payout-details)
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject getPayoutDetails(String referenceNo) throws MidtransError;

    /**
     * Do get `/statements` Use this API for list all transactions history for a month. You can specified start date and also end date for range transaction history.
     *
     * @param fromDate String date parameter, start date range for payouts (YYYY-MM-DD) more params detail refer to: https://iris-docs.midtrans.com/#transaction-history
     * @param toDate   String date parameter, end date range for payouts (YYYY-MM-DD) more params detail refer to: https://iris-docs.midtrans.com/#get-payout-details
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONArray getTransactionHistory(String fromDate, String toDate) throws MidtransError;

    /**
     * Do get `/channels` Use this API for get top up information channel only for Aggregator Partner
     *
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response (more params detail refer to: https://iris-docs.midtrans.com/#top-up-channel-information-aggregator)
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONArray getTopUpChannels() throws MidtransError;

    /**
     * Do get `/bank_accounts` Use this API for show list of registered bank accounts for facilitator partner
     *
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response (more params detail refer to: https://iris-docs.midtrans.com/#bank-accounts-facilitator)
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONArray getBankAccounts() throws MidtransError;

    /**
     * Do get `/bank_accounts/{bank_account_id}/balance` For Facilitator Partner, use this API is to get current balance information of your registered bank account.
     *
     * @param bankAccountId String Bank Account Number
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response refer to: https://iris-docs.midtrans.com/#check-balance-facilitator
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject getFacilitatorBalance(String bankAccountId) throws MidtransError;

    /**
     * Do get `/beneficiary_banks` Use this API for show list of supported banks in IRIS. https://iris-docs.midtrans.com/#supported-banks
     *
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response (more params detail refer to: https://iris-docs.midtrans.com/#list-banks)
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject getBeneficiaryBanks() throws MidtransError;

    /**
     * Do validate `/account_validation` Use this API for check if an account is valid, if valid return account information.
     *
     * @param bank    String bank code
     * @param account String Account number
     * @return {JSONObject} - org.json Promise contains Object from JSON decoded response (more params detail refer to: https://iris-docs.midtrans.com/#validate-bank-account)
     * @throws MidtransError when an exception was occurred during executing the request.
     */
    JSONObject validateBankAccount(String bank, String account) throws MidtransError;
```
## 3. Snap-BI (*NEW FEATURE starting v3.2.0)
Standar Nasional Open API Pembayaran, or in short SNAP, is a national payment open API standard published by Bank Indonesia. To learn more you can read this [docs](https://docs.midtrans.com/reference/core-api-snap-open-api-overview)

### 3.1 General Settings

```java
//These config value are based on the header stated here https://docs.midtrans.com/reference/getting-started-1
// Set to Development/Sandbox Environment (default). Set to true for Production Environment (accept real transaction).
SnapBiConfig.setProduction(true);
// Set your client id. Merchant’s client ID that will be given by Midtrans, will be used as X-CLIENT-KEY on request’s header in B2B Access Token API.
SnapBiConfig.setSnapBiClientId("YOUR CLIENT ID");
// Set your private key here, make sure to add \n on the private key, you can refer to the examples
SnapBiConfig.setSnapBiPrivateKey("YOUR PRIVATE KEY");
// Set your client secret. Merchant’s secret key that will be given by Midtrans, will be used for symmetric signature generation for Transactional API’s header.
SnapBiConfig.setSnapBiClientSecret("YOUR CLIENT SECRET");
// Set your partner id. Merchant’s partner ID that will be given by Midtrans, will be used as X-PARTNER-ID on Transactional API’s header.
SnapBiConfig.setSnapBiPartnerId("YOUR PARTNER ID");
// Set the channel id here.
SnapBiConfig.setSnapBiChannelId("YOUR CHANNEL ID");
// Enable logging to see details of the request/response make sure to disable this on production, the default is disabled.
SnapBiConfig.setEnableLogging(true);
// Set your public key here if you want to verify your webhook notification, make sure to add \n on the public key, you can refer to the examples
SnapBiConfig.setSnapBiPublicKey("YOUR PUBLIC KEY");
```

### 3.2 Create Payment

#### 3.2.1 Direct Debit (Gopay, Dana, Shopeepay)
Refer to this [docs](https://docs.midtrans.com/reference/direct-debit-api-gopay) for more detailed information about creating payment using direct debit.

```java

// Create the top-level map of request body
public Map<String, Object> createDirectDebitRequestBody() {
    // Create the top-level map
    Map<String, Object> requestBody = new HashMap<>();

    // Add partnerReferenceNo and other fields
    requestBody.put("partnerReferenceNo", externalId);
    requestBody.put("chargeToken", "");
    requestBody.put("merchantId", merchantId);
    requestBody.put("validUpTo", "2030-07-20T20:34:15.452305Z");

    // Create and add urlParam map
    Map<String, String> urlParam = new HashMap<>();
    urlParam.put("url", "https://midtrans-test.com/api/notification");
    urlParam.put("type", "PAY_RETURN");
    urlParam.put("isDeeplink", "N");
    requestBody.put("urlParam", urlParam);

    // Create and add payOptionDetails list
    List<Map<String, Object>> payOptionDetails = new ArrayList<>();
    Map<String, Object> payOptionDetail = new HashMap<>();
    payOptionDetail.put("payMethod", "GOPAY");
    payOptionDetail.put("payOption", "GOPAY_WALLET");

    // Create and add transAmount map
    Map<String, String> transAmount = new HashMap<>();
    transAmount.put("value", "1500");
    transAmount.put("currency", "IDR");
    payOptionDetail.put("transAmount", transAmount);

    payOptionDetails.add(payOptionDetail);
    requestBody.put("payOptionDetails", payOptionDetails);

    // Create and add additionalInfo map
    Map<String, Object> additionalInfo = new HashMap<>();

    // Create and add customerDetails map
    Map<String, Object> customerDetails = new HashMap<>();
    customerDetails.put("firstName", "Merchant");
    customerDetails.put("lastName", "Operation");
    customerDetails.put("email", "merchant-ops@midtrans.com");
    customerDetails.put("phone", "+6281932358123");

    // Create and add billingAddress map
    Map<String, String> billingAddress = new HashMap<>();
    billingAddress.put("firstName", "Merchant");
    billingAddress.put("lastName", "Operation");
    billingAddress.put("phone", "+6281932358123");
    billingAddress.put("address", "Pasaraya Blok M");
    billingAddress.put("city", "Jakarta");
    billingAddress.put("postalCode", "12160");
    billingAddress.put("countryCode", "IDN");
    customerDetails.put("billingAddress", billingAddress);

    // Create and add shippingAddress map
    Map<String, String> shippingAddress = new HashMap<>();
    shippingAddress.put("firstName", "Merchant");
    shippingAddress.put("lastName", "Operation");
    shippingAddress.put("phone", "+6281932358123");
    shippingAddress.put("address", "Pasaraya Blok M");
    shippingAddress.put("city", "Jakarta");
    shippingAddress.put("postalCode", "12160");
    shippingAddress.put("countryCode", "IDN");
    customerDetails.put("shippingAddress", shippingAddress);

    additionalInfo.put("customerDetails", customerDetails);

    // Create and add items list
    List<Map<String, Object>> items = new ArrayList<>();
    Map<String, Object> item = new HashMap<>();
    item.put("id", "8143fc4f-ec05-4c55-92fb-620c212f401e");

    // Create and add price map
    Map<String, String> price = new HashMap<>();
    price.put("value", "1500.00");
    price.put("currency", "IDR");
    item.put("price", price);

    item.put("quantity", 1);
    item.put("name", "test item name");
    item.put("brand", "test item brand");
    item.put("category", "test item category");
    item.put("merchantName", "Merchant Operation");

    items.add(item);
    additionalInfo.put("items", items);

    requestBody.put("additionalInfo", additionalInfo);

    return requestBody;
}
/**
 *  Basic example
 * to change the payment method, you can change the value of the request body on the `payOptionDetails`
 * the `currency` value that we support for now is only `IDR`
 */
JSONObject snapBiResponse1 = SnapBi.directDebit()
        .withBody(createDirectDebitRequestBody)
        .createPayment(externalId);

```
#### 3.2.2 VA (Bank Transfer)
Refer to this [docs](https://docs.midtrans.com/reference/virtual-account-api-bank-transfer) for more detailed information about VA/Bank Transfer.
```java

//function to create request body
public Map<String, Object> createVaRequestBody() {
    // Create the top-level map
    Map<String, Object> requestBody = new HashMap<>();

    // Create and add transAmount map
    Map<String, Object> totalAmount = new HashMap<>();
    totalAmount.put("value", "1500.00");
    totalAmount.put("currency", "IDR");
    requestBody.put("totalAmount", totalAmount);

    Map<String, Object> flags = new HashMap<>();
    flags.put("shouldRandomizeVaNumber", true);

    Map<String, Object> billingAddress = new HashMap<>();
    billingAddress.put("firstName", "Merchant");
    billingAddress.put("lastName", "Operation");
    billingAddress.put("phone", "+6281932358123");
    billingAddress.put("address", "Pasaraya Blok M");
    billingAddress.put("city", "Jakarta");
    billingAddress.put("postalCode", "12160");
    billingAddress.put("countryCode", "IDN");

    Map<String, Object> shippingAddress = new HashMap<>();
    shippingAddress.put("firstName", "Merchant");
    shippingAddress.put("lastName", "Operation");
    shippingAddress.put("phone", "6281932358123");
    shippingAddress.put("address", "Pasaraya Blok M");
    shippingAddress.put("city", "Jakarta");
    shippingAddress.put("postalCode", "12160");
    shippingAddress.put("countryCode", "IDN");


    Map<String, Object> customerDetails = new HashMap<>();
    customerDetails.put("firstName", "Merchant");
    customerDetails.put("lastName", "Operation");
    customerDetails.put("email", "merchant-ops@midtrans.com");
    customerDetails.put("phone", "+6281932358123");
    customerDetails.put("billingAddress", billingAddress);
    customerDetails.put("shippingAddress", shippingAddress);

    List<Map<String, Object>> items = new ArrayList<>();
    Map<String, Object> item = new HashMap<>();
    item.put("id", "8143fc4f-ec05-4c55-92fb-620c212f401e");
    // Create and add price map
    Map<String, Object> price = new HashMap<>();
    price.put("value", "1500.00");
    price.put("currency", "IDR");
    item.put("price", price);

    item.put("quantity", 1);
    item.put("name", "test item name");
    item.put("brand", "test item brand");
    item.put("category", "test item category");
    item.put("merchantName", "Merchant Operation");

    items.add(item);

    Map<String, Object> additionalInfo = new HashMap<>();
    additionalInfo.put("merchantId", merchantId);
    additionalInfo.put("bank", "bca");
    additionalInfo.put("flags", flags);
    additionalInfo.put("customerDetails", customerDetails);
    additionalInfo.put("shippingAddress", shippingAddress);
    additionalInfo.put("billingAddress", billingAddress);
    additionalInfo.put("items", items);

    requestBody.put("partnerServiceId", "    1234");
    requestBody.put("customerNo", "0000000000");
    requestBody.put("virtualAccountNo", "    12340000000000");
    requestBody.put("virtualAccountName", "Merchant Operation");
    requestBody.put("virtualAccountEmail", "merchant-ops@midtrans.com");
    requestBody.put("virtualAccountPhone", "6281932358123");
    requestBody.put("trxId", externalId);
    requestBody.put("additionalInfo", additionalInfo);

    return requestBody;
}

/**
 * basic implementation to create payment using va
 */
JSONObject snapBiResponse1 = SnapBi.va()
        .withBody(createVaRequestBody())
        .createPayment(externalId);
```
#### 3.2.3 Qris
Refer to this [docs](https://docs.midtrans.com/reference/mpm-api-qris) for more detailed information about Qris.
```java

//function to create the request body
public static Map<String, Object> createQrisRequestBody() {
    // Create the top-level map
    Map<String, Object> requestBody = new HashMap<>();

    // Add partnerReferenceNo and other fields
    requestBody.put("partnerReferenceNo", externalId);
    requestBody.put("merchantId", merchantId);
    requestBody.put("validityPeriod", "2030-07-03T12:08:56-07:00");

    // Create and add amount map
    Map<String, String> amount = new HashMap<>();
    amount.put("value", "1500.00");
    amount.put("currency", "IDR");
    requestBody.put("amount", amount);

    // Create and add additionalInfo map
    Map<String, Object> additionalInfo = new HashMap<>();

    // Create and add customerDetails map
    Map<String, Object> customerDetails = new HashMap<>();
    customerDetails.put("firstName", "Merchant");
    customerDetails.put("lastName", "Operation");
    customerDetails.put("email", "merchant-ops@midtrans.com");
    customerDetails.put("phone", "+6281932358123");

    additionalInfo.put("customerDetails", customerDetails);

    // Create and add items list
    List<Map<String, Object>> items = new ArrayList<>();
    Map<String, Object> item = new HashMap<>();
    item.put("id", "8143fc4f-ec05-4c55-92fb-620c212f401e");

    // Create and add price map
    Map<String, String> price = new HashMap<>();
    price.put("value", "1500.00");
    price.put("currency", "IDR");
    item.put("price", price);

    item.put("quantity", 1);
    item.put("name", "test item name");
    item.put("brand", "test item brand");
    item.put("category", "test item category");
    item.put("merchantName", "Merchant Operation");

    items.add(item);
    additionalInfo.put("items", items);
    additionalInfo.put("acquirer", "gopay");
    additionalInfo.put("customerDetails", customerDetails);
    additionalInfo.put("countryCode", "ID");
    additionalInfo.put("locale", "id_ID");

    requestBody.put("additionalInfo", additionalInfo);

    return requestBody;
}

/**
 * basic implementation to create payment using Qris
 */
JSONObject snapBiResponse1 = SnapBi.qris()
        .withBody(createQrisRequestBody())
        .createPayment(externalId);
```

### 3.4 Get Transaction Status
Refer to this [docs](https://docs.midtrans.com/reference/get-transaction-status-api) for more detailed information about getting the transaction status.
```java
public static Map<String, Object> createDirectDebitStatusByReferenceNoBody
        () {
    // Create the top-level map
    Map<String, Object> requestBody = new HashMap<>();

    requestBody.put("originalReferenceNo", "A1202409230511097Hmk31oa4UID");
    requestBody.put("serviceCode", "54");
    requestBody.put("originalExternalId", "b8cc77cd-64c2-4edb-b083-39a320f67c06");
    return requestBody;
}

public static Map<String, Object> createDirectDebitStatusByExternalIdBody
        () {
    // Create the top-level map
    Map<String, Object> requestBody = new HashMap<>();

    requestBody.put("originalExternalId", "uzi-order-testing66ce90ce90ee5");
    requestBody.put("serviceCode", "54");
    return requestBody;
}
public static  Map<String, String > createAdditionalHeader(){
    Map<String, String> headers = new HashMap<>();
    headers.put("X-Device-id", "device id");
    headers.put("debug-id", "debug id");
    return headers;
}

public static Map<String, Object> createVaStatusBody
        () {
    // Create the top-level map
    Map<String, Object> additionalInfo = new HashMap<>();
    additionalInfo.put("merchantId", merchantId);

    Map<String, Object> requestBody = new HashMap<>();

    requestBody.put("partnerServiceId", "    1234");
    requestBody.put("customerNo", "083430");
    requestBody.put("virtualAccountNo", "    1234083430");
    requestBody.put("inquiryRequestId", "4b1da710-fbf5-425e-9648-06e40b290326");
    requestBody.put("additionalInfo", additionalInfo);
    return requestBody;
}

/**
 * Example code for Direct Debit getStatus using externalId
 */
JSONObject snapBiResponse1 = SnapBi.directDebit()
        .withBody(createDirectDebitStatusByReferenceNoBody())
        .getStatus(externalId);

/**
 * Example code for Direct Debit getStatus using referenceNo
 */
JSONObject snapBiResponse4 = SnapBi.directDebit()
        .withBody(createDirectDebitStatusByReferenceNoBody())
        .getStatus(externalId);
    
/**
 * Example code for VA (Bank Transfer) getStatus
 */
JSONObject snapBiResponse7 = SnapBi.va()
        .withBody(createVaStatusBody())
        .getStatus(externalId);
/**
 * 
 * Example code for Qris getStatus
 */
JSONObject snapBiResponse10 = SnapBi.qris()
        .withBody(createVaStatusBody())
        .getStatus(externalId);     

```

### 3.5 Cancel Transaction
Refer to this [docs](https://docs.midtrans.com/reference/cancel-api) for more detailed information about cancelling the payment.
```java
public static Map<String, Object> createDirectDebitCancelByExternalIdBody
        () {
    // Create the top-level map
    Map<String, Object> requestBody = new HashMap<>();

    requestBody.put("originalExternalId", "67be74c3-8cf5-4af3-b62b-0715899fd713");
    return requestBody;
}

public static Map<String, Object> createDirectCancelByReferenceNoBody
        () {
    // Create the top-level map
    Map<String, Object> requestBody = new HashMap<>();

    requestBody.put("originalReferenceNo", "A12024092305243239N7MuWlDCID");
    return requestBody;
}

public static Map<String, Object> createVaCancelBody
        () {
    // Create the top-level map
    Map<String, Object> additionalInfo = new HashMap<>();
    additionalInfo.put("merchantId", merchantId);

    Map<String, Object> requestBody = new HashMap<>();

    requestBody.put("partnerServiceId", "    1234");
    requestBody.put("customerNo", "741633");
    requestBody.put("virtualAccountNo", "    1234741633");
    requestBody.put("trxId", "02a9c5a3-e088-45de-8688-424b5f65c927");
    requestBody.put("additionalInfo", additionalInfo);
    return requestBody;
}

public static Map<String, Object> createQrisCancelBody
        () {
    // Create the top-level map
    Map<String, Object> requestBody = new HashMap<>();

    requestBody.put("originalReferenceNo", "A120240923112046EjKURlq1QqID");
    requestBody.put("merchantId", merchantId);
    requestBody.put("reason", "cancel reason");
    return requestBody;
}
/**
 * Basic implementation to cancel transaction using referenceNo
 */
JSONObject snapBiResponse1 = SnapBi.directDebit()
        .withBody(createDirectCancelByReferenceNoBody())
        .cancel(externalId);

/**
 * Basic implementation to cancel transaction using externalId
 */
JSONObject snapBiResponse4 = SnapBi.directDebit()
        .withBody(createDirectDebitCancelByExternalIdBody())
        .cancel(externalId);

/**
 * Basic implementation of VA (Bank Transfer) to cancel transaction
 */
JSONObject snapBiResponse7 = SnapBi.va()
        .withBody(createVaCancelBody())
        .cancel(externalId);

/**
 * Basic implementation of Qris to cancel transaction
 */
JSONObject snapBiResponse10 = SnapBi.qris()
        .withBody(createQrisCancelBody())
        .cancel(externalId);
```

### 3.6 Refund Transaction
Refer to this [docs](https://docs.midtrans.com/reference/refund-api) for more detailed information about refunding the payment.

```java
 public static Map<String, Object> createDirectDebitRefundByExternalIdBody() {
    // Create the top-level map

    Map<String, Object> refundAmount = new HashMap<>();
    refundAmount.put("value", "100.00");
    refundAmount.put("currency", "IDR");

    Map<String, Object> requestBody = new HashMap<>();

    requestBody.put("originalExternalId", "9d18b81c-485c-4b67-a308-7cc060dc202e");
    requestBody.put("partnerRefundNo", "9d18b81c-485c-4b67-a308-7cc060dc202e refund-0001");
    requestBody.put("reason", "some-reason");
    requestBody.put("refundAmount", refundAmount);

    return requestBody;
}

public static Map<String, Object> createDirectDebitRefundByReferenceNoBody() {
    // Create the top-level map
    Map<String, Object> refundAmount = new HashMap<>();
    refundAmount.put("value", "100.00");
    refundAmount.put("currency", "IDR");

    Map<String, Object> requestBody = new HashMap<>();

    requestBody.put("originalReferenceNo", "A1202409230511097Hmk31oa4UID");
    requestBody.put("reason", "some-reason");
    requestBody.put("refundAmount", refundAmount);
    return requestBody;
}
public static Map<String, Object> createQrisRefundBody() {
    Map<String, Object> refundAmount = new HashMap<>();
    refundAmount.put("value", "100.00");
    refundAmount.put("currency", "IDR");

    Map<String, Object> additionalInfo = new HashMap<>();
    additionalInfo.put("foo", "bar");

    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("merchantId", merchantId);
    requestBody.put("originalPartnerReferenceNo", "uzi-order-testing66e01a9b8c6bf");
    requestBody.put("originalReferenceNo", "A120240923082857KzdNmUKObJID");
    requestBody.put("partnerRefundNo", "refund-abc123456");
    requestBody.put("reason", "some-reason");
    requestBody.put("refundAmount", refundAmount);
    requestBody.put("additionalInfo", additionalInfo);

    return requestBody;
}
/**
 * Example code for refund using externalId
 */
JSONObject snapBiResponse4 = SnapBi.directDebit()
        .withBody(createDirectDebitRefundByExternalIdBody())
        .refund(externalId);

/**
 * Example code for refund using reference no
 */
JSONObject snapBiResponse1 = SnapBi.directDebit()
        .withBody(createDirectDebitRefundByReferenceNoBody())
        .refund(externalId);
    
/**
 * Example code for refund using Qris
 */
JSONObject snapBiResponse7 = SnapBi.qris()
        .withBody(createQrisRefundBody())
        .refund(externalId);
```

### 3.7 Adding additional header / override the header

You can add or override the header value, by utilizing the `withAccessTokenHeader` or `withTransactionHeader` method chain.
Refer to this [docs](https://docs.midtrans.com/reference/core-api-snap-open-api-overview) to see the header value required by Snap-Bi , and see the default header on each payment method

```java
//function to create the additional header
public static  Map<String, String > createAdditionalHeader(){
    Map<String, String> headers = new HashMap<>();
    headers.put("X-Device-id", "device id");
    headers.put("debug-id", "debug id");
    return headers;
}

 /**
 * Example code for Direct Debit refund using additional header
 */
SONObject snapBiResponse3 = SnapBi.directDebit()
        .withBody(createDirectDebitRequestBody())
        .withAccessTokenHeader(createAdditionalHeader())
        .withTransactionHeader(createAdditionalHeader())
        .createPayment(externalId);
/**
 * Example code for using additional header on creating payment using VA
 */
JSONObject snapBiResponse3 = SnapBi.va()
        .withBody(createVaRequestBody())
        .withAccessTokenHeader(createAdditionalHeader())
        .withTransactionHeader(createAdditionalHeader())
        .createPayment(externalId);
```

### 3.8 Reusing Access Token

If you've saved your previous access token and wanted to re-use it, you can do it by utilizing the `.withAccessToken()`.

```java
/**
 * Example reusing your existing accessToken by using .withAccessToken()
 */
JSONObject snapBiResponse2 = SnapBi.va()
        .withBody(createVaRequestBody())
        .withAccessToken("your access token")
        .createPayment(externalId);

```

### 3.9 Payment Notification
To implement Snap-Bi Payment Notification you can refer to this [docs](https://docs.midtrans.com/reference/payment-notification-api)
To verify the webhook notification that you recieve you can use this method below
```java
 
//the request body/ payload sent by the webhook
String notificationPayload = "{\"originalPartnerReferenceNo\":\"GP24043015193402809\",\"originalReferenceNo\":\"A120240430081940S9vu8gSjaRID\",\"merchantId\":\"G099333790\",\"amount\":{\"value\":\"102800.00\",\"currency\":\"IDR\"},\"latestTransactionStatus\":\"00\",\"transactionStatusDesc\":\"SUCCESS\",\"additionalInfo\":{\"refundHistory\":[]}}"; // Sample notification body, replace with actual data you receive from Midtrans


// to get the signature value, you need to retrieve it from the webhook header called X-Signature
String signature = "CgjmAyC9OZ3pB2JhBRDihL939kS86LjP1VLD1R7LgI4JkvYvskUQrPXgjhrZqU2SFkfPmLtSbcEUw21pg2nItQ0KoX582Y6Tqg4Mn45BQbxo4LTPzkZwclD4WI+aCYePQtUrXpJSTM8D32lSJQQndlloJfzoD6Rh24lNb+zjUpc+YEi4vMM6MBmS26PpCm/7FZ7/OgsVh9rlSNUsuQ/1QFpldA0F8bBNWSW4trwv9bE1NFDzliHrRAnQXrT/J3chOg5qqH0+s3E6v/W21hIrBYZVDTppyJPtTOoCWeuT1Tk9XI2HhSDiSuI3pevzLL8FLEWY/G4M5zkjm/9056LTDw==";

// to get the timeStamp value, you need to retrieve it from the webhook header called X-Timestamp
String timeStamp = "2024-10-07T15:45:22+07:00";

// the url path is based on the webhook url of the payment method for example for direct debit is `/v1.0/debit/notify`
String notificationUrlPath = "/v1.0/debit/notify";
/**
 * Example verifying the webhook notification
 */
Boolean isVerified = SnapBi.notification()
        .withNotificationPayload(notificationPayload)
        .withSignature(signature)
        .withTimeStamp(timeStamp)
        .withNotificationUrlPath(notificationUrlPath)
        .isWebhookNotificationVerified();
```


## 4. Examples
Examples are available on [/examples](example) folder 
There are:
- [Core Api examples](example/src/main/java/com/midtrans/sample/controller/CoreApiController.java)
- [Snap examples](example/src/main/java/com/midtrans/sample/controller/SnapController.java)
- [Mobile SDK examples](example/src/main/java/com/midtrans/sample/controller/MobileSdkController.java)
- [Iris examples](example/src/main/java/com/midtrans/sample/controller/IrisController.java)
- [Sample Functional Test](library/src/test/java/com/midtrans/java)
- [Live Demo App](https://midtrans-java.herokuapp.com/)

## Get help

* [Midtrans Docs](https://docs.midtrans.com)
* [Midtrans Dashboard ](https://dashboard.midtrans.com/)
* [SNAP documentation](http://snap-docs.midtrans.com)
* [Core API documentation](http://api-docs.midtrans.com)
* Can't find answer you looking for? email to [support@midtrans.com](mailto:support@midtrans.com)
