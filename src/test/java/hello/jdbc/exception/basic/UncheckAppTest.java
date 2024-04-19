package hello.jdbc.exception.basic;

import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class UncheckAppTest {

    @Test
    void unchecked() {
        Controller controller = new Controller();

        Assertions.assertThatThrownBy(controller::request)
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void print_exception() {
        Controller controller = new Controller();

        try {
            controller.request();
        } catch (Exception e) {
            log.info("Exception !!", e);
        }
    }

    static class Controller {

        Service service = new Service();

        public void request() {
            service.logic();
        }
    }

    static class Service {

        NetworkClient networkClient = new NetworkClient();
        Repository repository = new Repository();

        public void logic() {
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient {

        public void call() {
            throw new RuntimeConnectException("Failed connect to Network !!");
        }
    }

    static class Repository {

        public void call() {
            try {
                runSQL();
            } catch (SQLException e) {
                throw new RuntimeSQLException(e);
            }
        }

        private void runSQL() throws SQLException {
            throw new SQLException("Database Error !!");
        }
    }

    static class RuntimeConnectException extends RuntimeException {

        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException {

        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }
}
