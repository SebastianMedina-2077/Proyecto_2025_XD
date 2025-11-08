package Clases_DAO;

import Clases.Contacto;
import Clases.Direccion;
import Clases.Persona;
import DB_Conection.DAOAdaptado;
import Enum.Genero;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO extends DAOAdaptado<Persona, String> {
    @Override
    protected String obtenerProcedureListarTodos() {
        return "SP_ListarPersonas";
    }

    @Override
    protected String obtenerProcedureListarPorId() {
        return "SP_ListarPersonaPorDni";
    }

    @Override
    protected String obtenerProcedureGuardar() {
        return "SP_RegistrarPersona";
    }

    @Override
    protected String obtenerProcedureEliminar() {
        return null; // No se elimina personas
    }

    @Override
    protected String obtenerProcedureActualizar() {
        return "SP_ActualizarPersona";
    }

    @Override
    protected List<Persona> ejecutarProcedureListar(String procedure) {
        return ejecutarProcedureGenerico(procedure, new PreparedStatementCallback<Persona>() {
            @Override
            public void setParameters(CallableStatement cs) throws SQLException {
                // No hay parámetros para listar todos
            }

            @Override
            public Persona parsear(ResultSet rs) throws SQLException {
                return parsearPersona(rs);
            }
        });
    }

    @Override
    protected List<Persona> ejecutarProcedureListarPorId(String dni, String procedure) {
        List<Persona> lista = new ArrayList<>();
        try (Connection con = obtenerConexion()) {
            if (con == null) return lista;

            try (CallableStatement cs = con.prepareCall("{CALL " + procedure + "(?)}")) {
                cs.setString(1, dni);

                try (ResultSet rs = cs.executeQuery()) {
                    if (rs.next()) {
                        Persona persona = parsearPersona(rs);
                        if (persona != null) {
                            lista.add(persona);
                        }
                    }
                }
            }
        } catch (Exception e) {
            manejarError("Error al listar persona por DNI", e);
        }
        return lista;
    }

    @Override
    protected Persona ejecutarProcedureGuardar(Connection con, String procedure, Persona persona) {
        try (CallableStatement cs = con.prepareCall(
                "{CALL SP_RegistrarPersona(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}"
        )) {
            // Parámetros de entrada
            cs.setString(1, persona.getDni());
            cs.setString(2, persona.getNombre());
            cs.setString(3, persona.getApellido());

            // Fecha de nacimiento (opcional)
            if (persona.getFechaNacimiento() != null) {
                cs.setDate(4, Date.valueOf(persona.getFechaNacimiento()));
            } else {
                cs.setNull(4, Types.DATE);
            }

            // Género (opcional)
            if (persona.getGenero() != null) {
                cs.setString(5, persona.getGenero().name().substring(0, 1));
            } else {
                cs.setNull(5, Types.CHAR);
            }

            // Datos de contacto (opcionales)
            if (persona.getContacto() != null) {
                Contacto contacto = persona.getContacto();

                cs.setString(6, contacto.getNumeroTel());
                cs.setString(7, contacto.getWhatsapp());
                cs.setString(8, contacto.getCorreo());

                // Datos de dirección (opcionales)
                if (contacto.getDireccion() != null) {
                    Direccion dir = contacto.getDireccion();
                    cs.setString(9, dir.getDireccionCompleta());
                    cs.setString(10, dir.getDepartamento());
                    cs.setString(11, dir.getProvincia());
                    cs.setString(12, dir.getDistrito());
                    cs.setString(13, dir.getReferencia());
                } else {
                    cs.setNull(9, Types.VARCHAR);
                    cs.setNull(10, Types.VARCHAR);
                    cs.setNull(11, Types.VARCHAR);
                    cs.setNull(12, Types.VARCHAR);
                    cs.setNull(13, Types.VARCHAR);
                }
            } else {
                // Sin contacto
                for (int i = 6; i <= 13; i++) {
                    cs.setNull(i, Types.VARCHAR);
                }
            }

            // Parámetros de salida
            cs.registerOutParameter(14, Types.INTEGER); // @Resultado
            cs.registerOutParameter(15, Types.VARCHAR); // @Mensaje

            cs.execute();

            int resultado = cs.getInt(14);
            String mensaje = cs.getString(15);

            if (resultado == 1) {
                mostrarMensajeExito(mensaje);
                return persona;
            } else {
                mostrarMensajeError(mensaje);
                return null;
            }

        } catch (SQLException e) {
            manejarError("Error al registrar persona", e);
            return null;
        }
    }

    @Override
    protected boolean ejecutarProcedureEliminar(Connection con, String procedure, String id) {
        // No se implementa eliminación de personas
        mostrarMensajeAdvertencia("No se permite eliminar personas del sistema");
        return false;
    }

    @Override
    protected boolean ejecutarProcedureActualizar(Connection con, String procedure, Persona persona) {
        try (CallableStatement cs = con.prepareCall(
                "{CALL SP_ActualizarPersona(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}"
        )) {
            cs.setString(1, persona.getDni());
            cs.setString(2, persona.getNombre());
            cs.setString(3, persona.getApellido());

            if (persona.getFechaNacimiento() != null) {
                cs.setDate(4, Date.valueOf(persona.getFechaNacimiento()));
            } else {
                cs.setNull(4, Types.DATE);
            }

            if (persona.getGenero() != null) {
                cs.setString(5, persona.getGenero().name().substring(0, 1));
            } else {
                cs.setNull(5, Types.CHAR);
            }

            if (persona.getContacto() != null) {
                Contacto contacto = persona.getContacto();
                cs.setString(6, contacto.getNumeroTel());
                cs.setString(7, contacto.getWhatsapp());
                cs.setString(8, contacto.getCorreo());

                if (contacto.getDireccion() != null) {
                    Direccion dir = contacto.getDireccion();
                    cs.setString(9, dir.getDireccionCompleta());
                    cs.setString(10, dir.getDepartamento());
                    cs.setString(11, dir.getProvincia());
                    cs.setString(12, dir.getDistrito());
                    cs.setString(13, dir.getReferencia());
                } else {
                    for (int i = 9; i <= 13; i++) {
                        cs.setNull(i, Types.VARCHAR);
                    }
                }
            } else {
                for (int i = 6; i <= 13; i++) {
                    cs.setNull(i, Types.VARCHAR);
                }
            }

            cs.registerOutParameter(14, Types.INTEGER);
            cs.registerOutParameter(15, Types.VARCHAR);

            cs.execute();

            int resultado = cs.getInt(14);
            String mensaje = cs.getString(15);

            if (resultado == 1) {
                mostrarMensajeExito(mensaje);
                return true;
            } else {
                mostrarMensajeError(mensaje);
                return false;
            }

        } catch (SQLException e) {
            manejarError("Error al actualizar persona", e);
            return false;
        }
    }

    @Override
    protected boolean validarEntidad(Persona persona) {
        if (!validarObjetoNulo(persona, "Persona")) {
            return false;
        }

        if (!ValidarCampoVacio(persona.getDni(), "DNI")) {
            return false;
        }

        if (!ValidarCampoVacio(persona.getNombre(), "Nombre")) {
            return false;
        }

        if (!ValidarCampoVacio(persona.getApellido(), "Apellido")) {
            return false;
        }

        // Validar formato DNI (8 dígitos)
        if (!persona.getDni().matches("^[0-9]{8}$")) {
            mostrarMensajeError("El DNI debe tener exactamente 8 dígitos");
            return false;
        }

        // Validar email si existe
        if (persona.getContacto() != null && persona.getContacto().getCorreo() != null) {
            if (!persona.getContacto().getCorreo().matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
                mostrarMensajeError("El formato del email es incorrecto");
                return false;
            }
        }

        // Validar teléfono si existe
        if (persona.getContacto() != null && persona.getContacto().getNumeroTel() != null) {
            if (!persona.getContacto().getNumeroTel().matches("^[0-9]{9}$")) {
                mostrarMensajeError("El teléfono debe tener exactamente 9 dígitos");
                return false;
            }
        }

        return true;
    }

    @Override
    protected String obtenerProcedureBuscarPorCriterio() {
        return "SP_BuscarPersonaPorCriterio";
    }

    @Override
    protected List<Persona> ejecutarProcedureBuscarPorCriterio(String criterio, Object valor, String procedure) {
        return ejecutarProcedureGenerico(procedure, new PreparedStatementCallback<Persona>() {
            @Override
            public void setParameters(CallableStatement cs) throws SQLException {
                cs.setString(1, criterio);
                cs.setString(2, valor.toString());
            }

            @Override
            public Persona parsear(ResultSet rs) throws SQLException {
                return parsearPersona(rs);
            }
        });
    }

    // Método auxiliar para parsear ResultSet a Persona
    // Como Persona es abstracta, retornamos un objeto anónimo
    private Persona parsearPersona(ResultSet rs) throws SQLException {
        String dni = rs.getString("DNI_SM");
        String nombre = rs.getString("NOMBRE_SM");
        String apellido = rs.getString("APELLIDO_SM");

        Date fechaNacDate = rs.getDate("FECHA_NACIMIENTO");
        LocalDate fechaNacimiento = fechaNacDate != null ? fechaNacDate.toLocalDate() : null;

        String generoStr = rs.getString("GENERO");
        Genero genero = (generoStr != null && !generoStr.isEmpty())
                ? Genero.valueOf(generoStr.equals("M") ? "MASCULINO" : "FEMENINO")
                : null;

        // Crear contacto
        Contacto contacto = new Contacto();
        contacto.setNumeroTel(rs.getString("NUMERO_TEL"));
        contacto.setWhatsapp(rs.getString("WHATSAPP"));
        contacto.setCorreo(rs.getString("CORREO"));

        // Crear dirección
        String direccionCompleta = rs.getString("DIRECCION_COMPLETA");
        if (direccionCompleta != null) {
            Direccion direccion = new Direccion();
            direccion.setDireccionCompleta(direccionCompleta);
            direccion.setDepartamento(rs.getString("DEPARTAMENTO_DIR"));
            direccion.setProvincia(rs.getString("PROVINCIA_DIR"));
            direccion.setDistrito(rs.getString("DISTRITO_DIR"));
            direccion.setReferencia(rs.getString("REFERENCIA"));
            contacto.setDireccion(direccion);
        }

        // Retornar una implementación concreta de Persona
        return new Persona(null, dni, nombre, apellido, fechaNacimiento, genero, contacto) {
            @Override
            public String getTipoPersona() {
                return "PERSONA";
            }

            @Override
            public String getIdentificacionCompleta() {
                return dni + " - " + getNombreCompleto();
            }

            @Override
            public boolean isActivo() {
                return true;
            }
        };
    }

    // Método específico para verificar si una persona existe
    public boolean existePersonaPorDni(String dni) {
        if (!ValidarCampoVacio(dni, "DNI")) {
            return false;
        }

        List<Persona> personas = (List<Persona>) obtenerPorId(dni);
        return !personas.isEmpty();
    }
}
