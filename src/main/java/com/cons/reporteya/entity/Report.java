package com.cons.reporteya.entity;

import java.util.ArrayList;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reports")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
    private String additional_directions;

	private double budget;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User creator;
	
	@OneToOne(mappedBy = "report", fetch = FetchType.LAZY, cascade =  CascadeType.ALL)
	private Marker marker;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "reports_companies", joinColumns = @JoinColumn(name = "report_id"), inverseJoinColumns = @JoinColumn(name = "company_id"))
	private List<Company> companies;

	@OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
	private List<Comment> comments;

	@NotNull
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "reports_tags", joinColumns = @JoinColumn(name = "report_id"), inverseJoinColumns = @JoinColumn(name = "tags_id"))
	@Builder.Default
	private List<Tag> tags = new ArrayList<>();

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
