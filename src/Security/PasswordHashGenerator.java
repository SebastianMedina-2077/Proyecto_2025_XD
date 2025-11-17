package Security;

import Security.PasswordEncryption;
import Services.EmailService;
import raven.alerts.MessageAlerts;
import raven.toast.Notifications;

import java.security.NoSuchAlgorithmException;

public class PasswordHashGenerator {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        PasswordEncryption encryption = new PasswordEncryption();

        System.out.println("HASHES DE CONTRASEÑAS:");
        System.out.println("admin123: " + encryption.encriptar("admin123"));
        System.out.println("vendedor123: " + encryption.encriptar("vendedor123"));
        System.out.println("almacenero123: " + encryption.encriptar("Marmota25"));

        // Código de prueba en algún método main:
        EmailService service = EmailService.getInstance();
        boolean enviado = service.enviarEmailRecuperacion(
                "kernelred20@gmail.com",
                "Capibara25"
        );



        if (enviado) {
            System.out.println("✓ Email enviado exitosamente");
        } else {
            System.out.println("✗ Error al enviar email");
        }
    }


}
