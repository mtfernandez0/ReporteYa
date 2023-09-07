package com.cons.reporteya.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "markers")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Builder
public class Marker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private String country;

    private String town;

    private String city;

    private String village;

    private String suburb;
    private String road;
}
