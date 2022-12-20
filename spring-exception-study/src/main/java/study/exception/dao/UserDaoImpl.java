package study.exception.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import study.exception.domain.User;
import study.exception.exception.DuplicateUserException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserDaoImpl implements UserDao{

    private final JdbcOperations jdbcOperations;

    private RowMapper<User> userMapper=new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.createUser()
                    .id(rs.getString("id"))
                    .password(rs.getString("password"))
                    .name(rs.getString("name"))
                    .build();
        }
    };

    @Override
    public void add(User user) {
        jdbcOperations.update("insert into users(id,name,password) values(?,?,?)",
                user.getId(),user.getName(),user.getPassword());
    }

    @Override
    public Optional<User> get(String id) {
        return Optional.ofNullable(
                jdbcOperations.queryForObject("select * from users u where u.id=?", userMapper,new Object[]{id}));
    }

    @Override
    public List<User> getAll() {
        return jdbcOperations.query("select * from users u order by id", userMapper);
    }

    @Override
    public void deleteAll() {
        jdbcOperations.update("delete from users");
    }

    @Override
    public int getCount() {
        return jdbcOperations.queryForObject("select count(*) from users",Integer.class);
    }
}
