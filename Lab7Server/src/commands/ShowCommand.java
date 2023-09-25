package commands;

import application.Main;
import data.Data;
import data.Flat;
import managers.CollectionManager;

import java.sql.Connection;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShowCommand extends Command {
    public ShowCommand(CollectionManager collectionManager, Data data, Connection connection) {
        super(collectionManager, data, connection);
    }

    public String execute() {
        StringBuilder showCommand = new StringBuilder();
        ArrayDeque<Flat> collection = CollectionManager.getCollection();
        if (collection.isEmpty()) {
            return Main.ANSI_YELLOW + "Коллекция пуста!\n" + Main.ANSI_RESET;
        } else {
            List<Flat> sortCollection = new ArrayList<>(collection);
            Collections.sort(sortCollection);
            collection.removeIf(element -> true);
            collection.addAll(sortCollection);
            for (Flat flat : collection) {
                showCommand.append(flat.toString()).append("\n");
            }
            return showCommand.toString();
        }
    }

}
