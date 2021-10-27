# ING Open Banking SDK
With this SDK we provide the means to generate drivers to interact with ING's Open Banking APIs. It includes a custom generator that creates drivers which encapsulate HTTP signature signing and OAuth application token flows. As shown by the demo-app these drivers significantly reduce complexity to interact with ING's Open Banking APIs. Since the drivers are generated from OpenAPI Specification files, updating is easy by regenerating the drivers based on updated files.

## Features:
- [x] Open Banking Driver Generator (OpenApi Generator extension)
### Features (Java):
- [x] Open Banking Driver Generator Java templates
- [x] Example of generated drivers based on Swagger API definition targeting the ING Sandbox
- [x] Demo App for educational purposes
- [x] Helper classes for client configuration and http signature signing
- [x] Wrapper class for OAuth 2.0 technical driver (using OpenAPI Generator)
- [ ] Test generated drivers for feature completeness on Open Banking APIs (including PSD2)
- [ ] Publicly available drivers
### Features (Other languages):
- [ ] ...

## CLI
To test your connection to ING Open Banking APIs we also provide a [Command Line Interface (CLI)](https://github.com/ing-bank/ing-open-banking-cli) using scripts.

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
* Go to `http://localhost:8080/account/authorize`
* Click the link you get back and is displayed.
* Select the top profile and click next.
* You are redirected back to `localhost` and see the customer token displayed.
* Click the link to view the test profile accounts.

## Slightly Slower Start
### Folder structure
In this repository you can find api descriptions in swagger json format in the `api` directory. In addition, you can find the `open-banking-driver-generator` folder containing the modified Java [Open API Generator](https://github.com/OpenAPITools/openapi-generator) to generate drivers. Moreover, you can find a demo app, modules to generate Open Banking Drivers using the modified generator, and some wrapper classes in the `java` folder.

### Download certificates
On *nix systems use `download-certificates.sh` to automatically set up your certificates for the Sandbox environment. Alternatively you can manually download the required certificates as described below.
#### Premium APIs
At `developer.ing.com/openbanking/get-started/premium` (renaming `.cer` to `.pem`) (or [GitHub](https://github.com/ing-bank/ing-open-banking-cli/tree/master/apps/sandbox/certificates)) download the certificates and keys and place them in the `./scripts/certs/` directory:
```
./certs/example_client_signing.pem
./certs/example_client_signing.key
./certs/example_client_tls.pem
./certs/example_client_tls.key
``` 
#### PSD2 APIs
At `https://developer.ing.com/openbanking/get-started/psd2` (renaming `.cer` to `.pem`) (or [GitHub](https://github.com/ing-bank/ing-open-banking-cli/tree/master/apps/sandbox/certificates/psd2/)) download the certificates and keys and place them in the `./scripts/certs/psd2` directory:
```
./certs/psd2/example_client_signing.pem
./certs/psd2/example_client_signing.key
./certs/psd2/example_client_tls.pem
./certs/psd2/example_client_tls.key
```

### Create keystores
#### Premium APIs
Run `keygen-premium.sh` and provide a password (use `secret` for quick test; see below). This password is used to access the keystore that will be generated. The keystore can be found in `./java/open-banking-demo-app/src/main/resources/keystore-premium.jks`.
#### PSD2 APIs
Run `keygen-psd2.sh` and provide a password (use `secret2` for quick test; see below). This password is used to access the keystore that will be generated. The keystore can be found in `./java/open-banking-demo-app/src/main/resources/keystore-psd2.jks`.

### Test the provided app
#### Configuration
Configure the `open-banking-demo-app` module for a test run. If you used `secret` and `secret2` for openbanking and psd2 keystores respectively you are all set! If not you can configure the passwords used in `./java/open-banking-demo-app/src/main/resources/application.properties`. You can find other settings there as well. The open-banking-demo-app is just a showcase of how to use the SDK.
##### Proxy settings
In the application properties you can configure proxy settings as well.
#### Start testing
Run `mvn clean install` to test the SDK. Alternatively run `mvn clean install -Dproxy.use` to enable your proxy settings when the default setting in properties is `false`. 
