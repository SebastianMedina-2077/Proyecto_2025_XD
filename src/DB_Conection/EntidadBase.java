package DB_Conection;

import java.time.LocalDateTime;

public abstract class EntidadBase<T> {
    protected Integer id;
    protected LocalDateTime fechaRegistro;
    protected LocalDateTime fechaModificacion;

    // Constructor por defecto
    public EntidadBase() {
        this.fechaRegistro = LocalDateTime.now();
        this.fechaModificacion = LocalDateTime.now();
    }

    // Constructor con parámetros básicos
    public EntidadBase(Integer id) {
        this.id = id;
        this.fechaRegistro = LocalDateTime.now();
        this.fechaModificacion = LocalDateTime.now();
    }

    // Constructor completo
    public EntidadBase(Integer id, LocalDateTime fechaRegistro, LocalDateTime fechaModificacion) {
        this.id = id;
        this.fechaRegistro = fechaRegistro != null ? fechaRegistro : LocalDateTime.now();
        this.fechaModificacion = fechaModificacion != null ? fechaModificacion : LocalDateTime.now();
    }

    // Métodos abstractos
    public abstract String getNombre();
    public abstract boolean isActivo();

    // Métodos auxiliares
    public void actualizarFechaModificacion() {
        this.fechaModificacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;

    }
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }
    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
}
