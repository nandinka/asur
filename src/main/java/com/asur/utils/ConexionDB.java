package com.asur.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionDB {
    private static ConexionDB instancia = null;
    private Connection conexion;

    private ConexionDB() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties.example")) {
            if (input == null) {
                throw new RuntimeException("no se encontro config.properties.example");
            }

            Properties prop = new Properties();
            prop.load(input);

            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.user");
            String password = prop.getProperty("db.password");

            this.conexion = DriverManager.getConnection(url, user, password);
            conexion.createStatement().execute("SET search_path TO proyecto");

        } catch (IOException e) {
            throw new RuntimeException("error leyendo config", e);
        } catch (SQLException e) {
            throw new RuntimeException("error conectando a bd", e);
        }
    }

    public static ConexionDB getInstancia() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    public Connection getConexion() {
        return this.conexion;
    }

    public void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
