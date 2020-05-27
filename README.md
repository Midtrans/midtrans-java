# Midtrans Client - Java
[ ![Download](https://api.bintray.com/packages/midtrans/midtrans-java/com.midtrans/images/download.svg) ](https://bintray.com/midtrans/midtrans-java/com.midtrans/_latestVersion) [![Build Status](https://travis-ci.org/Xaxxis/midtrans-java.svg?branch=master)](https://travis-ci.org/Xaxxis/midtrans-java) [![Demo apps](https://img.shields.io/badge/Go%20to-Demo%20Apps-green)](https://midtrans-java.herokuapp.com/)

Midtrans :heart: Java, This is the Official Java API client/library for Midtrans Payment API. Visit [https://midtrans.com](https://midtrans.com). More information about the product and see documentation at [http://docs.midtrans.com](https://beta-docs.midtrans.com/) for more technical details. This library used java version 1.8

## 1. Installation

### 1.a Using Maven or Gradle
If you're using Maven as the build tools for your project, please add jcenter repository to your build definition, then add the following dependency to your project's build definition (pom.xml):
Maven:
```xml
<repositories>
    <repository>
        <id>jcenter</id>
        <name>bintray</name>
        <url>https://jcenter.bintray.com</url>
    </repository>
</repositories>

<dependencies>
  <dependency>
	  <groupId>com.midtrans</groupId>
	  <artifactId>java-library</artifactId>
	  <version>2.1.1</version>
</dependency>
</dependencies>
```
Gradle:
If you're using Gradle as the build tools for your project, please add jcenter repository to your build script then add the following dependency to your project's build definition (build.gradle):
```Gradle
repositories {
    maven {
        url  "https://jcenter.bintray.com" 
    }
}

dependencies {
    compile 'com.midtrans:java-library:2.1.1'
}
```

### 1.b Using JAR File

If you are not using project build management like Maven, Gradle or Ant you can use manual jar library download JAR Library on [here](https://dl.bintray.com/midtrans/midtrans-java/com/midtrans/java-library/2.1.1/java-library-2.1.1.jar)

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

Create API client object

```java
import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.service.MidtransCoreApi;

MidtransCoreApi coreApi = new ConfigFactory(new Config("YOU_SERVER_KEY","YOUR_CLIENT_KEY", false)).getCoreApi();

//You can set Config.class with
`YOUR_SERVER_KEY`
`YOUR_CLIENT_KEY`
`isProduction`
```


```java
import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.service.MidtransSnapApi;

MidtransSnapApi snapApi = new ConfigFactory(new Config("YOU_SERVER_KEY","YOUR_CLIENT_KEY", false)).getSnapApi();

//You can set Config.class with
`YOUR_SERVER_KEY`
`YOUR_CLIENT_KEY`
`isProduction`
```

```java
import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.service.MidtransIrisApi;

MidtransIrisApi irisApi = new ConfigFactory(new Config("IRIS-CREDENTIALS",null , false)).getIrisApi();

//You can set Config.class with
`IRIS-CREDNTIALS`
`null`
`isProduction`
```

You can also re-set config using `apiConfig()` method on MidtransCoreApi.Class, MidtransSnapApi.Class or MidtransIrisApi.class like `coreApi.apiConfig.set( ... )`
example:

```java
// Create Snap API instance, empty config
coreApi.apiConfig().setProduction(false);
coreApi.apiConfig().setCLIENT_KEY("YOUR_CLIENT_KEY");
coreApi.apiConfig().setSERVER_KEY("YOUR_SERVER_KEY");

// You don't have to re-set using all the options, 
// i.e. set serverKey only
coreApi.apiConfig().setSERVER_KEY("YOUR_SERVER_KEY");

// For Iris Disbursement can set creator & approver credentials with apiConfig()
irisApi.apiConfig().setSERVER_KEY("IRIS-CREDENTIALS");

irisApi.apiConfig().setIrisIdempotencyKey("IRIS-IDEMPOTENCY-KEY");

```

In production environment, LOG is by default turned off, you can enable by `setEnabledLog`, e.g:

```java
coreApi.apiConfig().setEnabledLog(true);
```

Using internal proxy, you can set ProxyConfig object with ProxyConfigBuilder to set hostname, port, username, and password. and also connectionTimeout, readTimeout, and writeTimeout. But if you not define network configuration like
ProxyConfig, connectionTimeout, readTimeout and writeTimeout, default value is 10 second and the connection no proxy configuration.

example:
```java
import com.midtrans.proxy.ProxyConfig;
import com.midtrans.proxy.ProxyConfigBuilder;
import com.midtrans.service.MidtransCoreApi;

private ProxyConfig proxyConfig = new ProxyConfigBuilder().setHost("36.92.108.150").setPort(3128).setUsername("").setPassword("").build();

//Intitialize MidtransCoreApi config with proxy and network configuration
private MidtransCoreApi coreApi = new ConfigFactory(new Config("YOU_SERVER_KEY","YOUR_CLIENT_KEY", false, 10, 10, 10, proxyConfig)).getCoreApi();
```
add new 4 params to use http proxy and network config,
- connectionTimeout - default value 10s
- readTimeout - default values 10s
- writeTimeout - default value 10s
- proxyConfig
- maxConnectionPool - default value 16
- keepAliveDuration - default value 300s

and also you can set value for connectionTimeout or etc with configuration class,
example: 
```java
//TimeUnit for integer value is SECONDS

coreApi.apiConfig().setConnectionTimeout(5);
coreApi.apiConfig().setReadTimeout(5);
coreApi.apiConfig().setWriteTimeout(5);
coreApi.apiConfig().setMaxConnectionPool(16);
coreApi.apiConfig().setKeepAliveDuration(300);
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
coreApi.apiConfig().paymentAppendNotification("https://example.com/test1,https://example.com/test");

//X-Override-Notification
coreApi.apiConfig().paymentOverrideNotification("https://example.com/test1,https://example.com/test");
```
When both `X-Append-Notification` and `X-Override-Notification` are used together then only override will be used.

#### CoreAPI Simple Sample Usage
```java

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.service.MidtransCoreApi;
import com.midtrans.httpclient.error.MidtransError;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.json.JSONObject;


public class MidtransExample {

    public static void main(String[] args) throws MidtransError {
        MidtransCoreApi coreApi = new ConfigFactory(new Config("YOU_SERVER_KEY","YOUR_CLIENT_KEY", false)).getCoreApi();

        UUID idRand = UUID.randomUUID();
        Map<String, Object> chargeParams = new HashMap<>();

        Map<String, String> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", idRand.toString());
        transactionDetails.put("gross_amount", "265000");

        chargeParams.put("transaction_details", transactionDetails);
        chargeParams.put("payment_type", "gopay");
        
            JSONObject result = coreApi.chargeTransaction(chargeParams);
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

## 4. Examples
Examples are available on [/examples](example) folder 
There are:
- [Core Api examples](example/src/main/java/com/midtrans/sample/controller/CoreApiController.java)
- [Snap examples](example/src/main/java/com/midtrans/sample/controller/SnapController.java)
- [Mobile SDK examples](example/src/main/java/com/midtrans/sample/controller/MobileSdkController.java)
- [Iris examples](example/src/main/java/com/midtrans/sample/controller/IrisController.java)
- [Live Demo App](https://midtrans-java.herokuapp.com/)

## Get help

* [Midtrans Docs](https://docs.midtrans.com)
* [Midtrans Dashboard ](https://dashboard.midtrans.com/)
* [SNAP documentation](http://snap-docs.midtrans.com)
* [Core API documentation](http://api-docs.midtrans.com)
* Can't find answer you looking for? email to [support@midtrans.com](mailto:support@midtrans.com)
