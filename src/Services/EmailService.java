package Services;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {
    private static EmailService instance;
    private Map<String, TokenRecuperacion> tokensActivos;

    // Configuración del servidor SMTP
    private final String SMTP_HOST = "smtp.gmail.com";
    private final String SMTP_PORT = "587";
    private final String EMAIL_FROM = "tu_email@gmail.com"; // Configurar
    private final String EMAIL_PASSWORD = "tu_contraseña_app"; // Configurar

    private EmailService() {
        tokensActivos = new HashMap<>();
    }

    public static synchronized EmailService getInstance() {
        if (instance == null) {
            instance = new EmailService();
        }
        return instance;
    }

    // Generar token aleatorio
    private String generarToken() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            token.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }

        return token.toString();
    }

    // Enviar email de recuperación
    public boolean enviarEmailRecuperacion(String emailDestino, String nombreUsuario) {
        String token = generarToken();

        // Guardar token con expiración de 15 minutos
        TokenRecuperacion tokenObj = new TokenRecuperacion(token, nombreUsuario,
                LocalDateTime.now().plusMinutes(15));
        tokensActivos.put(token, tokenObj);

        // Limpiar tokens expirados
        limpiarTokensExpirados();

        String asunto = "Recuperación de Contraseña - Juguetería";
        String cuerpo = construirCuerpoEmail(nombreUsuario, token);

        return enviarEmail(emailDestino, asunto, cuerpo);
    }

    private String construirCuerpoEmail(String nombreUsuario, String token) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; }\n" +
                "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }\n" +
                "        .header { background: #4CAF50; color: white; padding: 20px; text-align: center; }\n" +
                "        .content { padding: 20px; background: #f9f9f9; }\n" +
                "        .token { font-size: 32px; font-weight: bold; color: #4CAF50; " +
                "                 letter-spacing: 5px; text-align: center; padding: 20px; " +
                "                 background: white; border: 2px dashed #4CAF50; margin: 20px 0; }\n" +
                "        .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class='container'>\n" +
                "        <div class='header'>\n" +
                "            <h1>🎮 Recuperación de Contraseña</h1>\n" +
                "        </div>\n" +
                "        <div class='content'>\n" +
                "            <p>Hola <strong>" + nombreUsuario + "</strong>,</p>\n" +
                "            <p>Hemos recibido una solicitud para restablecer tu contraseña.</p>\n" +
                "            <p>Tu código de verificación es:</p>\n" +
                "            <div class='token'>" + token + "</div>\n" +
                "            <p><strong>⏰ Este código es válido por 15 minutos.</strong></p>\n" +
                "            <p>Si no solicitaste este cambio, ignora este correo.</p>\n" +
                "        </div>\n" +
                "        <div class='footer'>\n" +
                "            <p>© 2025 Sistema de Juguetería. Todos los derechos reservados.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    private boolean enviarEmail(String destinatario, String asunto, String cuerpo) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.trust", SMTP_HOST);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setContent(cuerpo, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Email enviado exitosamente a: " + destinatario);
            return true;

        } catch (MessagingException e) {
            System.err.println("Error al enviar email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Validar token
    public boolean validarToken(String token, String nombreUsuario) {
        TokenRecuperacion tokenObj = tokensActivos.get(token);

        if (tokenObj == null) {
            return false; // Token no existe
        }

        if (tokenObj.estaExpirado()) {
            tokensActivos.remove(token);
            return false; // Token expirado
        }

        if (!tokenObj.getNombreUsuario().equals(nombreUsuario)) {
            return false; // Token no corresponde al usuario
        }

        return true;
    }

    // Consumir token (usar una sola vez)
    public boolean consumirToken(String token) {
        TokenRecuperacion tokenObj = tokensActivos.remove(token);
        return tokenObj != null && !tokenObj.estaExpirado();
    }

    private void limpiarTokensExpirados() {
        tokensActivos.entrySet().removeIf(entry -> entry.getValue().estaExpirado());
    }

    // Clase interna para manejar tokens
    private static class TokenRecuperacion {
        private String token;
        private String nombreUsuario;
        private LocalDateTime expiracion;

        public TokenRecuperacion(String token, String nombreUsuario, LocalDateTime expiracion) {
            this.token = token;
            this.nombreUsuario = nombreUsuario;
            this.expiracion = expiracion;
        }

        public boolean estaExpirado() {
            return LocalDateTime.now().isAfter(expiracion);
        }

        public String getNombreUsuario() {
            return nombreUsuario;
        }
    }
}
