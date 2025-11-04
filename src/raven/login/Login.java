package raven.login;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.menu.FormManager;
import raven.model.ModelUser;

/**
 *
 *
 */
public class Login extends JPanel {

    public Login() {
        init();
    }

    private void init() {
        setOpaque(false);
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        chRememberMe = new JCheckBox("Recuerdame");
        cmdLogin = new JButton("Login");
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "fill,250:280"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:30;"
                + "[light]background:rgba(255,255,255,200);"
                + "[dark]background:rgba(40,40,40,200)");

        txtPassword.putClientProperty(FlatClientProperties.STYLE, ""
                + "showRevealButton:true");
        cmdLogin.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]background:darken(@background,10%);"
                + "[dark]background:lighten(@background,10%);"
                + "borderWidth:0;"
                + "focusWidth:0;"
                + "innerFocusWidth:0");

        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ingresa tu email o usuario");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ingresa tu contraseña");

        JLabel lbTitle = new JLabel("Bienvenido de nuevo");
        JLabel description = new JLabel("Por favor ingresa tus credenciales para iniciar sesión.");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold +10");
        description.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]foreground:lighten(@foreground,30%);"
                + "[dark]foreground:darken(@foreground,30%)");

        panel.add(lbTitle);
        panel.add(description);
        panel.add(new JLabel("Username"), "gapy 8");
        panel.add(txtUsername);
        panel.add(new JLabel("Password"), "gapy 8");
        panel.add(txtPassword);
        panel.add(chRememberMe, "grow 0");
        panel.add(cmdLogin, "gapy 10");
        add(panel);

        // event
        cmdLogin.addActionListener((e) -> {
            String userName = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            // this is just for example to check admin user :)
            boolean isAdmin = userName.equals("admin");
            FormManager.login(new ModelUser(userName, isAdmin));
        });
    }

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox chRememberMe;
    private JButton cmdLogin;
}
