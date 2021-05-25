#! /bin/bash

mkdir -p certs && cd certs || exit

curl --remote-name-all https://raw.githubusercontent.com/ing-bank/ing-open-banking-cli/master/apps/sandbox/certificates/example_{eidas_,}client_{tls,signing}.{pem,key}
