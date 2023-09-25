package managers;

import commands.*;
import data.Data;

import java.sql.Connection;

public class CommandManager {
    private Data data;

    public CommandManager(Data data) {
        this.data = data;
    }

    public String serverWork(CollectionManager collectionManager, Connection connection) {
        switch (data.getCommandName()) {
            case ("help"):
                return new HelpCommand(collectionManager, data, connection).execute();
            case ("info"):
                return new InfoCommand(collectionManager, data, connection).execute();
            case ("clear"):
                return new ClearCommand(collectionManager, data, connection).execute();
            case ("show"):
                return new ShowCommand(collectionManager, data, connection).execute();
            case ("remove_first"):
                return new RemoveFirstCommand(collectionManager, data, connection).execute();
            case ("remove_head"):
                return new RemoveHeadCommand(collectionManager, data, connection).execute();
            case ("add"):
                return new AddCommand(collectionManager, data, connection).execute();
            case ("update"):
                return new UpdateCommand(collectionManager, data, connection).execute();
            case ("remove_by_id") :
                return new RemoveByIdCommand(collectionManager, data, connection).execute();
            case ("add_if_min") :
                return new AddIfMinCommand(collectionManager, data, connection).execute();
            case ("filter_by_view") :
                return new FilterByViewCommand(collectionManager, data, connection).execute();
            case ("filter_starts_with_name") :
                return new FilterStartsWithNameCommand(collectionManager, data, connection).execute();
            case ("print_field_descending_transport") :
                return  new PrintFieldDescendingTransportCommand(collectionManager, data, connection).execute();
            case ("reg") :
                return new RegistrationCommand(collectionManager, data, connection).execute();
            case ("log_in") :
                return new LogInCommand(collectionManager, data, connection).execute();
        }
        return null;
    }
}
