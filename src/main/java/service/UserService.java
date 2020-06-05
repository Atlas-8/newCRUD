package service;

import DAO.UserDAO;
import model.User;
import javax.servlet.ServletException;
import java.sql.*;
import java.util.List;

public class UserService {

    public UserService() {
    }

    public User getUserById(long id) throws Exception {
        try {
            return getUserDAO().getUserById(id);
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    public User getUserByName(String name) throws ServletException {
        UserDAO dao = getUserDAO();
        try {
            return dao.getUserByName(name);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    public List<User> getAllUsers() throws ServletException {
        UserDAO dao = getUserDAO();
        try {
            dao.createTable();
            return dao.getAllUsers();
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    public boolean deleteUser(String name) {
        Connection connection = getMysqlConnection();
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM users WHERE name='" + name + "'");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void deleteUserById(long id) {
        Connection connection = getMysqlConnection();
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM users WHERE id=" + id);
        } catch (SQLException e) {

        }
    }

    public void addUser(User user) throws Exception {
        UserDAO dao = getUserDAO();
        Connection connection = getMysqlConnection();
        dao.addUser(user);
        connection.close();
    }

    public void cleanUp() throws Exception {
        UserDAO dao = getUserDAO();
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    public void createTable() throws Exception{
        UserDAO dao = getUserDAO();
        try {
            dao.createTable();
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    private static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.cj.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").          //db type
                    append("localhost:").             //host name
                    append("3306/").                  //port
                    append("CRUD?").                  //db name
                    append("user=Atlas&").            //login
                    append("password=1987010688&").   //password
                    append("useSSL=false&").           //disable SSL
                    append("serverTimezone=UTC");

            System.out.println("URL: " + url + "\n");

            Connection connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    private static UserDAO getUserDAO() {
        return new UserDAO(getMysqlConnection());
    }
}
