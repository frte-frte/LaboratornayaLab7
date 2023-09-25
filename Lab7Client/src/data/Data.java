package data;

import java.io.Serializable;
import java.util.Arrays;

public class Data implements Serializable {
    private String commandName;
    private String[] param;
    private final User user;
    public Data(String commandName, String[] param, User user) {
        this.commandName = commandName;
        this.param = param;
        this.user = user;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getParam() {
        return param;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Data{" +
                "commandName='" + commandName + '\'' +
                ", param=" + Arrays.toString(param) +
                ", user=" + user +
                '}';
    }
}
