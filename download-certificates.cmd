@echo off

REM Create 'certs' directory (if not exists)
mkdir certs
cd certs || exit /b

REM Download the example_client_tls.pem and example_client_signing.pem files
curl -O https://raw.githubusercontent.com/ing-bank/ing-open-banking-cli/master/apps/sandbox/certificates/example_client_tls.pem
curl -O https://raw.githubusercontent.com/ing-bank/ing-open-banking-cli/master/apps/sandbox/certificates/example_client_signing.pem
curl -O https://raw.githubusercontent.com/ing-bank/ing-open-banking-cli/master/apps/sandbox/certificates/example_client_tls.key
curl -O https://raw.githubusercontent.com/ing-bank/ing-open-banking-cli/master/apps/sandbox/certificates/example_client_signing.key

REM Create the 'psd2' directory (if not exists)
mkdir psd2
cd psd2 || exit /b

REM Download the PSD2 example_client_tls.pem and example_client_signing.pem files
curl -O https://raw.githubusercontent.com/ing-bank/ing-open-banking-cli/master/apps/sandbox/certificates/psd2/example_client_tls.pem
curl -O https://raw.githubusercontent.com/ing-bank/ing-open-banking-cli/master/apps/sandbox/certificates/psd2/example_client_signing.pem
curl -O https://raw.githubusercontent.com/ing-bank/ing-open-banking-cli/master/apps/sandbox/certificates/psd2/example_client_tls.key
curl -O https://raw.githubusercontent.com/ing-bank/ing-open-banking-cli/master/apps/sandbox/certificates/psd2/example_client_signing.key
