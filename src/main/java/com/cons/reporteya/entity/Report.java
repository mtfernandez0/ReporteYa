package com.cons.reporteya.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private double budget; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User creator;
    
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "reports_companies", joinColumns = @JoinColumn(name = "report_id"), inverseJoinColumns = @JoinColumn(name = "company_id"))
//    private List<Company> companies;

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
