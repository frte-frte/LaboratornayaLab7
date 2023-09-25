package commands;

import application.Main;
import data.Data;
import managers.CollectionManager;

import java.sql.Connection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HelpCommand extends Command{
    private final Map<String, String> manual;

    public HelpCommand (CollectionManager collectionManager, Data data, Connection connection) {
        super(collectionManager, data, connection);
        manual = new HashMap<>();
        manual.put("help", ": вывести справку по доступным командам");
        manual.put("info", ": вывести в стандартный поток вывода информацию о коллекции");
        manual.put("show", ": вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        manual.put("add ", ": добавить новый элемент в коллекцию");
        manual.put("update id {element}", ": обновить значение элемента коллекции, id которого равен заданному");
        manual.put("remove_by_id id", ": удалить элемент из коллекции по его id");
        manual.put("clear", ": очистить коллекцию");
        manual.put("execute_script file_name", ": считать и исполнить скрипт из указанного файла.");
        manual.put("exit", ": завершить программу");
        manual.put("remove_first", ": удалить первый элемент из коллекции");
        manual.put("remove_head", ": вывести первый элемент коллекции и удалить его");
        manual.put("add_if_min {element}", ": добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции");
        manual.put("filter_by_view view", ": вывести элементы, значение поля view которых равно заданному");
        manual.put("filter_starts_with_name name", ": вывести элементы, значение поля name которых начинается с заданной подстроки");
        manual.put("print_field_descending_transport", ": вывести значения поля transport всех элементов в порядке убывания");
    }

    public String execute() {
        StringBuilder helpCommand = new StringBuilder();
        for (Map.Entry<String, String> pair : manual.entrySet()) {
            helpCommand.append(Main.ANSI_GREEN + pair.getKey() + Main.ANSI_RESET).append(pair.getValue()).append("\n");
        }
        return helpCommand.toString();
    }
}
