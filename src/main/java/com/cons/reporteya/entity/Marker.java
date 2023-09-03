package com.cons.reporteya.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "markers")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Builder
public class Marker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude;

    private Double longitude;
}
