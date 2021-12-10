package study.aop.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import study.aop.domain.Level;
import study.aop.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final JdbcOperations jdbcOperations;

    private RowMapper<User> userMapper=new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.createUser()
                    .id(rs.getString("id"))
                    .password(rs.getString("password"))
                    .name(rs.getString("name"))
                    .level(Level.valueOf(rs.getInt("level")))
                    .login(rs.getInt("login"))
                    .recommend(rs.getInt("recommend"))
                    .email(rs.getString("email"))
                    .createdAt(rs.getTimestamp("createdAt").toLocalDateTime())
                    .lastUpgraded(rs.getTimestamp("lastUpgraded").toLocalDateTime())
                    .build();
        }
    };

    @Override
    public void add(User user) {
        jdbcOperations.update("insert into users(id,name,password,level,login,recommend,email,createdAt,lastUpgraded) " +
                        "values(?,?,?,?,?,?,?,?,?)",
                user.getId(),user.getName(),user.getPassword()
                ,user.getLevel().getValue(),user.getLogin(),user.getRecommend(), user.getEmail()
                , Timestamp.valueOf(user.getCreatedAt()),Timestamp.valueOf(user.getLastUpgraded()));
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

    @Override
    public void update(User user) {
        jdbcOperations.update("update users set name=?,password=?,level=?,login=?,recommend=?,email=?, createdAt=?," +
                        "lastUpgraded=? where id=?",
                user.getName(), user.getPassword(),user.getLevel().getValue()
                ,user.getLogin(),user.getRecommend(),user.getEmail(),user.getCreatedAt(),user.getLastUpgraded(),user.getId());
    }
}
