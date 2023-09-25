package commands;

import application.Main;
import data.Data;
import data.Flat;
import data.Transport;
import managers.CollectionManager;

import java.sql.Connection;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrintFieldDescendingTransportCommand extends Command{
    public PrintFieldDescendingTransportCommand(CollectionManager collectionManager, Data data, Connection connection) {
        super(collectionManager, data, connection);
    }

    public String execute() {
        ArrayDeque<Flat> collection = CollectionManager.getCollection();
        if (collection.isEmpty()) {
            return Main.ANSI_YELLOW + "Колекция пуста\n" + Main.ANSI_RESET;
        } else {
            List<Transport> transportsType = new ArrayList<>();
            for (Flat flat : collection) {
                transportsType.add(flat.getTransport());
            }
            Collections.sort(transportsType);
            StringBuilder stringBuilder = new StringBuilder();
            for (Transport transport : transportsType) {
                stringBuilder.append(transport).append("\n");
            }
            return stringBuilder.toString();
        }
    }
}
