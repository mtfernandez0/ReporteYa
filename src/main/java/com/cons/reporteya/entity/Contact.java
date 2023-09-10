package com.cons.reporteya.entity;

import java.util.Date;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contacts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String number;
	
	private String country;

	private String town;

	private String road;

	private String city;

	@Column(nullable = false)
	private Date created_at;

	private Date updated_at;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	


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
