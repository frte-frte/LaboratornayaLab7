package commands;

import application.Main;
import data.Data;
import data.Flat;
import data.View;
import managers.CollectionManager;

import java.sql.Connection;
import java.util.ArrayDeque;
import java.util.Collections;

public class FilterByViewCommand extends Command{
    public FilterByViewCommand(CollectionManager collectionManager, Data data, Connection connection) {
        super(collectionManager, data, connection);
    }

    public String execute() {
        ArrayDeque<Flat> collection = CollectionManager.getCollection();
        StringBuilder filterByView = new StringBuilder();
        for (Flat flat : collection) {
            if (flat.getView() == View.valueOf(data.getParam()[0])) {
                filterByView.append(flat).append("\n");
            }
        }
        if (filterByView.isEmpty()) {
            return Main.ANSI_YELLOW + "Нет элементов с таким полем!\n" + Main.ANSI_RESET;
        }
        return filterByView.toString();
    }

}
