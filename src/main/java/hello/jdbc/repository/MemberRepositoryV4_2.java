package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;
import java.util.Objects;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

@Slf4j
public class MemberRepositoryV4_2 implements MemberRepository {

    private final DataSource dataSource;
    private final SQLExceptionTranslator exceptionTranslator;

    public MemberRepositoryV4_2(DataSource dataSource) {
        this.dataSource = dataSource;
        this.exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member(member_id, money) values(?, ?)";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            setQueryStatementForSave(member, statement);
            return member;
        } catch (SQLException e) {
            throw Objects.requireNonNull(
                    exceptionTranslator.translate("save", sql, e));
        } finally {
            close(connection, statement);
        }
    }

    private void setQueryStatementForSave(Member member, PreparedStatement statement) throws SQLException {
        statement.setString(1, member.getMemberId());
        statement.setInt(2, member.getMoney());
        statement.executeUpdate();
    }

    @Override
    public Member findById(String memberId) {
        String sql = "select * from member where member_id = ?";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, memberId);
            return findMember(statement);
        } catch (SQLException e) {
            throw Objects.requireNonNull(
                    exceptionTranslator.translate("findById", sql, e));
        } finally {
            close(connection, statement);
        }
    }

    private Member findMember(PreparedStatement statement) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return Member.valueOf(
                        resultSet.getString("member_id"),
                        resultSet.getInt("money"));
            }
            throw new NoSuchElementException("member not found");
        }
    }

    @Override
    public void update(String memberId, int money) {
        String sql = "update member set money=? where member_id=?";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            setQueryStatementForUpdate(statement, memberId, money);
        } catch (SQLException e) {
            throw Objects.requireNonNull(
                    exceptionTranslator.translate("update", sql, e));
        } finally {
            close(connection, statement);
        }
    }

    private void setQueryStatementForUpdate(PreparedStatement statement, String memberId, int money)
            throws SQLException {

        statement.setInt(1, money);
        statement.setString(2, memberId);
        statement.executeUpdate();
    }

    @Override
    public void delete(String memberId) {
        String sql = "delete from member where member_id=?";

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, memberId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw Objects.requireNonNull(
                    exceptionTranslator.translate("delete", sql, e));
        } finally {
            close(connection, statement);
        }
    }

    public Connection getConnection() throws SQLException {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        log.info("connection={} class={}", connection, connection.getClass());
        return connection;
    }

    private void close(Connection connection, Statement statement) {
        JdbcUtils.closeStatement(statement);
        DataSourceUtils.releaseConnection(connection, dataSource);
    }
}
