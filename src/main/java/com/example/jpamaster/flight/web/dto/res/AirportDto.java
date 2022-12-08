package com.example.jpamaster.flight.web.dto.res;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AirportDto {
    private String nameEn;

    private String nameKr;

    private String IATACode;

    private String ICAOCode;

    private String location;

    private String countryEn;

    private String countryKr;

    private String cityEn;
}