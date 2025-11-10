package Models;

import java.time.LocalDateTime;

public class LogAcceso {
    private int idLog;
    private String nombreUsuario;
    private boolean exitoso;
    private String ipAcceso;
    private LocalDateTime fechaIntento;
    private String motivo;

    public LogAcceso() {}

    public LogAcceso(String nombreUsuario, boolean exitoso, String ipAcceso, String motivo) {
        this.nombreUsuario = nombreUsuario;
        this.exitoso = exitoso;
        this.ipAcceso = ipAcceso;
        this.motivo = motivo;
    }

    // Getters y Setters
    public int getIdLog() { return idLog; }
    public void setIdLog(int idLog) { this.idLog = idLog; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public boolean isExitoso() { return exitoso; }
    public void setExitoso(boolean exitoso) { this.exitoso = exitoso; }

    public String getIpAcceso() { return ipAcceso; }
    public void setIpAcceso(String ipAcceso) { this.ipAcceso = ipAcceso; }

    public LocalDateTime getFechaIntento() { return fechaIntento; }
    public void setFechaIntento(LocalDateTime fechaIntento) { this.fechaIntento = fechaIntento; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
