package raven.login_register;

import DB_Conection.DatabaseConnection;
import Models.Usuario;
import Repository.UsuarioDAO;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import raven.alerts.MessageAlerts;
import raven.login_register.component.ButtonLink;
import net.miginfocom.swing.MigLayout;
import raven.menu.FormManager;
import raven.modal.ModalDialog;
import Proxy.AuthenticationProxy;
import Models.ResultadoLogin;
import Security.PasswordEncryption;
import raven.model.ModelUser;
import raven.toast.Notifications;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

public class Login extends JPanel {
    public static final String ID = "login_id";
    private AuthenticationProxy authProxy;
    private UsuarioDAO usuarioDAO;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox chkRememberMe;

    public Login() {
        authProxy = new AuthenticationProxy();
        usuarioDAO = new UsuarioDAO();
        setLayout(new MigLayout("insets n 20 n 20,fillx,wrap,width 380", "[fill]"));

        JTextArea text = new JTextArea("Inicie sesion para acceder al sistema de gestion.\nDisfrute de todas las funcionalidades disponibles.");
        text.setEditable(false);
        text.setFocusable(false);
        text.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:0,0,0,0;" +
                "background:null;");
        add(text);

        add(new JSeparator(), "gapy 15 15");

        JLabel lbEmail = new JLabel("Usuario");
        lbEmail.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold;");
        add(lbEmail);

        txtUsername = new JTextField();
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ingrese su usuario");
        add(txtUsername);

        JLabel lbPassword = new JLabel("Contrasena");
        lbPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold;");
        add(lbPassword, "gapy 10 n");

        txtPassword = new JPasswordField();
        installRevealButton(txtPassword);
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ingrese su contrasena");
        add(txtPassword);

        chkRememberMe = new JCheckBox("Recordar sesion");
        add(chkRememberMe, "split 2,gapy 10 10");

        ButtonLink cmdForgotPassword = new ButtonLink("Olvido su contrasena?");
        add(cmdForgotPassword, "gapx push n");

        JButton cmdLogin = new JButton("Iniciar Sesion") {
            @Override
            public boolean isDefaultButton() {
                return true;
            }
        };
        cmdLogin.putClientProperty(FlatClientProperties.STYLE, "" +
                "foreground:#FFFFFF;");
        add(cmdLogin);

        add(new JSeparator(), "gapy 15 15");

        add(new JLabel("No tienes cuenta?"), "split 2,gapx push n");
        ButtonLink cmdSignUp = new ButtonLink("Registrarse");
        add(cmdSignUp, "gapx n push");

        cmdSignUp.addActionListener(actionEvent -> {
            String icon = "raven/login_register/icon/signup.svg";
            ModalDialog.pushModal(new CustomModalBorder(new SignUp(), "Registrar Usuario", icon), ID);
        });

        cmdForgotPassword.addActionListener(actionEvent -> {
            String icon = "raven/login_register/icon/forgot_password.svg";
            ModalDialog.pushModal(new CustomModalBorder(new ForgotPassword(), "Recuperar Contrasena", icon), ID);
        });

        cmdLogin.addActionListener((e) -> {
            realizarLogin();
        });

        txtPassword.addActionListener(e -> cmdLogin.doClick());
        txtUsername.addActionListener(e -> txtPassword.requestFocus());

        cargarSesionGuardada();
    }

    private void realizarLogin() {
        String userName = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        boolean recordar = chkRememberMe.isSelected();

        if (userName.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Por favor, ingrese su nombre de usuario");
            txtUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Por favor, ingrese su contrasena");
            txtPassword.requestFocus();
            return;
        }

        JDialog loadingDialog = mostrarLoading();
        String ipAcceso = obtenerIP();

        SwingWorker<ResultadoLogin, Void> worker = new SwingWorker<>() {
            @Override
            protected ResultadoLogin doInBackground() {
                try {
                    DatabaseConnection.getInstance().refreshConnection();

                    Thread.sleep(500);
                    return authProxy.login(userName, password, ipAcceso);
                } catch (InterruptedException ex) {
                    ResultadoLogin error = new ResultadoLogin();
                    error.setLoginExitoso(false);
                    error.setMensaje("Proceso interrumpido");
                    return error;
                }
            }

            @Override
            protected void done() {
                loadingDialog.dispose();
                try {
                    ResultadoLogin resultado = get();
                    procesarResultadoLogin(resultado, userName, recordar);
                } catch (Exception ex) {
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            "Error al procesar login: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void cargarSesionGuardada() {
        try {
            java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userRoot()
                    .node("JugueteriaApp");

            boolean recordar = prefs.getBoolean("recordar", false);

            if (recordar) {
                String ultimoUsuario = prefs.get("ultimoUsuario", "");
                if (!ultimoUsuario.isEmpty()) {
                    chkRememberMe.setSelected(true);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar sesion: " + e.getMessage());
        }
    }

    private void guardarSesion(ResultadoLogin resultado) {
        try {
            java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userRoot()
                    .node("JugueteriaApp");
            prefs.put("ultimoUsuario", resultado.getNombreCompleto());
            prefs.putInt("idUsuario", resultado.getIdUsuario());
            prefs.putBoolean("recordar", true);
            prefs.flush();
        } catch (Exception e) {
            System.err.println("Error al guardar sesion: " + e.getMessage());
        }
    }

    private String obtenerIP() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            return localhost.getHostAddress();
        } catch (Exception e) {
            return "Desconocida";
        }
    }

    private void procesarResultadoLogin(ResultadoLogin resultado, String userName, boolean recordar) {
        if (resultado.isLoginExitoso()) {
            Usuario usuarioCompleto = usuarioDAO.obtenerPorId(resultado.getIdUsuario());

            boolean isAdmin = "Administrador".equals(resultado.getRol());
            ModelUser modelUser = new ModelUser(userName, isAdmin);

            if (usuarioCompleto != null) {
                modelUser = new ModelUser(
                        usuarioCompleto.getIdUsuario(),
                        usuarioCompleto.getNombreUsuario(),
                        usuarioCompleto.getNombreCompleto(),
                        usuarioCompleto.getRol(),
                        usuarioCompleto.getEmail(),
                        usuarioCompleto.getTelefono()
                );
            }

            Notifications.getInstance().show(Notifications.Type.SUCCESS,
                    "Bienvenido " + resultado.getNombreCompleto() + "!");

            if (recordar) {
                guardarSesion(resultado);
            }

            limpiarCampos();
            ModalDialog.closeAllModal();
            FormManager.login(modelUser);
            return;
        }

        if (resultado.isUsuarioBloqueado()) {
            int minutos = resultado.getMinutosRestantesBloq();
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    "Usuario bloqueado por " + minutos + " minutos.\n" +
                            "Demasiados intentos fallidos.");
            mostrarDialogoBloqueado(minutos);
            return;
        }

        if (resultado.isUsuarioInactivo()) {
            MessageAlerts.getInstance().showMessage("Su cuenta esta inactiva.\nContacte al administrador.",
                    "Su cuenta ha sido desactivada.\n\n" +
                            "Por favor, contacte al administrador del sistema\n" +
                            "para mas informacion.",MessageAlerts.MessageType.WARNING);
            return;
        }

        if (resultado.isCredencialesInvalidas()) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    "Usuario o contrasena incorrectos");
            txtPassword.setText("");
            txtPassword.requestFocus();
            shakePanel();
        }
    }

    private void mostrarDialogoBloqueado(int minutos) {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap,insets 20", "[center]"));

        JLabel icono = new JLabel("\u26A0");
        icono.setFont(new Font("Dialog", Font.PLAIN, 48));
        panel.add(icono);

        JLabel titulo = new JLabel("Usuario Bloqueado");
        titulo.putClientProperty(FlatClientProperties.STYLE, "font:bold +4");
        panel.add(titulo);

        JTextArea mensaje = new JTextArea(
                "Tu cuenta ha sido bloqueada temporalmente\n" +
                        "por multiples intentos fallidos de inicio de sesion.\n\n" +
                        "Tiempo restante: " + minutos + " minutos\n\n" +
                        "Si olvidaste tu contrasena, usa la opcion\n" +
                        "de recuperacion de contrasena."
        );
        mensaje.setEditable(false);
        mensaje.setFocusable(false);
        mensaje.setOpaque(false);
        mensaje.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:0,0,0,0");
        panel.add(mensaje);

        JOptionPane.showMessageDialog(this, panel,
                "Cuenta Bloqueada", JOptionPane.WARNING_MESSAGE);
    }

    private JDialog mostrarLoading() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                "Validando Credenciales", true);
        dialog.setUndecorated(true);

        JPanel panel = new JPanel(new MigLayout("fillx,wrap,insets 20", "[center]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:$Panel.background;" +
                "arc:20");

        JLabel lblTexto = new JLabel("Validando credenciales...");
        lblTexto.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
        panel.add(lblTexto);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:10");
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
        toolBar.putClientProperty(FlatClientProperties.STYLE, "" +
                "margin:0,0,0,5;");
        JButton button = new JButton(iconEye);

        button.addActionListener(new ActionListener() {
            private char defaultEchoChart = txt.getEchoChar();
            private boolean show;

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                show = !show;
                if (show) {
                    button.setIcon(iconHide);
                    txt.setEchoChar((char) 0);
                } else {
                    button.setIcon(iconEye);
                    txt.setEchoChar(defaultEchoChart);
                }
            }
        });
        toolBar.add(button);
        txt.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_COMPONENT, toolBar);
    }

    private void limpiarCampos() {
        txtUsername.setText("");
        txtPassword.setText("");
    }

    private void shakePanel() {
        Point originalLocation = getLocation();
        try {
            for (int i = 0; i < 3; i++) {
                setLocation(originalLocation.x + 10, originalLocation.y);
                Thread.sleep(50);
                setLocation(originalLocation.x - 10, originalLocation.y);
                Thread.sleep(50);
            }
            setLocation(originalLocation);
        } catch (InterruptedException e) {
            setLocation(originalLocation);
        }
    }
}
