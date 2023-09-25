package application;

import managers.ClientManager;

public class Client {
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    private static int PORT;

    public static void main(String[] args) {
        try {
            PORT = Integer.parseInt(args[0]);
            if (PORT < 1023 || PORT > 65535) {
                System.out.println("\u001B[31m" + "Выбран несуществующий или занятый сисемой порт!" + "\u001B[0m");
                System.exit(1);
            }
            ClientManager client = new ClientManager();
            client.start();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(ANSI_RED + "Порт для подключения не указан!" + ANSI_RESET);
            System.exit(1);
        }
    }

    public static int getPORT() {
        return PORT;
    }
}
