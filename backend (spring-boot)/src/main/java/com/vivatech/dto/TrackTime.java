package com.vivatech.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("unused")
public class TrackTime {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;
	public void setCreatedAt(LocalDateTime time) {
		createdAt = time;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setUpdatedAt(LocalDateTime time) {
		updatedAt = time;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}
