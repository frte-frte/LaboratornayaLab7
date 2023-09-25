package commands;

import application.Main;
import data.Data;
import data.Flat;
import managers.CollectionManager;
import managers.DatabaseCommands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayDeque;

public class RemoveByIdCommand extends Command{
    public RemoveByIdCommand(CollectionManager collectionManager, Data data, Connection connection) {
        super(collectionManager, data, connection);
    }

    public String execute() {
        ArrayDeque<Flat> collection = CollectionManager.getCollection();
        for (Flat flat : collection) {
            if (flat.getId() == Long.parseLong(data.getParam()[0]) & flat.getOwnerLogin().equals(data.getUser().getLogin())) {
                try {
                    PreparedStatement ps = connection.prepareStatement(DatabaseCommands.deleteObject);
                    ps.setLong(1, Long.parseLong(data.getParam()[0]));
                    ps.execute();

                    collection.remove(flat);
                    return Main.ANSI_YELLOW + "Объект с id = " + data.getParam()[0] + " был удалён" + Main.ANSI_RESET + "\n";
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if (flat.getId() == Long.parseLong(data.getParam()[0]) & !flat.getOwnerLogin().equals(data.getUser().getLogin())) {
                return Main.ANSI_YELLOW + "У вас нет прав для удаления элемента" + Main.ANSI_RESET + "\n";
            } else {
                return Main.ANSI_YELLOW + "Нет элемента с id = "+ data.getParam()[0] + Main.ANSI_RESET + "\n";
            }
        }
        return null;
    }
}
