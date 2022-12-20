package basic.shop.member.command.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@AllArgsConstructor
public class MemberId implements Serializable {
    @Column(name = "member_id")
    private String id;

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(!(o instanceof MemberId memberId))
            return false;
        return Objects.equals(id, memberId.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }
}
