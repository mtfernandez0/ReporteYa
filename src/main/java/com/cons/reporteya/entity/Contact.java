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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
	
	@NotBlank
	@Size(max = 255)
	private String address;
	
	private String email;



	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "User_id")
	private User user;
	

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
