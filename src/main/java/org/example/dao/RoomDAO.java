package org.example.dao;

import org.example.Util.Util;
import org.example.model.Room;

import java.sql.Statement;

public class RoomDAO extends DAO {
    public RoomDAO() {
        super();
    }

    public boolean createRoom(Room room) {
        try {
            connect();
            String sql = "INSERT INTO `dots&boxes`.room(createTime) VALUES (?);";
            pre = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pre.setDate(1, Util.convertToSqlDate(room.getCreateTime()));

            int affectedRows = pre.executeUpdate();
            if (affectedRows > 0) {
                rs = pre.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    room.setId(id);
                    return true;
                }
            }

            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateWinner(Room room) {
        try {
            connect();
            String sql = "UPDATE `dots&boxes`.room SET winner = ? WHERE id = ?;";
            pre = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pre.setInt(1, room.getWinner().getId());
            pre.setInt(1, room.getId());

            int affectedRows = pre.executeUpdate();
            if (affectedRows > 0) {
                return true;
            }
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
