package study.querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.querydsl.dto.UserSearchCondition;
import study.querydsl.dto.UserWithTeamDto;
import study.querydsl.entity.User;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

//@Repository
@RequiredArgsConstructor
public class UserRepositoryByJpql implements UserRepository {
    private final EntityManager em;

    public void save(User user){
        em.persist(user);
    }

    public Optional<User> findById(Long id){
        return Optional.ofNullable(em.find(User.class, id));
    }

    public List<User> findAll(){
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }

    public List<User> findByUserName(String userName){
        return em.createQuery("select u from User u where u.userName = :userName", User.class)
                .setParameter("userName", userName)
                .getResultList();
    }

    @Override
    public List<UserWithTeamDto> searchByBuilder(UserSearchCondition userSearchCondition) {
        return null;
    }

    @Override
    public List<UserWithTeamDto> searchByWhere(UserSearchCondition condition) {
        return null;
    }
}
