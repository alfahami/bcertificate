/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.fabcar;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public final class CertificateTest {

    @Nested
    class Equality {

        @Test
        public void isReflexive() {
            Certificate certificate = new Certificate("ALFAHAMI TOIHIR", "18013942", "NBE388507", "21/05/1992", "MORONI", "MASTER GLCL", "Assez-bien", "07/12/2021");
            assertThat(certificate).isEqualTo(certificate);
        }

        @Test
        public void isSymmetric() {
            Certificate certA = new Certificate("ALFAHAMI", "18013942", "NBE388507", "21/05/1992", "MORONI", "MASTER GLCL", "Assez-bien", "07/12/2021");
            Certificate certB = new Certificate("ALFAHAMI", "18013942", "NBE388507", "21/05/1992", "MORONI", "MASTER GLCL", "Assez-bien", "07/12/2021");
            
            assertThat(certA).isEqualTo(certB);
            assertThat(certB).isEqualTo(certA);
        }

        @Test
        public void isTransitive() {

            Certificate cert1 = new Certificate("ALFAHAMI TOIHIR", "18013942", "NBE388507", "21/05/1992", "MORONI", "MASTER GLCL", "Assez-bien", "07/12/2021");

            Certificate cert2 = new Certificate("ALFAHAMI TOIHIR", "18013942", "NBE388507", "21/05/1992", "MORONI", "MASTER GLCL", "Assez-bien", "07/12/2021");
            
            Certificate cert3 = new Certificate("ALFAHAMI TOIHIR", "18013942", "NBE388507", "21/05/1992", "MORONI", "MASTER GLCL", "Assez-bien", "07/12/2021");

            assertThat(cert1).isEqualTo(cert2);
            assertThat(cert2).isEqualTo(cert3);
            assertThat(cert1).isEqualTo(cert3);
        }

        @Test
        public void handlesInequality() {
            Certificate cert1 = new Certificate("ALFAHAMI TOIHIR", "18013942", "NBE388507", "21/05/1992", "MORONI", "MASTER GLCL", "Assez-bien", "07/12/2021");

            Certificate cert2 = new Certificate("HADI", "21546897", "CIN578507", "12/04/1990", "RABAT", "PROFESSOR", "Honorable", "07/12/2021");

            assertThat(cert1).isNotEqualTo(cert2);
        }

        @Test
        public void handlesOtherObjects() {
            Certificate cert1 = new Certificate("ALFAHAMI TOIHIR", "18013942", "NBE388507", "21/05/1992", "MORONI", "MASTER GLCL", "Assez-bien", "07/12/2021");

            String cert2 = "not a certificate";

            assertThat(cert1).isNotEqualTo(cert2);
        }

        @Test
        public void handlesNull() {
            Certificate certificate = new Certificate("ALFAHAMI TOIHIR", "18013942", "NBE388507", "21/05/1992", "MORONI", "MASTER GLCL", "Assez-bien", "07/12/2021");

            assertThat(certificate).isNotEqualTo(null);
        }
    }

    @Test
    public void toStringIdentifiesCertificate() {
        Certificate cert = new Certificate("ALFAHAMI TOIHIR", "18013942", "NBE388507", "21/05/1992", "MORONI", "MASTER GLCL", "Assez-bien", "07/12/2021");

        assertThat(cert.toString()).isEqualTo("Certificate@365185bd [Name = ALFAHAMI TOIHIR, cin = NBE388507, birth = 21/05/1992, honor = Assez-bien, title = MASTER GLCL]");
    }
}
