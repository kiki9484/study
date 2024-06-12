package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class JpaBaseEntity {
    @Column(updatable = false, insertable = true)
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    // persist() 전에 호출된다.
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    // update() 전에 호출된다.
    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
