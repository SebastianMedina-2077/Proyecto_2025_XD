package Clases_DAO;

import Clases.Contacto;
import Clases.Persona;
import DB_Conection.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class PersonaDAO extends DAO<Persona> {
    private static final String LISTAR_TODO_PROC = "SP_ListarPersonas";
    private static final String LISTAR_POR_ID_PROC = "SP_ListarPersonaPorId";
    private static final String REGISTRAR_PERSONA_BASE_PROC = "SP_RegistrarPersona";

    // Dependencia de DAOs de catálogos (inicializarlos en el constructor o inyectarlos)
    protected EstadoDAO estadoDAO = new EstadoDAO();
    protected TipoDAO tipoDAO = new TipoDAO();

    @Override
    public Persona parsear(ResultSet rs) throws SQLException {
        // Este parsear puede ser usado para listados generales de PERSONA_SM
        // o como base para parsear subclases.
        String dni = rs.getString("DNI_SM");
        String nombre = rs.getString("NOMBRE_SM");
        String apellido = rs.getString("APELLIDO_SM");
        Date fechaNac = rs.getDate("FECHA_NACIMIENTO");
        String genero = rs.getString("GENERO");
        int idContacto = rs.getInt("ID_CONTACTO");
        if (rs.wasNull()) idContacto = 0;
        Integer idEstado = rs.getInt("ID_ESTADO"); // Solo si el SP lo incluye
        if (rs.wasNull()) idEstado = null;
        Integer idTipo = rs.getInt("ID_TIPO");     // Solo si el SP lo incluye
        if (rs.wasNull()) idTipo = null;

        // Obtener datos del contacto (asumiendo que vienen en el mismo ResultSet por JOIN)
        String tel = rs.getString("NUMERO_TEL");
        String correo = rs.getString("CORREO");
        String whatsapp = rs.getString("WHATSAPP");
        String direccion = rs.getString("DIRECCION_COMPLETA");
        String depto = rs.getString("DEPARTAMENTO_DIR");
        String prov = rs.getString("PROVINCIA_DIR");
        String dist = rs.getString("DISTRITO_DIR");
        String ref = rs.getString("REFERENCIA");

        // Crea el objeto Contacto
        Contacto contacto = null;
        if (tel != null || correo != null || whatsapp != null || direccion != null) {
            contacto = new Contacto(tel, null, correo, whatsapp, direccion, depto, prov, dist, ref); // operador es null
        }

        // Obtener objetos Estado y Tipo (simplificado)
        Catalogos.Estado estadoObj = idEstado != null ? obtenerEstadoPorId(idEstado) : null; // Reemplaza con lógica real
        Catalogos.Tipo tipoObj = idTipo != null ? obtenerTipoPorId(idTipo) : null;           // Reemplaza con lógica real

        // Crear la instancia específica de la subclase de Persona
        return crearInstancia(dni, nombre, apellido, fechaNac, genero, contacto, estadoObj, tipoObj);
    }

    // Método abstracto para que subclases creen instancias concretas
    protected abstract Persona crearInstancia(String dni, String nombre, String apellido, Date fechaNac, String genero, Contacto contacto, Catalogos.Estado estado, Catalogos.Tipo tipo);

    // Método auxiliar para obtener Estado
    private Catalogos.Estado obtenerEstadoPorId(Integer id) {
        if (id == null) return null;
        try {
            List<Catalogos.Estado> estados = estadoDAO.listarPorId(id, "SP_ListarEstadoPorId"); // Ajusta SP
            return !estados.isEmpty() ? estados.get(0) : null;
        } catch (Exception e) {
            manejarError("Error al obtener Estado por ID: " + id, e);
            return null;
        }
    }

    // Método auxiliar para obtener Tipo
    private Catalogos.Tipo obtenerTipoPorId(Integer id) {
        if (id == null) return null;
        try {
            List<Catalogos.Tipo> tipos = tipoDAO.listarPorId(id, "SP_ListarTipoPorId"); // Ajusta SP
            return !tipos.isEmpty() ? tipos.get(0) : null;
        } catch (Exception e) {
            manejarError("Error al obtener Tipo por ID: " + id, e);
            return null;
        }
    }

    // --- Métodos Específicos (Genéricos o para la base) ---

    // Este método registra solo en PERSONA_SM, CONTACTO_SM, DIRECCION_SM
    protected boolean registrarPersonaBase(Persona persona) {
        boolean exito = false;
        String mensaje = "";
        int resultado = 0;

        try (Connection con = getconection();
             CallableStatement cs = con.prepareCall("{CALL " + REGISTRAR_PERSONA_BASE_PROC + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {

            cs.setString(1, persona.getDni());
            cs.setString(2, persona.getNombre());
            cs.setString(3, persona.getApellido());
            cs.setDate(4, persona.getFechanacimiento()); // Puede ser null
            cs.setString(5, persona.getGenero());         // Puede ser null

            Contacto contacto = persona.getContacto();
            if (contacto != null) {
                cs.setString(6, contacto.getNumeroTelefonico());
                cs.setString(7, contacto.getWhatsapp());
                cs.setString(8, contacto.getCorreoElectronico());
                cs.setString(9, contacto.getDireccion());
                cs.setString(10, contacto.getDepartamento());
                cs.setString(11, contacto.getProvincia());
                cs.setString(12, contacto.getDistrito());
                cs.setString(13, contacto.getReferencia());
            } else {
                for (int i = 6; i <= 13; i++) {
                    cs.setNull(i, Types.VARCHAR);
                }
            }

            cs.registerOutParameter(14, Types.INTEGER); // @Resultado
            cs.registerOutParameter(15, Types.VARCHAR, 200); // @Mensaje

            cs.execute();

            resultado = cs.getInt(14);
            mensaje = cs.getString(15);

            exito = (resultado == 1);
            if (!exito) {
                manejarError("Error del SP: " + mensaje, null);
                mensajeDeError("Error del SP: " + mensaje);
            } else {
                mostrarMensajeExito("Persona registrada exitosamente.");
            }

        } catch (SQLException e) {
            manejarError("Error al registrar persona base", e);
            mensajeDeError("Error al registrar persona: " + e.getMessage());
        }
        return exito;
    }

    public List<Persona> listarTodo() {
        return super.listarTodo(LISTAR_TODO_PROC);
    }
}
