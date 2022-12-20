package study.template.dao.context;

import lombok.RequiredArgsConstructor;
import study.template.dao.strategy.StatementStrategy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RequiredArgsConstructor
public class JdbcContext {

    private final DataSource dataSource;

    public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException{
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
