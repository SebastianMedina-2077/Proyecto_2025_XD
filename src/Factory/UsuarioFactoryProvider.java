package Factory;

public class UsuarioFactoryProvider {
    public static UsuarioFactory getFactory(String tipoUsuario) {
        return switch (tipoUsuario.toUpperCase()) {
            case "ADMINISTRADOR" -> new AdministradorFactory();
            case "VENDEDOR" -> new VendedorFactory();
            case "ALMACENERO" -> new AlmaceneroFactory();
            default -> throw new IllegalArgumentException("Tipo de usuario no válido: " + tipoUsuario);
        };
    }
}
