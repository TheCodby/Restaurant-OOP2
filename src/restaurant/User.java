/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurant;
import java.sql.*;

/**
 *
 * @author theco
 */
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private boolean isAdmin;
    public User(String firstName, String lastName, String email, boolean isAdmin){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isAdmin = isAdmin;
    }
    public static User createUser(String firstName, String lastName, String email, String password) throws SQLException{
        Connection connection = Connector.connect();
        // Prepare the SQL statement
        String sql = "INSERT INTO Users (Email, Password, FirstName, LastName) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);

        // Set the parameter values
        statement.setString(1, email);
        statement.setString(2, password);
        statement.setString(3, firstName);
        statement.setString(4, lastName);

        // Execute the SQL statement
        int rowsInserted = statement.executeUpdate();
        statement.close();
        if (rowsInserted == 0) {
            throw new SQLException("Can't create the user");
        }else{
            return new User(firstName, lastName, email, false);
        }
    }
    public static User login(String email, String password) throws SQLException{
        Connection connection = Connector.connect();
        String sql = "SELECT * FROM Users WHERE Email = ? AND Password = ?";
        PreparedStatement statement = connection.prepareStatement(sql);

        // Set the parameter values
        statement.setString(1, email);
        statement.setString(2, password);

        // Execute the SQL statement
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            // User exists, perform login logic
            String firstName = resultSet.getString("FirstName");
            String lastName = resultSet.getString("LastName");
            String userEmail = resultSet.getString("Email");
            String isAdmin = resultSet.getString("isAdmin");
            statement.close();
            resultSet.close();
            return new User(firstName, lastName, userEmail, isAdmin.equals("1") ? true : false);
        } else {
            // User does not exist or invalid credentials
            throw new SQLException("Wrong email or password");
        }        
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public boolean isAdmin(){
        return isAdmin;
    }
}
