package commands;

import application.Main;
import data.Data;
import managers.CollectionManager;
import managers.DatabaseCommands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogInCommand extends Command {

    public LogInCommand (CollectionManager collectionManager, Data data, Connection connection) {
        super (collectionManager, data, connection);
    }

    public String execute() {
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.getUser);
            ps.setString(1, data.getUser().getLogin());
            ResultSet result = ps.executeQuery();
            if (!result.isBeforeFirst()) {
                return Main.ANSI_YELLOW + "Пользователь " + data.getUser().getLogin() + " не зарегистрирован!\n" + Main.ANSI_RESET;
            } else {
                result.next();
                String password = result.getString(3);
                if (password.equals(data.getUser().getPassword())) {
                    return Main.ANSI_GREEN + "Пользователь " + data.getUser().getLogin() + " успешно авторизован!\n" + Main.ANSI_RESET;
                } else {
                    return Main.ANSI_RED + "Не верный пароль!\n" + Main.ANSI_RESET;
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка чтения таблицы!");
        }
        return null;
    }
}
