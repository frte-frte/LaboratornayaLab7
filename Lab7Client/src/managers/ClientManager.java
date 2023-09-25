package managers;

import application.Client;
import data.Data;
import data.User;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientManager {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = Client.getPORT();
    private static final int BUFFER_SIZE = 65536;
    private final DatagramChannel channel;
    private User user;
    private final ByteBuffer sendBuffer;
    private final ByteBuffer receiveBuffer;
    Scanner scanner = new Scanner(System.in);

    public ClientManager() {
        try {
            channel = DatagramChannel.open();
            channel.configureBlocking(false);

            sendBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            receiveBuffer = ByteBuffer.allocate(BUFFER_SIZE);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public User startLog() {
        User user = null;
        System.out.println(Client.ANSI_GREEN + "Клиент запущен." + Client.ANSI_RESET);

        while (user == null) {
                System.out.println(Client.ANSI_GREEN + "Войдите в аккаунт (log_in) или зарегистрируйтесь (reg)" + Client.ANSI_RESET);
                String regCommand = scanner.nextLine().trim();

                switch (regCommand) {
                    case ("reg"):
                        user = reg();
                        break;
                    case ("log_in"):
                        user = logIn();
                        break;
                    case ("exit"):
                        System.out.println(Client.ANSI_YELLOW + "Клиент закончил работу!" + Client.ANSI_RESET);
                        System.exit(0);
                    default:
                        System.out.println(Client.ANSI_YELLOW + "Неизвестная команда!" + Client.ANSI_RESET);
                }
             }
        return user;
        }

    public void start() {
        user = startLog();
        try {
            while (true) {
                System.out.println("Введите команду:");
                String commands = scanner.nextLine().trim();
                String[] command = commands.split(" ");
                if (!command[0].equals("execute_script")) {
                    Data clientData = new CommandManager(command, user).commandToObj();
                    if (clientData != null) {
                        send(clientData);
                        System.out.println(receive());
                    }
                } else {
                    new CommandManager(command, user).commandToObj();
                }
            }
        } catch (NoSuchElementException exception) {
            System.out.println("\u001B[33m" + "Введена пустая строка!" + "\u001B[0m");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        scanner.close();
    }

    public void send(Data clientData) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(clientData);
            oos.flush();

            ByteBuffer sendBuffer = ByteBuffer.wrap(bos.toByteArray());
            channel.send(sendBuffer, new InetSocketAddress(SERVER_IP, SERVER_PORT));
        } catch (IOException e) {
//            System.out.println("Сервер недоступен");
            throw new RuntimeException(e);
        }
    }

    public String receive() throws IOException, ClassNotFoundException {
        Selector selector = this.channel.provider().openSelector();

        this.channel.register(selector, SelectionKey.OP_READ);

        while (true) {
            if (selector.select(100000) == 0) {
                throw new SocketTimeoutException("Сервер недоступен");
            }

            Iterator keys = selector.selectedKeys().iterator();

            while (keys.hasNext()) {
                SelectionKey key = (SelectionKey) keys.next();

                if (!key.isValid()) {
                    continue;
                }

                if (key.isReadable()) {
                    receiveBuffer.clear();
                    SocketAddress serverAddress = channel.receive(receiveBuffer);

                    ByteArrayInputStream byteStream = new ByteArrayInputStream(receiveBuffer.array());
                    ObjectInputStream objStream = new ObjectInputStream(byteStream);

                    Data response = (Data) objStream.readObject();
                    keys.remove();
                    return "Получен ответ от сервера:\n" + response.getParam()[0];
                }
            }
        }
    }

    public User reg() {
        try {
            System.out.println(Client.ANSI_GREEN + "Введите логин:" + Client.ANSI_RESET);
            String login = scanner.nextLine();
            System.out.println(Client.ANSI_GREEN + "Введите пароль:" + Client.ANSI_RESET);
            String password = scanner.nextLine();
            User user = new User(login, password);
            Data data = new Data("reg", new String[]{}, user);
            send(data);
            String serverAnswer = receive();
            System.out.println(serverAnswer);
            Pattern pattern = Pattern.compile("уже существует");
            Matcher matcher = pattern.matcher(serverAnswer);
            if (matcher.find()) {
                return null;
            } else {
                return user;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка регистрации!\n");
        }
        return null;
    }

    public User logIn() {
        try {
            System.out.println(Client.ANSI_GREEN + "Введите логин:" + Client.ANSI_RESET);
            String login = scanner.nextLine();
            System.out.println(Client.ANSI_GREEN + "Введите пароль:" + Client.ANSI_RESET);
            String password = scanner.nextLine();
            User user = new User(login, password);
            Data data = new Data("log_in", new String[]{}, user);
            send(data);
            String serverAnswer = receive();
            System.out.println(serverAnswer);
            Pattern pattern1 = Pattern.compile("Не верный пароль!");
            Pattern pattern2 = Pattern.compile("не зарегистрирован");
            Matcher matcher1 = pattern1.matcher(serverAnswer);
            Matcher matcher2 = pattern2.matcher(serverAnswer);
            if (matcher1.find() | matcher2.find()) {
                return null;
            } else {
                return user;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка входа!\n");
        }
        return null;
    }

    public ByteBuffer getSendBuffer() {
        return sendBuffer;
    }

    public ByteBuffer getReceiveBuffer() {
        return receiveBuffer;
    }
}
