package Security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordEncryption {
    // Encriptar con SHA-256
    public String encriptar(String contrasena) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(contrasena.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }

    // Verificar contraseña
    public boolean verificar(String contrasena, String hashAlmacenado) {
        try {
            String hashNuevo = encriptar(contrasena);
            return hashNuevo.equals(hashAlmacenado);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Generar contraseña aleatoria
    public String generarContrasenaAleatoria(int longitud) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        StringBuilder password = new StringBuilder();
        java.security.SecureRandom random = new java.security.SecureRandom();

        for (int i = 0; i < longitud; i++) {
            password.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }

        return password.toString();
    }
}
