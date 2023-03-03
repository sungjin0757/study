package basic.asyncevents.common.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class AbstractDateEntity (
    @CreatedDate
    @Column(updatable = false)
    open var createdDate: LocalDateTime? = null,

    @LastModifiedDate
    @Column(updatable = true)
    open var updatedDate: LocalDateTime? = null
) {

}