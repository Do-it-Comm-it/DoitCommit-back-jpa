package com.web.doitcommit.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@EntityListeners(value = {AuditingEntityListener.class})
public abstract class BaseEntity {

    @DateTimeFormat(pattern = "yyyy/MM/ddTHH:mm")
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime joinDate;

    @DateTimeFormat(pattern = "yyyy/MM/ddTHH:mm")
    @LastModifiedDate
    private LocalDateTime updateDate;
}
