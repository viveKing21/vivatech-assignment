package com.vivatech.utils;


import java.time.LocalDateTime;

import com.vivatech.dto.TrackTime;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class EntityAuditListener<T extends TrackTime> {
    @PrePersist
    public void setCreatedAt(T entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void setUpdatedAt(T entity) {
        entity.setUpdatedAt(LocalDateTime.now());
    }
}