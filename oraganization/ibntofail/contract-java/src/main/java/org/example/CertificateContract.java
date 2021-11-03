/*
SPDX-License-Identifier: Apache-2.0
*/
package org.example;

import java.util.logging.Logger;

import org.example.ledgerapi.State;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeStub;

/**
 * A custom context provides easy access to list of all certificates
 */

/**
 * Define certificate smart contract by extending Fabric Contract class
 *
 */
@Contract(name = "org.bcertificate.certificate", info = @Info(title = "MyAsset contract", description = "", version = "0.0.1", license = @License(name = "SPDX-License-Identifier: Apache-2.0", url = ""), contact = @Contact(email = "toihir.alfahamit@uit.ca.ma", name = "java-contract", url = "http://altoihir@gmai.com")))
@Default
public class CertificateContract implements ContractInterface {

    // use the classname for the logger, this way you can refactor
    private final static Logger LOG = Logger.getLogger(CertificateContract.class.getName());

    @Override
    public Context createContext(ChaincodeStub stub) {
        return new CertificateContext(stub);
    }

    public CertificateContract() {

    }

    /**
     * Instantiate to perform any setup of the ledger that might be required.
     *
     * @param {Context} ctx the transaction context
     */
    @Transaction
    public void instantiate(CertificateContext ctx) {
        // No implementation required with this example
        // It could be where data migration is performed, if necessary
        LOG.info("No data migration to perform");
    }

    /**
     * ADD certificate
     *
     * @param {Context} ctx the transaction context
     * @param {String} student's name
     * @param {String} certificate number for this student
     * @param {String} birht date
     * @param {String} nationality
     * @param {String} honor of the certificate
     * @param {String} year the certificate is delivered
     */
    @Transaction
    public Certificate add(CertificateContext ctx, String studentName, String certificateNumber, String birthDate,
            String placeOfBirth, String nationality, String honor, String yearOfObtention, String state) {

        System.out.println(ctx);

        // create an instance of the certificate
        Certificate certificate = Certificate.createInstance(studentName, certificateNumber, birthDate, placeOfBirth,
                nationality,honor, yearOfObtention, "");

        // Smart contract, rather than certificate bean, moves the certificate into ADDED state
        certificate.setAdded();

        /*
        TO BE USED - if the blockchain is used by many univ

        // Newly added certificate is owned by the university
        certificate.setOwner(univ);

        */

        System.out.println(certificate);
        // Add the paper to the list of all similar commercial papers in the ledger
        // world state
        ctx.certificateList.addCertificate(certificate);

        // Must return a serialized paper to caller of smart contract
        return certificate;
    }

    /**
     * Certify the added certificate
     *
     * @param {Context} ctx the transaction context
     * @param {String} student's name
     * @param {String} certificate number for this student
     * @param {String} birht date
     * @param {String} nationality
     * @param {String} honor of the certificate
     * @param {String} year the certificate is delivered
     */
    @Transaction
    public Certificate certify(CertificateContext ctx, String studentName, String certificateNumber, String birthDate,
            String placeOfBirth, String nationality, String honor, String yearOfObtention, String state)  {

        // Retrieve the current certificate using key fields provided
        String certificateKey = State.makeKey(new String[] { certificateNumber });
        Certificate certificate = ctx.certificateList.getCertificate(certificateKey);

        /*
        TO DO - When multiple univ are using the blockchain

        // Validate current owner
        if (!certificate.getOwner().equals(currentOwner)) {
            throw new RuntimeException("Certificate " + studentName + certificateNumber + " is not owned by " + currentOwner);
        }

        */

        // First buy moves state from Added to CERTIFIED
        if (certificate.isAdded()) {
            certificate.setPending();
        }

        // Check paper is not already CERTIFIED
        if (certificate.isPending()) {
            certificate.setCertified();
        } else {
            throw new RuntimeException(
                    "Certificate " + studentName + certificateNumber + " is not Pending. Current state = " + certificate.getState());
        }

        // Update the paper
        ctx.certificateList.updateCertificate(certificate);
        return certificate;
    }

}
