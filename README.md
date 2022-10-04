# ING Open Banking SDK
With this SDK we provide the means to generate drivers to interact with ING's Open Banking APIs. It includes a custom generator that creates drivers which encapsulate HTTP signature signing and OAuth application token flows. As shown by the demo-app these drivers significantly reduce complexity to interact with ING's Open Banking APIs. Since the drivers are generated from OpenAPI Specification files, updating is easy by regenerating the drivers based on updated files.

## Features:
- [x] Open Banking Driver Generator (OpenApi Generator extension)
- [x] Open Banking Driver Generator Java templates
- [x] Example of generated drivers based on Swagger API definition targeting the ING Sandbox
- [x] Demo App for educational purposes
- [x] Helper classes for client configuration and http signature signing
- [x] Wrapper class for OAuth 2.0 technical driver (using OpenAPI Generator)
- [ ] Test generated drivers for feature completeness on Open Banking APIs (including PSD2)
- [ ] Publicly available drivers

## Dependencies:
* Java JDK 1.8+
* Maven 3.6+
* (curl)

## Quick Start (*nix)
* Run `./download-certificates.sh`
* Run `./keygen-premium.sh` and set `secret` as password.
* Run `./keygen-psd2.sh` and set `secret2` as password.
* Run  `mvn clean install`
* Run `./run-java.sh`
* Go to `http://localhost:8080`
* Click the link you get back and is displayed.
* Select the top profile and click next.
* You are redirected back to `localhost` and see the customer token displayed.
* Click the link to view the test profile accounts.

## Slightly Slower Start
### Folder structure
In this repository you can find api descriptions in swagger json format in the `api` directory. In addition, you can find the `open-banking-driver-generator` folder containing the modified Java [Open API Generator](https://github.com/OpenAPITools/openapi-generator) to generate drivers. Moreover, you can find a demo app, modules to generate Open Banking Drivers using the modified generator, and some wrapper classes in the `java` folder.

### Step1: Download certificates
On *nix systems use `download-certificates.sh` to automatically set up your certificates for the Sandbox environment. Alternatively you can manually download the required certificates as described below.
#### Premium APIs
At `developer.ing.com/openbanking/get-started/premium` (renaming `.cer` to `.pem`) (or [GitHub](https://github.com/ing-bank/ing-open-banking-cli/tree/master/apps/sandbox/certificates)) download the certificates and keys and place them in the `./certs/` directory:
```
./certs/example_client_signing.pem
./certs/example_client_signing.key
./certs/example_client_tls.pem
./certs/example_client_tls.key
``` 
#### PSD2 APIs
At `https://developer.ing.com/openbanking/get-started/psd2` (renaming `.cer` to `.pem`) (or [GitHub](https://github.com/ing-bank/ing-open-banking-cli/tree/master/apps/sandbox/certificates/psd2/)) download the certificates and keys and place them in the `./certs/psd2` directory:
```
./certs/psd2/example_client_signing.pem
./certs/psd2/example_client_signing.key
./certs/psd2/example_client_tls.pem
./certs/psd2/example_client_tls.key
```

These certificates are used to generate the required keystore files required to call the sandbox api's.
### Step2: Create keystores
#### Premium APIs
Run `keygen-premium.sh` and provide a password (use `secret` for quick test; see below). This password is used to access the keystore that will be generated. The keystore can be found in `./java/open-banking-demo-app/src/main/resources/keystore-premium.jks`.
#### PSD2 APIs
Run `keygen-psd2.sh` and provide a password (use `secret2` for quick test; see below). This password is used to access the keystore that will be generated. The keystore can be found in `./java/open-banking-demo-app/src/main/resources/keystore-psd2.jks`.

### Step3: Test the provided app
#### Configuration
Configure the `open-banking-demo-app` module for a test run. If you used `secret` and `secret2` for openbanking and psd2 keystores respectively you are all set! If not you can configure the passwords used in `./java/open-banking-demo-app/src/main/resources/application.properties`. You can find other settings there as well. The open-banking-demo-app is just a showcase of how to use the SDK.
##### Proxy settings
In the application properties you can configure proxy settings as well.
#### Start testing
Run `mvn clean install` to test the SDK. Alternatively run `mvn clean install -Dproxy.use` to enable your proxy settings when the default setting in properties is `false`. 


## Examples using the ING Open Banking Drivers
This section contains example Java code snippets to assist in the implementation the ING Open Banking Drivers.

Make sure to have downloaded or created the certificates as mentioned above.

Run `mvn clean install` to build the drivers, and then add the following dependencies to the pom.xml file of your java app.

```xml
<dependency>
    <groupId>com.ing.developer</groupId>
    <artifactId>open-banking-common</artifactId>
    <version>${open-banking-sdk.version}</version>
</dependency>
<dependency>
    <groupId>com.ing.developer</groupId>
    <artifactId>open-banking-account-information-driver</artifactId>
    <version>${open-banking-sdk.version}</version>
</dependency>
<dependency>
    <groupId>com.ing.developer</groupId>
    <artifactId>open-banking-payment-initiation-driver</artifactId>
    <version>${open-banking-sdk.version}</version>
</dependency>
<dependency>
    <groupId>com.ing.developer</groupId>
    <artifactId>open-banking-payment-request-driver</artifactId>
    <version>${open-banking-sdk.version}</version>
</dependency>
<dependency>
    <groupId>com.ing.developer</groupId>
    <artifactId>open-banking-showcase-driver</artifactId>
    <version>${open-banking-sdk.version}</version>
</dependency>
```

### Using ING Open Banking Drivers for PSD2 API's
#### Request an authorization URL

The OpenBankingOAuthApi driver is used to request an authorization URL.
The 1st step is to import the required classes.

```java
import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import com.ing.developer.common.clients.OpenBankingOAuthApi;
import javax.ws.rs.client.Client;
```

The OpenBankingOAuthApi requires a keyId, trustMaterial and Client.
In the example below Sandbox specific parameters are used.
The used certificates are generated by the scripts mentioned above.
The code below will initialize the OpenBankingOAuthApi object that we will use to call the API.

```java
String keyId = "SN=5E4299BE";
String keyStoreFileName = "keystore-psd2.jks";
char[] keyStorePassword = "secret2".toCharArray();

Utils.Pair<Certificate, PrivateKey> trustMaterial = Companion.Utils.getTrustMaterial(keyStoreFileName, keyStorePassword);
Client openBankingClient = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, false, false).getSecond().build();

OpenBankingOAuthApi openBankingOAuthApi = new OpenBankingOAuthApi(keyId, trustMaterial, openBankingClient);
```

Once the OpenBankingOAuthApi is initialized an authorization URL can be requested by calling the code below.
Scope and redirectUri are mandatory fields. For more information please visit the [psd2 get-gestarted page](https://developer.ing.com/openbanking/get-started/psd2#step-3-request-an-authorization-code-and-the-customer-access-token).

```java
String redirectUri = "https://www.example.com";
String countryCode = "NL";
String scope = "payment-accounts%3Abalances%3Aview%20payment-accounts%3Atransactions%3Aview";

String consentUri = openBankingOAuthApi.getConsentUri(redirectUri, scope, countryCode);
```
The getConsentUri command will request a token from the OAuth api.
After a successful token response, the driver will request an authorization URL from the OAuth api. 
A successful request will return an URL similar to the example below:
```
https://myaccount.sandbox.ing.com/granting/cf29c75e-ffc7-42aa-8e0c-ce782aa129e8/NL?client_id=5ca1ab1e-c0ca-c01a-cafe-154deadbea75&scope=payment-accounts%3Abalances%3Aview%20payment-accounts%3Atransactions%3Aview&redirect_uri=https://www.example.com
```

Full Example
```java
import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import com.ing.developer.common.clients.OpenBankingOAuthApi;

import javax.ws.rs.client.Client;
import java.security.PrivateKey;
import java.security.cert.Certificate;

public class psd2AuthUrl {
    public static void getAuthUrl() {
        String keyId = "SN=5E4299BE";
        String keyStoreFileName = "keystore-psd2.jks";
        char[] keyStorePassword = "secret2".toCharArray();

        Utils.Pair<Certificate, PrivateKey> trustMaterial = Companion.Utils.getTrustMaterial(keyStoreFileName, keyStorePassword);
        Client openBankingClient = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, false, false).getSecond().build();

        OpenBankingOAuthApi openBankingOAuthApi = new OpenBankingOAuthApi(keyId, trustMaterial, openBankingClient);

        String redirectUri = "https://www.example.com";
        String countryCode = "NL";
        String scope = "payment-accounts%3Abalances%3Aview%20payment-accounts%3Atransactions%3Aview";

        String consentUri = openBankingOAuthApi.getConsentUri(redirectUri, scope, countryCode);
        System.out.println("consentUri:" + consentUri);
    }
}
```

#### Request a customer token:

The OpenBankingOAuthApi driver is used to request a customer token.
The same configuration can be used when requesting the authorization URL.
An authorization code is needed when calling the customer token. For the sandbox environment we use the code below.
The getCustomerToken function will 

The getCustomerToken function will request a token from the OAuth api.
After a successful token response, the driver will request a customer token also from the OAuth api. and a successful request will return a customer token.

```java
String authorizationCode = "8b6cd77a-aa44-4527-ab08-a58d70cca286";

TokenResponse customerToken = openBankingOAuthApi.getCustomerToken(authorizationCode);
```

Full Example:
```java
import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import com.ing.developer.common.clients.OpenBankingOAuthApi;
import org.openapitools.client.model.TokenResponse;

import javax.ws.rs.client.Client;
import java.security.PrivateKey;
import java.security.cert.Certificate;

public class psd2CustomerToken {
    public static TokenResponse getCustomerToken() {
        String keyId = "SN=5E4299BE";
        String keyStoreFileName = "keystore-psd2.jks";
        char[] keyStorePassword = "secret2".toCharArray();

        Utils.Pair<Certificate, PrivateKey> trustMaterial = Companion.Utils.getTrustMaterial(keyStoreFileName, keyStorePassword);
        Client openBankingClient = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, false, false).getSecond().build();

        OpenBankingOAuthApi openBankingOAuthApi = new OpenBankingOAuthApi(keyId, trustMaterial, openBankingClient);

        String authorizationCode = "8b6cd77a-aa44-4527-ab08-a58d70cca286";

        TokenResponse customerToken = openBankingOAuthApi.getCustomerToken(authorizationCode);
        System.out.println("customerToken:" + customerToken.getAccessToken());
        return customerToken;
    }
}
```

#### Call the Account Information API:
The AccountDetailsApi Driver is used to fetch account information.
This call requires a customer token, and can me requested as seen above.
The classes required to fetch account information can be imported as seen below.

```java
import com.ing.developer.account.information.client.ApiClient;
import com.ing.developer.account.information.client.ApiException;
import com.ing.developer.account.information.client.api.AccountDetailsApi;
import com.ing.developer.account.information.client.model.Account;
```

The AccountDetailsApi requires an ApiClient object which has a similar setup as the Client used in the openBankingOAuthApi above.

```java
String keyId = "SN=5E4299BE";
char[] keyStorePassword = "secret2".toCharArray();
String keyStoreFileName = "keystore-psd2.jks";

Utils.Pair<PrivateKey, ClientBuilder> trustMaterial = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, true, false);
ApiClient apiClient = new ApiClient(keyId, trustMaterial.getFirst(), trustMaterial.getSecond());
AccountDetailsApi accountDetailsApi = new AccountDetailsApi(apiClient);
```

The v3AccountsGet is used to fetch account information.
It requires a clientId, that was retrieved using the authorization URL, and the customer token fetched in the step above section.

```java
String clientId = "5ca1ab1e-c0ca-c01a-cafe-154deadbea75";
List<Account> accountDetails = accountDetailsApi.v3AccountsGet(UUID.fromString(clientId), "Bearer " + customerToken.getAccessToken()).getAccounts();
```

This will respond with account information seen in the example below:
```json
[
  {
    "resourceId": "a217d676-7559-4f2a-83dc-5da0c2279223",
    "iban": "AT861921012345678912",
    "maskedPan": null,
    "name": "Hans Mustermann",
    "currency": "EUR",
    "product": "Girokonto",
    "links": {
      "balances": {
        "href": "/v3/accounts/a217d676-7559-4f2a-83dc-5da0c2279223/balances?currency=EUR"
      },
      "transactions": {
        "href": "/v2/accounts/a217d676-7559-4f2a-83dc-5da0c2279223/transactions?currency=EUR"
      }
    }
  },
  {
    "resourceId": "22ea6e67-b1db-48c6-b0fb-c8c3a6c4433c",
    "iban": "AT861921012345678913",
    "maskedPan": null,
    "name": "Laura Musterfrau",
    "currency": "EUR",
    "product": "Girokonto",
    "links": {
      "balances": {
        "href": "/v3/accounts/22ea6e67-b1db-48c6-b0fb-c8c3a6c4433c/balances?currency=EUR"
      },
      "transactions": {
        "href": "/v2/accounts/22ea6e67-b1db-48c6-b0fb-c8c3a6c4433c/transactions?currency=EUR"
      }
    }
  }
]
```

Full Example:
```java
import com.ing.developer.account.information.client.ApiClient;
import com.ing.developer.account.information.client.ApiException;
import com.ing.developer.account.information.client.api.AccountDetailsApi;
import com.ing.developer.account.information.client.model.Account;
import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import org.openapitools.client.model.TokenResponse;

import javax.ws.rs.client.ClientBuilder;
import java.security.PrivateKey;
import java.util.List;
import java.util.UUID;

public class psd2AccountInfo {
    public static void getAccountInfo() throws ApiException {
        TokenResponse customerToken = psd2CustomerToken.getCustomerToken();

        String keyId = "SN=5E4299BE";
        char[] keyStorePassword = "secret2".toCharArray();
        String keyStoreFileName = "keystore-psd2.jks";

        Utils.Pair<PrivateKey, ClientBuilder> trustMaterial = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, true, false);
        ApiClient apiClient = new ApiClient(keyId, trustMaterial.getFirst(), trustMaterial.getSecond());
        AccountDetailsApi accountDetailsApi = new AccountDetailsApi(apiClient);

        String clientId = "5ca1ab1e-c0ca-c01a-cafe-154deadbea75";
        List<Account> accountDetails = accountDetailsApi.v3AccountsGet(UUID.fromString(clientId), "Bearer " + customerToken.getAccessToken()).getAccounts();
        System.out.println("accountInfo:" + accountDetails);
    }
}

```
### Using ING Open Banking Drivers for Premium API's
#### Call Payment Request API:

The RegistrationApi is used to create the payment requests registration.
The classes required to create the request can be imported as seen below.

```java
import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import com.ing.developer.payment.request.client.ApiClient;
import com.ing.developer.payment.request.client.ApiException;
import com.ing.developer.payment.request.client.api.RegistrationApi;
import com.ing.developer.payment.request.client.model.DailyReceivableLimit;
import com.ing.developer.payment.request.client.model.RegistrationRequest;
```

The RegistrationApi requires an apiClient, which in turn requires a clientId and trustMaterial.
In the example below Sandbox specific parameters are used.
The used certificates are generated by the scripts mentioned above.
The code below will initialize the RegistrationApi object that we will use to call the API.

```java
String clientId = "e77d776b-90af-4684-bebc-521e5b2614dd";
String keyStoreFileName = "keystore-premium.jks";
char[] keyStorePassword = "secret".toCharArray();

Utils.Pair<PrivateKey, ClientBuilder> trustMaterial = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, false, false);
ApiClient apiClient = new ApiClient(clientId, trustMaterial.getFirst(), trustMaterial.getSecond());

RegistrationApi registrationApi = new RegistrationApi(apiClient);
```

Once the RegistrationApi is initialized a paymentRequestsRegistrations can be created.
It requires a clientID and a RegistrationRequest object with information regarding the registration.
The sandbox clientId is used, and the RegistrationRequest can be created as seen in the code snippet below.


```java
RegistrationRequest request = new RegistrationRequest();
DailyReceivableLimit dailyReceivableLimit = new DailyReceivableLimit();
dailyReceivableLimit.setValue(new BigDecimal(500000L));
request.merchantId("001234567");
request.merchantSubId("123456");
request.merchantName("Company BV");
request.merchantIBAN("NL26INGB0003275339");
request.dailyReceivableLimit(dailyReceivableLimit);
request.allowIngAppPayments("Y");

String response = registrationApi.paymentRequestsRegistrationsPost(clientId, null, request);
System.out.println("Premium response:" + response);
```

The paymentRequestsRegistrationsPost function will request a token from the OAuth api.
After a successful token response, the driver will send the payment registrations request to the Payment Api.
A successful request will return a valid certificate.

Full Example:
```java
import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import com.ing.developer.payment.request.client.ApiClient;
import com.ing.developer.payment.request.client.ApiException;
import com.ing.developer.payment.request.client.api.RegistrationApi;
import com.ing.developer.payment.request.client.model.DailyReceivableLimit;
import com.ing.developer.payment.request.client.model.RegistrationRequest;

import javax.ws.rs.client.ClientBuilder;
import java.math.BigDecimal;
import java.security.PrivateKey;

public class PremiumRegisterMerchant {
    public static void registerMerchant() throws ApiException {
        String clientId = "e77d776b-90af-4684-bebc-521e5b2614dd";
        String keyStoreFileName = "keystore-premium.jks";
        char[] keyStorePassword = "secret".toCharArray();

        Utils.Pair<PrivateKey, ClientBuilder> trustMaterial = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, false, false);
        ApiClient apiClient = new ApiClient(clientId, trustMaterial.getFirst(), trustMaterial.getSecond());

        RegistrationApi registrationApi = new RegistrationApi(apiClient);

        RegistrationRequest request = new RegistrationRequest();
        DailyReceivableLimit dailyReceivableLimit = new DailyReceivableLimit();
        dailyReceivableLimit.setValue(new BigDecimal(500000L));
        request.merchantId("001234567");
        request.merchantSubId("123456");
        request.merchantName("Company BV");
        request.merchantIBAN("NL26INGB0003275339");
        request.dailyReceivableLimit(dailyReceivableLimit);
        request.allowIngAppPayments("Y");

        String response = registrationApi.paymentRequestsRegistrationsPost(clientId, null, request);
        System.out.println("Premium registerMerchant response:" + response);
    }
}
```

#### Call Showcase API using signature:
The GreetingsApi is used to test a connection with a sandbox or production API's.
The classes required to create the request can be imported as seen below.

```java
import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import com.ing.developer.showcase.client.api.GreetingsApi;
```

The GreetingsApi requires an clientAPI, which in turn requires a clientId and trustMaterial.
In the example below Production parameters needs to be used.
Use your own clientId and certificates from the developer portal. Make sure your app is subscribed to the showcase API.  
Make sure to set the BASE_URL to https://api.ing.com, and use your keystore password.
The code below will initialize the GreetingsApi object that we will use to call the Showcase API.

```java
static String clientId = "e77d776b-90af-4684-bebc-521e5b2614dd";
static String keyStoreFileName = "keystore-premium.jks";
static char[] keyStorePassword = "secret".toCharArray();

Utils.Pair<PrivateKey, ClientBuilder> trustMaterial = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, false, false);
ApiClient clientAPI = new ApiClient(clientId, trustMaterial.getFirst(), trustMaterial.getSecond());
GreetingsApi greetingsApi = new GreetingsApi(clientAPI);
```

To call the production Showcase API the greetingsSingleGet function is used.
```java
String greeting = greetingsApi.greetingsSingleGet(null).getMessage();
```

A successful call will respond with:
```
Welcome to ING!
```

Full Example:
```java
import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import com.ing.developer.showcase.client.ApiException;
import com.ing.developer.showcase.client.api.GreetingsApi;

import javax.ws.rs.client.ClientBuilder;
import java.security.PrivateKey;

public class ProductionShowcaseAPI {
    public static void callShowcaseAPI() throws ApiException {
        static String clientId = "e77d776b-90af-4684-bebc-521e5b2614dd";
        static String keyStoreFileName = "keystore-premium.jks";
        static char[] keyStorePassword = "secret".toCharArray();

        Utils.Pair<PrivateKey, ClientBuilder> trustMaterial = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, false, false);
        ApiClient clientAPI = new ApiClient(clientId, trustMaterial.getFirst(), trustMaterial.getSecond());
        GreetingsApi greetingsApi = new GreetingsApi(clientAPI);
        String greeting = greetingsApi.greetingsSingleGet(null).getMessage();
        System.out.println(greeting);
    }
}
```

#### Call Showcase API using mTLS Pinning:
The Greeting endpoint can be called using mTLS Pinning. This does not require the signature headers. 

To set the request the use mTLS Pinning, call the setMTLSPinning as seen below. This will enable mTLS only connections.
```java
ApiClient clientAPI = new ApiClient(clientId, trustMaterial.getFirst(), trustMaterial.getSecond()).setMTLSPinning(true);
```

Use the mtlsOnlyGreetingsGet function to call the mtls only endpoint.
```java
String greeting = greetingsApi.mtlsOnlyGreetingsGet(null).getMessage();
```

A successful call will respond with:
```
Welcome to ING!
```

Full Example:
```java
import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import com.ing.developer.showcase.client.ApiException;
import com.ing.developer.showcase.client.api.GreetingsApi;

import javax.ws.rs.client.ClientBuilder;
import java.security.PrivateKey;

public class ProductionShowcaseAPI {
    public static void callShowcaseAPI() throws ApiException {
        static String clientId = "e77d776b-90af-4684-bebc-521e5b2614dd";
        static String keyStoreFileName = "keystore-premium.jks";
        static char[] keyStorePassword = "secret".toCharArray();

        Utils.Pair<PrivateKey, ClientBuilder> trustMaterial = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, false, false);
        ApiClient clientAPI = new ApiClient(clientId, trustMaterial.getFirst(), trustMaterial.getSecond()).setMTLSPinning(true);
        GreetingsApi greetingsApi = new GreetingsApi(clientAPI);
        String greeting = greetingsApi.mtlsOnlyGreetingsGet(null).getMessage();
        System.out.println(greeting);
    }
}
```

#### Call Showcase API using JWS:
The Greeting endpoint can be called using JWS.

To set the request the use JWS signing, call the setJwsSigning and setMTLSPinning as seen below. This will enable JWS with mTLS only connections. The Certificate will also need to be passed to the ApiClient object.
```java
Utils.Pair<Certificate, PrivateKey> trustMaterial = Companion.Utils.getTrustMaterial(keyStoreFileName, keyStorePassword);
Utils.Pair<PrivateKey, ClientBuilder> openBankingClient = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, false, false);
ApiClient clientAPI = new ApiClient(clientId, trustMaterial.getSecond(), openBankingClient.getSecond(), null, trustMaterial.getFirst()).setMTLSPinning(true).setJwsSigning(true);
```
Use the signedGreetingsGet function to call the mtls only endpoint.
```java
String greeting = greetingsApi.signedGreetingsGet(null).getMessage();
```

A successful call will respond with:
```
Welcome to ING!
```

Full Example:
```java
import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import com.ing.developer.showcase.client.ApiException;
import com.ing.developer.showcase.client.api.GreetingsApi;

import javax.ws.rs.client.ClientBuilder;
import java.security.PrivateKey;

public class ProductionShowcaseAPI {
    public static void callShowcaseAPI() throws ApiException {
        static String clientId = "e77d776b-90af-4684-bebc-521e5b2614dd";
        static String keyStoreFileName = "keystore-premium.jks";
        static char[] keyStorePassword = "secret".toCharArray();

        Utils.Pair<Certificate, PrivateKey> trustMaterial = Companion.Utils.getTrustMaterial(keyStoreFileName, keyStorePassword);
        Utils.Pair<PrivateKey, ClientBuilder> openBankingClient = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, false, false);
        ApiClient clientAPI = new ApiClient(clientId, trustMaterial.getSecond(), openBankingClient.getSecond(), null, trustMaterial.getFirst()).setMTLSPinning(true).setJwsSigning(true);
        GreetingsApi greetingsApi = new GreetingsApi(clientAPI);
        String greeting = greetingsApi.signedGreetingsGet(null,null).getMessage();
        System.out.println(greeting);
    }
}
```

### Using ING Open Banking Drivers for Production API's
#### Call Showcase API in production:

The GreetingsApi is used to test a connection with a production API.
The classes required to create the request can be imported as seen below.

```java
import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import com.ing.developer.showcase.client.api.GreetingsApi;
```

The GreetingsApi requires an clientAPI, which in turn requires a clientId and trustMaterial.
In the example below Production parameters needs to be used.
Use your own clientId and certificates from the developer portal. Make sure your app is subscribed to the showcase API.  
Make sure to set the BASE_URL to https://api.ing.com, and use your keystore password.
The code below will initialize the GreetingsApi object that we will use to call the Showcase API.

```java
String clientId = "3e19a706-511d-480c-ac3e-4ba135b154f4";
String keyStoreFileName = "keystore-premium.jks";
char[] keyStorePassword = "secret".toCharArray();

Utils.Pair<PrivateKey, ClientBuilder> trustMaterial = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, false, false);
ApiClient clientAPI = new ApiClient(clientId, trustMaterial.getFirst(), trustMaterial.getSecond());
GreetingsApi greetingsApi = new GreetingsApi(clientAPI);
```

To call the production Showcase API the greetingsSingleGet function is used.
```java
String greeting = greetingsApi.greetingsSingleGet(null).getMessage();
```

A successful call will respond with:
```
Welcome to ING!
```

Full Example:
```java
import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import com.ing.developer.showcase.client.ApiException;
import com.ing.developer.showcase.client.api.GreetingsApi;

import javax.ws.rs.client.ClientBuilder;
import java.security.PrivateKey;

public class ProductionShowcaseAPI {
    public static void callShowcaseAPI() throws ApiException {
        String clientId = "3e19a706-511d-480c-ac3e-4ba135b154f4";
        String keyStoreFileName = "keystore-premium.jks";
        char[] keyStorePassword = "secret".toCharArray();

        Utils.Pair<PrivateKey, ClientBuilder> trustMaterial = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, false, false);
        ApiClient clientAPI = new ApiClient(clientId, trustMaterial.getFirst(), trustMaterial.getSecond());
        GreetingsApi greetingsApi = new GreetingsApi(clientAPI);
        String greeting = greetingsApi.greetingsSingleGet(null).getMessage();
        System.out.println(greeting);
    }
}
```

## CLI
To test your connection to ING Open Banking APIs we also provide a [Command Line Interface (CLI)](https://github.com/ing-bank/ing-open-banking-cli) using scripts.

