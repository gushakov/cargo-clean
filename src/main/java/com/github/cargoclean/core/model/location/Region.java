package com.github.cargoclean.core.model.location;

public enum Region {
    Africa("Africa"),
    Asia("Asia"),
    Oceania("Oceania"),
    Europe("Europe"),
    NorthAmerica("North America"),
    SouthAmerica("South America");

    private final String region;

    Region(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return region;
    }
}
