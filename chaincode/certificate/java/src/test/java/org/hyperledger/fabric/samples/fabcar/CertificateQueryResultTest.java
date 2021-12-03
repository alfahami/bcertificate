/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.fabcar;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public final class CertificateQueryResultTest {

    @Nested
    class Equality {

        @Test
        public void isReflexive() {
            CertificateQueryResult cqr = new CertificateQueryResult("CERT1", new Certificate("ALFAHAMI", "18013942", "NBE388507", "21/05/1992", "MORONI", "MASTER GLCL", "Assez-bien", "07/12/2021"));

            assertThat(cqr).isEqualTo(cqr);
        }

        @Test
        public void isSymmetric() {
            Certificate certificate = new Certificate("ALFAHAMI", "18013942", "NBE388507", "21/05/1992", "MORONI", "MASTER GLCL", "Assez-bien", "07/12/2021");
            CertificateQueryResult cqrA = new CertificateQueryResult("CERT1", certificate);
            CertificateQueryResult cqrB = new CertificateQueryResult("CERT1", certificate);

            assertThat(cqrA).isEqualTo(cqrB);
            assertThat(cqrB).isEqualTo(cqrA);
        }

        @Test
        public void isTransitive() {
            Certificate certificate = new Certificate("ALFAHAMI", "18013942", "NBE388507", "21/05/1992", "MORONI", "MASTER GLCL", "Assez-bien", "07/12/2021");
            CertificateQueryResult cqrA = new CertificateQueryResult("CERT1", certificate);
            CertificateQueryResult cqrB = new CertificateQueryResult("CERT1", certificate);
            CertificateQueryResult cqrC = new CertificateQueryResult("CERT1", certificate);

            assertThat(cqrA).isEqualTo(cqrB);
            assertThat(cqrB).isEqualTo(cqrC);
            assertThat(cqrA).isEqualTo(cqrC);
        }

        @Test
        public void handlesKeyInequality() {
            CertificateQueryResult cqrA = new CertificateQueryResult("CERT0", new Certificate("ALFAHAMI", "18013942", "NBE388507", "21/05/1992", "MORONI", "MASTER GLCL", "Assez-bien", "07/12/2021"));

            CertificateQueryResult cqrB = new CertificateQueryResult("CERT2", new Certificate("HADI", "21546897", "CIN578507", "12/04/1990", "RABAT", "PROFESSOR", "Honorable", "07/12/2021"));

            assertThat(cqrA).isNotEqualTo(cqrB);
        }

        @Test
        public void handlesRecordInequality() {
            CertificateQueryResult cqrA = new CertificateQueryResult("CERT1", new Certificate("ALFAHAMI", "18013942", "NBE388507", "21/05/1992", "MORONI", "MASTER GLCL", "Assez-bien", "07/12/2021"));

            CertificateQueryResult cqrB = new CertificateQueryResult("CERT2", new Certificate("HADI", "21546897", "CIN578507", "12/04/1990", "RABAT", "PROFESSOR", "Honorable", "07/12/2021"));

            assertThat(cqrA).isNotEqualTo(cqrB);
        }

        @Test
        public void handlesKeyRecordInequality() {
            CertificateQueryResult cqrA = new CertificateQueryResult("CERT1", new Certificate("ALFAHAMI", "18013942", "NBE388507", "21/05/1992", "MORONI", "MASTER GLCL", "Assez-bien", "07/12/2021"));
            CertificateQueryResult cqrB = new CertificateQueryResult("CERT2", new Certificate("HADI", "21546897", "CIN578507", "12/04/1990", "RABAT", "PROFESSOR", "Honorable", "07/12/2021"));

            assertThat(cqrA).isNotEqualTo(cqrB);
        }

        @Test
        public void handlesOtherObjects() {
            CertificateQueryResult cqrA = new CertificateQueryResult("CERT1", new Certificate("ALFAHAMI", "18013942", "NBE388507", "21/05/1992", "MORONI", "MASTER GLCL", "Assez-bien", "07/12/2021"));
            String cqrB = "not a certificate";

            assertThat(cqrA).isNotEqualTo(cqrB);
        }

        @Test
        public void handlesNull() {
            CertificateQueryResult cqr = new CertificateQueryResult("CERT1", new Certificate("ALFAHAMI", "18013942", "NBE388507", "21/05/1992", "MORONI", "MASTER GLCL", "Assez-bien", "07/12/2021"));

            assertThat(cqr).isNotEqualTo(null);
        }
    }

    @Test
    public void toStringIdentifiesCertificateQueryResult() {
        CertificateQueryResult cqr = new CertificateQueryResult("CERT1", new Certificate("ALFAHAMI", "18013942", "NBE388507", "21/05/1992", "MORONI", "MASTER GLCL", "Assez-bien", "07/12/2021"));

        assertThat(cqr.toString()).isEqualTo("CertificateQueryResult@65766eb3 [key=CERT1, "
                + "record=Certificate@61a77e4f [Name = ALFAHAMI, cin = NBE388507, birth = 21/05/1992, honor = Assez-bien, title = MASTER GLCL]");
    }
}
