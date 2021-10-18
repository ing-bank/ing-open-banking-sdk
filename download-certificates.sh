#! /bin/bash

mkdir -p certs && cd certs || exit
curl --remote-name-all https://raw.githubusercontent.com/ing-bank/ing-open-banking-cli/master/apps/sandbox/certificates/example_client_{tls,signing}.{pem,key}

mkdir -p psd2 && cd psd2 || exit
curl --remote-name-all https://raw.githubusercontent.com/ing-bank/ing-open-banking-cli/master/apps/sandbox/certificates/psd2/example_client_{tls,signing}.{pem,key}
