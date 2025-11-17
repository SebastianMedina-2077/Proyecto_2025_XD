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

    private final String SMTP_HOST = "smtp.gmail.com";
    private final String SMTP_PORT = "587";
    private final String EMAIL_FROM = "fonixerpaul@gmail.com";
    private final String EMAIL_PASSWORD = "uyom bmaq aewk clcr";

    private EmailService() {
        tokensActivos = new HashMap<>();
    }

    public static synchronized EmailService getInstance() {
        if (instance == null) {
            instance = new EmailService();
        }
        return instance;
    }

    private String generarToken() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            token.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }

        return token.toString();
    }

    public boolean enviarEmailRecuperacion(String emailDestino, String nombreUsuario) {
        String token = generarToken();

        TokenRecuperacion tokenObj = new TokenRecuperacion(token, nombreUsuario,
                LocalDateTime.now().plusMinutes(15));
        tokensActivos.put(token, tokenObj);

        limpiarTokensExpirados();

        String asunto = "Recuperacion de Contrasena - Sistema Jugueteria";
        String cuerpo = construirCuerpoEmail(nombreUsuario, token);

        return enviarEmail(emailDestino, asunto, cuerpo);
    }

    private String construirCuerpoEmail(String nombreUsuario, String token) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }\n" +
                "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }\n" +
                "        .header { background: #4CAF50; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }\n" +
                "        .content { padding: 30px; background: #f9f9f9; border: 1px solid #ddd; }\n" +
                "        .token-box { font-size: 32px; font-weight: bold; color: #4CAF50; " +
                "                     letter-spacing: 8px; text-align: center; padding: 25px; " +
                "                     background: white; border: 2px dashed #4CAF50; margin: 25px 0; " +
                "                     border-radius: 8px; }\n" +
                "        .warning { background: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0; }\n" +
                "        .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; border-top: 1px solid #ddd; }\n" +
                "        .info { color: #666; font-size: 14px; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class='container'>\n" +
                "        <div class='header'>\n" +
                "            <h1>Recuperacion de Contrasena</h1>\n" +
                "        </div>\n" +
                "        <div class='content'>\n" +
                "            <p>Estimado/a <strong>" + nombreUsuario + "</strong>,</p>\n" +
                "            <p>Hemos recibido una solicitud para restablecer la contrasena de su cuenta.</p>\n" +
                "            <p>Su codigo de verificacion es:</p>\n" +
                "            <div class='token-box'>" + token + "</div>\n" +
                "            <div class='warning'>\n" +
                "                <strong>IMPORTANTE:</strong> Este codigo es valido por 15 minutos.\n" +
                "            </div>\n" +
                "            <p class='info'>Si no solicitaste este cambio, puedes ignorar este correo de forma segura. " +
                "            Tu contrasena actual permanecera sin cambios.</p>\n" +
                "            <p class='info'>Por tu seguridad, nunca compartas este codigo con nadie.</p>\n" +
                "        </div>\n" +
                "        <div class='footer'>\n" +
                "            <p>Sistema de Gestion - Jugueteria</p>\n" +
                "            <p>Este es un mensaje automatico, por favor no responder a este correo.</p>\n" +
                "            <p>&copy; 2025 Todos los derechos reservados.</p>\n" +
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
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM, "Sistema Jugueteria"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setContent(cuerpo, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Email enviado exitosamente a: " + destinatario);
            return true;

        } catch (Exception e) {
            System.err.println("Error al enviar email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean validarToken(String token, String nombreUsuario) {
        if (token == null || nombreUsuario == null) {
            return false;
        }

        TokenRecuperacion tokenObj = tokensActivos.get(token.toUpperCase());

        if (tokenObj == null) {
            return false;
        }

        if (tokenObj.estaExpirado()) {
            tokensActivos.remove(token.toUpperCase());
            return false;
        }

        if (!tokenObj.getNombreUsuario().equals(nombreUsuario)) {
            return false;
        }

        return true;
    }

    public boolean consumirToken(String token) {
        if (token == null) {
            return false;
        }

        TokenRecuperacion tokenObj = tokensActivos.remove(token.toUpperCase());
        return tokenObj != null && !tokenObj.estaExpirado();
    }

    private void limpiarTokensExpirados() {
        tokensActivos.entrySet().removeIf(entry -> entry.getValue().estaExpirado());
    }

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
