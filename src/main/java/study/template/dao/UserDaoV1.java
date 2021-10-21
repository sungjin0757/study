package study.template.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import study.template.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
public class UserDaoV1 {

    private final DataSource dataSource;

    public void add(User user) throws  SQLException{
        Connection connection = dataSource.getConnection();

        PreparedStatement ps=connection.prepareStatement("insert into users(id,name,password) values(?,?,?)");
        ps.setString(1,user.getId());
        ps.setString(2,user.getName());
        ps.setString(3,user.getPassword());

        ps.executeUpdate();

        ps.close();
        connection.close();
    }

    public User get(String id) throws SQLException{
        Connection connection= dataSource.getConnection();

        PreparedStatement ps=connection.prepareStatement("select * from users where id=?");
        ps.setString(1,id);

        ResultSet rs=ps.executeQuery();

        User user=null;

        if(rs.next()) {
            user = User.createUser()
                    .id(rs.getString("id"))
                    .name(rs.getString("name"))
                    .password(rs.getString("password"))
                    .build();
        }

        rs.close();
        ps.close();
        connection.close();

        if(user==null){
            throw new EmptyResultDataAccessException(1);
        }

        return user;
    }

    public void deleteAll() throws SQLException{
        Connection connection= dataSource.getConnection();

        PreparedStatement ps=connection.prepareStatement("delete from users");
        ps.executeUpdate();

        ps.close();
        connection.close();
    }

    public int getCount() throws SQLException{
        Connection connection= dataSource.getConnection();

        PreparedStatement ps=connection.prepareStatement("select count(*) from users");

        ResultSet rs=ps.executeQuery();
        rs.next();

        int count=rs.getInt(1);

        rs.close();
        ps.close();
        connection.close();

        return count;
    }

}
