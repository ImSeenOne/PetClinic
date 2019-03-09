package org.springframework.samples.petclinic.owner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZipCode {

    @JsonProperty("codigo_postal")
    private String zipCode;

    @JsonProperty("municipio")
    private String city;

    @JsonProperty("estado")
    private String state;

    public String getZipCode() {
        return zipCode;
    }

    public ZipCode setZipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public String getCity() {
        return city;
    }

    public ZipCode setCity(String city) {
        this.city = city;
        return this;
    }

    public String getState() {
        return state;
    }

    public ZipCode setState(String state) {
        this.state = state;
        return this;
    }

    @Override
    public String toString() {
        return "ZipCodeObject{" +
            "zipCode='" + zipCode + '\'' +
            ", city='" + city + '\'' +
            ", state='" + state + '\'' +
            '}';
    }
}
