package com.cons.reporteya.dto;

import com.cons.reporteya.entity.Contact;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Builder
public class ContactDto {

    private String number;

    private String country;

    private String postcode;

    private String location_name;

    public Contact contactDtoToContact(){
        return Contact.builder()
                .number(number)
                .country(country)
                .postcode(postcode)
                .location_name(location_name)
                .build();
    }

    public ContactDto contactToContactDto(Contact contact){
        return ContactDto.builder()
                .number(contact.getNumber())
                .country(contact.getCountry())
                .postcode(contact.getPostcode())
                .location_name(contact.getLocation_name())
                .build();
    }
}
