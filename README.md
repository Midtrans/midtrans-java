# Midtrans Client - Java
Midtrans :heart: Java, This is the Official Java API client/library for Midtrans Payment API. Visit [https://midtrans.com](https://midtrans.com). More information about the product and see documentation at [http://docs.midtrans.com](https://docs.midtrans.com) for more technical details.

## 1. Installation

### 1.a Using Maven or Gradle
Maven:
```xml
<repositories>
    <repository>
        <id>jcenter</id>
        <name>bintray</name>
        <url>http://jcenter.bintray.com</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.midtrans</groupId>
        <artifactId>midtrans-java-client</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```
Gradle:
```Gradle
repositories {
    maven {
        url  "http://jcenter.bintray.com" 
    }
}

dependencies {
    compile 'com.midtrans:midtrans-java-client:1.0'
}
```

### 1.b Using JAR File

If you are not using project build management like Maven, Gradle or Ant you can use manual jar library download JAR Library on this link

## 2. Usage

### 2.1 Choose Product/Method

We have [2 different products](https://docs.midtrans.com/en/welcome/index.html) of payment that you can use:
- [Snap](#22A-snap) - Customizable payment popup will appear on **your web/app** (no redirection). [doc ref](https://snap-docs.midtrans.com/)
- [Snap Redirect](#22B-snap-redirect) - Customer need to be redirected to payment url **hosted by midtrans**. [doc ref](https://snap-docs.midtrans.com/)
- [Core API (VT-Direct)](#22C-core-api-vt-direct) - Basic backend implementation, you can customize the frontend embedded on **your web/app** as you like (no redirection). [doc ref](https://api-docs.midtrans.com/)

Choose one that you think best for your unique needs.

### 2.2 Client Initialization and Configuration

Get your client key and server key from [Midtrans Dashboard](https://dashboard.midtrans.com)

Create API client object

```java
import com.midtrans.api.Config;
import com.midtrans.api.ConfigFactory;
import com.midtrans.api.service.MidtransCoreApi;

MidtransCoreApi coreApi = new ConfigFactory(new Config("YOU_SERVER_KEY","YOUR_CLIENT_KEY", false)).getCoreApi();

//You can set Config.class with
`YOUR_SERVER_KEY`
`YOUR_CLIENT_KEY`
`isProduction`
```


```java
import com.midtrans.api.Config;
import com.midtrans.api.ConfigFactory;
import com.midtrans.api.service.MidtransCoreApi;

MidtransSnapApi snapApi = new ConfigFactory(new Config("YOU_SERVER_KEY","YOUR_CLIENT_KEY", false)).getSnapApi();

//You can set Config.class with
`YOUR_SERVER_KEY`
`YOUR_CLIENT_KEY`
`isProduction`
```

You can also re-setting config using `apiConfig()` method on MidtransCoreApi.Class or MidtransSnapApi.Class like `coreApi.apiConfig.set( ... )`
example:

```java
// Create Snap API instance, empty config
coreApi.apiConfig().setProduction(false);
coreApi.apiConfig().setCLIENT_KEY("YOUR_CLIENT_KEY");
coreApi.apiConfig().setSERVER_KEY("YOUR_SERVER_KEY");

// You don't have to re-set using all the options, 
// i.e. set serverKey only
coreApi.apiConfig().setSERVER_KEY("YOUR_SERVER_KEY");
```

#### CoreAPI Simple Sample Usage
```java

import com.midtrans.api.Config;
import com.midtrans.api.ConfigFactory;
import com.midtrans.api.service.MidtransCoreApi;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.json.JSONObject;


public class MidtransExample {
    private MidtransCoreApi coreApi = new ConfigFactory(new Config("YOU_SERVER_KEY","YOUR_CLIENT_KEY", false)).getCoreApi();

    public static void main(String[] args) {
        
        UUID idRand = UUID.randomUUID();
        Map<String, Object> chargeParams = new HashMap<>();
    
        Map<String, String> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", idRand);
        transactionDetails.put("gross_amount", "265000");
    
        Map<String, String> creditCard = new HashMap<>();
        creditCard.put("authentication", "true");
    
        chargeParams.put("transaction_details", transactionDetails);
        chargeParams.put("credit_card", creditCard);
        

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
Replace `PUT_TRANSACTION_TOKEN_HERE` with `transactionToken` acquired above, you can use java template engine like Thymeleaf to parse `transactionToken` to frontEnd like [this](https://github.com/Xaxxis/midtrans-java/blob/master/application/src/main/resources/templates/snap/check-out.html#L90) `[[${transactionToken}]]`
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

Also available as examples [here](/application/src/main/java/com/midtrans/sample/controller/SnapController.java).

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

You can see some Core API examples [here](example/src/main/java/com/midtrans/sample/controller/CoreApiController.java).

Available methods for `MidtransCoreApi` class
```java
    /**
     * Do re-setting config Class like clientKey, serverKey, isProduction
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
- [App examples](application/src/main/) that implement Snap & Core Api

### 2.3 Handle HTTP Notification

> **IMPORTANT NOTE**: To update transaction status on your backend/database, **DO NOT** solely rely on frontend callbacks! For security reason to make sure the status is authentically coming from Midtrans, only update transaction status based on HTTP Notification or API Get Status.

Create separated web endpoint (notification url) to receive HTTP POST notification callback/webhook. 
HTTP notification will be sent whenever transaction status is changed.
Example also available [here](application/src/main)

```java
@PostMapping(value = "/notification", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<String> handleNotification(@RequestBody Map<String, Object> response) {
        String notifResponse = null;
        if (!(response.isEmpty())) {
            String orderId = (String) response.get("order_id");
            String transactionStatus = (String) response.get("transaction_status");
            String fraudStatus = (String) response.get("fraud_status");

            notifResponse = "Transaction notification received. Order ID: "+orderId+". Transaction status: "+transactionStatus+". Fraud status: "+fraudStatus;
            System.out.println(notifResponse);

            if (fraudStatus.equals("capture")) {
                if (fraudStatus.equals("challenge")) {
                    // TODO set transaction status on your database to 'challenge'
                } else if (fraudStatus.equals("accept")){
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
Also available as examples [here](application/src/main)
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

## 4. Examples
Examples are available on [/examples](/application/src/main) folder.
There are:
- [Core Api examples](/application/src/main)
- [Snap examples](/application/src/main)

## Get help

* [Midtrans Docs](https://docs.midtrans.com)
* [Midtrans Dashboard ](https://dashboard.midtrans.com/)
* [SNAP documentation](http://snap-docs.midtrans.com)
* [Core API documentation](http://api-docs.midtrans.com)
* Can't find answer you looking for? email to [support@midtrans.com](mailto:support@midtrans.com)

