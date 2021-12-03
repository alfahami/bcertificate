/*
 * SPDX-License-Identifier: Apache-2.0
 */

'use strict';

const { Gateway, Wallets } = require('fabric-network');
const fs = require('fs');
const path = require('path');

async function main() {
    try {
        // load the network configuration
        const ccpPath = path.resolve(__dirname, '..', '..', 'test-network', 'organizations', 'peerOrganizations', 'org1.example.com', 'connection-org1.json');
        let ccp = JSON.parse(fs.readFileSync(ccpPath, 'utf8'));

        // Create a new file system based wallet for managing identities.
        const walletPath = path.join(process.cwd(), 'wallet');
        const wallet = await Wallets.newFileSystemWallet(walletPath);
        console.log(`Wallet path: ${walletPath}`);

        // Check to see if we've already enrolled the user.
        const identity = await wallet.get('appUser');
        if (!identity) {
            console.log('An identity for the user "appUser" does not exist in the wallet');
            console.log('Run the registerUser.js application before retrying');
            return;
        }

        // Create a new gateway for connecting to our peer node.
        const gateway = new Gateway();
        await gateway.connect(ccp, { wallet, identity: 'appUser', discovery: { enabled: true, asLocalhost: true } });

        // Get the network (channel) our contract is deployed to.
        const network = await gateway.getNetwork('mychannel');

        // Get the contract from the network.
        const contract = network.getContract('certificate');

        // Submit the specified transaction.
        // createCertificate transaction - requires 9 argument, ex: ('createCertificate', 'CERT12', 'ABDEL', '1857469', 'NBE388507', '05/08/1999', 'NADOR', 'MASTER RH', 'A. Bien', '07/07/2021')

        // changeStudentName transaction - requires 2 args , ex: ('changeCarOwner', 'CERT12', 'Dave')
         await contract.submitTransaction('createCertificate', 'CERT30', 'NELSON MANDELA', '1857469', 'NBE388507', '05/08/1999', 'SOUTH AFRICA', 'MASTER RH', 'A. Bien', '07/07/2021');
        console.log('Transaction has been submitted');

        // const response = await contract.submitTransaction('queryCertificate', 'CERT1');
        // console.log('Transaction has been submitted');
        // console.log(`Transaction has been evaluated, result is: ${response.toString()}`);

        // const response = await contract.submitTransaction('changeStudentName', 'CERT2', 'MACRON');
        // console.log('Transaction has been submitted');
        //console.log(`Transaction has been evaluated, result is: ${response.toString()}`);
        

        // Disconnect from the gateway.
        await gateway.disconnect();

    } catch (error) {
        console.error(`Failed to submit transaction: ${error}`);
        process.exit(1);
    }
}

main();
