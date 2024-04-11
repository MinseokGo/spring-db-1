package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryV1 {

    private final DataSource dataSource;

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values(?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, member.getMemberId());
            statement.setInt(2, member.getMoney());
            statement.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("Database error!!", e);
            throw e;
        }
    }

    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, memberId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Member.valueOf(
                            resultSet.getString("member_id"),
                            resultSet.getInt("money"));
                }
            }
            throw new NoSuchElementException("member not found memberId = " + memberId);
        } catch (SQLException e) {
            log.error("Database error!!", e);
            throw e;
        }
    }

    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, money);
            statement.setString(2, memberId);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.info("Database error!!", e);
            throw e;
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, memberId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        log.info("connection={} class={}", connection, connection.getClass());
        return connection;
    }
}
