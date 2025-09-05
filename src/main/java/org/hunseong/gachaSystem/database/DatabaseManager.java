package org.hunseong.gachaSystem.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

import org.hunseong.gachaSystem.GachaSystem;


public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    private DatabaseManager(File dataFolder) {
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + new File(dataFolder, "database.db").getAbsolutePath();
            this.connection = DriverManager.getConnection(url);
            createTables();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager(GachaSystem.getInstance().getDataFolder());
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                String url = "jdbc:sqlite:" + new File(GachaSystem.getInstance().getDataFolder(), "database.db").getAbsolutePath();
                this.connection = DriverManager.getConnection(url);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private void createTables() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            // 아이템 정보를 저장할 테이블 생성 SQL
            String reward_sql = "CREATE TABLE IF NOT EXISTS rewards ("
                    + "grade TEXT NOT NULL,"
                    + "data BLOB NOT NULL," // BLOB은 아이템 스택을 바이너리 데이터로 저장
                    + "PRIMARY KEY(grade)"
                    + ");";
            statement.execute(reward_sql);
            String player_gacha_sql = "CREATE TABLE IF NOT EXISTS player_reward_board ("
                    + "player_uuid TEXT NOT NULL PRIMARY KEY,"
                    + "data TEXT NOT NULL,"
                    + "ticket INT NOT NULL DEFAULT 0"
                    + ");";
            statement.execute(player_gacha_sql);
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

