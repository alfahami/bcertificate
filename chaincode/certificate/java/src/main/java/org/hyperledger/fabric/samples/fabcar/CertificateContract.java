/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.fabcar;

import java.util.ArrayList;
import java.util.List;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.owlike.genson.Genson;

/**
 * Java implementation of the Certificate Contract
*/
@Contract(
        name = "CertificateContract",
        info = @Info(
                title = "Certificate contract",
                description = "Ibn Tofail Certificate Contract",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "altoihir@gmail.com/behram.mahamat@gmail.com",
                        name = "ALFAHAMI & BEHRAM",
                        url = "")))
@Default
public final class CertificateContract implements ContractInterface {

    private final Genson genson = new Genson();

    private enum CertificateContractErrors {
        CERTIFICATE_NOT_FOUND,
        CERTIFICATE_ALREADY_EXISTS
    }

    /**
     * Retrieves a certificate with the specified key from the ledger.
     *
     * @param ctx the transaction context
     * @param key the key
     * @return the certificate found on the ledger if there was one
     */
    @Transaction()
    public Certificate queryCertificate(final Context ctx, final String key) {
        ChaincodeStub stub = ctx.getStub();
        String certificateState = stub.getStringState(key);

        if (certificateState.isEmpty()) {
            String errorMessage = String.format("Certificate %s does not exist", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, CertificateContractErrors.CERTIFICATE_NOT_FOUND.toString());
        }

        Certificate certificate = genson.deserialize(certificateState, Certificate.class);

        return certificate;
    }

    /**
     * Creates some initial Certificate on the ledger.
     *
     * @param ctx the transaction context
     */
    @Transaction()
    public void initLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        String[] certificateData = {
                "{\"studentFullName\": \"MOULAY YOUSSEF HADI\", \"apogee\": \"01313940\", \"cin\": \"MAR315509\", \"bithDate\": \"11/01/1980\", \"bithPlace\": \"RABAT\", \"title\": \"PROFESSOR ES SCIENCE\", \"honor\": \"Honorable\", \"Graduation Date\": \"07/12/2021\"}",

                "{\"studentFullName\": \"ALFAHAMI TOIHIR\", \"apogee\": \"18013942\", \"cin\": \"NBE388507\", \"bithDate\": \"21/05/1992\", \"bithPlace\": \"MORONI\", \"title\": \"MASTER GLCL\", \"honor\": \"Assez-bien\", \"Graduation Date\": \"07/12/2021\"}",
                "{\"studentFullName\": \"BEHRAM MAHAMAT SALAH\", \"apogee\": \"18013942\", \"cin\": \"NBE388507\", \"bithDate\": \"21/05/1992\", \"bithPlace\": \"NDJAMENA\", \"title\": \"MASTER GLCL\", \"honor\": \"Assez-bien\", \"Graduation Date\": \"07/12/2021\"}",
                "{\"studentFullName\": \"YOUSSFI ABDESSAMAD\", \"apogee\": \"11014043\", \"cin\": \"MAR886304\", \"bithDate\": \"21/05/1992\", \"bithPlace\": \"RABAT\", \"title\": \"DOCTEUR ES INFORMATIQUE\", \"honor\": \"Honorable\", \"Graduation Date\": \"07/12/2021\"}",
                "{\"studentFullName\": \"KHADIDJA EL FELLA\", \"apogee\": \"18011254\", \"cin\": \"NBE388507\", \"bithDate\": \"12/04/1997\", \"bithPlace\": \"BOUQNADEL\", \"title\": \"MASTER GLCL\", \"honor\": \"Bien\", \"Graduation Date\": \"07/12/2021\"}",
                "{\"studentFullName\": \"KHALIL LAABOUDI\", \"apogee\": \"18018575\", \"cin\": \"CEI388507\", \"bithDate\": \"21/05/1992\", \"bithPlace\": \"KENITRA\", \"title\": \"MASTER GLCL\", \"honor\": \"Bien\", \"Graduation Date\": \"07/12/2021\"}",
                "{\"studentFullName\": \"SOUFIANE MTER\", \"apogee\": \"18024974\", \"cin\": \"CNE388507\", \"bithDate\": \"18/02/1998\", \"bithPlace\": \"Moroni\", \"title\": \"MASTER GLCL\", \"honor\": \"Très-bien\", \"Graduation Date\": \"07/12/2021\"}",
                "{\"studentFullName\": \"KELLY MANSARE\", \"apogee\": \"14013965\", \"cin\": \"GUI347500\", \"bithDate\": \"21/05/1992\", \"bithPlace\": \"KONAKRY\", \"title\": \"MASTER RESEAU ET TELECOM\", \"honor\": \"Excellent\", \"Graduation Date\": \"07/12/2021\"}",
                "{\"studentFullName\": \"SIDI BEN ARFA MZE\", \"apogee\": \"18248761\", \"cin\": \"NBE855521\", \"bithDate\": \"12/02/1993\", \"bithPlace\": \"Moroni\", \"title\": \"MASTER GLCL\", \"honor\": \"Très-bien\", \"Graduation Date\": \"07/12/2021\"}",
                "{\"studentFullName\": \"SENBLI UNESS\", \"apogee\": \"18024813\", \"cin\": \"NBE210325\", \"bithDate\": \"03/01/1999\", \"bithPlace\": \"Moroni\", \"title\": \"MASTER GLCL\", \"honor\": \"Assez-bien\", \"Graduation Date\": \"07/12/2021\"}"
                // "{\"studentFullName\": \"KHALID HOUSNI\", \"apogee\": \"05019324\", \"cin\": \"CIN254705\", \"bithDate\": \"06/06/1989\", \"bithPlace\": \"AGADIR\", \"title\": \"PROFESSOR ES SCIENCE\", \"honor\": \"Honorable\", \"Graduation Date\": \"07/12/2021\"}",
                // "{\"studentFullName\": \"BEN KHOUYA REDA\", \"apogee\": \"05006841\", \"cin\": \"MAR847502\", \"bithDate\": \"01/01/1985\", \"bithPlace\": \"SALE\", \"title\": \"DOCTORAT ES INFORMATIQUE\", \"honor\": \"Honorable\", \"Graduation Date\": \"07/12/2021\"}"
        };

        for (int i = 0; i < certificateData.length; i++) {
            String key = String.format("CERT%d", i);

            Certificate certificate = genson.deserialize(certificateData[i], Certificate.class);
            String certificateState = genson.serialize(certificate);
            stub.putStringState(key, certificateState);
        }
    }

    /**
     * Creates a new certificate on the ledger.
     *
     * @param ctx : the transaction context
     * @param key : the key for the new certificate
     * @param studentFullName : student full name
     * @param apogee : the university student's id
     * @param cin : the student's international code
     * @param birthDate : student's birth date
     * @param birthPlace : student's birth place
     * @param title : certificate title
     * @param honor : student honor
     * @param graduationDate : graduation's year
     * @return the created Certificate
     */
    @Transaction()
    public Certificate createCertificate(final Context ctx, final String key, final String studentFullName, final String apogee,
            final String cin, final String birthDate, final String birthPlace, final String title, final String honor, final String graduationDate) {
        ChaincodeStub stub = ctx.getStub();

        String certificateState = stub.getStringState(key);
        if (!certificateState.isEmpty()) {
            String errorMessage = String.format("Certificate %s already exists", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, CertificateContractErrors.CERTIFICATE_ALREADY_EXISTS.toString());
        }

        Certificate certificate = new Certificate(studentFullName, apogee, cin, birthDate, birthPlace, title, honor, graduationDate);
        certificateState = genson.serialize(certificate);
        stub.putStringState(key, certificateState);

        return certificate;
    }

    /**
     * Retrieves all certificates from the ledger.
     *
     * @param ctx the transaction context
     * @return array of Certificates found on the ledger
     */
    @Transaction()
    public String queryAllCertificates(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        final String startKey = "CERT1";
        final String endKey = "CERT99";
        List<CertificateQueryResult> queryResults = new ArrayList<CertificateQueryResult>();

        QueryResultsIterator<KeyValue> results = stub.getStateByRange(startKey, endKey);

        for (KeyValue result: results) {
            Certificate certificate = genson.deserialize(result.getStringValue(), Certificate.class);
            queryResults.add(new CertificateQueryResult(result.getKey(), certificate));
        }

        final String response = genson.serialize(queryResults);

        return response;
    }

    /**
     * Changes the certificate infos on the ledger.
     *
     * @param ctx the transaction context
     * @param key the key
     * @param newOwner the new owner
     * @return the updated Certificate
     */
    @Transaction()
    public Certificate updateStudentName(final Context ctx, final String key, final String newStudentFullName) {
        ChaincodeStub stub = ctx.getStub();

        String certificateState = stub.getStringState(key);

        if (certificateState.isEmpty()) {
            String errorMessage = String.format("Certificate %s does not exist", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, CertificateContractErrors.CERTIFICATE_NOT_FOUND.toString());
        }

        Certificate certificate = genson.deserialize(certificateState, Certificate.class);

        Certificate newCertificate = new Certificate(newStudentFullName, certificate.getApogee(), certificate.getCin(), certificate.getBirthDate(), certificate.getBirthPlace(), certificate.getTitle(), certificate.getHonor(), certificate.getGraduationDate());

        String newCertificateState = genson.serialize(newCertificate);
        stub.putStringState(key, newCertificateState);

        return newCertificate;
    }
}
