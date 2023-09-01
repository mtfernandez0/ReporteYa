package com.cons.reporteya.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "reports")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank
    private String municipality;

    @NotBlank
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User creator;

    @Column(nullable = false)
    private Date created_at;

    private Date updated_at;

    @PrePersist
    private void onCreate(){
        this.created_at = new Date();
        this.updated_at = new Date();
    }

    @PreUpdate
    private void onUpdate(){
        this.updated_at = new Date();
    }
}
