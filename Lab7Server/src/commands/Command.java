package commands;

import data.Data;
import managers.CollectionManager;

import java.sql.Connection;

public abstract class Command {
    protected CollectionManager collectionManager;
    protected Data data;
    protected Connection connection;

    public Command(CollectionManager collectionManager, Data data, Connection connection) {
        this.collectionManager = collectionManager;
        this.data = data;
        this.connection = connection;
    }
}
