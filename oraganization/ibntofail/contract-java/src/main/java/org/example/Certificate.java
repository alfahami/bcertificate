/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.example;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.example.ledgerapi.State;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;
import org.json.JSONPropertyIgnore;

@DataType()
public class Certificate extends State {

    // Enumerate commercial paper state values
    public final static String ADDED = "ADDED";
    public final static String PENDING = "PENDING";
    public final static String CERTIFIED = "CERTIFIED";
   
    @Property()
    private String state="";

    public String getState() {
        return state;
    }

    public Certificate setState(String state) {
        this.state = state;
        return this;
    }

    @JSONPropertyIgnore()
    public boolean isAdded() {
        return this.state.equals(Certificate.ADDED);
    }

    @JSONPropertyIgnore()
    public boolean isPending() {
        return this.state.equals(Certificate.PENDING);
    }

    @JSONPropertyIgnore()
    public boolean isCertified() {
        return this.state.equals(Certificate.CERTIFIED);
    }

    public Certificate setAdded() {
        this.state = Certificate.ADDED;
        return this;
    }

    public Certificate setPending() {
        this.state = Certificate.PENDING;
        return this;
    }

    public Certificate setCertified() {
        this.state = Certificate.CERTIFIED;
        return this;
    }


    @Property()
    private String certificateNumber;

    @Property()
    private String studentName;

    @Property()
    private String birthDate;

    @Property()
    private String birthPlace;

    @Property()
    private String nationality;

    @Property()
    private String honor;

    @Property()
    private int graduationYear;

    public String getStudentName() {
        return studentName;
    }

    public Certificate setStudentName(String studentName) {
        this.studentName = studentName;
        return this;
    }

    public Certificate() {
        super();
    }

    public Certificate setKey() {
        this.key = State.makeKey(new String[] { this.certificateNumber });
        return this;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public Certificate setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
        return this;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public Certificate setBithDate(String birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public Certificate setBirthPlace(String birthPlaceaceOfBirth) {
        this.birthPlace = birthPlace;
     return this;
    }

    public Certificate setNationality(String nationality) {
        this.nationality = nationality;
        return this;
    }

    public String getNationality() {
        return nationality;
    }

    public Certificate setHonor(String honor) {
        this.honor = honor;
        return this;
    }

    public String getHonor() {
        return honor;
    }

    public Certificate setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear;
        return this;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    @Override
    public String toString() {
        return "Certificate::" + this.key + " Certificate Number" + this.getCertificateNumber() + " Student Name: " + getStudentName() + " Honor: " + getHonor() + " Current State: " + getState();
    }

    /**
     * Deserialize a state data to commercial paper
     *
     * @param {Buffer} data to form back into the object
     */
    public static Certificate deserialize(byte[] data) {
        JSONObject json = new JSONObject(new String(data, UTF_8));

        String studentName = json.getString("studentName");
        String certificateNumber = json.getString("certificateNumber");
        String birthDate = json.getString("birthDate");
        String birthPlace = json.getString("birthPlace");
        String nationality = json.getString("nationality");
        int graduationYear = json.getInt("graduationYear");
        String honor  = json.getString("honor");
        String state = json.getString("state");        
        return createInstance(studentName, certificateNumber, birthDate, birthPlace, nationality, honor, graduationYear ,state);
    }

    public static byte[] serialize(Certificate paper) {
        return State.serialize(paper);
    }

    /**
     * Factory method to create a commercial paper object
     */
    public static Certificate createInstance(String studentName, String certificateNumber, String birthDate, String birthPlace,
            String nationality, String honor, int graduationYear, String state) {
        return new Certificate().setStudentName(studentName).setCertificateNumber(certificateNumber).setBithDate(birthDate)
                .setBirthPlace(birthPlace).setKey().setNationality(nationality).setHonor(honor).setGraduationYear(graduationYear).setState(state);
    }


}
