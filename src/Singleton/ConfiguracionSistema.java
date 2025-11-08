package Singleton;

import java.math.BigDecimal;

public class ConfiguracionSistema {
    private static ConfiguracionSistema instancia;

    private String nombreEmpresa;
    private String direccionEmpresa;
    private BigDecimal porcentajeIGV;
    private int diasAnticipacionVencimiento;
    private int maximoIntentosLogin;
    private int minutosBloqueoUsuario;

    private ConfiguracionSistema() {
        // Configuración por defecto
        this.nombreEmpresa = "YENYS S.A.";
        this.direccionEmpresa = "Jr Aayacucho 380 - Lima, Perú";
        this.porcentajeIGV = BigDecimal.valueOf(18.0);
        this.diasAnticipacionVencimiento = 30;
        this.maximoIntentosLogin = 3;
        this.minutosBloqueoUsuario = 30;
    }

    public static ConfiguracionSistema obtenerInstancia() {
        if (instancia == null) {
            synchronized (ConfiguracionSistema.class) {
                if (instancia == null) {
                    instancia = new ConfiguracionSistema();
                }
            }
        }
        return instancia;
    }

    // Getters y Setters
    public String getNombreEmpresa() { return nombreEmpresa; }
    public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }

    public String getDireccionEmpresa() { return direccionEmpresa; }
    public void setDireccionEmpresa(String direccionEmpresa) { this.direccionEmpresa = direccionEmpresa; }

    public BigDecimal getPorcentajeIGV() { return porcentajeIGV; }
    public void setPorcentajeIGV(BigDecimal porcentajeIGV) { this.porcentajeIGV = porcentajeIGV; }

    public int getDiasAnticipacionVencimiento() { return diasAnticipacionVencimiento; }
    public void setDiasAnticipacionVencimiento(int diasAnticipacionVencimiento) {
        this.diasAnticipacionVencimiento = diasAnticipacionVencimiento;
    }

    public int getMaximoIntentosLogin() { return maximoIntentosLogin; }
    public void setMaximoIntentosLogin(int maximoIntentosLogin) {
        this.maximoIntentosLogin = maximoIntentosLogin;
    }

    public int getMinutosBloqueoUsuario() { return minutosBloqueoUsuario; }
    public void setMinutosBloqueoUsuario(int minutosBloqueoUsuario) {
        this.minutosBloqueoUsuario = minutosBloqueoUsuario;
    }
}
