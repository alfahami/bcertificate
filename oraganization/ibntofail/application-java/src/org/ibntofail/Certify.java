/*
SPDX-License-Identifier: Apache-2.0
*/

package org.ibntofail;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.GatewayException;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.bcertificate.Certificate;

import org.hyperledger.fabric.sdk.exception.TransactionEventException;
import org.hyperledger.fabric.sdk.BlockEvent;

import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.ProposalResponse;


public class Certify {

  private static final String ENVKEY="CONTRACT_NAME";

  public static void main(String[] args) {

    String contractName="certificatecontrac";
    // get the name of the contract, in case it is overridden
    Map<String,String> envvar = System.getenv();
    if (envvar.containsKey(ENVKEY)){
      contractName=envvar.get(ENVKEY);
    }

    Gateway.Builder builder = Gateway.createBuilder();

    try {
      // A wallet stores a collection of identities
      Path walletPath = Paths.get(".", "wallet");
      Wallet wallet = Wallets.newFileSystemWallet(walletPath);
      System.out.println("Read wallet info from: " + walletPath.toString());

      String userName = "admin@uit.ac.ma";

      Path connectionProfile = Paths.get("..",  "gateway", "connection-org1.yaml");

      // Set connection options on the gateway builder
      builder.identity(wallet, userName).networkConfig(connectionProfile).discovery(false);

      // Connect to gateway using application specified parameters
      try(Gateway gateway = builder.connect()) {

        // Access PaperNet network
        System.out.println("Use network channel: mychannel.");
        Network network = gateway.getNetwork("mychannel");

        // Get addressability to commercial paper contract
        System.out.println("Use org.bcertificate.certificate smart contract.");
        Contract contract = network.getContract(contractName, "org.bcertificate.certificate");

        // Issue commercial paper
        System.out.println("Submit commercial paper issue transaction.");
        byte[] response = contract.submitTransaction("certify", "ALFAHAMI", "00001", "21-05-1992", "Comoros", "Comorian", "Good", "2021");

        // Process response
        System.out.println("Process issue transaction response.");
        Certificate paper = Certificate.deserialize(response);
        System.out.println(paper);
      }
    } catch (GatewayException | IOException | TimeoutException | InterruptedException e) {
      e.printStackTrace();
      System.exit(-1);

      Throwable cause = e.getCause();
      if (cause instanceof TransactionEventException) {
      BlockEvent.TransactionEvent txEvent = ((TransactionEventException) cause).getTransactionEvent();
      byte validationCode = txEvent.getValidationCode();
      }
    }
  }

}