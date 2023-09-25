package managers;

import application.Main;
import data.*;

import java.sql.*;
import java.util.ArrayDeque;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager() {
        this.connectToDatabase();
        this.createTables();
    }

    public void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(Main.getPostgresSQL_URL(), "s368759", "1xBcMdgDVQQ2wHyD");
            System.out.println(Main.ANSI_GREEN + "Успешное подключение к базе данных" + Main.ANSI_RESET);
        } catch (SQLException e) {
            System.out.println(Main.ANSI_RED + "Не удалось подключиться к базе данных" + Main.ANSI_RESET);
            System.exit(0);
        }
    }

    public void createTables() {
        try {
            connection.prepareStatement(DatabaseCommands.createAllTables);
        } catch (SQLException e) {
            System.out.println("Ошибка при создании таблиц");
        }
    }

    public Coordinates loadCoordinates(int id) {
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.getCoordinates);
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();

            resultSet.next();

            return new Coordinates(resultSet.getLong(2), resultSet.getInt(3));
        } catch (SQLException e) {
            System.out.println("Ошибка чтения таблицы coordinates");
        }
        return null;
    }

    public House loadHouse(int id) {
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.getHouse);
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();

            resultSet.next();

            return new House(resultSet.getString(2), resultSet.getInt(3),
                    resultSet.getLong(4));
        } catch (SQLException e) {
            System.out.println("Ошибка чтения таблицы house");
        }
        return null;
    }

    public String loadUser(int id) {
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.getUserById);
            ps.setInt(1, id);

            ResultSet result = ps.executeQuery();

            result.next();

            return result.getString(2);
        } catch (SQLException e) {
            System.out.println("Ошибка чтения таблицы user");
        }
        return null;
    }

    public ArrayDeque<Flat> loadCollection() {
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.getAllObjects);
            ResultSet resultSet = ps.executeQuery();
            ArrayDeque<Flat> collection = new ArrayDeque<>();
            while (resultSet.next()) {
                collection.add(new Flat(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        loadCoordinates(resultSet.getInt(3)),
                        resultSet.getTimestamp(4).toLocalDateTime().toLocalDate(),
                        resultSet.getDouble(5),
                        resultSet.getLong(6),
                        Furnish.valueOf(resultSet.getString(7)),
                        View.valueOf(resultSet.getString(8)),
                        Transport.valueOf(resultSet.getString(9)),
                        loadHouse(resultSet.getInt(10)),
                        loadUser(resultSet.getInt(11))
                ));
            }
            System.out.println(Main.ANSI_GREEN + "Коллекция успешно загружена");
            System.out.println(Main.ANSI_GREEN + "В коллекции находится " + Main.ANSI_RESET
                    + collection.size() + Main.ANSI_GREEN + " объект(-а/-ов)" + Main.ANSI_RESET);
            return collection;
        } catch (SQLException e) {
            System.out.println("Коллекция пуста или произошла ошибка при чтения таблицы flat");
            return new ArrayDeque<>();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
