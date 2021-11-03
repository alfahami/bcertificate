package org.example;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;

class CertificateContext extends Context {

    public CertificateContext(ChaincodeStub stub) {
        super(stub);
        this.certificateList = new CertificateList(this);
    }

    public CertificateList certificateList;

}