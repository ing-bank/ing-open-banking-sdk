#!/bin/bash

SRC_DIR=certs/
TLS=example_client_tls
SIGN=example_client_signing
DESTINATION=java/open-banking-demo-app/src/main/resources/
FILENAME=keystore-premium.jks
SIMPLE_DESTINATION=java/open-banking-simple-app/src/main/resources/

./keygen-generic.sh $SRC_DIR $TLS $SIGN $DESTINATION $FILENAME
cp "$DESTINATION$FILENAME" "$SIMPLE_DESTINATION$FILENAME"