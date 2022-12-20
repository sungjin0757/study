package study.template.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMakerImpl implements ConnectionMaker{

    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection connection= DriverManager.getConnection("jdbc:h2:tcp://localhost/~/templatetest","sa",
                "1234");
        return connection;
    }
}
