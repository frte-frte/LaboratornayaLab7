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
import java.util.ArrayDeque;

public class ClearCommand extends Command{
    public ClearCommand(CollectionManager collectionManager, Data data, Connection connection){
        super(collectionManager, data, connection);
    }

    public String execute() {
        ArrayDeque<Flat> collection = CollectionManager.getCollection();
        if (collection.isEmpty()) {
            return Main.ANSI_YELLOW + "Коллекция пуста\n" + Main.ANSI_RESET;
        } else {
            collection.clear();
            try {
                PreparedStatement ps = connection.prepareStatement(DatabaseCommands.getUser);
                ps.setString(1, data.getUser().getLogin());
                ResultSet result = ps.executeQuery();
                result.next();
                int id_owner =  result.getInt(1);

                PreparedStatement ps1 = connection.prepareStatement(DatabaseCommands.deleteAllObjects);
                ps1.setInt(1, id_owner);

                ps1.execute();
            } catch (SQLException e) {
                System.out.println("Ошибка удаления таблицы!");;
            }
            return Main.ANSI_YELLOW + "Коллекция была отчищена!\n" + Main.ANSI_RESET;
        }
    }
}
