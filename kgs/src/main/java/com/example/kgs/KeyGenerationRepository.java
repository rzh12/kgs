package com.example.kgs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class KeyGenerationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public long generateUniqueId() {
        String sql = "INSERT INTO key_generation (generated_key) VALUES (NULL)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void saveGeneratedKey(long id, String generatedKey) {
        String sql = "UPDATE key_generation SET generated_key = ? WHERE id = ?";
        jdbcTemplate.update(sql, generatedKey, id);
    }
}


