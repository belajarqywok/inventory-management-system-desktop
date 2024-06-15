package com.lestarieragemilang.app.desktop.Repositories;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import com.lestarieragemilang.app.desktop.Configurations.DatabaseConfiguration;

/**
 *  Auth Repositories
 */
public class AuthRepositories extends DatabaseConfiguration {
  /**
   * Login Repository
   * 
   * @param username
   * @param password
   * @return
   */
  protected boolean loginRepo(String username, String password) {
    Connection connection = getConnection();
    String query = (
      "SELECT * FROM auth WHERE username = ?" +
      "AND password = ?"
    );
        
    try {
      PreparedStatement statement = connection
        .prepareStatement(query);
                
      statement.setString(1, username);
      statement.setString(2, password);

      ResultSet resultSet = statement.executeQuery();
      return resultSet.next() ? true : false;

    } catch (SQLException exception) {
      exception.printStackTrace();
      return false;

    }
  }

  // Manual testing
  public static void main(String[] args) {
    AuthRepositories repository = new AuthRepositories();
    boolean result = repository.loginRepo("", "");

    System.out.println(result);
  }
}
