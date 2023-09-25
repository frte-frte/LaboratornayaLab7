package commands;

import application.Main;
import data.*;
import managers.CollectionManager;
import managers.DatabaseCommands;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayDeque;

public class AddIfMinCommand extends Command{
    public AddIfMinCommand(CollectionManager collectionManager, Data data, Connection connection) {
        super(collectionManager, data, connection);
    }

    public synchronized String execute() {
        ArrayDeque<Flat> collection = CollectionManager.getCollection();
        for (Flat flat : collection) {
            if (flat.getId() < Long.parseLong(data.getParam()[0])) {
                try {
                    PreparedStatement ps = connection.prepareStatement(DatabaseCommands.addFlatWithId);
                    ps.setLong(1, Long.parseLong(data.getParam()[0]));
                    ps.setString(2, data.getParam()[1]);
                    ps.setInt(3, addCoordinates());
                    ps.setTimestamp(4, Timestamp.valueOf(LocalDate.now().atStartOfDay()));
                    ps.setDouble(5, Double.parseDouble(data.getParam()[4]));
                    ps.setLong(6, Long.parseLong(data.getParam()[5]));
                    ps.setString(7, data.getParam()[6]);
                    ps.setString(8, data.getParam()[7]);
                    ps.setString(9, data.getParam()[8]);
                    ps.setInt(10, addHouse());
                    ps.setInt(11, getUser());

                    ps.execute();

                    collection.add(new Flat(Long.parseLong(data.getParam()[0]), data.getParam()[1],
                            new Coordinates(Long.parseLong(data.getParam()[2]), Integer.parseInt(data.getParam()[3])),
                            LocalDate.now(), Double.parseDouble(data.getParam()[4]),
                            Long.parseLong(data.getParam()[5]), Furnish.valueOf(data.getParam()[6]),
                            View.valueOf(data.getParam()[7]), Transport.valueOf(data.getParam()[8]), new House(data.getParam()[9],
                            Integer.parseInt(data.getParam()[10]), Long.parseLong(data.getParam()[11])), data.getUser().getLogin()));
                    return Main.ANSI_YELLOW + "Элемент с id = " + data.getParam()[0] + " был добавлен" + Main.ANSI_RESET + "\n";
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                return Main.ANSI_YELLOW + "В коллекции есть элементы с id меньше " + data.getParam()[0] + Main.ANSI_RESET + "\n";
            }
        }
        return null;
    }

    public int addCoordinates() {
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.addCoordinates);

            ps.setLong(1, Long.parseLong(data.getParam()[2]));
            ps.setInt(2, Integer.parseInt(data.getParam()[3]));

            ResultSet result = ps.executeQuery();

            result.next();

            return result.getInt(1);
        } catch (SQLException e) {
            System.out.println(Main.ANSI_RED + "Не удалось добавить данные в таблицу coordinates!" + Main.ANSI_RESET);
        }
        return 0;
    }

    public int addHouse() {
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.addHouse);

            ps.setString(1, data.getParam()[9]);
            ps.setInt(2, Integer.parseInt(data.getParam()[10]));
            ps.setLong(3, Long.parseLong(data.getParam()[11]));

            ResultSet result = ps.executeQuery();

            result.next();

            return result.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getUser() {
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.getUser);

            ps.setString(1, data.getUser().getLogin());

            ResultSet result = ps.executeQuery();

            result.next();

            return result.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
