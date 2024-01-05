package org.example.dao;

import org.example.Util.Util;
import org.example.model.Score;

import java.sql.Statement;

public class ScoreDAO extends DAO{
    public ScoreDAO() {
        super();
    }

    public boolean createScore(Score score) {
        try {
            connect();
            String sql = "INSERT INTO `dots&boxes`.score(boxes, playingId) VALUES (?, ?);";
            pre = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pre.setInt(1, score.getBoxes());
            pre.setInt(2, score.getPlaying().getId());
            int affectedRows = pre.executeUpdate();
            if (affectedRows > 0) {
                rs = pre.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    score.setId(id);
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
