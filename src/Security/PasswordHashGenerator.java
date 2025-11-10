package Security;

import Security.PasswordEncryption;
import java.security.NoSuchAlgorithmException;

public class PasswordHashGenerator {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        PasswordEncryption encryption = new PasswordEncryption();

        System.out.println("HASHES DE CONTRASEÑAS:");
        System.out.println("admin123: " + encryption.encriptar("admin123"));
        System.out.println("vendedor123: " + encryption.encriptar("vendedor123"));
        System.out.println("almacenero123: " + encryption.encriptar("almacenero123"));
    }
}
