package com.codingplatform.service;

import com.codingplatform.model.User;
import com.codingplatform.util.DatabaseUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.Key;

public class UserService {
    private static final String SECRET = "your-256-bit-secret-key-here-32chars"; // Replace with a secure key
    private static final Key SECRET_KEY = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());

    public UserService() {
        DatabaseUtil.initDatabase();
    }

    public User register(String username, String password) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users (username, password) VALUES (?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, password); // In production, hash the password
            stmt.executeUpdate();

            try (ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT id FROM users WHERE username = '" + username + "'")) {
                if (rs.next()) {
                    return new User(rs.getLong("id"), username, password);
                }
            }
        }
        return null;
    }

    public String login(String username, String password) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT id, password FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getString("password").equals(password)) { // In production, compare hashed passwords
                return Jwts.builder()
                        .setSubject(username)
                        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                        .compact();
            }
        }
        return null;
    }

    public static Key getSecretKey() {
        return SECRET_KEY;
    }
}