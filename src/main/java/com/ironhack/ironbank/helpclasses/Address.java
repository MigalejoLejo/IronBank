package com.ironhack.ironbank.helpclasses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
    String street;
    String number;
    String floor;
    String postalCode;
    String city;
    String land;
}
