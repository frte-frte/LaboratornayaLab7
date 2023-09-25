package commands;

import application.Main;
import data.Data;
import data.Flat;
import managers.CollectionManager;
import managers.DatabaseCommands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RemoveHeadCommand extends Command{
    public RemoveHeadCommand(CollectionManager collectionManager, Data data, Connection connection) {
        super(collectionManager, data, connection);
    }

    public String execute() {
        try {
            ArrayDeque<Flat> collection = CollectionManager.getCollection();
            if (collection.isEmpty()) {
                return Main.ANSI_YELLOW + "Коллекция пуста!\n" + Main.ANSI_RESET;
            } else {
                PreparedStatement ps = connection.prepareStatement(DatabaseCommands.getUser);
                ps.setString(1, data.getUser().getLogin());

                ResultSet result = ps.executeQuery();
                result.next();

                int id_owner = result.getInt(1);

                PreparedStatement ps1 = connection.prepareStatement(DatabaseCommands.getFlat);
                ps1.setInt(1, id_owner);

                ResultSet result1 = ps1.executeQuery();
                List<Integer> listId = new ArrayList<>();
                while (result1.next()) {
                    listId.add(result1.getInt(1));
                }
                int minId = Collections.min(listId);

                PreparedStatement ps2 = connection.prepareStatement(DatabaseCommands.deleteObject);
                ps2.setInt(1, minId);

                ps2.execute();

                Flat flatFirst = getFirstFlat(collection, minId);
                for (Flat flat : collection) {
                    if (flat.getId() == minId) {
                        collection.remove(flat);
                    }
                }
                return flatFirst + "\n" + Main.ANSI_YELLOW + "Первый элемент коллекции был удалён!\n" + Main.ANSI_RESET;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Flat getFirstFlat(ArrayDeque<Flat> collection, int minId) {
        for (Flat flat : collection) {
            if (flat.getId() == minId) {
                return flat;
            }
        }
        return null;
    }
}
