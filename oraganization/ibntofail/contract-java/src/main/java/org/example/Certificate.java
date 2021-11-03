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

    @Property()
    private int currentState = 0;

    public String getState() {
        return state;
    }

    public int getCurrentState() {
        return this.currentState;
    }

    public Certificate setCurrentState(int currentState) {
        this.currentState = currentState;
        return this;
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
        this.setCurrentState(1);
        this.state = Certificate.ADDED;
        return this;
    }

    public Certificate setPending() {
        this.setCurrentState(2);
        this.state = Certificate.PENDING;
        return this;
    }

    public Certificate setCertified() {
        this.setCurrentState(2);
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
    private String placeOfBirth;

    @Property()
    private String nationality;

    @Property()
    private String honor;

    @Property()
    private String yearOfObtention;

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

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public Certificate setPlaceOfBirth(String placeOfBirthaceOfBirth) {
        this.placeOfBirth = placeOfBirth;
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

    public Certificate yearOfObtention(String yearOfObtention) {
        this.yearOfObtention = yearOfObtention;
        return this;
    }

    public String getYearOfObtention() {
        return yearOfObtention;
    }

    @Override
    public String toString() {
        return "Certificate::" + this.key + " Certificate Number" + this.getCertificateNumber() + " Student Name: " + getStudentName() + " Honor: " + getHonor() + " Current State: " + getCurrentState();
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
        String placeOfBirth = json.getString("placeOfBirth");
        String nationality = json.getString("nationality");
        String yearOfObtention = json.getString("yearOfObtention");
        String honor  = json.getString("honor");
        String state = json.getString("state");        
        return createInstance(studentName, certificateNumber, birthDate, placeOfBirth, nationality, honor, yearOfObtention ,state);
    }

    public static byte[] serialize(Certificate paper) {
        return State.serialize(paper);
    }

    /**
     * Factory method to create a commercial paper object
     */
    public static Certificate createInstance(String studentName, String certificateNumber, String birthDate, String placeOfBirth,
            String nationality, String honor, String yearOfObtention, String state) {
        return new Certificate().setStudentName(studentName).setCertificateNumber(certificateNumber).setBithDate(birthDate)
                .setPlaceOfBirth(placeOfBirth).setKey().setNationality(nationality).setHonor(honor).setState(state);
    }


}
