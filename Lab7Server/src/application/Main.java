package application;

import managers.CollectionManager;
import managers.DatabaseManager;
import managers.ServerManager;

public class Main {
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    private static Integer PORT;
    private static final String PostgresSQL_URL = "jdbc:postgresql://localhost:5432/studs";

    public static void main(String[] args) {
        try{
            PORT = Integer.parseInt(args[0]);
            DatabaseManager databaseManager = new DatabaseManager();
            CollectionManager collectionManager = new CollectionManager(databaseManager);
            ServerManager server = new ServerManager();
            server.start(collectionManager, databaseManager);
        } catch (ArrayIndexOutOfBoundsException exception) {
            System.out.println("\u001B[31m" + "Используйте [java -jar Server.jar PORT] для коректного использования." + "\u001B[31m");
        }
    }

    public static String getPostgresSQL_URL() {
        return PostgresSQL_URL;
    }

    public static Integer getPORT() {
        return PORT;
    }
}
