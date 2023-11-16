package com.vivatech.models;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vivatech.dto.TrackTime;
import com.vivatech.enums.Gender;
import com.vivatech.utils.EntityAuditListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(EntityAuditListener.class)
public class UserProfile extends TrackTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Name must not be blank")
	private String name;

	@NotBlank(message = "Email must not be blank")
	@Email
	@Column(unique = true)
	private String email;
	
	@NotNull(message = "Date of birth must not be blank")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate dob;

	@NotNull(message = "Gender must not be blank")
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(nullable = true)
	private String profileURL;
	
	@NotNull(message = "User must not be null")
	@OneToOne
	@JsonIgnore
	private User user;
}
