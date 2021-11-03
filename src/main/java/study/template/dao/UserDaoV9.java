package study.template.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import study.template.dao.context.JdbcContext;
import study.template.dao.strategy.StatementStrategy;
import study.template.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UserDaoV9 {

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

    public void add(User user) {
        jdbcOperations.update("insert into users(id,name,password) values(?,?,?)",
                user.getId(),user.getName(),user.getPassword());

    }

    public User get(String id) {
        return jdbcOperations.queryForObject("select * from users u where u.id=?", userMapper,new Object[]{id});
    }

    public void deleteAll(){
        jdbcOperations.update("delete from users");
    }

    public int getCount() {
        return jdbcOperations.queryForObject("select count(*) from users",Integer.class);
    }

    public List<User> getAll(){
        return jdbcOperations.query("select * from users u order by id", userMapper);
    }

}
