package study.template.dao.strategy;

import lombok.RequiredArgsConstructor;
import study.template.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RequiredArgsConstructor
public class AddStatement implements StatementStrategy {

    private final User user;

    @Override
    public PreparedStatement makePreparedStatement(Connection connection) throws SQLException {
        PreparedStatement ps= connection.prepareStatement("insert into users(id,name,password) values(?,?,?)");

        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        return ps;
    }
}
