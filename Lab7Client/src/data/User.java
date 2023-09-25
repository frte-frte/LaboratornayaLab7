package data;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User implements Serializable {
    private final String login;
    private final String password;

    public User(String login, String password) {
        this.login = login;
        this.password = hashPassword(password);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public static String hashPassword(String password) {
        try{
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] hashedByte = sha1.digest(password.getBytes());

            StringBuilder passwordBuilder = new StringBuilder();
            for (byte b : hashedByte) {
                passwordBuilder.append(String.format("%02x", b));
            }

            return passwordBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Не существует указаного алгоритма для хеширования");
            return null;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
