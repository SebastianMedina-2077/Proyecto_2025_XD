package raven.components;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Background extends JPanel {

    private final boolean udecorated;
    private BufferedImage backgroundImage;
    
    public Background(boolean udecorated) {
        this.udecorated=udecorated;
        init();
        loadBackgroundImage();
    }

    private void init() {
        setOpaque(true);
        setLayout(new BorderLayout());
        putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5;"
                + "arc:30");
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("img/fondo.jpg"));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
            backgroundImage = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        FlatUIUtils.setRenderingHints(g2);

        // Pintar la imagen de fondo si está cargada
        if (backgroundImage != null) {
            // Escalar la imagen para que cubra todo el panel
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Si no hay imagen, pintar el fondo por defecto
            if (udecorated) {
                int arc = UIScale.scale(30);
                g2.setColor(getBackground());
                FlatUIUtils.paintComponentBackground(g2, 0, 0, getWidth(), getHeight(), 0, arc);
            }
        }
        g2.dispose();
    }
}
