package org.example.dao;

import org.example.model.Player;

import java.sql.Statement;

public class PlayerDAO extends DAO {
    public PlayerDAO() {
        super();
    }

    public boolean addPlayer(Player player) {
        try {
            connect();
            String sql = "INSERT INTO `dots&boxes`.player (name) VALUES (?);";
            pre = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pre.setString(1, player.getName());

            int affectedRows = pre.executeUpdate();
            if (affectedRows > 0) {
                rs = pre.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    player.setId(id);
                    return true;
                }
            }

            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkPlayer(Player player) {
        try {
            connect();
            String sql = "SELECT id, name FROM `dots&boxes`.player WHERE name LIKE \"?\";";
            pre = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pre.setString(1, player.getName());

            rs = pre.executeQuery();
            if (rs.next()) {
                return true;
            }
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
