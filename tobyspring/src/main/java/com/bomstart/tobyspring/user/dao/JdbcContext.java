package com.bomstart.tobyspring.user.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 템플릿
 */
public class JdbcContext {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        // (2) Workflow 시작
        Connection c = null;
        PreparedStatement ps = null;

        try {
            // (3) 참조정보 생성
            c = dataSource.getConnection();

            // (4) Callback 호출 / 참조정보 전달
            ps = stmt.makePreparedStatement(c);

            // (9) Workflow 진행
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            // (10) Workflow 마무리
            if (ps != null) { try{ ps.close(); } catch (SQLException e) {} }
            if (c != null) { try{ c.close(); } catch (SQLException e) {} }
        }
    }

    public void executeSql(final String query) throws SQLException {
        this.workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                return c.prepareStatement(query);
            }
        });
    }

}
