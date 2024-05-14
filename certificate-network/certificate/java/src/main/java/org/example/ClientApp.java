/*
SPDX-License-Identifier: Apache-2.0
*/

package org.example;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;

public class ClientApp {

	static {
		System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
	}

	public static void main(String[] args) throws Exception {
		// Load a file system based wallet for managing identities.
		Path walletPath = Paths.get("wallet");
		Wallet wallet = Wallets.newFileSystemWallet(walletPath);
		// load a CCP
		Path networkConfigPath = Paths.get("..", "..", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");

		Gateway.Builder builder = Gateway.createBuilder();
		builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);

		// create a gateway connection
		try (Gateway gateway = builder.connect()) {

			// get the network and contract
			Network network = gateway.getNetwork("mychannel");
			Contract contract = network.getContract("certificate");

			byte[] result;

			result = contract.evaluateTransaction("queryAllCertificates");
			System.out.println(new String(result));

			contract.submitTransaction("createCertificate", "CERT11", "RHAZALI YASSINE", "21053648", "MAR388547", "07/09/1999", "RABAT", "DOCTORAT CP", "Honorable", "07/12/2021");

			result = contract.evaluateTransaction("queryCertificate", "CERT11");
			System.out.println(new String(result));

			contract.submitTransaction("updateStudentName", "CAR11", "MHOUMADI");

			result = contract.evaluateTransaction("queryCertificate", "CERT11");
			System.out.println(new String(result));
		}
	}

}
