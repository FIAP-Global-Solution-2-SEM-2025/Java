package com.br.fiap.skillsfast.infrastructure.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@ApplicationScoped
@Alternative
public class DatabaseConnectionImpl implements DatabaseConnection {

    @Inject
    DataSource dataSource;

    @Override
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
}