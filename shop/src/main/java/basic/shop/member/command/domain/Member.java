package basic.shop.member.command.domain;

import lombok.Getter;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
@Getter
public class Member {
    @EmbeddedId
    private MemberId memberId;
    @Embedded
    private Password password;


    public void changePassword(String currentPassword, String newPassword) {
        if(!password.match(currentPassword)) {
            throw new IllegalArgumentException("Not Match Password");
        }
        this.password = new Password(newPassword);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(memberId, member.memberId) && Objects.equals(password, member.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, password);
    }
}
