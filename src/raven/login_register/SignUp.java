package raven.login_register;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import raven.login_register.component.ButtonLink;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.alerts.MessageAlerts;
import raven.toast.Notifications;
import raven.popup.component.PopupCallbackAction;
import raven.popup.component.PopupController;
import Facade.AuthenticationFacade;
import Models.Usuario;
import Repository.UsuarioDAO;
import Security.PasswordEncryption;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class SignUp extends JPanel {

    private JTextField txtNombreUsuario;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JTextField txtNombreCompleto;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JComboBox<String> cboRol;

    private AuthenticationFacade authFacade;
    private UsuarioDAO usuarioDAO;
    private PasswordEncryption encryption;

    public SignUp() {
        this.authFacade = new AuthenticationFacade();
        this.usuarioDAO = new UsuarioDAO();
        this.encryption = new PasswordEncryption();

        initComponents();
    }

    private void initComponents() {
        setLayout(new MigLayout("insets n 20 n 20,fillx,wrap,width 450", "[fill]"));

        JTextArea text = new JTextArea("Complete el formulario para registrar un nuevo usuario.\nSe requieren credenciales de administrador para continuar.");
        text.setEditable(false);
        text.setFocusable(false);
        text.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:0,0,0,0;" +
                "background:null;");
        add(text);

        add(new JSeparator(), "gapy 10 10");

        JLabel lbNombreUsuario = new JLabel("Nombre de Usuario");
        lbNombreUsuario.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lbNombreUsuario, "gapy 5 n");

        txtNombreUsuario = new JTextField();
        txtNombreUsuario.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "usuario123");
        add(txtNombreUsuario);

        JLabel lbPassword = new JLabel("Contrasena");
        lbPassword.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lbPassword, "gapy 8 n");

        txtPassword = new JPasswordField();
        installRevealButton(txtPassword);
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Minimo 8 caracteres");
        add(txtPassword);

        JLabel lbConfirmPassword = new JLabel("Confirmar Contrasena");
        lbConfirmPassword.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lbConfirmPassword, "gapy 8 n");

        txtConfirmPassword = new JPasswordField();
        installRevealButton(txtConfirmPassword);
        txtConfirmPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Confirme su contrasena");
        add(txtConfirmPassword);

        JLabel lbNombreCompleto = new JLabel("Nombre Completo");
        lbNombreCompleto.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lbNombreCompleto, "gapy 8 n");

        txtNombreCompleto = new JTextField();
        txtNombreCompleto.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Juan Perez Lopez");
        add(txtNombreCompleto);

        JLabel lbTelefono = new JLabel("Telefono");
        lbTelefono.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lbTelefono, "gapy 8 n");

        txtTelefono = new JTextField();
        txtTelefono.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "987654321");
        add(txtTelefono);

        JLabel lbEmail = new JLabel("Email");
        lbEmail.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lbEmail, "gapy 8 n");

        txtEmail = new JTextField();
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "usuario@correo.com");
        add(txtEmail);

        JLabel lbRol = new JLabel("Rol");
        lbRol.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lbRol, "gapy 8 n");

        cboRol = new JComboBox<>(new String[]{"Vendedor", "Almacenero", "Administrador"});
        add(cboRol);

        JButton cmdSignUp = new JButton("Registrar Usuario") {
            @Override
            public boolean isDefaultButton() {
                return true;
            }
        };
        cmdSignUp.putClientProperty(FlatClientProperties.STYLE, "foreground:#FFFFFF;");
        add(cmdSignUp, "gapy 15 10");

        add(new JSeparator(), "gapy 10 10");

        add(new JLabel("Ya tienes cuenta?"), "split 2, gapx push n");

        ButtonLink cmdBackLogin = new ButtonLink("Iniciar Sesion");
        add(cmdBackLogin, "gapx n push");

        cmdBackLogin.addActionListener(e -> ModalDialog.popModel(Login.ID));
        cmdSignUp.addActionListener(e -> solicitarCredencialesAdmin());
        txtConfirmPassword.addActionListener(e -> cmdSignUp.doClick());
    }

    private void solicitarCredencialesAdmin() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap,insets 15", "[fill]"));

        JLabel lblTitulo = new JLabel("Autenticacion de Administrador");
        lblTitulo.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
        panel.add(lblTitulo, "align center");

        panel.add(new JSeparator(), "gapy 10 10");

        JLabel lblInfo = new JLabel("Ingrese sus credenciales de administrador para continuar");
        lblInfo.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground");
        panel.add(lblInfo);

        JLabel lblUsuario = new JLabel("Usuario Admin:");
        lblUsuario.putClientProperty(FlatClientProperties.STYLE, "font:bold");
        panel.add(lblUsuario, "gapy 10 n");

        JTextField txtAdminUser = new JTextField();
        panel.add(txtAdminUser);

        JLabel lblClave = new JLabel("Contrasena Admin:");
        lblClave.putClientProperty(FlatClientProperties.STYLE, "font:bold");
        panel.add(lblClave, "gapy 8 n");

        JPasswordField txtAdminPass = new JPasswordField();
        panel.add(txtAdminPass);

        int option = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Autenticacion Requerida",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (option == JOptionPane.OK_OPTION) {
            verificarAdminYRegistrar(
                    txtAdminUser.getText().trim(),
                    new String(txtAdminPass.getPassword()).trim()
            );
        }
    }

    private void verificarAdminYRegistrar(String adminUser, String adminPass) {
        if (adminUser.isEmpty() || adminPass.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Debe ingresar usuario y contrasena de administrador");
            return;
        }

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                try {
                    String passEncriptada = encryption.encriptar(adminPass);
                    Usuario admin = usuarioDAO.obtenerPorNombreUsuario(adminUser);

                    if (admin == null) {
                        return false;
                    }

                    if (!admin.getRol().equals("Administrador")) {
                        return false;
                    }

                    if (!admin.getContrasena().equals(passEncriptada)) {
                        return false;
                    }

                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void done() {
                try {
                    boolean esAdmin = get();
                    if (esAdmin) {
                        realizarRegistro();
                    } else {
                        Notifications.getInstance().show(Notifications.Type.ERROR,
                                "Credenciales de administrador incorrectas");
                    }
                } catch (Exception e) {
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            "Error al verificar credenciales: " + e.getMessage());
                }
            }
        };

        worker.execute();
    }

    private void realizarRegistro() {
        String nombreUsuario = txtNombreUsuario.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();
        String nombreCompleto = txtNombreCompleto.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String email = txtEmail.getText().trim();
        String rol = (String) cboRol.getSelectedItem();

        if (!validarCampos(nombreUsuario, password, confirmPassword,
                nombreCompleto, telefono, email)) {
            return;
        }

        JDialog loadingDialog = mostrarLoading();

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            String mensajeError = "";

            @Override
            protected Boolean doInBackground() {
                try {
                    Thread.sleep(500);

                    if (usuarioDAO.existeUsuario(nombreUsuario)) {
                        mensajeError = "El nombre de usuario ya existe";
                        return false;
                    }

                    if (usuarioDAO.existeEmail(email)) {
                        mensajeError = "El email ya esta registrado";
                        return false;
                    }

                    boolean resultado = authFacade.registrarUsuario(
                            nombreUsuario, password, nombreCompleto,
                            rol, telefono, email
                    );

                    if (!resultado) {
                        mensajeError = "Error al registrar usuario";
                    }

                    return resultado;

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
                                "Usuario registrado exitosamente");
                        limpiarCampos();
                        ModalDialog.popModel(Login.ID);
                    } else {
                        Notifications.getInstance().show(Notifications.Type.ERROR,
                                mensajeError);
                    }
                } catch (Exception e) {
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            "Error al procesar registro: " + e.getMessage());
                }
            }
        };

        worker.execute();
    }

    private boolean validarCampos(String nombreUsuario, String password,
                                  String confirmPassword, String nombreCompleto,
                                  String telefono, String email) {
        if (nombreUsuario.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Ingrese un nombre de usuario");
            txtNombreUsuario.requestFocus();
            return false;
        }

        if (nombreUsuario.length() < 4) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "El nombre de usuario debe tener al menos 4 caracteres");
            txtNombreUsuario.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Ingrese una contrasena");
            txtPassword.requestFocus();
            return false;
        }

        if (password.length() < 8) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "La contrasena debe tener al menos 8 caracteres");
            txtPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Las contrasenas no coinciden");
            txtConfirmPassword.requestFocus();
            return false;
        }

        if (nombreCompleto.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Ingrese el nombre completo");
            txtNombreCompleto.requestFocus();
            return false;
        }

        if (!nombreCompleto.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+(\\s[a-zA-ZáéíóúÁÉÍÓÚñÑ]+)+$")) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "El nombre completo debe contener al menos nombre y apellido");
            txtNombreCompleto.requestFocus();
            return false;
        }

        if (telefono.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Ingrese un telefono");
            txtTelefono.requestFocus();
            return false;
        }

        if (!telefono.matches("^[0-9]{9}$")) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "El telefono debe tener 9 digitos");
            txtTelefono.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Ingrese un email");
            txtEmail.requestFocus();
            return false;
        }

        if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "El formato del email es incorrecto");
            txtEmail.requestFocus();
            return false;
        }

        return true;
    }

    private JDialog mostrarLoading() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                "Procesando", true);
        dialog.setUndecorated(true);

        JPanel panel = new JPanel(new MigLayout("fillx,wrap,insets 20", "[center]"));
        panel.putClientProperty(FlatClientProperties.STYLE,
                "background:$Panel.background;arc:20");

        JLabel lblTexto = new JLabel("Registrando usuario...");
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
        txtNombreUsuario.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        txtNombreCompleto.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        cboRol.setSelectedIndex(0);
    }
}
