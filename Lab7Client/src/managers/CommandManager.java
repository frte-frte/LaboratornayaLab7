package managers;

import application.Client;
import data.Data;
import data.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private String[] command;
    private User user;
    List<String> recList = new ArrayList<>();
    public CommandManager(String[] command, User user) {
        this.command = command;
        this.user = user;
    }

    public Data commandToObj() throws IOException {
        try {
            switch (command[0]) {
                case ("help") :
                case ("info") :
                case ("show") :
                case ("clear") :
                case ("remove_first") :
                case ("remove_head") :
                case ("print_field_descending_transport") :
                    if (command.length == 1) {
                        return (new Data(command[0],null, user));
                    } else {
                        System.out.println(Client.ANSI_YELLOW + "У этой команды нет аргументов\n" + Client.ANSI_RESET);
                        break;
                    }
                case ("remove_by_id") :
                    try {
                        if (command.length == 2){
                            Long id = Long.parseLong(command[1]);
                            return (new Data(command[0], new String[]{command[1]}, user));
                        } else {
                            System.out.println(Client.ANSI_YELLOW + "Неверное количество аргументов для команды!\n" + Client.ANSI_RESET);
                            break;
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println(Client.ANSI_YELLOW + "Неверные данные!\n" + Client.ANSI_RESET);
                        break;
                    }
                case ("add") :
                    try {
                        if (command.length == 12) {
                            Long x = Long.parseLong(command[2]);
                            Integer y = Integer.parseInt(command[3]);
                            Double area = Double.parseDouble(command[4]);
                            Long numberOfRooms = Long.parseLong(command[5]);
                            Furnish furnish = Furnish.valueOf(command[6]);
                            View view = View.valueOf(command[7]);
                            Transport transport = Transport.valueOf(command[8]);
                            Integer year = Integer.parseInt(command[10]);
                            Long numberOfFlatsOnFloor = Long.parseLong(command[11]);
                            return (new Data(command[0], new String[]{command[1], command[2], command[3], command[4],
                                    command[5], command[6], command[7], command[8], command[9],command[10], command[11]}, user));
                        } else {
                            System.out.println(Client.ANSI_YELLOW + "Неверное количество аргументов для команды!\n" + Client.ANSI_RESET);
                            break;
                        }
                    } catch (IllegalArgumentException exception) {
                        System.out.println(Client.ANSI_YELLOW + "Неверные данные!\n" + Client.ANSI_RESET);
                        break;
                    }
                case ("update") :
                case ("add_if_min") :
                    try {
                        if (command.length == 13) {
                            Long id = Long.parseLong(command[1]);
                            Long x = Long.parseLong(command[3]);
                            Integer y = Integer.parseInt(command[4]);
                            Double area = Double.parseDouble(command[5]);
                            Long numberOfRooms = Long.parseLong(command[6]);
                            Furnish furnish = Furnish.valueOf(command[7]);
                            View view = View.valueOf(command[8]);
                            Transport transport = Transport.valueOf(command[9]);
                            Integer year = Integer.parseInt(command[11]);
                            Long numberOfFlatsOnFloor = Long.parseLong(command[12]);
                            return (new Data(command[0], new String[]{command[1], command[2], command[3], command[4],
                                    command[5], command[6], command[7], command[8], command[9], command[10], command[11],
                                    command[12]}, user));
                        } else {
                            System.out.println(Client.ANSI_YELLOW + "Неверное количество аргументов для команды!\n" + Client.ANSI_RESET);
                            break;
                        }
                    } catch (IllegalArgumentException exception) {
                        System.out.println(Client.ANSI_YELLOW + "Неверные данные!\n" + Client.ANSI_RESET);
                        break;
                    }
                case ("filter_by_view") :
                    try {
                        View view = View.valueOf(command[1]);
                        return (new Data(command[0], new String[]{command[1]}, user));
                    } catch (IllegalArgumentException exception) {
                        System.out.println(Client.ANSI_YELLOW + "Неверные данные!\n" + Client.ANSI_RESET);
                        break;
                    }
                case ("filter_starts_with_name") :
                    if (command.length == 2) {
                        return (new Data(command[0], new String[]{command[1]}, user));
                    } else {
                        System.out.println(Client.ANSI_YELLOW + "Неверное количество аргументов для команды!\n" + Client.ANSI_RESET);
                        break;
                    }
                case ("exit") :
                    if (command.length == 1) {
                        System.out.println(Client.ANSI_YELLOW + "Клиент завершил работу!" + Client.ANSI_RESET);
                        System.exit(0);
                        break;
                    } else {
                        System.out.println(Client.ANSI_YELLOW + "У этой команды нет аргументов\n" + Client.ANSI_RESET);
                        break;
                    }
                case ("execute_script") :
                    String fileName = command[1];
                    StringBuilder text = new StringBuilder();
                    if (checkExecuteScript(fileName)) {
                        recList.clear();
                        System.out.println(Client.ANSI_RED + "Обнаружен рекурсивный вызов!" + Client.ANSI_RESET);
                    } else {
                        recList.add(fileName);
                        ClientManager clientManager = new ClientManager();
                        try (FileReader reader = new FileReader(fileName)) {
                            while (reader.ready()) {
                                int t = reader.read();
                                text.append((char) t);
                            }
                            reader.close();
                            if (!text.isEmpty()) {
                                String[] commandList = text.toString().split("\n");
                                for (String commands : commandList) {
                                    String[] command = commands.replace("\r", "").split(" ");
                                    ArrayList<String> arrayList = new ArrayList<>();
                                    for (String param : command) {
                                        arrayList.add(param);
                                    }
                                    arrayList.remove(0);
                                    String[] commandParam = arrayList.toArray(new String[arrayList.size()]);
                                    Data serverData = new Data(command[0], commandParam, user);
                                    clientManager.send(serverData);
                                    Thread.sleep(100);
                                }
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                default :
                    System.out.println(Client.ANSI_YELLOW + "Неизвестная команда! Напишите [help] для вывода списка команд.\n" + Client.ANSI_RESET);
                    break;
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            System.out.println(Client.ANSI_YELLOW + "Неверное количество аргументов для команды!\n" + Client.ANSI_RESET);
        }
        return null;
    }

    public String[] getCommand() {
        return command;
    }

    public void setCommand(String[] command) {
        this.command = command;
    }

    public boolean checkExecuteScript(String filePath) {
        return this.recList.contains(filePath);
    }
}

