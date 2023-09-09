package com.cons.reporteya.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
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

  	private String additional_directions;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User creator;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "report")
	private Marker marker;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "reports_companies", joinColumns = @JoinColumn(name = "report_id"), inverseJoinColumns = @JoinColumn(name = "company_id"))
	private List<Company> companies;

	@OneToMany(mappedBy = "report")
	private List<Comment> comments;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "reports_tags", joinColumns = @JoinColumn(name = "report_id"), inverseJoinColumns = @JoinColumn(name = "tags_id"))
	private List<Tag> tags;

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
