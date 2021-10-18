#!/bin/bash

SRC_DIR=certs/psd2/
TLS=example_client_tls
SIGN=example_client_signing
DESTINATION=java/open-banking-demo-app/src/main/resources/
FILENAME=keystore-psd2.jks

./keygen-generic.sh $SRC_DIR $TLS $SIGN $DESTINATION $FILENAME
