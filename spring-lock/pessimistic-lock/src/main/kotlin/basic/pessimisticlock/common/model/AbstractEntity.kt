package basic.pessimisticlock.common.model

import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class AbstractTimeEntity (
    @Column(name = "created_date", updatable = false)
    @CreatedDate
    open var createdDate: LocalDateTime? = null,

    @Column(name = "updated_date")
    @LastModifiedDate
    open var updatedDate: LocalDateTime? = null
) {

}