package commands;

import application.Main;
import data.Data;
import data.Flat;
import managers.CollectionManager;

import java.sql.Connection;
import java.util.ArrayDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterStartsWithNameCommand extends Command{
    public FilterStartsWithNameCommand(CollectionManager collectionManager, Data data, Connection connection) {
        super(collectionManager, data, connection);
    }

    public String execute() {
        ArrayDeque<Flat> collection = CollectionManager.getCollection();
        StringBuilder filterStartsWithName = new StringBuilder();
        Pattern pattern = Pattern.compile("^" + data.getParam()[0]);
        for (Flat flat : collection) {
            Matcher matcher = pattern.matcher(flat.getName());
            if (matcher.find()) {
                filterStartsWithName.append(flat).append("\n");
            }
        }
        if (filterStartsWithName.isEmpty()) {
            return Main.ANSI_YELLOW + "Нет элементов, значение поля name, которых начинается с заданной подстроки: '" + data.getParam()[0]
                    + "'!\n" + Main.ANSI_RESET;
        } else {
            return filterStartsWithName.toString();
        }
    }
}
