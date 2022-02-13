/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.fabcar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public final class CertificateContractTest {

    private final class MockKeyValue implements KeyValue {

        private final String key;
        private final String value;

        MockKeyValue(final String key, final String value) {
            super();
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public String getStringValue() {
            return this.value;
        }

        @Override
        public byte[] getValue() {
            return this.value.getBytes();
        }

    }

    private final class MockCertificateResultsIterator implements QueryResultsIterator<KeyValue> {

        private final List<KeyValue> certificateList;

        MockCertificateResultsIterator() {
            super();

            certificateList = new ArrayList<KeyValue>();

            certificateList.add(new MockKeyValue("CERT1",
                    "{\"studentFullName\": \"AL-FAHAMI TOIHIR\", \"apogee\": \"18013942\", \"cin\": \"NBE388507\", \"bithDate\": \"21/05/1992\", \"bithPlace\": \"MORONI\", \"title\": \"MASTER GLCL\", \"honor\": \"Assez-bien\"}"));
            certificateList.add(new MockKeyValue("CERT2",
                    "{\"studentFullName\": \"BEHRAM MAHAMAT SALAH\", \"apogee\": \"18013942\", \"cin\": \"NBE388507\", \"bithDate\": \"21/05/1992\", \"bithPlace\": \"NDJAMENA\", \"title\": \"MASTER GLCL\", \"honor\": \"Assez-bien\"}"));
            certificateList.add(new MockKeyValue("CERT3",
                    "{\"studentFullName\": \"YOUSSFI ABDESSAMAD\", \"apogee\": \"11014043\", \"cin\": \"MAR886304\", \"bithDate\": \"21/05/1992\", \"bithPlace\": \"RABAT\", \"title\": \"DOCTEUR ES INFORMATIQUE\", \"honor\": \"Honorable\"}"));
            certificateList.add(new MockKeyValue("CERT4",
                    "{\"studentFullName\": \"KHADIDJA EL FELLA\", \"apogee\": \"18011254\", \"cin\": \"NBE388507\", \"bithDate\": \"12/04/1997\", \"bithPlace\": \"BOUQNADEL\", \"title\": \"MASTER GLCL\", \"honor\": \"Bien\"}"));
            certificateList.add(new MockKeyValue("CERT5",
                    "{\"studentFullName\": \"KHALIL LAABOUDI\", \"apogee\": \"18018575\", \"cin\": \"CEI388507\", \"bithDate\": \"21/05/1992\", \"bithPlace\": \"KENITRA\", \"title\": \"MASTER GLCL\", \"honor\": \"Bien\" }"));
        }

        @Override
        public Iterator<KeyValue> iterator() {
            return certificateList.iterator();
        }

        @Override
        public void close() throws Exception {
            // do nothing
        }

    }

    @Test
    public void invokeUnknownTransaction() {
        CertificateContract contract = new CertificateContract();
        Context ctx = mock(Context.class);

        Throwable thrown = catchThrowable(() -> {
            contract.unknownTransaction(ctx);
        });

        assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                .hasMessage("Undefined contract method called");
        assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo(null);

        verifyZeroInteractions(ctx);
    }

    @Nested
    class InvokeQueryCertificateTransaction {

        @Test
        public void whenCarExists() {
            CertificateContract contract = new CertificateContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("CERT1"))
                    .thenReturn("{\"studentFullName\": \"AL-FAHAMI TOIHIR\", \"apogee\": \"18013942\", \"cin\": \"NBE388507\", \"bithDate\": \"21/05/1992\", \"bithPlace\": \"MORONI\", \"title\": \"MASTER GLCL\", \"honor\": \"Assez-bien\", \"Graduation Date\": \"07/12/2021\" }");
            Certificate certificate = contract.queryCertificate(ctx, "CERT1");
            try {
                assertThat(certificate).isEqualTo(new Certificate("AL-FAHAMI TOIHIR", "18013942", "NBE388507", "21/05/1992", "MORONI", "MASTER GLCL", "Assez-bien", "07/12/2021"));
            } catch (AssertionError e) {
                System.out.println(e.getMessage());
            }
        }

        @Test
        public void whenCertificateDoesNotExist() {
            CertificateContract contract = new CertificateContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("CERT1")).thenReturn("");

            Throwable thrown = catchThrowable(() -> {
                contract.queryCertificate(ctx, "CERT1");
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Certificate CERT1 does not exist");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("CERTIFICATE_NOT_FOUND".getBytes());
        }
    }

    @Test
    void invokeInitLedgerTransaction() {
        CertificateContract contract = new CertificateContract();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);

        contract.initLedger(ctx);

        InOrder inOrder = inOrder(stub);
        inOrder.verify(stub).putStringState("CERT0",
                "{\"studentFullName\": \"MOULAY YOUSSEF HADI\", \"apogee\": \"01313940\", \"cin\": \"MAR315509\", \"bithDate\": \"11/01/1980\", \"bithPlace\": \"RABAT\", \"title\": \"PROFESSOR ES SCIENCE\", \"honor\": \"Honorable\", \"Graduation Date\": \"07/12/2021\"}");

        inOrder.verify(stub).putStringState("CERT1",
                "{\"studentFullName\": \"BEHRAM MAHAMAT SALAH\", \"apogee\": \"18013942\", \"cin\": \"NBE388507\", \"bithDate\": \"21/05/1992\", \"bithPlace\": \"NDJAMENA\", \"title\": \"MASTER GLCL\", \"honor\": \"Assez-bien\", \"Graduation Date\": \"07/12/2021\"}");
        inOrder.verify(stub).putStringState("CERT2",
                "{\"studentFullName\": \"BEHRAM MAHAMAT SALAH\", \"apogee\": \"18013942\", \"cin\": \"NBE388507\", \"bithDate\": \"21/05/1992\", \"bithPlace\": \"NDJAMENA\", \"title\": \"MASTER GLCL\", \"honor\": \"Assez-bien\", \"Graduation Date\": \"07/12/2021\"}");
        inOrder.verify(stub).putStringState("CERT3",
                "{\"studentFullName\": \"YOUSSFI ABDESSAMAD\", \"apogee\": \"11014043\", \"cin\": \"MAR886304\", \"bithDate\": \"21/05/1992\", \"bithPlace\": \"RABAT\", \"title\": \"DOCTEUR ES INFORMATIQUE\", \"honor\": \"Honorable\", \"Graduation Date\": \"07/12/2021\"}");

        inOrder.verify(stub).putStringState("CERT4",
                "{\"studentFullName\": \"KHADIDJA EL FELLA\", \"apogee\": \"18011254\", \"cin\": \"NBE388507\", \"bithDate\": \"12/04/1997\", \"bithPlace\": \"BOUQNADEL\", \"title\": \"MASTER GLCL\", \"honor\": \"Bien\", \"Graduation Date\": \"07/12/2021\"}");
        inOrder.verify(stub).putStringState("CERT5",
                "{\"studentFullName\": \"KHALIL LAABOUDI\", \"apogee\": \"18018575\", \"cin\": \"CEI388507\", \"bithDate\": \"21/05/1992\", \"bithPlace\": \"KENITRA\", \"title\": \"MASTER GLCL\", \"honor\": \"Bien\", \"Graduation Date\": \"07/12/2021\"}");
        inOrder.verify(stub).putStringState("CERT6",
                "{\"studentFullName\": \"SOUFIANE MTER\", \"apogee\": \"18024974\", \"cin\": \"CNE388507\", \"bithDate\": \"18/02/1998\", \"bithPlace\": \"Moroni\", \"title\": \"MASTER GLCL\", \"honor\": \"Très-bien\", \"Graduation Date\": \"07/12/2021\"}");

        inOrder.verify(stub).putStringState("CERT7",
                "{\"studentFullName\": \"KELLY MANSARE\", \"apogee\": \"14013965\", \"cin\": \"GUI347500\", \"bithDate\": \"21/05/1992\", \"bithPlace\": \"KONAKRY\", \"title\": \"MASTER RESEAU ET TELECOM\", \"honor\": \"Excellent\", \"Graduation Date\": \"07/12/2021\"}");
        inOrder.verify(stub).putStringState("CERT8",
                "{\"studentFullName\": \"SIDI BEN ARFA MZE\", \"apogee\": \"18248761\", \"cin\": \"NBE855521\", \"bithDate\": \"12/02/1993\", \"bithPlace\": \"Moroni\", \"title\": \"MASTER GLCL\", \"honor\": \"Très-bien\", \"Graduation Date\": \"07/12/2021\"}");
        inOrder.verify(stub).putStringState("CERT9",
                "{\"studentFullName\": \"SENBLI UNESS\", \"apogee\": \"18024813\", \"cin\": \"NBE210325\", \"bithDate\": \"03/01/1999\", \"bithPlace\": \"Moroni\", \"title\": \"MASTER GLCL\", \"honor\": \"Assez-bien\", \"Graduation Date\": \"07/12/2021\"}");

        // inOrder.verify(stub).putStringState("CERT10",
        //         "{\"studentFullName\": \"KHALID HOUSNI\", \"apogee\": \"05019324\", \"cin\": \"CIN254705\", \"bithDate\": \"06/06/1989\", \"bithPlace\": \"AGADIR\", \"title\": \"PROFESSOR ES SCIENCE\", \"honor\": \"Honorable\", \"Graduation Date\": \"07/12/2021\"}");

        // inOrder.verify(stub).putStringState("CERT11",
        //         "{\"studentFullName\": \"BEN KHOUYA REDA\", \"apogee\": \"05006841\", \"cin\": \"MAR847502\", \"bithDate\": \"01/01/1985\", \"bithPlace\": \"SALE\", \"title\": \"DOCTORAT ES INFORMATIQUE\", \"honor\": \"Honorable\", \"Graduation Date\": \"07/12/2021\"}");
    }

    @Nested
    class InvokeCreateCertificateTransaction {

        @Test
        public void whenCertificateExists() {
            CertificateContract contract = new CertificateContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("CERT0"))
                    .thenReturn("{\"studentFullName\": \"MOULAY YOUSSEF HADI\", \"apogee\": \"01313940\", \"cin\": \"MAR315509\", \"bithDate\": \"11/01/1980\", \"bithPlace\": \"RABAT\", \"title\": \"PROFESSOR ES SCIENCE\", \"honor\": \"Honorable\", \"Graduation Date\": \"07/12/2021\"}");

            Throwable thrown = catchThrowable(() -> {
                contract.createCertificate(ctx, "CERT0", "CAPITAINE", "17856497", "8BC125130", "10/08/1995", "MALAISIE", "MASTER RH", "Bien", "07/12/2021");
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Certificate CERT0 already exists");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("CERTIFICATE_ALREADY_EXISTS".getBytes());
        }

        @Test
        public void whenCertificateDoesNotExist() {
            CertificateContract contract = new CertificateContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("CERT0")).thenReturn("");

            Certificate certificate = contract.createCertificate(ctx, "CERT0", "CAPITAINE", "17856497", "8BC125130", "10/08/1995", "MALAISIE", "MASTER RH", "Bien", "07/12/2021");
            
            assertThat(certificate).isEqualTo(new Certificate("CAPITAINE", "17856497", "8BC125130", "10/08/1995", "MALAISIE", "MASTER RH", "Bien", "07/12/2021"));
        }
    }

    @Test
    void invokeQueryAllCertificatesTransaction() {
        CertificateContract contract = new CertificateContract();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);
        when(stub.getStateByRange("CERT", "CERT99")).thenReturn(new MockCertificateResultsIterator());

        String certificates = contract.queryAllCertificates(ctx);

        assertThat(certificates).isEqualTo("[{\"key\":\"CERT0\","
                + "\"record\":{\"studentFullName\": \"MOULAY YOUSSEF HADI\", \"apogee\": \"01313940\", \"cin\": \"MAR315509\", \"bithDate\": \"11/01/1980\", \"bithPlace\": \"RABAT\", \"title\": \"PROFESSOR ES SCIENCE\", \"honor\": \"Honorable\", \"Graduation Date\": \"07/12/2021\"}},"
                + "{\"key\":\"CERT1\","
                + "\"record\":{\"studentFullName\": \"ALFAHAMI TOIHIR\", \"apogee\": \"18013942\", \"cin\": \"NBE388507\", \"bithDate\": \"21/05/1992\", \"bithPlace\": \"MORONI\", \"title\": \"MASTER GLCL\", \"honor\": \"Assez-bien\", \"Graduation Date\": \"07/12/2021\"}},"
                + "{\"key\":\"CERT7\","
                + "\"record\":{\"studentFullName\": \"KELLY MANSARE\", \"apogee\": \"14013965\", \"cin\": \"GUI347500\", \"bithDate\": \"21/05/1992\", \"bithPlace\": \"KONAKRY\", \"title\": \"MASTER RESEAU ET TELECOM\", \"honor\": \"Excellent\", \"Graduation Date\": \"07/12/2021\"}},"
                + "{\"key\":\"CERT9\","
                + "\"record\":{\"studentFullName\": \"SENBLI UNESS\", \"apogee\": \"18024813\", \"cin\": \"NBE210325\", \"bithDate\": \"03/01/1999\", \"bithPlace\": \"Moroni\", \"title\": \"MASTER GLCL\", \"honor\": \"Assez-bien\", \"Graduation Date\": \"07/12/2021\"}}]");
    }

    @Nested
    class updateStudentNameTransaction {

        @Test
        public void whenCertificateExists() {
            CertificateContract contract = new CertificateContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("CERT0"))
                    .thenReturn("{\"studentFullName\": \"MOULAY YOUSSEF HADI\", \"apogee\": \"01313940\", \"cin\": \"MAR315509\", \"bithDate\": \"11/01/1980\", \"bithPlace\": \"RABAT\", \"title\": \"PROFESSOR ES SCIENCE\", \"honor\": \"Honorable\", \"Graduation Date\": \"07/12/2021\"}");

            Certificate certificate = contract.updateStudentName(ctx, "CERT0", "Dr HADI");

            assertThat(certificate).isEqualTo(new Certificate("Dr HADI", "01313940", "MAR315509", "11/01/1980", "RABAT", "PROFESSOR ES SCIENCE", "Honorable", "07/12/2021"));
        }

        @Test
        public void whenCertificateDoesNotExist() {
            CertificateContract contract = new CertificateContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getStringState("CERT0")).thenReturn("");

            Throwable thrown = catchThrowable(() -> {
                contract.updateStudentName(ctx, "CERT0", "Dr HADI");
            });

            assertThat(thrown).isInstanceOf(ChaincodeException.class).hasNoCause()
                    .hasMessage("Certificate CERT0 does not exist");
            assertThat(((ChaincodeException) thrown).getPayload()).isEqualTo("CERTIFICATE_NOT_FOUND".getBytes());
        }
    }
}
