package com.cons.reporteya.entity;

import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String comment;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User owner;

	@ManyToOne
	@JoinColumn(name = "report_id")
	private Report report;
	
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

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
