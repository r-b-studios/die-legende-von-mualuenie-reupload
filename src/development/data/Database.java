package development.data;

import engine.tools.SafeList;
import java.sql.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public final class Database {

    private final static String URL = "jdbc:mysql://sql7.freesqldatabase.com/sql11660230";
    private final static String USER = "sql11660230";
    private final static String PASSWORD = "bznF9K7Nud";

    public static void load() {
        // trägt offline gespeicherte Dateien in die Datenbank ein
        setHighscore(DataFile.getHighscore(), DataFile.getDate());
        setName(DataFile.getName());
    }

    private static void connect(Consumer<Connection> func) {
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);

            if (con != null) {
                func.accept(con);
            }
        } catch (Exception e) {
            System.out.println(e.getClass().getTypeName());
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.getClass().getTypeName());
            }
        }
    }

    public static void setHighscore(int value, String date) {
        connect(con -> {
            try {
                DataFile.setHighscore(value);
                DataFile.setDate(date);

                Statement stmt = con.createStatement();

                stmt.executeUpdate("DELETE FROM users WHERE id='" + DataFile.getUID() + '\'');
                stmt.executeUpdate("INSERT INTO users (id, name, highscore, date) VALUES ('" + DataFile.getUID() + "', '" + DataFile.getName() + "', " + value + ", '" + date + "')");
            } catch (Exception e) {
                System.out.println(e.getClass().getTypeName());
            }
        });
    }

    public static void setName(String name) {
        connect(con -> {
            try {
                DataFile.setName(name);

                Statement stmt = con.createStatement();

                stmt.executeUpdate("DELETE FROM users WHERE id='" + DataFile.getUID() + '\'');
                stmt.executeUpdate("INSERT INTO users (id, name, highscore, date) VALUES ('" + DataFile.getUID() + "', '" + name + "', " + DataFile.getHighscore() + ", '" + DataFile.getDate()+ "')");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getClass().getTypeName());
            }
        });
    }

    public static SafeList<User> getSortedByHighscore() throws SQLException {
        SafeList<User> result = new SafeList<>();
        AtomicBoolean connected = new AtomicBoolean(false);

        connect(con -> {
            try {
                String query = "SELECT * FROM users ORDER BY highscore DESC";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    User user = new User(rs.getString("id"), rs.getString("name"), rs.getInt("highscore"), rs.getString("date"));
                    result.add(user);
                }
            } catch (SQLException e) {
                System.out.println(e.getClass().getTypeName());
            }

            connected.set(true);
        });

        if (!connected.get()) {
            throw new SQLException("database connection failed");
        }

        return result;
    }
}