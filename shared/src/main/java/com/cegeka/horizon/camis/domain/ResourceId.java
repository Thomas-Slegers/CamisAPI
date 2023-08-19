package com.cegeka.horizon.camis.domain;

public record ResourceId(String value) {

    public ResourceId{
        if (! isValid(value)) throw new IllegalArgumentException("Invalid value for ResourceId : " + value);
    }

    private boolean isValid(String value) {
        return value.matches("I?\\d{6}") || value.matches("\\d{7}");
    }

    public boolean isExternal(){
        return value.startsWith("I");
    }
}
