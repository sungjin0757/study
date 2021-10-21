package study.template.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import study.template.dao.strategy.DeleteAllStatement;
import study.template.dao.strategy.StatementStrategy;
import study.template.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
public class UserDaoV4 {

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
        StatementStrategy stmt=new DeleteAllStatement();
        jdbcContextWithStatementStrategy(stmt);
    }

    public int getCount() throws SQLException{

        Connection connection=null;
        PreparedStatement ps=null;
        ResultSet rs=null;

        try{
            connection= dataSource.getConnection();
            ps=connection.prepareStatement("select count(*) from users");
            rs=ps.executeQuery();

            rs.next();

            return rs.getInt(1);
        }catch (SQLException e){
            throw e;
        }finally {
            if(rs!=null){
                try{
                    rs.close();
                }catch(SQLException e){

                }
            }
            if(ps!=null){
                try{
                    ps.close();
                }catch (SQLException e){

                }
            }
            if(connection!=null){
                try{
                    connection.close();
                }catch (SQLException e){

                }
            }
        }

    }

    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException{
        Connection connection=null;
        PreparedStatement ps=null;

        try{
            connection=dataSource.getConnection();

            ps=stmt.makePreparedStatement(connection);
            ps.executeUpdate();
        }catch(SQLException e){
            throw e;
        }finally {
            if(ps!=null){
                try{
                    ps.close();
                }catch(SQLException e){

                }
            }
            if(connection!=null){
                try{
                    connection.close();
                }catch(SQLException e){

                }
            }

        }
    }
}
