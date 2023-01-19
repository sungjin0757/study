package basic.orderapi.common.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class AbstractTimeEntity (
    @Column(name = "created_date", updatable = false)
    @CreatedDate
    open var createdDate: LocalDateTime? = null,

    @Column(name = "updated_date")
    @LastModifiedDate
    open var updatedDate: LocalDateTime? = null
)