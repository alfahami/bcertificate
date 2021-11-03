/*
SPDX-License-Identifier: Apache-2.0
*/

package org.example;

import org.example.ledgerapi.StateList;
import org.hyperledger.fabric.contract.Context;

public class CertificateList {

    private StateList stateList;

    public CertificateList(Context ctx) {
        this.stateList = StateList.getStateList(ctx, CertificateList.class.getSimpleName(), Certificate::deserialize);
    }

    public CertificateList addCertificate(Certificate certificate) {
        stateList.addState(certificate);
        return this;
    }

    public Certificate getCertificate(String certificateKey) {
        return (Certificate) this.stateList.getState(certificateKey);
    }

    public CertificateList updateCertificate(Certificate certificate) {
        this.stateList.updateState(certificate);
        return this;
    }
}
