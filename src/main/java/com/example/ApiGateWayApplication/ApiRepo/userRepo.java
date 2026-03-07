package com.example.ApiGateWayApplication.ApiRepo;

import com.example.ApiGateWayApplication.ApiEntity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class userRepo {

    private final JdbcTemplate jdbcTemplate;

    public userRepo(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveUser(User user){
        String sql = "INSERT INTO users(username,password) VALUES(?,?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword());
    }

    public Optional<User> findByUsername(String username){
        String sql = "Select * from users where username = ?";
        List<User> users = jdbcTemplate.query(sql,userRowMapper, username);
        return users.stream().findFirst();
    }

    public final RowMapper<User> userRowMapper = (rs, rowNum) ->{
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        return user;
    };

}
