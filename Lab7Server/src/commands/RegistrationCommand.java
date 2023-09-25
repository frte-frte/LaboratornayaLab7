package commands;

import application.Main;
import data.Data;
import managers.CollectionManager;
import managers.DatabaseCommands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegistrationCommand extends Command {

    public RegistrationCommand (CollectionManager collectionManager, Data data, Connection connection) {
        super(collectionManager, data, connection);
    }

    public String execute() {
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.getAllUsers);
            ResultSet result = ps.executeQuery();
            List<String> users = new ArrayList<>();
            while (result.next()) {
                users.add(result.getString(2));
            }
            if (users.contains(data.getUser().getLogin())) {
                return Main.ANSI_YELLOW + "Пользователь " + data.getUser().getLogin() + " уже существует\n!" + Main.ANSI_RESET;
            } else {
                PreparedStatement ps1 = connection.prepareStatement(DatabaseCommands.addUser);

                ps1.setString(1, data.getUser().getLogin());
                ps1.setString(2, data.getUser().getPassword());

                ps1.execute();

                return Main.ANSI_GREEN + "Пользователь " + data.getUser().getLogin() + " успешно зарегистрирован!\n" + Main.ANSI_RESET;
            }
        } catch (SQLException e) {
            System.out.println("Ошибка чтения таблицы!");
        }
        return null;
    }
}
