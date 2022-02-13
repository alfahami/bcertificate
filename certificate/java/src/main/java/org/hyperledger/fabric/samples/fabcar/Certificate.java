/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples.fabcar;

import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class Certificate {

    @Property()
    private final String studentFullName;

    @Property()
    private final String apogee;

    @Property()
    private final String cin;

    @Property()
    private final String birthDate;

    @Property()
    private final String birthPlace;

    @Property()
    private final String title;

    @Property()
    private String honor;

    @Property()
    private String graduationDate;

    public String getStudentFullName() {
        return studentFullName;
    }

    public String getApogee() {
        return apogee;
    }

    public String getCin() {
        return cin;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public String getTitle() {
        return birthPlace;
    }

    public String getHonor() {
        return honor;
    }

    public String getGraduationDate() {
        return graduationDate;
    }

    public Certificate(@JsonProperty("studentFullName") final String studentFullName, @JsonProperty("apogee") final String apogee, @JsonProperty("cin") final String cin, @JsonProperty("birthDate") final String birthDate,
            @JsonProperty("birthPlace") final String birthPlace, @JsonProperty("title") final String title, @JsonProperty("honor") final String honor, @JsonProperty("gradutationDate") final String graduationDate) {

        this.studentFullName = studentFullName;
        this.apogee = apogee;
        this.cin = cin;
        this.birthDate = birthDate;
        this.birthPlace = birthPlace;
        this.title = title;
        this.honor = honor;
        this.graduationDate = graduationDate;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Certificate other = (Certificate) obj;

        return Objects.deepEquals(new String[] {getStudentFullName(), getApogee(), getCin(), getBirthDate(), getBirthPlace(), getTitle(), getHonor(), getGraduationDate()},
                new String[] {other.getStudentFullName(), other.getApogee(), other.getCin(), other.getBirthDate(), other.getBirthPlace(), other.getTitle(), other.getHonor(), other.getGraduationDate()});
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStudentFullName(), getApogee(), getCin(), getBirthDate(), getBirthPlace(), getTitle(), getHonor(), getGraduationDate());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [Name = " + studentFullName + ", cin = "
                + cin + ", birth = " + birthDate + ", honor = " + honor + ", title = " + title + "]";
    }
}
