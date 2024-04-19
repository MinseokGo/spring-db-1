package hello.jdbc.exception.basic;

import java.net.ConnectException;
import java.sql.SQLException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CheckAppTest {

    @Test
    void checked() {
        Controller controller = new Controller();

        Assertions.assertThatThrownBy(controller::request)
                .isInstanceOf(Exception.class);
    }

    static class Controller {

        Service service = new Service();

        public void request() throws SQLException, ConnectException {
            service.logic();
        }
    }

    static class Service {

        NetworkClient networkClient = new NetworkClient();
        Repository repository = new Repository();

        public void logic() throws SQLException, ConnectException {
            networkClient.call();
            repository.call();
        }
    }

    static class NetworkClient {

        public void call() throws ConnectException {
            throw new ConnectException("Failed connect to Network !!");
        }
    }

    static class Repository {

        public void call() throws SQLException {
            throw new SQLException("Database Error !!");
        }
    }
}
