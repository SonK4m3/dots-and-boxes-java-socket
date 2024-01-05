package org.example.dao;

import org.example.model.Playing;

import java.sql.Statement;

public class PlayingDAO extends DAO {
    public PlayingDAO() {
        super();
    }

    public boolean createPlaying(Playing playing) {
        try {
            connect();
            String sql = "INSERT INTO `dots&boxes`.playing(playerId, roomId) VALUES (?, ?);";
            pre = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pre.setInt(1, playing.getPlayer().getId());
            pre.setInt(2, playing.getRoom().getId());
            int affectedRows = pre.executeUpdate();
            if (affectedRows > 0) {
                rs = pre.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    playing.setId(id);
                    return true;
                }
            }

            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
