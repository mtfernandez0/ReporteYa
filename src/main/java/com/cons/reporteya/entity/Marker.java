package com.cons.reporteya.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "markers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Marker {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private Double latitude;

	@NotNull
	private Double longitude;

	private String country;

	private String town;

	private String road;

	private String city;

	private String village;
	
	private String suburb;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "report_id")
	private Report report;

}
