#!/bin/bash
#
# SPDX-License-Identifier: Apache-2.0
#
# Script that adds org environment variables (couldn't make this work fine because 
# every pushd create a new instance of the terminal and already set environment variables get lost )
# 
# #Adding peer cli, peer and fabric config related path
# dir=${PWD%/*}/test-network 
# export PATH=$dir/../bin:$PATH
# export FABRIC_CFG_PATH=$dir/../config/

# # Environment variables for Org1

# export CORE_PEER_TLS_ENABLED=true
# export CORE_PEER_LOCALMSPID="Org1MSP"
# export CORE_PEER_TLS_ROOTCERT_FILE=$dir/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt
# export CORE_PEER_MSPCONFIGPATH=$dir/organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp
# export CORE_PEER_ADDRESS=localhost:7051

# Exit on first error
set -e

# don't rewrite paths for Windows Git Bash users
export MSYS_NO_PATHCONV=1
starttime=$(date +%s)
CC_SRC_LANGUAGE=${1:-"javascript"}
CC_SRC_LANGUAGE=`echo "$CC_SRC_LANGUAGE" | tr [:upper:] [:lower:]`

if [ "$CC_SRC_LANGUAGE" = "go" -o "$CC_SRC_LANGUAGE" = "golang" ] ; then
	CC_SRC_PATH="../chaincode/certificate/go/"
elif [ "$CC_SRC_LANGUAGE" = "javascript" ]; then
	CC_SRC_PATH="../chaincode/certificate/javascript/"
elif [ "$CC_SRC_LANGUAGE" = "java" ]; then
	CC_SRC_PATH="../chaincode/certificate/java"
elif [ "$CC_SRC_LANGUAGE" = "typescript" ]; then
	CC_SRC_PATH="../chaincode/certificate/typescript/"
else
	echo The chaincode language ${CC_SRC_LANGUAGE} is not supported by this script
	echo Supported chaincode languages are: go, java, javascript, and typescript
	exit 1
fi

# clean out any old identites in the wallets
rm -rf javascript/wallet/*
rm -rf java/wallet/*
rm -rf apiserver/wallet/*

# launch network; create channel and join peer to channel
pushd ../test-network
./network.sh down
./network.sh up createChannel -ca -s couchdb
./network.sh deployCC -ccn certificate -ccv 1 -cci initLedger -ccl ${CC_SRC_LANGUAGE} -ccp ${CC_SRC_PATH}
popd

. ../test-network/add_path_org1.sh

cat <<EOF

Total setup execution time : $(($(date +%s) - starttime)) secs ...

Next, use the BCertificate applications to interact with the deployed Certificate contract.
The Certificate applications are available in multiple programming languages.
Follow the instructions for the programming language of your choice:

JavaScript:

  Start by changing into the "javascript" directory:
    cd javascript

  Next, install all required packages:
    npm install

  Then run the following applications to enroll the admin user, and register a new user
  called appUser which will be used by the other applications to interact with the deployed
  Certificate contract:
    node enrollAdmin
    node registerUser

  You can run the invoke application as follows. By default, the invoke application will
  create a new certificate, but you can update the application to submit other transactions:
    node invoke

  You can run the query application as follows. By default, the query application will
  return all certificates, but you can update the application to evaluate other transactions:
    node query

TypeScript:

  Start by changing into the "typescript" directory:
    cd typescript

  Next, install all required packages:
    npm install

  Next, compile the TypeScript code into JavaScript:
    npm run build

  Then run the following applications to enroll the admin user, and register a new user
  called appUser which will be used by the other applications to interact with the deployed
  Certificate contract:
    node dist/enrollAdmin
    node dist/registerUser

  You can run the invoke application as follows. By default, the invoke application will
  create a new certificate, but you can update the application to submit other transactions:
    node dist/invoke

  You can run the query application as follows. By default, the query application will
  return all certificates, but you can update the application to evaluate other transactions:
    node dist/query

Java:

  Start by changing into the "java" directory:
    cd java

  Then, install dependencies and run the test using:
    mvn test

  The test will invoke the sample client app which perform the following:
    - Enroll admin and appUser and import them into the wallet (if they don't already exist there)
    - Submit a transaction to create a new certificate
    - Evaluate a transaction (query) to return details of this certificate
    - Submit a transaction to change the owner of this certificate
    - Evaluate a transaction (query) to return the updated details of this certificate

Go:

  Start by changing into the "go" directory:
    cd go

  Then, install dependencies and run the test using:
    go run certificate.go

  The test will invoke the sample client app which perform the following:
    - Import user credentials into the wallet (if they don't already exist there)
    - Submit a transaction to create a new certificate
    - Evaluate a transaction (query) to return details of this certificate
    - Submit a transaction to change the owner of this certificate
    - Evaluate a transaction (query) to return the updated details of this certificate

EOF