package raven.login_register;

import Facade.AuthenticationFacade;
import Models.Usuario;
import Repository.UsuarioDAO;
import Services.EmailService;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import raven.login_register.component.ButtonLink;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.toast.Notifications;

import javax.swing.*;

public class ForgotPassword extends JPanel {

    private JTextField txtEmail;
    private JTextField txtToken;
    private JPasswordField txtNuevaPassword;
    private JPasswordField txtConfirmarPassword;
    private JButton cmdEnviarToken;
    private JButton cmdValidarToken;
    private JButton cmdCambiarPassword;

    private JPanel panelEmail;
    private JPanel panelToken;
    private JPanel panelPassword;

    private EmailService emailService;
    private AuthenticationFacade authFacade;
    private UsuarioDAO usuarioDAO;

    private String nombreUsuarioRecuperar;
    private String tokenActual;

    private enum Paso {
        SOLICITAR_EMAIL,
        VALIDAR_TOKEN,
        CAMBIAR_PASSWORD
    }

    private Paso pasoActual = Paso.SOLICITAR_EMAIL;

    public ForgotPassword() {
        this.emailService = EmailService.getInstance();
        this.authFacade = new AuthenticationFacade();
        this.usuarioDAO = new UsuarioDAO();

        initComponents();
        mostrarPaso(Paso.SOLICITAR_EMAIL);
    }

    private void initComponents() {
        setLayout(new MigLayout("insets n 20 n 20,fillx,wrap,width 420", "[fill]"));

        JTextArea text = new JTextArea("Para recuperar su contrasena, ingrese el email\nregistrado en su cuenta. Le enviaremos un codigo\nde verificacion.");
        text.setEditable(false);
        text.setFocusable(false);
        text.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:0,0,0,0;" +
                "background:null;");
        add(text);

        add(new JSeparator(), "gapy 15 15");

        crearPanelEmail();
        crearPanelToken();
        crearPanelPassword();

        add(panelEmail);
        add(panelToken);
        add(panelPassword);

        ButtonLink cmdBackLogin = new ButtonLink("Volver al inicio de sesion");
        add(cmdBackLogin, "grow 0,al center,gapy 10 n");

        cmdBackLogin.addActionListener(e -> ModalDialog.popModel(Login.ID));
    }

    private void crearPanelEmail() {
        panelEmail = new JPanel(new MigLayout("fillx,wrap,insets 0", "[fill]"));

        JLabel lbEmail = new JLabel("Email");
        lbEmail.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        panelEmail.add(lbEmail);

        txtEmail = new JTextField();
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "ejemplo@correo.com");
        panelEmail.add(txtEmail);

        cmdEnviarToken = new JButton("Enviar Codigo") {
            @Override
            public boolean isDefaultButton() {
                return pasoActual == Paso.SOLICITAR_EMAIL;
            }
        };
        cmdEnviarToken.putClientProperty(FlatClientProperties.STYLE, "foreground:#FFFFFF;");
        panelEmail.add(cmdEnviarToken, "gapy 15 15");

        cmdEnviarToken.addActionListener(e -> solicitarToken());
        txtEmail.addActionListener(e -> cmdEnviarToken.doClick());
    }

    private void crearPanelToken() {
        panelToken = new JPanel(new MigLayout("fillx,wrap,insets 0", "[fill]"));

        JLabel lbInfo = new JLabel("Ingrese el codigo de 6 digitos enviado a su email");
        lbInfo.putClientProperty(FlatClientProperties.STYLE,
                "foreground:$Label.disabledForeground");
        panelToken.add(lbInfo);

        JLabel lbToken = new JLabel("Codigo de Verificacion");
        lbToken.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        panelToken.add(lbToken, "gapy 10 n");

        txtToken = new JTextField();
        txtToken.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "ABC123");
        panelToken.add(txtToken);

        cmdValidarToken = new JButton("Verificar Codigo") {
            @Override
            public boolean isDefaultButton() {
                return pasoActual == Paso.VALIDAR_TOKEN;
            }
        };
        cmdValidarToken.putClientProperty(FlatClientProperties.STYLE, "foreground:#FFFFFF;");
        panelToken.add(cmdValidarToken, "gapy 15 15");

        ButtonLink cmdReenviar = new ButtonLink("Reenviar codigo");
        panelToken.add(cmdReenviar, "grow 0,al center");

        cmdValidarToken.addActionListener(e -> validarToken());
        cmdReenviar.addActionListener(e -> reenviarToken());
        txtToken.addActionListener(e -> cmdValidarToken.doClick());
    }

    private void crearPanelPassword() {
        panelPassword = new JPanel(new MigLayout("fillx,wrap,insets 0", "[fill]"));

        JLabel lbNuevaPass = new JLabel("Nueva Contrasena");
        lbNuevaPass.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        panelPassword.add(lbNuevaPass);

        txtNuevaPassword = new JPasswordField();
        installRevealButton(txtNuevaPassword);
        txtNuevaPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                "Minimo 8 caracteres");
        panelPassword.add(txtNuevaPassword);

        JLabel lbConfirmar = new JLabel("Confirmar Contrasena");
        lbConfirmar.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        panelPassword.add(lbConfirmar, "gapy 10 n");

        txtConfirmarPassword = new JPasswordField();
        installRevealButton(txtConfirmarPassword);
        txtConfirmarPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                "Confirme su nueva contrasena");
        panelPassword.add(txtConfirmarPassword);

        cmdCambiarPassword = new JButton("Cambiar Contrasena") {
            @Override
            public boolean isDefaultButton() {
                return pasoActual == Paso.CAMBIAR_PASSWORD;
            }
        };
        cmdCambiarPassword.putClientProperty(FlatClientProperties.STYLE, "foreground:#FFFFFF;");
        panelPassword.add(cmdCambiarPassword, "gapy 15 15");

        cmdCambiarPassword.addActionListener(e -> cambiarPassword());
        txtConfirmarPassword.addActionListener(e -> cmdCambiarPassword.doClick());
    }

    private void mostrarPaso(Paso paso) {
        pasoActual = paso;

        panelEmail.setVisible(paso == Paso.SOLICITAR_EMAIL);
        panelToken.setVisible(paso == Paso.VALIDAR_TOKEN);
        panelPassword.setVisible(paso == Paso.CAMBIAR_PASSWORD);

        revalidate();
        repaint();
    }

    private void solicitarToken() {
        String email = txtEmail.getText().trim();

        if (email.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Por favor, ingrese su email");
            txtEmail.requestFocus();
            return;
        }

        if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "El formato del email es incorrecto");
            txtEmail.requestFocus();
            return;
        }

        JDialog loadingDialog = mostrarLoading("Enviando codigo...");

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            String mensajeError = "";

            @Override
            protected Boolean doInBackground() {
                try {
                    Thread.sleep(500);

                    Usuario usuario = usuarioDAO.obtenerPorEmail(email);

                    if (usuario == null) {
                        mensajeError = "No existe ninguna cuenta con ese email";
                        return false;
                    }

                    nombreUsuarioRecuperar = usuario.getNombreUsuario();

                    boolean enviado = emailService.enviarEmailRecuperacion(email,
                            nombreUsuarioRecuperar);

                    if (!enviado) {
                        mensajeError = "Error al enviar el email. Verifique su conexion";
                        return false;
                    }

                    return true;

                } catch (Exception e) {
                    mensajeError = "Error: " + e.getMessage();
                    return false;
                }
            }

            @Override
            protected void done() {
                loadingDialog.dispose();
                try {
                    boolean exitoso = get();
                    if (exitoso) {
                        Notifications.getInstance().show(Notifications.Type.SUCCESS,
                                "Codigo enviado exitosamente. Revise su email");
                        mostrarPaso(Paso.VALIDAR_TOKEN);
                        txtToken.requestFocus();
                    } else {
                        Notifications.getInstance().show(Notifications.Type.ERROR,
                                mensajeError);
                    }
                } catch (Exception e) {
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            "Error al procesar solicitud: " + e.getMessage());
                }
            }
        };

        worker.execute();
    }

    private void reenviarToken() {
        if (nombreUsuarioRecuperar == null || txtEmail.getText().trim().isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Debe solicitar un codigo primero");
            mostrarPaso(Paso.SOLICITAR_EMAIL);
            return;
        }

        solicitarToken();
    }

    private void validarToken() {
        String token = txtToken.getText().trim().toUpperCase();

        if (token.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Por favor, ingrese el codigo de verificacion");
            txtToken.requestFocus();
            return;
        }

        if (token.length() != 6) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "El codigo debe tener 6 caracteres");
            txtToken.requestFocus();
            return;
        }

        JDialog loadingDialog = mostrarLoading("Validando codigo...");

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                try {
                    Thread.sleep(300);
                    return emailService.validarToken(token, nombreUsuarioRecuperar);
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            protected void done() {
                loadingDialog.dispose();
                try {
                    boolean valido = get();
                    if (valido) {
                        tokenActual = token;
                        Notifications.getInstance().show(Notifications.Type.SUCCESS,
                                "Codigo verificado correctamente");
                        mostrarPaso(Paso.CAMBIAR_PASSWORD);
                        txtNuevaPassword.requestFocus();
                    } else {
                        Notifications.getInstance().show(Notifications.Type.ERROR,
                                "Codigo invalido o expirado");
                        txtToken.selectAll();
                        txtToken.requestFocus();
                    }
                } catch (Exception e) {
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            "Error al validar codigo");
                }
            }
        };

        worker.execute();
    }

    private void cambiarPassword() {
        String nuevaPass = new String(txtNuevaPassword.getPassword()).trim();
        String confirmarPass = new String(txtConfirmarPassword.getPassword()).trim();

        if (nuevaPass.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Ingrese la nueva contrasena");
            txtNuevaPassword.requestFocus();
            return;
        }

        if (nuevaPass.length() < 8) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "La contrasena debe tener al menos 8 caracteres");
            txtNuevaPassword.requestFocus();
            return;
        }

        if (!nuevaPass.equals(confirmarPass)) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Las contrasenas no coinciden");
            txtConfirmarPassword.requestFocus();
            return;
        }

        JDialog loadingDialog = mostrarLoading("Cambiando contrasena...");

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            String mensajeError = "";

            @Override
            protected Boolean doInBackground() {
                try {
                    Thread.sleep(500);

                    boolean exitoso = authFacade.restablecerContrasena(
                            tokenActual, nombreUsuarioRecuperar, nuevaPass);

                    if (!exitoso) {
                        mensajeError = "Error al cambiar la contrasena";
                    }

                    return exitoso;

                } catch (Exception e) {
                    mensajeError = "Error: " + e.getMessage();
                    return false;
                }
            }

            @Override
            protected void done() {
                loadingDialog.dispose();
                try {
                    boolean exitoso = get();
                    if (exitoso) {
                        Notifications.getInstance().show(Notifications.Type.SUCCESS,
                                "Contrasena cambiada exitosamente");

                        limpiarCampos();
                        mostrarPaso(Paso.SOLICITAR_EMAIL);

                        ModalDialog.popModel(Login.ID);
                    } else {
                        Notifications.getInstance().show(Notifications.Type.ERROR,
                                mensajeError);
                    }
                } catch (Exception e) {
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            "Error al procesar cambio: " + e.getMessage());
                }
            }
        };

        worker.execute();
    }

    private JDialog mostrarLoading(String mensaje) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                "Procesando", true);
        dialog.setUndecorated(true);

        JPanel panel = new JPanel(new MigLayout("fillx,wrap,insets 20", "[center]"));
        panel.putClientProperty(FlatClientProperties.STYLE,
                "background:$Panel.background;arc:20");

        JLabel lblTexto = new JLabel(mensaje);
        lblTexto.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
        panel.add(lblTexto);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        panel.add(progressBar, "width 250");

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);

        new Thread(() -> dialog.setVisible(true)).start();

        return dialog;
    }

    private void installRevealButton(JPasswordField txt) {
        FlatSVGIcon iconEye = new FlatSVGIcon("raven/login_register/icon/eye.svg", 0.3f);
        FlatSVGIcon iconHide = new FlatSVGIcon("raven/login_register/icon/hide.svg", 0.3f);

        JToolBar toolBar = new JToolBar();
        toolBar.putClientProperty(FlatClientProperties.STYLE, "margin:0,0,0,5;");
        JButton button = new JButton(iconEye);

        button.addActionListener(e -> {
            char echoChar = txt.getEchoChar();
            if (echoChar == 0) {
                button.setIcon(iconEye);
                txt.setEchoChar('\u2022');
            } else {
                button.setIcon(iconHide);
                txt.setEchoChar((char) 0);
            }
        });

        toolBar.add(button);
        txt.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_COMPONENT, toolBar);
    }

    private void limpiarCampos() {
        txtEmail.setText("");
        txtToken.setText("");
        txtNuevaPassword.setText("");
        txtConfirmarPassword.setText("");
        nombreUsuarioRecuperar = null;
        tokenActual = null;
    }
}
