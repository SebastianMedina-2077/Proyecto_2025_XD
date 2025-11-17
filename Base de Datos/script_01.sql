create table Almacen
(
    idAlmacen     int          not null
        primary key
        check ([idAlmacen] = 252 OR [idAlmacen] = 251),
    nombreAlmacen varchar(100) not null,
    direccion     varchar(200),
    responsable   varchar(100),
    telefono      varchar(20),
    estado        varchar(15) default 'Activo'
        check ([estado] = 'Inactivo' OR [estado] = 'Activo')
)
go

create table Categoria
(
    idCategoria     int identity
        primary key,
    nombreCategoria varchar(100) not null
        unique,
    descripcion     varchar(255),
    estado          varchar(15) default 'Activo'
        check ([estado] = 'Inactivo' OR [estado] = 'Activo')
)
go

create table Cliente
(
    idCliente              int identity
        primary key,
    tipoCliente            varchar(20)  not null
        check ([tipoCliente] = 'Eventual' OR [tipoCliente] = 'Minorista' OR [tipoCliente] = 'Mayorista'),
    razonSocial            varchar(150) not null,
    documento              varchar(11)  not null
        unique,
    tipoDocumento          varchar(10)
        check ([tipoDocumento] = 'DNI' OR [tipoDocumento] = 'RUC'),
    telefono               varchar(20),
    email                  varchar(80),
    direccion              varchar(250),
    descuentoPersonalizado decimal(5, 2) default 0
        check ([descuentoPersonalizado] >= 0 AND [descuentoPersonalizado] <= 100),
    fechaRegistro          date          default CONVERT([date], getdate()),
    estado                 varchar(15)   default 'Activo'
        check ([estado] = 'Inactivo' OR [estado] = 'Activo')
)
go

create index IX_Cliente_Documento
    on Cliente (documento)
go

create table LogAcceso
(
    idLog         int identity
        primary key,
    nombreUsuario varchar(50) not null,
    exitoso       bit         not null,
    ipAcceso      varchar(50),
    fechaIntento  datetime default getdate(),
    motivo        varchar(100)
)
go

create table Marca
(
    idMarca     int identity
        primary key,
    nombreMarca varchar(100) not null
        unique,
    estado      varchar(15) default 'Activo'
        check ([estado] = 'Inactivo' OR [estado] = 'Activo')
)
go

create table MedioPago
(
    idMedioPago int identity
        primary key,
    nombreMedio varchar(50) not null
        unique,
    tipo        varchar(20)
        check ([tipo] = 'Transferencia' OR [tipo] = 'Efectivo' OR [tipo] = 'Digital'),
    activo      bit default 1
)
go

create table Producto
(
    idProducto              int identity
        primary key,
    codigoProducto          varchar(50)    not null
        unique,
    nombreProducto          varchar(150)   not null,
    descripcion             varchar(500),
    idCategoria             int            not null
        references Categoria,
    idMarca                 int            not null
        references Marca,
    edadRecomendada         varchar(20),
    precioUnidad            decimal(10, 2) not null
        check ([precioUnidad] > 0),
    precioDocena            decimal(10, 2) not null
        check ([precioDocena] > 0),
    precioMayorista         decimal(10, 2) not null
        check ([precioMayorista] > 0),
    cantidadMinimaMayorista int         default 50,
    stockMinimo             int         default 10,
    pesoKg                  decimal(8, 3),
    fechaCreacion           datetime    default getdate(),
    estado                  varchar(15) default 'Activo'
        check ([estado] = 'Inactivo' OR [estado] = 'Activo')
)
go

create table Inventario
(
    idInventario        int identity
        primary key,
    idProducto          int not null
        references Producto,
    idAlmacen           int not null
        references Almacen,
    stockActual         int      default 0
        check ([stockActual] >= 0),
    ultimaActualizacion datetime default getdate(),
    unique (idProducto, idAlmacen)
)
go

create index IX_Inventario_Producto
    on Inventario (idProducto)
go

create index IX_Producto_Codigo
    on Producto (codigoProducto)
go

create table Promocion
(
    idPromocion         int identity
        primary key,
    nombrePromocion     varchar(100) not null,
    descripcion         varchar(255),
    tipoPromocion       varchar(30)  not null
        check ([tipoPromocion] = 'Regalo' OR [tipoPromocion] = 'Precio Especial' OR [tipoPromocion] = '3x2' OR
               [tipoPromocion] = '2x1' OR [tipoPromocion] = 'Descuento'),
    porcentajeDescuento decimal(5, 2)
        check ([porcentajeDescuento] >= 0 AND [porcentajeDescuento] <= 100),
    cantidadCompra      int,
    cantidadPaga        int,
    aplicaProducto      int
        references Producto,
    aplicaCategoria     int
        references Categoria,
    aplicaMedioPago     varchar(20),
    montoMinimo         decimal(10, 2) default 0,
    fechaInicio         date         not null,
    fechaFin            date         not null,
    activa              bit            default 1,
    estado              varchar(15)    default 'Activo'
        check ([estado] = 'Inactivo' OR [estado] = 'Activo'),
    check ([fechaFin] >= [fechaInicio])
)
go

create index IX_Promocion_Fechas
    on Promocion (fechaInicio, fechaFin, activa)
go

create table Proveedor
(
    idProveedor   int identity
        primary key,
    razonSocial   varchar(150) not null,
    ruc           varchar(11)  not null
        unique,
    telefono      varchar(20),
    email         varchar(80),
    direccion     varchar(250),
    contacto      varchar(100),
    fechaRegistro date        default CONVERT([date], getdate()),
    estado        varchar(15) default 'Activo'
        check ([estado] = 'Inactivo' OR [estado] = 'Activo')
)
go

create table Usuario
(
    idUsuario        int identity
        primary key,
    nombreUsuario    varchar(50)  not null
        unique,
    contrasena       varchar(255) not null,
    nombreCompleto   varchar(150) not null,
    rol              varchar(20)  not null
        check ([rol] = 'Almacenero' OR [rol] = 'Vendedor' OR [rol] = 'Administrador'),
    telefono         varchar(20),
    email            varchar(80),
    ultimoAcceso     datetime,
    intentosFallidos int         default 0,
    bloqueadoHasta   datetime,
    fechaCreacion    datetime    default getdate(),
    estado           varchar(15) default 'Activo'
        check ([estado] = 'Inactivo' OR [estado] = 'Activo')
)
go

create table Compra
(
    idCompra      int identity
        primary key,
    numeroCompra  varchar(20)    not null
        unique,
    idProveedor   int            not null
        references Proveedor,
    idAlmacen     int            not null
        references Almacen,
    idUsuario     int            not null
        references Usuario,
    fechaCompra   date        default CONVERT([date], getdate()),
    numeroFactura varchar(50),
    subtotal      decimal(12, 2) not null,
    igv           decimal(12, 2) not null,
    total         decimal(12, 2) not null,
    observaciones varchar(500),
    estado        varchar(20) default 'Completada'
        check ([estado] = 'Anulada' OR [estado] = 'Completada')
)
go

create table DetalleCompra
(
    idDetalle      int identity
        primary key,
    idCompra       int            not null
        references Compra,
    idProducto     int            not null
        references Producto,
    cantidad       int            not null
        check ([cantidad] > 0),
    precioUnitario decimal(10, 2) not null,
    subtotal       decimal(12, 2) not null
)
go

-- Trigger: Actualizar inventario al comprar
CREATE TRIGGER TRG_ActualizarInventarioCompra
ON DetalleCompra
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;
    
    UPDATE inv
    SET 
        inv.stockActual = inv.stockActual + i.cantidad,
        inv.ultimaActualizacion = GETDATE()
    FROM Inventario inv
    INNER JOIN inserted i ON inv.idProducto = i.idProducto
    INNER JOIN Compra c ON i.idCompra = c.idCompra
    WHERE inv.idAlmacen = c.idAlmacen;
    
    -- Registrar movimiento
    INSERT INTO MovimientoInventario (idProducto, idAlmacen, tipoMovimiento, cantidad, stockAnterior, stockNuevo, referencia)
    SELECT 
        i.idProducto,
        c.idAlmacen,
        'Entrada',
        i.cantidad,
        inv.stockActual - i.cantidad,
        inv.stockActual,
        'COMPRA-' + c.numeroCompra
    FROM inserted i
    INNER JOIN Compra c ON i.idCompra = c.idCompra
    INNER JOIN Inventario inv ON i.idProducto = inv.idProducto AND inv.idAlmacen = c.idAlmacen;
END;
go

create table MovimientoInventario
(
    idMovimiento    int identity
        primary key,
    idProducto      int         not null
        references Producto,
    idAlmacen       int         not null
        references Almacen,
    tipoMovimiento  varchar(30) not null
        check ([tipoMovimiento] = 'Transferencia' OR [tipoMovimiento] = 'Ajuste' OR [tipoMovimiento] = 'Salida' OR
               [tipoMovimiento] = 'Entrada'),
    cantidad        int         not null,
    stockAnterior   int         not null,
    stockNuevo      int         not null,
    motivo          varchar(200),
    referencia      varchar(100),
    idUsuario       int
        references Usuario,
    fechaMovimiento datetime default getdate()
)
go

create index IX_MovimientoInventario_Fecha
    on MovimientoInventario (fechaMovimiento)
go

-- ============================================================
-- TRIGGER: Auditoría de cambios importantes en Usuario
-- ============================================================
CREATE   TRIGGER TRG_AuditoriaUsuario
    ON Usuario
    AFTER UPDATE
    AS
BEGIN
    SET NOCOUNT ON;

    -- Detectar cambios importantes
    IF UPDATE(rol) OR UPDATE(estado)
        BEGIN
            INSERT INTO LogAcceso (nombreUsuario, exitoso, ipAcceso, motivo)
            SELECT
                i.nombreUsuario,
                1,
                NULL,
                'Cambio detectado - Rol: ' +
                CASE WHEN i.rol != d.rol THEN 'De ' + d.rol + ' a ' + i.rol ELSE 'Sin cambio' END +
                ', Estado: ' +
                CASE WHEN i.estado != d.estado THEN 'De ' + d.estado + ' a ' + i.estado ELSE 'Sin cambio' END
            FROM inserted i
                     INNER JOIN deleted d ON i.idUsuario = d.idUsuario
            WHERE i.rol != d.rol OR i.estado != d.estado;
        END
END;
go

CREATE TRIGGER TRG_LimpiarBloqueosExpirados
    ON Usuario
    AFTER UPDATE
    AS
BEGIN
    SET NOCOUNT ON;

    -- Limpiar bloqueos que ya expiraron al hacer UPDATE
    UPDATE Usuario
    SET bloqueadoHasta = NULL,
        intentosFallidos = 0
    WHERE bloqueadoHasta IS NOT NULL
      AND bloqueadoHasta <= GETDATE()
      AND idUsuario IN (SELECT idUsuario FROM inserted);
END;
go

create table Venta
(
    idVenta        int identity
        primary key,
    numeroVenta    varchar(20)              not null
        unique,
    idCliente      int                      not null
        references Cliente,
    idUsuario      int                      not null
        references Usuario,
    idMedioPago    int                      not null
        references MedioPago,
    fechaVenta     datetime       default getdate(),
    subtotal       decimal(12, 2) default 0 not null,
    descuentoTotal decimal(12, 2) default 0,
    igv            decimal(12, 2) default 0 not null,
    total          decimal(12, 2) default 0 not null,
    modalidadVenta varchar(20)
        check ([modalidadVenta] = 'Mixta' OR [modalidadVenta] = 'Mayorista' OR [modalidadVenta] = 'Docena' OR
               [modalidadVenta] = 'Unidad'),
    observaciones  varchar(500),
    estado         varchar(20)    default 'Completada'
        check ([estado] = 'Anulada' OR [estado] = 'Completada')
)
go

create table DetalleVenta
(
    idDetalle           int identity
        primary key,
    idVenta             int            not null
        references Venta,
    idProducto          int            not null
        references Producto,
    idAlmacen           int            not null
        references Almacen,
    cantidad            int            not null
        check ([cantidad] > 0),
    modalidadVenta      varchar(20)    not null
        check ([modalidadVenta] = 'Mayorista' OR [modalidadVenta] = 'Docena' OR [modalidadVenta] = 'Unidad'),
    precioUnitario      decimal(10, 2) not null,
    descuentoPromocion  decimal(10, 2) default 0,
    descuentoAdicional  decimal(10, 2) default 0,
    idPromocionAplicada int
        references Promocion,
    subtotal            decimal(12, 2) not null
)
go

-- Trigger: Actualizar inventario al vender
CREATE TRIGGER TRG_ActualizarInventarioVenta
ON DetalleVenta
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;
    
    UPDATE inv
    SET 
        inv.stockActual = inv.stockActual - i.cantidad,
        inv.ultimaActualizacion = GETDATE()
    FROM Inventario inv
    INNER JOIN inserted i ON inv.idProducto = i.idProducto AND inv.idAlmacen = i.idAlmacen;
    
    -- Registrar movimiento
    INSERT INTO MovimientoInventario (idProducto, idAlmacen, tipoMovimiento, cantidad, stockAnterior, stockNuevo, referencia)
    SELECT 
        i.idProducto,
        i.idAlmacen,
        'Salida',
        i.cantidad,
        inv.stockActual + i.cantidad,
        inv.stockActual,
        'VENTA-' + v.numeroVenta
    FROM inserted i
    INNER JOIN Inventario inv ON i.idProducto = inv.idProducto AND i.idAlmacen = i.idAlmacen
    INNER JOIN Venta v ON i.idVenta = v.idVenta;
END;
go

-- Trigger: Actualizar totales de venta automáticamente
CREATE TRIGGER TRG_ActualizarTotalesVenta
ON DetalleVenta
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;
    
    DECLARE @idVenta INT;
    
    SELECT @idVenta = COALESCE(i.idVenta, d.idVenta)
    FROM (SELECT idVenta FROM inserted UNION SELECT idVenta FROM deleted) AS cambios(idVenta)
    LEFT JOIN inserted i ON cambios.idVenta = i.idVenta
    LEFT JOIN deleted d ON cambios.idVenta = d.idVenta;
    
    UPDATE Venta
    SET 
        subtotal = ISNULL((SELECT SUM(subtotal) FROM DetalleVenta WHERE idVenta = @idVenta), 0),
        descuentoTotal = ISNULL((SELECT SUM(descuentoPromocion + descuentoAdicional) FROM DetalleVenta WHERE idVenta = @idVenta), 0),
        igv = ISNULL((SELECT SUM(subtotal) FROM DetalleVenta WHERE idVenta = @idVenta), 0) * 0.18,
        total = ISNULL((SELECT SUM(subtotal) FROM DetalleVenta WHERE idVenta = @idVenta), 0) * 1.18
    WHERE idVenta = @idVenta;
END;
go

create index IX_Venta_Fecha
    on Venta (fechaVenta)
go

create index IX_Venta_Cliente
    on Venta (idCliente)
go

CREATE VIEW VW_AccesosRecientes AS
SELECT TOP 100
    l.idLog,
    l.nombreUsuario,
    l.exitoso,
    l.ipAcceso,
    l.fechaIntento,
    l.motivo,
    u.rol,
    u.nombreCompleto
FROM LogAcceso l
         LEFT JOIN Usuario u ON l.nombreUsuario = u.nombreUsuario
ORDER BY l.fechaIntento DESC
go

-- ============================================================
-- VISTA: Actividad de login del día
-- ============================================================
CREATE   VIEW VW_ActividadLoginHoy AS
SELECT
    COUNT(*) AS totalIntentos,
    SUM(CASE WHEN exitoso = 1 THEN 1 ELSE 0 END) AS exitosos,
    SUM(CASE WHEN exitoso = 0 THEN 1 ELSE 0 END) AS fallidos,
    COUNT(DISTINCT nombreUsuario) AS usuariosUnicos,
    COUNT(DISTINCT ipAcceso) AS ipsUnicas
FROM LogAcceso
WHERE CAST(fechaIntento AS DATE) = CAST(GETDATE() AS DATE)
go

-- ============================================================
-- VISTA: Dashboard de usuarios para administrador
-- ============================================================
CREATE   VIEW VW_DashboardUsuarios AS
SELECT
    u.idUsuario,
    u.nombreUsuario,
    u.nombreCompleto,
    u.rol,
    u.email,
    u.ultimoAcceso,
    u.estado,
    CASE
        WHEN u.bloqueadoHasta IS NOT NULL AND u.bloqueadoHasta > GETDATE()
            THEN 'Bloqueado'
        WHEN u.estado = 'Inactivo'
            THEN 'Inactivo'
        ELSE 'Activo'
        END AS estadoActual,
    u.intentosFallidos,
    DATEDIFF(DAY, u.fechaCreacion, GETDATE()) AS diasRegistrado,
    (SELECT COUNT(*) FROM LogAcceso WHERE nombreUsuario = u.nombreUsuario AND exitoso = 1) AS totalAccesosExitosos,
    (SELECT COUNT(*) FROM LogAcceso WHERE nombreUsuario = u.nombreUsuario AND exitoso = 0) AS totalAccesosFallidos,
    (SELECT TOP 1 fechaIntento FROM LogAcceso WHERE nombreUsuario = u.nombreUsuario AND exitoso = 1 ORDER BY fechaIntento DESC) AS ultimoAccesoExitoso
FROM Usuario u
go

-- Vista: Promociones activas hoy
CREATE VIEW VW_PromocionesActivas AS
SELECT 
    pr.idPromocion,
    pr.nombrePromocion,
    pr.descripcion,
    pr.tipoPromocion,
    pr.porcentajeDescuento,
    pr.cantidadCompra,
    pr.cantidadPaga,
    pr.aplicaMedioPago,
    pr.montoMinimo,
    p.nombreProducto AS productoAplica,
    c.nombreCategoria AS categoriaAplica,
    pr.fechaInicio,
    pr.fechaFin
FROM Promocion pr
LEFT JOIN Producto p ON pr.aplicaProducto = p.idProducto
LEFT JOIN Categoria c ON pr.aplicaCategoria = c.idCategoria
WHERE pr.activa = 1 
    AND CAST(GETDATE() AS DATE) BETWEEN pr.fechaInicio AND pr.fechaFin
    AND pr.estado = 'Activo'
go

-- Vista: Stock consolidado de ambos almacenes
CREATE VIEW VW_StockConsolidado AS
SELECT 
    p.codigoProducto,
    p.nombreProducto,
    c.nombreCategoria,
    m.nombreMarca,
    ISNULL(SUM(i.stockActual), 0) AS stockTotal,
    ISNULL(SUM(CASE WHEN i.idAlmacen = 251 THEN i.stockActual ELSE 0 END), 0) AS stock251,
    ISNULL(SUM(CASE WHEN i.idAlmacen = 252 THEN i.stockActual ELSE 0 END), 0) AS stock252,
    p.stockMinimo,
    CASE 
        WHEN ISNULL(SUM(i.stockActual), 0) = 0 THEN 'SIN STOCK'
        WHEN ISNULL(SUM(i.stockActual), 0) <= p.stockMinimo THEN 'CRÍTICO'
        WHEN ISNULL(SUM(i.stockActual), 0) <= (p.stockMinimo * 2) THEN 'BAJO'
        ELSE 'NORMAL'
    END AS estadoStock,
    p.precioUnidad,
    p.precioDocena,
    p.precioMayorista
FROM Producto p
LEFT JOIN Inventario i ON p.idProducto = i.idProducto
LEFT JOIN Categoria c ON p.idCategoria = c.idCategoria
LEFT JOIN Marca m ON p.idMarca = m.idMarca
WHERE p.estado = 'Activo'
GROUP BY 
    p.idProducto, p.codigoProducto, p.nombreProducto, 
    c.nombreCategoria, m.nombreMarca, p.stockMinimo,
    p.precioUnidad, p.precioDocena, p.precioMayorista
go

-- Vista: Top productos más vendidos
CREATE VIEW VW_TopProductosVendidos AS
SELECT TOP 20
    p.codigoProducto,
    p.nombreProducto,
    c.nombreCategoria,
    SUM(dv.cantidad) AS totalVendido,
    SUM(dv.subtotal) AS ingresoTotal,
    COUNT(DISTINCT dv.idVenta) AS numeroVentas,
    AVG(dv.precioUnitario) AS precioPromedio
FROM DetalleVenta dv
INNER JOIN Producto p ON dv.idProducto = p.idProducto
INNER JOIN Categoria c ON p.idCategoria = c.idCategoria
INNER JOIN Venta v ON dv.idVenta = v.idVenta
WHERE v.estado = 'Completada'
    AND v.fechaVenta >= DATEADD(MONTH, -1, GETDATE())
GROUP BY p.codigoProducto, p.nombreProducto, c.nombreCategoria
ORDER BY totalVendido DESC
go

CREATE VIEW VW_UsuariosBloqueados AS
SELECT
    u.idUsuario,
    u.nombreUsuario,
    u.nombreCompleto,
    u.rol,
    u.bloqueadoHasta,
    DATEDIFF(MINUTE, GETDATE(), u.bloqueadoHasta) AS minutosRestantes,
    u.intentosFallidos
FROM Usuario u
WHERE u.bloqueadoHasta IS NOT NULL
  AND u.bloqueadoHasta > GETDATE()
  AND u.estado = 'Activo'
go

-- ============================================================
-- VISTA: Usuarios bloqueados actualmente
-- ============================================================
CREATE   VIEW VW_UsuariosBloqueadosActuales AS
SELECT
    u.idUsuario,
    u.nombreUsuario,
    u.nombreCompleto,
    u.rol,
    u.bloqueadoHasta,
    DATEDIFF(MINUTE, GETDATE(), u.bloqueadoHasta) AS minutosRestantes,
    u.intentosFallidos,
    (SELECT TOP 1 motivo FROM LogAcceso WHERE nombreUsuario = u.nombreUsuario AND exitoso = 0 ORDER BY fechaIntento DESC) AS ultimoMotivo
FROM Usuario u
WHERE u.bloqueadoHasta IS NOT NULL
  AND u.bloqueadoHasta > GETDATE()
  AND u.estado = 'Activo'
go

-- Vista: Ventas del día
CREATE VIEW VW_VentasHoy AS
SELECT 
    v.numeroVenta,
    c.razonSocial AS cliente,
    c.tipoCliente,
    v.fechaVenta,
    v.modalidadVenta,
    mp.nombreMedio AS medioPago,
    v.subtotal,
    v.descuentoTotal,
    v.igv,
    v.total,
    u.nombreCompleto AS vendedor
FROM Venta v
INNER JOIN Cliente c ON v.idCliente = c.idCliente
INNER JOIN Usuario u ON v.idUsuario = u.idUsuario
INNER JOIN MedioPago mp ON v.idMedioPago = mp.idMedioPago
WHERE CAST(v.fechaVenta AS DATE) = CAST(GETDATE() AS DATE)
    AND v.estado = 'Completada'
go

-- Función: Calcular precio según modalidad
CREATE FUNCTION FN_CalcularPrecio
(
    @idProducto INT,
    @modalidad VARCHAR(20)
)
RETURNS DECIMAL(10,2)
AS
BEGIN
    DECLARE @precio DECIMAL(10,2);
    
    SELECT @precio = CASE @modalidad
        WHEN 'Unidad' THEN precioUnidad
        WHEN 'Docena' THEN precioDocena / 12.0
        WHEN 'Mayorista' THEN precioMayorista
        ELSE precioUnidad
    END
    FROM Producto
    WHERE idProducto = @idProducto;
    
    RETURN ISNULL(@precio, 0);
END
go

-- Función: Verificar si producto tiene stock suficiente
CREATE FUNCTION FN_TieneStockSuficiente
(
    @idProducto INT,
    @idAlmacen INT,
    @cantidadRequerida INT
)
RETURNS BIT
AS
BEGIN
    DECLARE @resultado BIT;
    DECLARE @stockActual INT;
    
    SELECT @stockActual = stockActual
    FROM Inventario
    WHERE idProducto = @idProducto AND idAlmacen = @idAlmacen;
    
    SET @resultado = CASE 
        WHEN @stockActual >= @cantidadRequerida THEN 1
        ELSE 0
    END;
    
    RETURN @resultado;
END
go

-- ============================================================
-- FUNCIÓN: Validar fortaleza de contraseña
-- ============================================================
CREATE   FUNCTION FN_ValidarFortalezaContrasena
(
    @contrasena VARCHAR(255)
)
    RETURNS VARCHAR(20)
AS
BEGIN
    DECLARE @fortaleza VARCHAR(20) = 'Débil';
    DECLARE @longitud INT = LEN(@contrasena);
    DECLARE @tieneMayuscula BIT = 0;
    DECLARE @tieneMinuscula BIT = 0;
    DECLARE @tieneNumero BIT = 0;
    DECLARE @tieneEspecial BIT = 0;

    -- Verificar mayúsculas
    IF @contrasena COLLATE Latin1_General_CS_AS LIKE '%[A-Z]%'
        SET @tieneMayuscula = 1;

    -- Verificar minúsculas
    IF @contrasena COLLATE Latin1_General_CS_AS LIKE '%[a-z]%'
        SET @tieneMinuscula = 1;

    -- Verificar números
    IF @contrasena LIKE '%[0-9]%'
        SET @tieneNumero = 1;

    -- Verificar caracteres especiales
    IF @contrasena LIKE '%[!@#$%^&*()]%'
        SET @tieneEspecial = 1;

    -- Calcular fortaleza
    DECLARE @puntos INT = 0;

    IF @longitud >= 8 SET @puntos = @puntos + 1;
    IF @longitud >= 12 SET @puntos = @puntos + 1;
    IF @tieneMayuscula = 1 SET @puntos = @puntos + 1;
    IF @tieneMinuscula = 1 SET @puntos = @puntos + 1;
    IF @tieneNumero = 1 SET @puntos = @puntos + 1;
    IF @tieneEspecial = 1 SET @puntos = @puntos + 1;

    IF @puntos <= 2
        SET @fortaleza = 'Débil';
    ELSE IF @puntos <= 4
        SET @fortaleza = 'Media';
    ELSE
        SET @fortaleza = 'Fuerte';

    RETURN @fortaleza;
END
go

CREATE   PROCEDURE SP_ActualizarUsuario
    @idUsuario INT,
    @nombreCompleto VARCHAR(150),
    @telefono VARCHAR(20),
    @email VARCHAR(80),
    @actualizado BIT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    SET @actualizado = 0;

    BEGIN TRY
        -- Verificar que el email no esté usado por otro usuario
        IF EXISTS (SELECT 1 FROM Usuario WHERE email = @email AND idUsuario != @idUsuario)
            BEGIN
                RAISERROR('El email ya está registrado por otro usuario', 16, 1);
                RETURN;
            END

        UPDATE Usuario
        SET nombreCompleto = @nombreCompleto,
            telefono = @telefono,
            email = @email
        WHERE idUsuario = @idUsuario;

        SET @actualizado = 1;
    END TRY
    BEGIN CATCH
        SET @actualizado = 0;
        THROW;
    END CATCH
END;
go

-- SP: Aplicar promoción a un producto
CREATE PROCEDURE SP_AplicarPromocion
    @idProducto INT,
    @cantidad INT,
    @idMedioPago INT,
    @montoVenta DECIMAL(12,2),
    @descuento DECIMAL(10,2) OUTPUT,
    @idPromocion INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    
    SET @descuento = 0;
    SET @idPromocion = NULL;
    
    -- Buscar promoción aplicable
    SELECT TOP 1 
        @idPromocion = idPromocion,
        @descuento = CASE 
            WHEN tipoPromocion = 'Descuento' THEN (@montoVenta * porcentajeDescuento / 100)
            ELSE 0
        END
    FROM Promocion
    WHERE activa = 1
        AND CAST(GETDATE() AS DATE) BETWEEN fechaInicio AND fechaFin
        AND estado = 'Activo'
        AND (aplicaProducto IS NULL OR aplicaProducto = @idProducto)
        AND (aplicaMedioPago IS NULL OR aplicaMedioPago = (SELECT nombreMedio FROM MedioPago WHERE idMedioPago = @idMedioPago))
        AND @montoVenta >= montoMinimo
    ORDER BY porcentajeDescuento DESC;
    
    SELECT @descuento AS descuento, @idPromocion AS idPromocion;
END;
go

CREATE PROCEDURE SP_CambiarContrasena
    @idUsuario INT,
    @contrasenaActual VARCHAR(255),
    @contrasenaNueva VARCHAR(255),
    @cambioExitoso BIT OUTPUT,
    @contrasenaActualIncorrecta BIT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    SET @cambioExitoso = 0;
    SET @contrasenaActualIncorrecta = 0;

    BEGIN TRY
        DECLARE @contrasenaDB VARCHAR(255);

        SELECT @contrasenaDB = contrasena
        FROM Usuario
        WHERE idUsuario = @idUsuario;

        -- Verificar contraseña actual
        IF @contrasenaDB = @contrasenaActual
            BEGIN
                UPDATE Usuario
                SET contrasena = @contrasenaNueva
                WHERE idUsuario = @idUsuario;

                SET @cambioExitoso = 1;
            END
        ELSE
            BEGIN
                SET @contrasenaActualIncorrecta = 1;
            END

    END TRY
    BEGIN CATCH
        SET @cambioExitoso = 0;
    END CATCH
END;
go

-- ============================================================
-- SP: Activar/Desactivar usuario
-- ============================================================
CREATE   PROCEDURE SP_CambiarEstadoUsuario
    @idUsuarioAdmin INT,
    @idUsuarioModificar INT,
    @nuevoEstado VARCHAR(15),
    @cambioExitoso BIT OUTPUT,
    @sinPermisos BIT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    SET @cambioExitoso = 0;
    SET @sinPermisos = 0;

    BEGIN TRY
        DECLARE @rolAdmin VARCHAR(20);

        -- Verificar permisos
        SELECT @rolAdmin = rol
        FROM Usuario
        WHERE idUsuario = @idUsuarioAdmin;

        IF @rolAdmin != 'Administrador'
            BEGIN
                SET @sinPermisos = 1;
                RETURN;
            END

        -- Validar estado
        IF @nuevoEstado NOT IN ('Activo', 'Inactivo')
            BEGIN
                RAISERROR('Estado no válido', 16, 1);
                RETURN;
            END

        -- No permitir que el admin se desactive a sí mismo
        IF @idUsuarioAdmin = @idUsuarioModificar AND @nuevoEstado = 'Inactivo'
            BEGIN
                RAISERROR('No puede desactivarse a sí mismo', 16, 1);
                RETURN;
            END

        UPDATE Usuario
        SET estado = @nuevoEstado
        WHERE idUsuario = @idUsuarioModificar;

        SET @cambioExitoso = 1;

        INSERT INTO LogAcceso (nombreUsuario, exitoso, ipAcceso, motivo)
        VALUES (
                   (SELECT nombreUsuario FROM Usuario WHERE idUsuario = @idUsuarioModificar),
                   1,
                   NULL,
                   'Estado cambiado a ' + @nuevoEstado
               );

    END TRY
    BEGIN CATCH
        SET @cambioExitoso = 0;
    END CATCH
END;
go

-- ============================================================
-- SP: Cambiar rol de usuario (solo administrador)
-- ============================================================
CREATE   PROCEDURE SP_CambiarRolUsuario
    @idUsuarioAdmin INT,
    @idUsuarioModificar INT,
    @nuevoRol VARCHAR(20),
    @cambioExitoso BIT OUTPUT,
    @sinPermisos BIT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    SET @cambioExitoso = 0;
    SET @sinPermisos = 0;

    BEGIN TRY
        DECLARE @rolAdmin VARCHAR(20);

        -- Verificar que quien modifica sea administrador
        SELECT @rolAdmin = rol
        FROM Usuario
        WHERE idUsuario = @idUsuarioAdmin;

        IF @rolAdmin != 'Administrador'
            BEGIN
                SET @sinPermisos = 1;
                RETURN;
            END

        -- Validar el nuevo rol
        IF @nuevoRol NOT IN ('Administrador', 'Vendedor', 'Almacenero')
            BEGIN
                RAISERROR('Rol no válido', 16, 1);
                RETURN;
            END

        -- Actualizar rol
        UPDATE Usuario
        SET rol = @nuevoRol
        WHERE idUsuario = @idUsuarioModificar;

        SET @cambioExitoso = 1;

        -- Registrar el cambio
        INSERT INTO LogAcceso (nombreUsuario, exitoso, ipAcceso, motivo)
        VALUES (
                   (SELECT nombreUsuario FROM Usuario WHERE idUsuario = @idUsuarioModificar),
                   1,
                   NULL,
                   'Rol cambiado a ' + @nuevoRol + ' por administrador'
               );

    END TRY
    BEGIN CATCH
        SET @cambioExitoso = 0;
    END CATCH
END;
go

CREATE PROCEDURE SP_CerrarSesion
    @idUsuario INT,
    @cerradoCorrectamente BIT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    SET @cerradoCorrectamente = 0;

    BEGIN TRY
        UPDATE Usuario
        SET ultimoAcceso = GETDATE()
        WHERE idUsuario = @idUsuario;

        SET @cerradoCorrectamente = 1;
    END TRY
    BEGIN CATCH
        SET @cerradoCorrectamente = 0;
    END CATCH
END;
go

-- SP: Consultar precios de un producto
CREATE PROCEDURE SP_ConsultarPreciosProducto
    @codigoProducto VARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT 
        p.codigoProducto,
        p.nombreProducto,
        c.nombreCategoria,
        m.nombreMarca,
        p.precioUnidad AS precio_Unitario,
        p.precioDocena AS precio_Docena_Completa,
        CAST(p.precioDocena / 12.0 AS DECIMAL(10,2)) AS precio_Unitario_En_Docena,
        p.precioMayorista AS precio_Mayorista,
        p.cantidadMinimaMayorista AS cantidad_Minima_Mayorista,
        -- Calcular ahorros
        CAST(((p.precioUnidad - (p.precioDocena/12.0)) / p.precioUnidad) * 100 AS DECIMAL(5,2)) AS ahorro_Docena_Porcentaje,
        CAST(((p.precioUnidad - p.precioMayorista) / p.precioUnidad) * 100 AS DECIMAL(5,2)) AS ahorro_Mayorista_Porcentaje,
        -- Stock disponible
        ISNULL(SUM(i.stockActual), 0) AS stockTotal
    FROM Producto p
    LEFT JOIN Categoria c ON p.idCategoria = c.idCategoria
    LEFT JOIN Marca m ON p.idMarca = m.idMarca
    LEFT JOIN Inventario i ON p.idProducto = i.idProducto
    WHERE p.codigoProducto = @codigoProducto AND p.estado = 'Activo'
    GROUP BY 
        p.codigoProducto, p.nombreProducto, c.nombreCategoria, m.nombreMarca,
        p.precioUnidad, p.precioDocena, p.precioMayorista, p.cantidadMinimaMayorista;
END;
go

CREATE PROCEDURE SP_DesbloquearUsuario
    @idUsuarioAdmin INT,
    @idUsuarioBloqueado INT,
    @desbloqueado BIT OUTPUT,
    @sinPermisos BIT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    SET @desbloqueado = 0;
    SET @sinPermisos = 0;

    BEGIN TRY
        DECLARE @rolAdmin VARCHAR(20);

        -- Verificar que quien desbloquea sea administrador
        SELECT @rolAdmin = rol
        FROM Usuario
        WHERE idUsuario = @idUsuarioAdmin;

        IF @rolAdmin != 'Administrador'
            BEGIN
                SET @sinPermisos = 1;
                RETURN;
            END

        -- Desbloquear usuario
        UPDATE Usuario
        SET bloqueadoHasta = NULL,
            intentosFallidos = 0
        WHERE idUsuario = @idUsuarioBloqueado;

        SET @desbloqueado = 1;

        INSERT INTO LogAcceso (nombreUsuario, exitoso, ipAcceso, motivo)
        VALUES (
                   (SELECT nombreUsuario FROM Usuario WHERE idUsuario = @idUsuarioBloqueado),
                   1,
                   NULL,
                   'Desbloqueado por administrador'
               );

    END TRY
    BEGIN CATCH
        SET @desbloqueado = 0;
    END CATCH
END;
go

-- ============================================================
-- SP: Eliminar usuario (eliminación lógica)
-- ============================================================
CREATE   PROCEDURE SP_EliminarUsuario
    @idUsuarioAdmin INT,
    @idUsuarioEliminar INT,
    @eliminado BIT OUTPUT,
    @sinPermisos BIT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    SET @eliminado = 0;
    SET @sinPermisos = 0;

    BEGIN TRY
        DECLARE @rolAdmin VARCHAR(20);

        SELECT @rolAdmin = rol
        FROM Usuario
        WHERE idUsuario = @idUsuarioAdmin;

        IF @rolAdmin != 'Administrador'
            BEGIN
                SET @sinPermisos = 1;
                RETURN;
            END

        -- No permitir eliminar al propio administrador
        IF @idUsuarioAdmin = @idUsuarioEliminar
            BEGIN
                RAISERROR('No puede eliminarse a sí mismo', 16, 1);
                RETURN;
            END

        -- Eliminación lógica (cambiar estado a Inactivo)
        UPDATE Usuario
        SET estado = 'Inactivo',
            bloqueadoHasta = GETDATE() -- Marcar como bloqueado permanentemente
        WHERE idUsuario = @idUsuarioEliminar;

        SET @eliminado = 1;

        INSERT INTO LogAcceso (nombreUsuario, exitoso, ipAcceso, motivo)
        VALUES (
                   (SELECT nombreUsuario FROM Usuario WHERE idUsuario = @idUsuarioEliminar),
                   0,
                   NULL,
                   'Usuario eliminado (inactivado) por administrador'
               );

    END TRY
    BEGIN CATCH
        SET @eliminado = 0;
    END CATCH
END;
go

-- ============================================================
-- SP: Listar todos los usuarios con información resumida
-- ============================================================
CREATE   PROCEDURE SP_ListarUsuarios
    @rol VARCHAR(20) = NULL,
    @estado VARCHAR(15) = NULL
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        u.idUsuario,
        u.nombreUsuario,
        u.nombreCompleto,
        u.rol,
        u.telefono,
        u.email,
        u.ultimoAcceso,
        u.intentosFallidos,
        u.bloqueadoHasta,
        u.fechaCreacion,
        u.estado,
        CASE
            WHEN u.bloqueadoHasta IS NOT NULL AND u.bloqueadoHasta > GETDATE()
                THEN 'Bloqueado'
            WHEN u.estado = 'Inactivo'
                THEN 'Inactivo'
            ELSE 'Activo'
            END AS estadoActual,
        (SELECT COUNT(*) FROM LogAcceso WHERE nombreUsuario = u.nombreUsuario AND exitoso = 1) AS totalAccesos
    FROM Usuario u
    WHERE (@rol IS NULL OR u.rol = @rol)
      AND (@estado IS NULL OR u.estado = @estado)
    ORDER BY u.fechaCreacion DESC;
END;
go

-- ============================================================
-- SP: Obtener estadísticas de acceso de un usuario
-- ============================================================
CREATE   PROCEDURE SP_ObtenerEstadisticasAcceso
@idUsuario INT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @nombreUsuario VARCHAR(50);

    SELECT @nombreUsuario = nombreUsuario
    FROM Usuario
    WHERE idUsuario = @idUsuario;

    -- Resumen de accesos
    SELECT
        COUNT(*) AS totalIntentos,
        SUM(CASE WHEN exitoso = 1 THEN 1 ELSE 0 END) AS accesosExitosos,
        SUM(CASE WHEN exitoso = 0 THEN 1 ELSE 0 END) AS accesosFallidos,
        MAX(fechaIntento) AS ultimoIntento,
        MIN(fechaIntento) AS primerIntento
    FROM LogAcceso
    WHERE nombreUsuario = @nombreUsuario;

    -- Últimos 10 accesos
    SELECT TOP 10
        fechaIntento,
        exitoso,
        ipAcceso,
        motivo
    FROM LogAcceso
    WHERE nombreUsuario = @nombreUsuario
    ORDER BY fechaIntento DESC;
END;
go

CREATE PROCEDURE SP_ObtenerInfoUsuario
@idUsuario INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        idUsuario,
        nombreUsuario,
        contrasena,          -- ⭐ AGREGAR ESTA LÍNEA
        nombreCompleto,
        rol,
        telefono,
        email,
        ultimoAcceso,
        bloqueadoHasta,      -- ⭐ También esta para que coincida con parsear()
        intentosFallidos,
        fechaCreacion,
        estado,
        CASE
            WHEN bloqueadoHasta IS NOT NULL AND bloqueadoHasta > GETDATE() THEN 1
            ELSE 0
            END AS estaBloqueado,
        CASE
            WHEN bloqueadoHasta IS NOT NULL AND bloqueadoHasta > GETDATE()
                THEN DATEDIFF(MINUTE, GETDATE(), bloqueadoHasta)
            ELSE 0
            END AS minutosRestantesBloq
    FROM Usuario
    WHERE idUsuario = @idUsuario;
END;
go

-- SP: Productos con stock bajo
CREATE PROCEDURE SP_ProductosStockBajo
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT 
        p.codigoProducto,
        p.nombreProducto,
        c.nombreCategoria,
        m.nombreMarca,
        ISNULL(SUM(i.stockActual), 0) AS stockTotal,
        p.stockMinimo,
        ISNULL(SUM(CASE WHEN i.idAlmacen = 251 THEN i.stockActual ELSE 0 END), 0) AS stock251,
        ISNULL(SUM(CASE WHEN i.idAlmacen = 252 THEN i.stockActual ELSE 0 END), 0) AS stock252,
        CASE 
            WHEN ISNULL(SUM(i.stockActual), 0) = 0 THEN 'SIN STOCK'
            WHEN ISNULL(SUM(i.stockActual), 0) <= p.stockMinimo THEN 'CRÍTICO'
            ELSE 'BAJO'
        END AS alerta
    FROM Producto p
    LEFT JOIN Inventario i ON p.idProducto = i.idProducto
    LEFT JOIN Categoria c ON p.idCategoria = c.idCategoria
    LEFT JOIN Marca m ON p.idMarca = m.idMarca
    WHERE p.estado = 'Activo'
    GROUP BY 
        p.idProducto, p.codigoProducto, p.nombreProducto,
        c.nombreCategoria, m.nombreMarca, p.stockMinimo
    HAVING ISNULL(SUM(i.stockActual), 0) <= (p.stockMinimo * 2)
    ORDER BY stockTotal ASC, p.nombreProducto;
END;
go

-- SP: Registrar nueva venta
CREATE PROCEDURE SP_RegistrarVenta
    @idCliente INT,
    @idUsuario INT,
    @idMedioPago INT,
    @modalidadVenta VARCHAR(20),
    @observaciones VARCHAR(500) = NULL,
    @numeroVenta VARCHAR(20) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRANSACTION;
        
        -- Generar número de venta
        DECLARE @ultimoNumero INT;
        SELECT @ultimoNumero = ISNULL(MAX(CAST(SUBSTRING(numeroVenta, 2, LEN(numeroVenta)) AS INT)), 0)
        FROM Venta;
        
        SET @numeroVenta = 'V' + RIGHT('000000' + CAST(@ultimoNumero + 1 AS VARCHAR), 6);
        
        -- Insertar venta
        INSERT INTO Venta (numeroVenta, idCliente, idUsuario, idMedioPago, modalidadVenta, observaciones)
        VALUES (@numeroVenta, @idCliente, @idUsuario, @idMedioPago, @modalidadVenta, @observaciones);
        
        COMMIT TRANSACTION;
        
        SELECT @numeroVenta AS numeroVenta, SCOPE_IDENTITY() AS idVenta;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END;
go

-- SP: Reporte de ventas por período
CREATE PROCEDURE SP_ReporteVentas
    @fechaInicio DATE,
    @fechaFin DATE
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT 
        CAST(v.fechaVenta AS DATE) AS fecha,
        COUNT(v.idVenta) AS numeroVentas,
        SUM(v.subtotal) AS subtotal,
        SUM(v.descuentoTotal) AS descuentos,
        SUM(v.igv) AS igv,
        SUM(v.total) AS total,
        -- Por medio de pago
        SUM(CASE WHEN mp.nombreMedio = 'Efectivo' THEN v.total ELSE 0 END) AS totalEfectivo,
        SUM(CASE WHEN mp.nombreMedio = 'Yape' THEN v.total ELSE 0 END) AS totalYape,
        SUM(CASE WHEN mp.nombreMedio = 'Plin' THEN v.total ELSE 0 END) AS totalPlin,
        SUM(CASE WHEN mp.nombreMedio = 'Transferencia Bancaria' THEN v.total ELSE 0 END) AS totalTransferencia,
        -- Por tipo de cliente
        SUM(CASE WHEN c.tipoCliente = 'Mayorista' THEN v.total ELSE 0 END) AS totalMayorista,
        SUM(CASE WHEN c.tipoCliente = 'Minorista' THEN v.total ELSE 0 END) AS totalMinorista,
        SUM(CASE WHEN c.tipoCliente = 'Eventual' THEN v.total ELSE 0 END) AS totalEventual
    FROM Venta v
    INNER JOIN Cliente c ON v.idCliente = c.idCliente
    INNER JOIN MedioPago mp ON v.idMedioPago = mp.idMedioPago
    WHERE CAST(v.fechaVenta AS DATE) BETWEEN @fechaInicio AND @fechaFin
        AND v.estado = 'Completada'
    GROUP BY CAST(v.fechaVenta AS DATE)
    ORDER BY fecha DESC;
END;
go

-- ============================================================
-- SP: Resetear intentos fallidos manualmente
-- ============================================================
CREATE   PROCEDURE SP_ResetearIntentosFallidos
    @idUsuarioAdmin INT,
    @idUsuarioResetear INT,
    @reseteado BIT OUTPUT,
    @sinPermisos BIT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    SET @reseteado = 0;
    SET @sinPermisos = 0;

    BEGIN TRY
        DECLARE @rolAdmin VARCHAR(20);

        SELECT @rolAdmin = rol
        FROM Usuario
        WHERE idUsuario = @idUsuarioAdmin;

        IF @rolAdmin != 'Administrador'
            BEGIN
                SET @sinPermisos = 1;
                RETURN;
            END

        UPDATE Usuario
        SET intentosFallidos = 0,
            bloqueadoHasta = NULL
        WHERE idUsuario = @idUsuarioResetear;

        SET @reseteado = 1;

        INSERT INTO LogAcceso (nombreUsuario, exitoso, ipAcceso, motivo)
        VALUES (
                   (SELECT nombreUsuario FROM Usuario WHERE idUsuario = @idUsuarioResetear),
                   1,
                   NULL,
                   'Intentos fallidos reseteados por administrador'
               );

    END TRY
    BEGIN CATCH
        SET @reseteado = 0;
    END CATCH
END;
go

-- SP: Transferir stock entre almacenes
CREATE PROCEDURE SP_TransferirStock
    @idProducto INT,
    @idAlmacenOrigen INT,
    @idAlmacenDestino INT,
    @cantidad INT,
    @idUsuario INT,
    @motivo VARCHAR(200) = 'Transferencia entre almacenes'
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRANSACTION;
        
        -- Verificar stock origen
        DECLARE @stockOrigen INT;
        SELECT @stockOrigen = stockActual 
        FROM Inventario 
        WHERE idProducto = @idProducto AND idAlmacen = @idAlmacenOrigen;
        
        IF @stockOrigen < @cantidad
        BEGIN
            RAISERROR('Stock insuficiente en almacén origen', 16, 1);
            RETURN;
        END
        
        -- Descontar del origen
        UPDATE Inventario
        SET stockActual = stockActual - @cantidad,
            ultimaActualizacion = GETDATE()
        WHERE idProducto = @idProducto AND idAlmacen = @idAlmacenOrigen;
        
        -- Incrementar en destino
        UPDATE Inventario
        SET stockActual = stockActual + @cantidad,
            ultimaActualizacion = GETDATE()
        WHERE idProducto = @idProducto AND idAlmacen = @idAlmacenDestino;
        
        -- Registrar movimientos
        INSERT INTO MovimientoInventario (idProducto, idAlmacen, tipoMovimiento, cantidad, stockAnterior, stockNuevo, motivo, idUsuario)
        VALUES 
        (@idProducto, @idAlmacenOrigen, 'Transferencia', @cantidad, @stockOrigen, @stockOrigen - @cantidad, @motivo + ' (Salida)', @idUsuario),
        (@idProducto, @idAlmacenDestino, 'Transferencia', @cantidad, 
         (SELECT stockActual - @cantidad FROM Inventario WHERE idProducto = @idProducto AND idAlmacen = @idAlmacenDestino),
         (SELECT stockActual FROM Inventario WHERE idProducto = @idProducto AND idAlmacen = @idAlmacenDestino),
         @motivo + ' (Entrada)', @idUsuario);
        
        COMMIT TRANSACTION;
        
        SELECT 'Transferencia exitosa' AS mensaje;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END;
go

CREATE PROCEDURE SP_ValidarLogin
    @nombreUsuario VARCHAR(50),
    @contrasena VARCHAR(255),
    @ipAcceso VARCHAR(50) = NULL,
    -- SALIDAS para Java
    @loginExitoso BIT OUTPUT,
    @usuarioBloqueado BIT OUTPUT,
    @usuarioInactivo BIT OUTPUT,
    @credencialesInvalidas BIT OUTPUT,
    @idUsuario INT OUTPUT,
    @nombreCompleto VARCHAR(150) OUTPUT,
    @rol VARCHAR(20) OUTPUT,
    @minutosRestantesBloq INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    -- Inicializar salidas
    SET @loginExitoso = 0;
    SET @usuarioBloqueado = 0;
    SET @usuarioInactivo = 0;
    SET @credencialesInvalidas = 0;
    SET @idUsuario = NULL;
    SET @nombreCompleto = NULL;
    SET @rol = NULL;
    SET @minutosRestantesBloq = 0;

    BEGIN TRY
        DECLARE @estadoUsuario VARCHAR(15);
        DECLARE @bloqueadoHasta DATETIME;
        DECLARE @intentosFallidos INT;
        DECLARE @contrasenaDB VARCHAR(255);

        -- 1. Verificar si el usuario existe
        SELECT
            @idUsuario = idUsuario,
            @nombreCompleto = nombreCompleto,
            @rol = rol,
            @estadoUsuario = estado,
            @bloqueadoHasta = bloqueadoHasta,
            @intentosFallidos = intentosFallidos,
            @contrasenaDB = contrasena
        FROM Usuario
        WHERE nombreUsuario = @nombreUsuario;

        -- Si el usuario no existe
        IF @idUsuario IS NULL
            BEGIN
                SET @credencialesInvalidas = 1;

                INSERT INTO LogAcceso (nombreUsuario, exitoso, ipAcceso, motivo)
                VALUES (@nombreUsuario, 0, @ipAcceso, 'Usuario no existe');

                RETURN;
            END

        -- 2. Verificar si está inactivo
        IF @estadoUsuario = 'Inactivo'
            BEGIN
                SET @usuarioInactivo = 1;

                INSERT INTO LogAcceso (nombreUsuario, exitoso, ipAcceso, motivo)
                VALUES (@nombreUsuario, 0, @ipAcceso, 'Usuario inactivo');

                RETURN;
            END

        -- 3. Verificar si está bloqueado temporalmente
        IF @bloqueadoHasta IS NOT NULL AND @bloqueadoHasta > GETDATE()
            BEGIN
                SET @usuarioBloqueado = 1;
                SET @minutosRestantesBloq = DATEDIFF(MINUTE, GETDATE(), @bloqueadoHasta);

                INSERT INTO LogAcceso (nombreUsuario, exitoso, ipAcceso, motivo)
                VALUES (@nombreUsuario, 0, @ipAcceso, 'Usuario bloqueado temporalmente');

                RETURN;
            END

        -- 4. Si el bloqueo ya expiró, desbloquearlo
        IF @bloqueadoHasta IS NOT NULL AND @bloqueadoHasta <= GETDATE()
            BEGIN
                UPDATE Usuario
                SET bloqueadoHasta = NULL,
                    intentosFallidos = 0
                WHERE idUsuario = @idUsuario;

                SET @intentosFallidos = 0;
            END

        -- 5. Validar contraseña
        IF @contrasenaDB = @contrasena
            BEGIN
                -- Login exitoso
                SET @loginExitoso = 1;

                UPDATE Usuario
                SET ultimoAcceso = GETDATE(),
                    intentosFallidos = 0,
                    bloqueadoHasta = NULL
                WHERE idUsuario = @idUsuario;

                INSERT INTO LogAcceso (nombreUsuario, exitoso, ipAcceso, motivo)
                VALUES (@nombreUsuario, 1, @ipAcceso, 'Login exitoso');
            END
        ELSE
            BEGIN
                -- Contraseña incorrecta
                SET @credencialesInvalidas = 1;
                SET @intentosFallidos = @intentosFallidos + 1;

                -- Si alcanza 3 intentos, bloquear por 30 minutos
                IF @intentosFallidos >= 3
                    BEGIN
                        SET @usuarioBloqueado = 1;
                        SET @minutosRestantesBloq = 30;

                        UPDATE Usuario
                        SET intentosFallidos = @intentosFallidos,
                            bloqueadoHasta = DATEADD(MINUTE, 30, GETDATE())
                        WHERE idUsuario = @idUsuario;

                        INSERT INTO LogAcceso (nombreUsuario, exitoso, ipAcceso, motivo)
                        VALUES (@nombreUsuario, 0, @ipAcceso, 'Bloqueado por 3 intentos fallidos');
                    END
                ELSE
                    BEGIN
                        UPDATE Usuario
                        SET intentosFallidos = @intentosFallidos
                        WHERE idUsuario = @idUsuario;

                        INSERT INTO LogAcceso (nombreUsuario, exitoso, ipAcceso, motivo)
                        VALUES (@nombreUsuario, 0, @ipAcceso, 'Contraseña incorrecta');
                    END
            END

    END TRY
    BEGIN CATCH
        -- Si ocurre algún error inesperado
        SET @loginExitoso = 0;
        SET @usuarioBloqueado = 0;
        SET @usuarioInactivo = 0;
        SET @credencialesInvalidas = 0;

        INSERT INTO LogAcceso (nombreUsuario, exitoso, ipAcceso, motivo)
        VALUES (@nombreUsuario, 0, @ipAcceso, 'Error del sistema');
    END CATCH
END;
go

CREATE PROCEDURE SP_VerificarPermiso
    @idUsuario INT,
    @permisoRequerido VARCHAR(50),
    @tienePermiso BIT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    SET @tienePermiso = 0;

    BEGIN TRY
        DECLARE @rol VARCHAR(20);

        SELECT @rol = rol
        FROM Usuario
        WHERE idUsuario = @idUsuario AND estado = 'Activo';

        -- Administrador tiene todos los permisos
        IF @rol = 'Administrador'
            BEGIN
                SET @tienePermiso = 1;
                RETURN;
            END

        -- Permisos específicos por rol
        IF @permisoRequerido = 'VENTAS' AND @rol IN ('Vendedor', 'Administrador')
            SET @tienePermiso = 1;
        ELSE IF @permisoRequerido = 'INVENTARIO' AND @rol IN ('Almacenero', 'Administrador')
            SET @tienePermiso = 1;
        ELSE IF @permisoRequerido = 'COMPRAS' AND @rol IN ('Administrador', 'Almacenero')
            SET @tienePermiso = 1;
        ELSE IF @permisoRequerido = 'REPORTES' AND @rol IN ('Administrador', 'Vendedor')
            SET @tienePermiso = 1;
        ELSE IF @permisoRequerido = 'USUARIOS' AND @rol = 'Administrador'
            SET @tienePermiso = 1;
        ELSE IF @permisoRequerido = 'CLIENTES' AND @rol IN ('Vendedor', 'Administrador')
            SET @tienePermiso = 1;
        ELSE IF @permisoRequerido = 'PROVEEDORES' AND @rol IN ('Administrador', 'Almacenero')
            SET @tienePermiso = 1;
        ELSE IF @permisoRequerido = 'PRODUCTOS' AND @rol IN ('Administrador', 'Almacenero')
            SET @tienePermiso = 1;

    END TRY
    BEGIN CATCH
        SET @tienePermiso = 0;
    END CATCH
END;
go

CREATE PROCEDURE SP_VerificarSesion
    @idUsuario INT,
    @sesionValida BIT OUTPUT,
    @usuarioActivo BIT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    SET @sesionValida = 0;
    SET @usuarioActivo = 0;

    BEGIN TRY
        DECLARE @estado VARCHAR(15);

        SELECT @estado = estado
        FROM Usuario
        WHERE idUsuario = @idUsuario;

        IF @estado = 'Activo'
            BEGIN
                SET @sesionValida = 1;
                SET @usuarioActivo = 1;
            END
        ELSE IF @estado = 'Inactivo'
            BEGIN
                SET @sesionValida = 0;
                SET @usuarioActivo = 0;
            END

    END TRY
    BEGIN CATCH
        SET @sesionValida = 0;
        SET @usuarioActivo = 0;
    END CATCH
END;
go


