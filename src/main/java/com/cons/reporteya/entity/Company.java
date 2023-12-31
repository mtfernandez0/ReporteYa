package com.cons.reporteya.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "companies")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank
    private String location;

    @NotBlank
    @Email
    private String email;

    private String website;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "reports_companies", joinColumns = @JoinColumn(name = "company_id"), inverseJoinColumns = @JoinColumn(name = "report_id"))
    private List<Report> reportes;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "company", fetch = FetchType.LAZY)
    private FileUp logoCompania;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Column(nullable = false)
    private Date created_at;

    private Date updated_at;

    @PrePersist
    private void onCreate() {
        this.created_at = new Date();
        this.updated_at = new Date();
    }

    @PreUpdate
    private void onUpdate() {
        this.updated_at = new Date();
    }

}
