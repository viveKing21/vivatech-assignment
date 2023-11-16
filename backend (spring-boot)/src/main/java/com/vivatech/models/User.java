package com.vivatech.models;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.vivatech.dto.TrackTime;
import com.vivatech.enums.Role;
import com.vivatech.enums.Status;
import com.vivatech.utils.EntityAuditListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class User extends TrackTime{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Username must not be blank")
	@Size(min = 3, max = 20, message = "Username: min 3 and max 20 character allowed")
	@Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_.]{2,19}$", message = "Username must start with alphabets (min 3 and max 20 character allowed)")
	@Column(unique = true)
	private String username;
	
	@NotBlank(message = "Password must not be blank")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@JsonIgnore
	private Set<Role> roles; 
	
	@NotNull(message = "User profile must not be empty")
	@OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private UserProfile userProfile;
	
	@Enumerated(EnumType.ORDINAL)
	private Status status = Status.ACTIVE;
	
	@Column(nullable = true)
	@JsonIgnore
	public LocalDateTime deletedAt = null;
}
