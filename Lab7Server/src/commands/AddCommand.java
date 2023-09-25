package commands;

import application.Main;
import data.*;
import managers.CollectionManager;
import managers.DatabaseCommands;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddCommand extends Command{
    public AddCommand(CollectionManager collectionManager, Data data, Connection connection) {
        super(collectionManager, data, connection);
    }

    public synchronized String execute() {
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.addFlat);

            ps.setString(1, data.getParam()[0]);
            ps.setInt(2, addCoordinates());
            ps.setTimestamp(3, Timestamp.valueOf(LocalDate.now().atStartOfDay()));
            ps.setDouble(4, Double.parseDouble(data.getParam()[3]));
            ps.setLong(5, Long.parseLong(data.getParam()[4]));
            ps.setString(6, data.getParam()[5]);
            ps.setString(7, data.getParam()[6]);
            ps.setString(8, data.getParam()[7]);
            ps.setInt(9, addHouse());
            ps.setInt(10, getUser());

            ps.execute();

            PreparedStatement ps1 = connection.prepareStatement(DatabaseCommands.getFlat);
            ps1.setInt(1, getUser());
            ResultSet result = ps1.executeQuery();
            List<Integer> listId = new ArrayList<>();
            while (result.next()) {
                listId.add(result.getInt(1));
            }
            int id = Collections.max(listId);

            ArrayDeque<Flat> collection = collectionManager.getCollection();
            collection.add(new Flat(id, data.getParam()[0],
                    new Coordinates(Long.parseLong(data.getParam()[1]), Integer.parseInt(data.getParam()[2])),
                    LocalDate.now(), Double.parseDouble(data.getParam()[3]),
                    Long.parseLong(data.getParam()[4]), Furnish.valueOf(data.getParam()[5]),
                    View.valueOf(data.getParam()[6]), Transport.valueOf(data.getParam()[7]), new House(data.getParam()[8],
                    Integer.parseInt(data.getParam()[9]), Long.parseLong(data.getParam()[10])), data.getUser().getLogin()));
            return Main.ANSI_YELLOW + "Новый элемент был добавлен!\n" + Main.ANSI_RESET;
        } catch (SQLException e) {
            System.out.println("Ошибка добавления flat");
        }
        return null;
    }

    public int addCoordinates() {
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.addCoordinates);

            ps.setLong(1, Long.parseLong(data.getParam()[1]));
            ps.setInt(2, Integer.parseInt(data.getParam()[2]));

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

            ps.setString(1, data.getParam()[8]);
            ps.setInt(2, Integer.parseInt(data.getParam()[9]));
            ps.setLong(3, Long.parseLong(data.getParam()[10]));

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
