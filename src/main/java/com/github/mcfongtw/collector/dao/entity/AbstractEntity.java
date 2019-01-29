package com.github.mcfongtw.collector.dao.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity {

    @Column(name = "created_date", nullable = false)
    @CreatedDate
    protected Date createdDate;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    protected Date lastModifiedDate;

//    @Version()
//    private Long version = 0L;

}
