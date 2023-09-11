package com.cons.reporteya.dto;

import com.cons.reporteya.entity.Contact;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class ContactDto {

    private String country;

    private String state;

    private String city;

    private String state_district;

    public Contact contactDtoToContact(){
        return Contact.builder()
                .city(city)
                .country(country)
                .state_district(state_district)
                .town(state)
                .build();
    }
}
