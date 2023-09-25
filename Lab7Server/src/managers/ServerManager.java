package managers;

import application.Main;
import data.Data;

import java.io.*;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerManager {
    private static final int PORT = Main.getPORT();
    private static final int BUFFER_SIZE = 65536;
    private final int THREAD_COUNT = 10;
    private DatagramChannel channel;
    private Selector selector;
    private ByteBuffer receiveBuffer;
    private ByteBuffer sendBuffer;
    private ExecutorService receiverThread;
    private ExecutorService processThread;

    public ServerManager() {
        openServer();
    }

    public void start(CollectionManager collectionManager, DatabaseManager databaseManager) {
        ExecutorService receiverThread = Executors.newCachedThreadPool();
        ExecutorService processThread = Executors.newFixedThreadPool(THREAD_COUNT);
        while (true) {
            try {
                selector.select();
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()){
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        DatagramChannel channel = (DatagramChannel) key.channel();
                        InetSocketAddress clientAddress = (InetSocketAddress) channel.receive(receiveBuffer);
                        receiverThread.execute(() -> {
                            Data clientData = receive(clientAddress);
                            processThread.execute(() -> {
                                Data serverData = new Data("serverData",
                                        new String[]{new CommandManager(clientData).serverWork(collectionManager, databaseManager.getConnection())},
                                        clientData.getUser());


                                Thread sender = new Thread(() -> {
                                    try {
                                        try {
                                            sendBuffer.clear();

                                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                            ObjectOutputStream oos = new ObjectOutputStream(bos);

                                            oos.writeObject(serverData);
                                            oos.flush();

                                            sendBuffer = ByteBuffer.wrap(bos.toByteArray());
                                            channel.send(sendBuffer, clientAddress);

                                        } catch (IOException exception) {
                                            System.out.println("Сервер недоступен");
                                        }
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                                sender.start();
                            });
                        });
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void openServer() {
        try {
            DatagramChannel channel = DatagramChannel.open();
            channel.socket().bind(new InetSocketAddress(PORT));
            channel.configureBlocking(false);

            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);

            receiveBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            sendBuffer = ByteBuffer.allocate(BUFFER_SIZE);

            System.out.println(Main.ANSI_GREEN + "Сервер запущен! Ожидание подключений по порту: "
                    + Main.ANSI_RESET + PORT);
        } catch (BindException e) {
            System.out.println(Main.ANSI_RED + "Порт " + PORT + " уже используется!" + Main.ANSI_RESET);
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Data receive(SocketAddress clientAddress) {
        try {
            receiveBuffer.clear();
            ByteArrayInputStream bis = new ByteArrayInputStream(receiveBuffer.array());
            ObjectInputStream ois = new ObjectInputStream(bis);
            Data clientData = (Data) ois.readObject();
            System.out.println(Main.ANSI_GREEN + "Получено сообщение от клиента " + Main.ANSI_RESET + clientAddress + ": "
                    + clientData);
            return clientData;
        } catch(IOException e) {
            System.out.println(Main.ANSI_RED + "Сервер недоступен" + Main.ANSI_RESET);
            return null;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(Data serverData, SocketAddress clientAddress) {
        try {
            sendBuffer.clear();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(serverData);
            oos.flush();

            sendBuffer = ByteBuffer.wrap(bos.toByteArray());
            channel.send(sendBuffer, clientAddress);

        } catch (IOException exception) {
            System.out.println("Сервер недоступен");
        }
    }

    public ByteBuffer getReceiveBuffer() {
        return receiveBuffer;
    }

    public ByteBuffer getSendBuffer() {
        return sendBuffer;
    }
}