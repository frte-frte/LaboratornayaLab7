package commands;

import application.Main;
import data.*;
import managers.CollectionManager;
import managers.DatabaseCommands;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.Collections;

public class UpdateCommand extends Command{
    public UpdateCommand(CollectionManager collectionManager, Data data, Connection connection) {
        super(collectionManager, data, connection);
    }

    public synchronized String execute() {
        ArrayDeque<Flat> collection = CollectionManager.getCollection();
        for (Flat flat : collection) {
            if (flat.getId() == Long.parseLong(data.getParam()[0]) & flat.getOwnerLogin().equals(data.getUser().getLogin())) {
                try {
                    //удаление объекта
                    PreparedStatement ps = connection.prepareStatement(DatabaseCommands.deleteObject);
                    ps.setLong(1, Long.parseLong(data.getParam()[0]));
                    ps.execute();

                    collection.remove(flat);
                    //обновление объекта
                    PreparedStatement ps1 = connection.prepareStatement(DatabaseCommands.addFlatWithId);

                    ps1.setLong(1, Long.parseLong(data.getParam()[0]));
                    ps1.setString(2, data.getParam()[1]);
                    ps1.setInt(3, addCoordinates());
                    ps1.setTimestamp(4, Timestamp.valueOf(LocalDate.now().atStartOfDay()));
                    ps1.setDouble(5, Double.parseDouble(data.getParam()[4]));
                    ps1.setLong(6, Long.parseLong(data.getParam()[5]));
                    ps1.setString(7, data.getParam()[6]);
                    ps1.setString(8, data.getParam()[7]);
                    ps1.setString(9, data.getParam()[8]);
                    ps1.setInt(10, addHouse());
                    ps1.setInt(11, getUser());

                    ps1.execute();

                    collection.add(new Flat(Long.parseLong(data.getParam()[0]), data.getParam()[1],
                            new Coordinates(Long.parseLong(data.getParam()[2]), Integer.parseInt(data.getParam()[3])),
                            LocalDate.now(), Double.parseDouble(data.getParam()[4]),
                            Long.parseLong(data.getParam()[5]), Furnish.valueOf(data.getParam()[6]),
                            View.valueOf(data.getParam()[7]), Transport.valueOf(data.getParam()[8]), new House(data.getParam()[9],
                            Integer.parseInt(data.getParam()[10]), Long.parseLong(data.getParam()[11])), data.getUser().getLogin()));
                    return Main.ANSI_YELLOW + "Элемент с id = " + data.getParam()[0] + " был обновлён!" + Main.ANSI_RESET + "\n";
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if (flat.getId() == Long.parseLong(data.getParam()[0]) & !flat.getOwnerLogin().equals(data.getUser().getLogin())) {
                return Main.ANSI_YELLOW + "У вас нет прав для обновления элемента" + Main.ANSI_RESET + "\n";
            } else {
                return Main.ANSI_YELLOW + "Нет элемента с id = "+ data.getParam()[0] + Main.ANSI_RESET + "\n";
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
