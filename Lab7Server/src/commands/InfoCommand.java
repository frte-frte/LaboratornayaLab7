package commands;

import application.Main;
import data.Data;
import managers.CollectionManager;

import java.sql.Connection;

public class InfoCommand extends Command{
    public InfoCommand(CollectionManager collectionManager, Data data, Connection connection) {
        super(collectionManager, data, connection);
    }

    public String execute() {
        return Main.ANSI_GREEN + "Тип коллекции" + Main.ANSI_RESET + ": " +
                CollectionManager.getCollection().getClass() +
                "\n" +
                Main.ANSI_GREEN + "Время инициализации" + Main.ANSI_RESET + ": " +
                collectionManager.getInitializationTime() +
                "\n" +
                Main.ANSI_GREEN + "Количество элементов" + Main.ANSI_RESET + ": " + CollectionManager.getCollection().size() + "\n";
    }
}
