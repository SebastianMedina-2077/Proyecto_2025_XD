package raven.login_register;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import raven.login_register.component.ButtonLink;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.toast.Notifications;
import Facade.AuthenticationFacade;
import Repository.UsuarioDAO;
import Security.PasswordEncryption;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

/**
 * Panel de Registro de Usuarios - Diseño Moderno
 * Solo administradores pueden registrar nuevos usuarios
 */
public class SignUp extends JPanel {

    private AuthenticationFacade authFacade;
    private UsuarioDAO usuarioDAO;

    // Campos del formulario
    private JTextField txtNombreUsuario;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JTextField txtNombreCompleto;
    private JTextField txtEmail;
    private JTextField txtTelefono;
    private JComboBox<String> cmbRol;

    // Campos de validación de admin
    private JTextField txtAdminUser;
    private JPasswordField txtAdminPassword;

    private JCheckBox chkAceptoTerminos;
    private JLabel lblPasswordStrength;

    public SignUp() {
        authFacade = new AuthenticationFacade();
        usuarioDAO = new UsuarioDAO();

        setLayout(new MigLayout("insets 0,fillx,wrap", "[fill]", "[grow 0][grow 0][fill]"));
        setOpaque(false);

        // Header con gradiente visual
        add(createHeader());
        add(createSeparator(), "gapy 10 10");

        // Contenido principal en un panel con scroll
        JScrollPane scrollPane = new JScrollPane(createMainContent());
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new MigLayout("insets 20,fillx,wrap", "[center]"));
        panel.setOpaque(false);

        // Icono principal
        JLabel iconLabel = new JLabel("👥");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        panel.add(iconLabel);

        // Título principal
        JLabel lblTitulo = new JLabel("Crear Nueva Cuenta");
        lblTitulo.putClientProperty(FlatClientProperties.STYLE,
                "font:bold +8;" +
                        "foreground:$Component.accentColor");
        panel.add(lblTitulo);

        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Complete el formulario para registrar un nuevo usuario");
        lblSubtitulo.putClientProperty(FlatClientProperties.STYLE,
                "foreground:$Label.disabledForeground");
        panel.add(lblSubtitulo);

        return panel;
    }

    private JPanel createMainContent() {
        JPanel panel = new JPanel(new MigLayout("insets 20 30 20 30,fillx,wrap", "[fill]"));
        panel.setOpaque(false);

        // Sección 1: Validación de Administrador
        panel.add(createAdminSection());
        panel.add(createSeparator(), "gapy 20 20");

        // Sección 2: Datos del Nuevo Usuario
        panel.add(createUserDataSection());

        // Términos y condiciones
        panel.add(createTermsSection(), "gapy 15 10");

        // Botones de acción
        panel.add(createActionButtons(), "gapy 10 0");

        return panel;
    }

    private JPanel createAdminSection() {
        JPanel panel = new JPanel(new MigLayout("insets 15,fillx,wrap", "[fill]"));
        panel.putClientProperty(FlatClientProperties.STYLE,
                "arc:20;" +
                        "background:lighten($Panel.background,3%)");

        // Título de sección
        JLabel lblSeccion = new JLabel("Autenticación de Administrador");
        lblSeccion.putClientProperty(FlatClientProperties.STYLE,
                "font:bold +3;" +
                        "foreground:$Component.accentColor");
        panel.add(lblSeccion);

        JLabel lblInfo = new JLabel("Por seguridad, ingrese sus credenciales de administrador");
        lblInfo.putClientProperty(FlatClientProperties.STYLE,
                "font:-1;" +
                        "foreground:$Label.disabledForeground");
        panel.add(lblInfo, "gapy 0 10");

        // Usuario Admin
        panel.add(createLabel("Usuario Administrador", "👤"));
        txtAdminUser = createTextField("Ingrese su usuario");
        panel.add(txtAdminUser);

        // Contraseña Admin
        panel.add(createLabel("Contraseña", "🔑"), "gapy 8 0");
        txtAdminPassword = createPasswordField("Ingrese su contraseña");
        installRevealButton(txtAdminPassword);
        panel.add(txtAdminPassword);

        return panel;
    }

    private JPanel createUserDataSection() {
        JPanel panel = new JPanel(new MigLayout("insets 15,fillx,wrap", "[fill]"));
        panel.putClientProperty(FlatClientProperties.STYLE,
                "arc:20;" +
                        "background:lighten($Panel.background,3%)");

        // Título de sección
        JLabel lblSeccion = new JLabel("👤 Información del Nuevo Usuario");
        lblSeccion.putClientProperty(FlatClientProperties.STYLE,
                "font:bold +3;" +
                        "foreground:$Component.accentColor");
        panel.add(lblSeccion);

        JLabel lblInfo = new JLabel("Complete todos los campos requeridos");
        lblInfo.putClientProperty(FlatClientProperties.STYLE,
                "font:-1;" +
                        "foreground:$Label.disabledForeground");
        panel.add(lblInfo, "gapy 0 10");

        // Grid de 2 columnas para campos
        JPanel gridPanel = new JPanel(new MigLayout("insets 0,fillx", "[fill][fill]"));
        gridPanel.setOpaque(false);

        // Columna izquierda
        JPanel leftColumn = new JPanel(new MigLayout("insets 0,fillx,wrap", "[fill]"));
        leftColumn.setOpaque(false);

        leftColumn.add(createLabel("Nombre de Usuario", "🆔"));
        txtNombreUsuario = createTextField("usuario123");
        leftColumn.add(txtNombreUsuario);

        leftColumn.add(createLabel("Nombre Completo", "👨‍💼"), "gapy 8 0");
        txtNombreCompleto = createTextField("Juan Pérez García");
        leftColumn.add(txtNombreCompleto);

        leftColumn.add(createLabel("Email", "📧"), "gapy 8 0");
        txtEmail = createTextField("correo@ejemplo.com");
        leftColumn.add(txtEmail);

        // Columna derecha
        JPanel rightColumn = new JPanel(new MigLayout("insets 0,fillx,wrap", "[fill]"));
        rightColumn.setOpaque(false);

        rightColumn.add(createLabel("Teléfono", "📱"));
        txtTelefono = createTextField("999 888 777");
        rightColumn.add(txtTelefono);

        rightColumn.add(createLabel("Rol del Sistema", "🎭"), "gapy 8 0");
        String[] roles = {"Vendedor", "Almacenero", "Administrador"};
        cmbRol = new JComboBox<>(roles);
        cmbRol.putClientProperty(FlatClientProperties.STYLE,
                "arc:10");
        rightColumn.add(cmbRol);

        gridPanel.add(leftColumn, "width 50%");
        gridPanel.add(rightColumn, "width 50%");
        panel.add(gridPanel);

        // Contraseñas (ancho completo)
        panel.add(createLabel("Contraseña", "🔐"), "gapy 8 0");
        txtPassword = createPasswordField("Mínimo 8 caracteres");
        installRevealButton(txtPassword);

        // Indicador de fortaleza de contraseña
        txtPassword.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updatePasswordStrength(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updatePasswordStrength(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updatePasswordStrength(); }
        });

        panel.add(txtPassword);

        lblPasswordStrength = new JLabel(" ");
        lblPasswordStrength.putClientProperty(FlatClientProperties.STYLE, "font:-1");
        panel.add(lblPasswordStrength);

        panel.add(createLabel("Confirmar Contraseña", "✅"), "gapy 8 0");
        txtConfirmPassword = createPasswordField("Repita la contraseña");
        installRevealButton(txtConfirmPassword);
        panel.add(txtConfirmPassword);

        return panel;
    }

    private JPanel createTermsSection() {
        JPanel panel = new JPanel(new MigLayout("insets 10,fillx", "[]"));
        panel.setOpaque(false);

        chkAceptoTerminos = new JCheckBox();
        chkAceptoTerminos.setOpaque(false);
        panel.add(chkAceptoTerminos);

        JLabel lblTerms = new JLabel("<html>Acepto los <u>términos y condiciones</u> del sistema</html>");
        lblTerms.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblTerms.putClientProperty(FlatClientProperties.STYLE,
                "foreground:$Label.foreground");

        lblTerms.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mostrarTerminos();
            }
        });

        panel.add(lblTerms);

        return panel;
    }

    private JPanel createActionButtons() {
        JPanel panel = new JPanel(new MigLayout("insets 0,fillx,wrap", "[fill]"));
        panel.setOpaque(false);

        // Botón principal de registro
        JButton cmdSignUp = new JButton("Registrar Usuario") {
            @Override
            public boolean isDefaultButton() {
                return true;
            }
        };
        cmdSignUp.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                        "borderWidth:0;" +
                        "focusWidth:0;" +
                        "innerFocusWidth:0;" +
                        "font:bold +1;" +
                        "background:$Component.accentColor;" +
                        "foreground:#FFFFFF");
        cmdSignUp.setPreferredSize(new Dimension(0, 45));
        cmdSignUp.addActionListener(e -> realizarRegistro());
        panel.add(cmdSignUp);

        // Separador
        panel.add(new JSeparator(), "gapy 15 15");

        // Link para volver al login
        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        linkPanel.setOpaque(false);

        JLabel lblYaTienes = new JLabel("¿Ya tienes una cuenta?");
        lblYaTienes.putClientProperty(FlatClientProperties.STYLE,
                "foreground:$Label.disabledForeground");
        linkPanel.add(lblYaTienes);

        ButtonLink cmdBackLogin = new ButtonLink("Iniciar Sesión");
        cmdBackLogin.addActionListener(e -> ModalDialog.popModel(Login.ID));
        linkPanel.add(cmdBackLogin);

        panel.add(linkPanel, "align center");

        return panel;
    }

    private JLabel createLabel(String text, String emoji) {
        JLabel label = new JLabel(emoji + " " + text);
        label.putClientProperty(FlatClientProperties.STYLE, "font:bold");
        return label;
    }

    private JTextField createTextField(String placeholder) {
        JTextField txt = new JTextField();
        txt.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        txt.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                        "borderWidth:1");
        return txt;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField pwd = new JPasswordField();
        pwd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        pwd.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                        "borderWidth:1;" +
                        "showRevealButton:true");
        return pwd;
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE,
                "foreground:lighten($Panel.background,10%)");
        return separator;
    }

    private void updatePasswordStrength() {
        String password = new String(txtPassword.getPassword());
        int strength = calculatePasswordStrength(password);

        if (password.isEmpty()) {
            lblPasswordStrength.setText(" ");
            return;
        }

        String[] levels = {"Muy Débil", "Débil", "Media", "Fuerte", "Muy Fuerte"};
        Color[] colors = {
                new Color(255, 69, 58),   // Rojo
                new Color(255, 149, 0),   // Naranja
                new Color(255, 204, 0),   // Amarillo
                new Color(52, 199, 89),   // Verde
                new Color(48, 176, 199)   // Azul
        };

        lblPasswordStrength.setText("Fortaleza: " + levels[strength]);
        lblPasswordStrength.setForeground(colors[strength]);
    }

    private int calculatePasswordStrength(String password) {
        int strength = 0;
        if (password.length() >= 8) strength++;
        if (password.length() >= 12) strength++;
        if (password.matches(".*[A-Z].*")) strength++;
        if (password.matches(".*[0-9].*")) strength++;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) strength++;
        return Math.min(strength, 4);
    }

    private void mostrarTerminos() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap,insets 20", "[center]"));

        JLabel titulo = new JLabel("📜 Términos y Condiciones");
        titulo.putClientProperty(FlatClientProperties.STYLE, "font:bold +4");
        panel.add(titulo);

        JTextArea textArea = new JTextArea(
                "1. El usuario debe mantener su contraseña segura.\n" +
                        "2. No compartir credenciales con terceros.\n" +
                        "3. Uso responsable del sistema.\n" +
                        "4. Respeto a las políticas de la empresa.\n" +
                        "5. Confidencialidad de la información."
        );
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setRows(5);
        panel.add(new JScrollPane(textArea), "width 400");

        JOptionPane.showMessageDialog(this, panel, "Términos y Condiciones",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void realizarRegistro() {
        String adminUser = txtAdminUser.getText().trim();
        String adminPassword = new String(txtAdminPassword.getPassword()).trim();

        if (adminUser.isEmpty() || adminPassword.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Debe ingresar las credenciales del administrador");
            return;
        }

        if (!validarCredencialesAdmin(adminUser, adminPassword)) {
            Notifications.getInstance().show(Notifications.Type.ERROR,
                    "Las credenciales del administrador son incorrectas");
            txtAdminPassword.setText("");
            txtAdminPassword.requestFocus();
            return;
        }

        String nombreUsuario = txtNombreUsuario.getText().trim();
        String nombreCompleto = txtNombreCompleto.getText().trim();
        String email = txtEmail.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();
        String rol = (String) cmbRol.getSelectedItem();

        if (!validarCampos(nombreUsuario, nombreCompleto, email, telefono, password, confirmPassword)) {
            return;
        }

        if (!chkAceptoTerminos.isSelected()) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Debe aceptar los términos y condiciones");
            return;
        }

        JDialog loadingDialog = mostrarLoading();

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                try {
                    Thread.sleep(800);
                    return authFacade.registrarUsuario(nombreUsuario, password,
                            nombreCompleto, rol, telefono, email);
                } catch (InterruptedException ex) {
                    return false;
                }
            }

            @Override
            protected void done() {
                loadingDialog.dispose();
                try {
                    Boolean resultado = get();
                    if (resultado) {
                        mostrarExito();
                        limpiarCampos();
                        Timer timer = new Timer(1500, e -> ModalDialog.popModel(Login.ID));
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        Notifications.getInstance().show(Notifications.Type.ERROR,
                                "Error al registrar usuario. Verifique los datos.");
                    }
                } catch (Exception ex) {
                    Notifications.getInstance().show(Notifications.Type.ERROR,
                            "Error: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void mostrarExito() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap,insets 20", "[center]"));

        JLabel icono = new JLabel("✅");
        icono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        panel.add(icono);

        JLabel titulo = new JLabel("¡Usuario Registrado!");
        titulo.putClientProperty(FlatClientProperties.STYLE, "font:bold +4");
        panel.add(titulo);

        JLabel mensaje = new JLabel("El usuario ha sido creado exitosamente");
        panel.add(mensaje);

        Notifications.getInstance().show(Notifications.Type.SUCCESS,
                "¡Usuario registrado exitosamente!");
    }

    private boolean validarCredencialesAdmin(String adminUser, String adminPassword) {
        try {
            PasswordEncryption encryption = new PasswordEncryption();
            String passwordEncriptada = encryption.encriptar(adminPassword);
            Models.Usuario admin = usuarioDAO.obtenerPorNombreUsuario(adminUser);
            return admin != null &&
                    "Administrador".equals(admin.getRol()) &&
                    passwordEncriptada.equals(admin.getContrasena());
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validarCampos(String nombreUsuario, String nombreCompleto,
                                  String email, String telefono,
                                  String password, String confirmPassword) {

        if (nombreUsuario.isEmpty() || nombreUsuario.length() < 4) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "El nombre de usuario debe tener al menos 4 caracteres");
            txtNombreUsuario.requestFocus();
            return false;
        }

        if (nombreCompleto.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Debe ingresar el nombre completo");
            txtNombreCompleto.requestFocus();
            return false;
        }

        if (!validarEmail(email)) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Email inválido");
            txtEmail.requestFocus();
            return false;
        }

        if (telefono.isEmpty() || telefono.length() < 9) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Teléfono inválido (mínimo 9 dígitos)");
            txtTelefono.requestFocus();
            return false;
        }

        if (password.length() < 8) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "La contraseña debe tener al menos 8 caracteres");
            txtPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Notifications.getInstance().show(Notifications.Type.WARNING,
                    "Las contraseñas no coinciden");
            txtConfirmPassword.setText("");
            txtConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validarEmail(String email) {
        return Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$").matcher(email).matches();
    }

    private JDialog mostrarLoading() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                "Procesando", true);
        dialog.setUndecorated(true);

        JPanel panel = new JPanel(new MigLayout("fillx,wrap,insets 30", "[center]"));
        panel.putClientProperty(FlatClientProperties.STYLE,
                "background:$Panel.background;arc:20");

        JLabel iconoLoad = new JLabel("⏳");
        iconoLoad.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        panel.add(iconoLoad);

        JLabel lblTexto = new JLabel("Registrando usuario...");
        lblTexto.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
        panel.add(lblTexto);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        panel.add(progressBar, "width 300");

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

        button.addActionListener(new ActionListener() {
            private char defaultEchoChart = txt.getEchoChar();
            private boolean show;

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
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
        txtAdminUser.setText("");
        txtAdminPassword.setText("");
        txtNombreUsuario.setText("");
        txtNombreCompleto.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        cmbRol.setSelectedIndex(0);
        chkAceptoTerminos.setSelected(false);
        lblPasswordStrength.setText(" ");
    }
}
