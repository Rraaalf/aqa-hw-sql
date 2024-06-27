package mode;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtils {
    private final static QueryRunner run = new QueryRunner();

    public DbUtils() {
    }

    @SneakyThrows
    public static String getVerificationCode() {
        return run.query(connection(),
                "SELECT code FROM auth_codes WHERE created = (SELECT max(created) FROM auth_codes);",
                new ScalarHandler<>());
    }

    @SneakyThrows
    private static Connection connection() {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    @SneakyThrows
    public static void wipeEverything() {
        run.execute(connection(), "TRUNCATE auth_codes");
        run.execute(connection(), "TRUNCATE cards;");
        run.execute(connection(), "TRUNCATE card_transactions;");
        run.execute(connection(), "DELETE FROM users WHERE status LIKE '%ive';");
    }
}
