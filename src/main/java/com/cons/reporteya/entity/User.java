package com.cons.reporteya.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.Date;
import java.util.List;



@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 255)
	private String first_name;

	@NotBlank
	@Size(max = 255)
	private String last_name;

//    @NotNull
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    private Date date_of_birth;

	

	@NotBlank
	@Email
	private String email;

	@Size(min = 8, max = 64)
	@NotBlank
	private String password;

	@Transient
	@NotBlank
	private String passwordConfirmation;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Contact contact;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "creator")
	private List<Report> reports;

	 @OneToOne(mappedBy="user", fetch=FetchType.LAZY)
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
