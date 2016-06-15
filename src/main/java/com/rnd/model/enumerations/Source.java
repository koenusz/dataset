package com.rnd.model.enumerations;

/**
 * Represents the qualification of the actor that constituted that dataset.
 */
public enum Source {
    GOVERNMENT("Government"), ACADEMIC("Academic"), BUSINESS("Business"), NGO("Non Governmental Organization"), OTHER("Other");


    private String value;

    Source(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
