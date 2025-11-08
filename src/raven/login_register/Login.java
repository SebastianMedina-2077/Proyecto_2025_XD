package raven.login_register;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import raven.login_register.component.ButtonLink;
import net.miginfocom.swing.MigLayout;
import raven.menu.FormManager;
import raven.modal.ModalDialog;
import raven.model.ModelUser;

import Clases.ResultadoLogin;
import Clases.Usuario;
import Servicios.UsuarioServicio;
import Singleton.SesionManager;
import raven.toast.Notifications;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JPanel {

    public static final String ID = "login_id";

    private final UsuarioServicio usuarioServicio;

    public Login() {
        this.usuarioServicio = new UsuarioServicio();
        setLayout(new MigLayout("insets n 20 n 20,fillx,wrap,width 380", "[fill]"));
        JTextArea text = new JTextArea("Become a member you'll enjoy exclusive deals,\noffers, invites and rewards.");
        text.setEditable(false);
        text.setFocusable(false);
        text.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:0,0,0,0;" +
                "background:null;");
        add(text);

        add(new JSeparator(), "gapy 15 15");

        JLabel lbEmail = new JLabel("Email");
        lbEmail.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold;");
        add(lbEmail);

        JTextField txtEmail = new JTextField();
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your email");
        add(txtEmail);

        JLabel lbPassword = new JLabel("Password");
        lbPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold;");
        add(lbPassword, "gapy 10 n");

        JPasswordField txtPassword = new JPasswordField();
        installRevealButton(txtPassword);
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");

        add(txtPassword);

        JCheckBox chkRememberMe = new JCheckBox("Recordar sesión");
        add(chkRememberMe, "split 2,gapy 10 10");
        ButtonLink cmdForgotPassword = new ButtonLink("Forgot password ?");
        add(cmdForgotPassword, "gapx push n");

        JButton cmdLogin = new JButton("Login") {
            @Override
            public boolean isDefaultButton() {
                return true;
            }
        };
        cmdLogin.putClientProperty(FlatClientProperties.STYLE, "" +
                "foreground:#FFFFFF;");
        add(cmdLogin);

        add(new JSeparator(), "gapy 15 15");

        add(new JLabel("Don't have an account ?"), "split 2,gapx push n");
        ButtonLink cmdSignUp = new ButtonLink("Sign up");
        add(cmdSignUp, "gapx n push");

        // event
        cmdSignUp.addActionListener(actionEvent -> {
            String icon = "raven/login_register/icon/signup.svg";
            ModalDialog.pushModal(new CustomModalBorder(new SignUp(), "Sign up", icon), ID);
        });

        cmdForgotPassword.addActionListener(actionEvent -> {
            String icon = "raven/login_register/icon/forgot_password.svg";
            ModalDialog.pushModal(new CustomModalBorder(new ForgotPassword(), "Forgot password", icon), ID);
        });

        cmdLogin.addActionListener((e) -> {
            realizarLogin(txtEmail, txtPassword, chkRememberMe);
        });
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

    private void realizarLogin(JTextField txtUsuario, JPasswordField txtPassword, JCheckBox chkRememberMe) {
        String nombreUsuario = txtUsuario.getText().trim();
        String contrasena = new String(txtPassword.getPassword()).trim();

        // Validaciones básicas
        if (nombreUsuario.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Por favor ingrese su nombre de usuario");
            txtUsuario.requestFocus();
            return;
        }

        if (contrasena.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Por favor ingrese su contraseña");
            txtPassword.requestFocus();
            return;
        }

        // Mostrar loading (opcional)
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

        // Realizar login
        try {
            ResultadoLogin resultado = usuarioServicio.iniciarSesion(nombreUsuario, contrasena);

            if (resultado.isExitoso()) {
                Usuario usuario = resultado.getUsuario();

                // Crear ModelUser para FormManager
                boolean isAdmin = resultado.esAdministrador();
                ModelUser modelUser = new ModelUser(
                        usuario.getNombreCompleto(),
                        isAdmin
                );

                // Notificar éxito
                Notifications.getInstance().show(
                        Notifications.Type.SUCCESS,
                        "Bienvenido " + usuario.getNombreCompleto()
                );

                // Registrar en FormManager
                FormManager.login(modelUser);

                // Cerrar modal de login
                ModalDialog.popModel(ID);

                // Limpiar campos
                txtUsuario.setText("");
                txtPassword.setText("");

            } else {
                // Login fallido
                Notifications.getInstance().show(
                        Notifications.Type.ERROR,
                        resultado.getMensaje()
                );
                txtPassword.setText("");
                txtPassword.requestFocus();
            }
        } catch (Exception ex) {
            Notifications.getInstance().show(
                    Notifications.Type.ERROR,
                    "Error al iniciar sesión: " + ex.getMessage()
            );
            ex.printStackTrace();
        } finally {
            setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
}
