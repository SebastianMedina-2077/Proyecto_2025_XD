-- =====================================================
-- SISTEMA SIMPLIFICADO PARA JUGUETERÍA PEQUEÑA
-- Base de Datos: BD_JUGUETERIA_SIMPLE
-- Motor: SQL Server
-- Fecha: 2025
-- Enfoque: Venta mayorista, docenas, unidades con promociones
-- =====================================================

USE master;
GO

-- Eliminar base de datos si existe
IF EXISTS (SELECT name FROM sys.databases WHERE name = 'BD_JUGUETERIA_SIMPLE')
BEGIN
    ALTER DATABASE BD_JUGUETERIA_SIMPLE SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE BD_JUGUETERIA_SIMPLE;
END
GO

-- Crear base de datos
CREATE DATABASE BD_JUGUETERIA_SIMPLE;
GO

USE BD_JUGUETERIA_SIMPLE;
GO

-- =====================================================
-- MÓDULO 1: USUARIOS Y ACCESO
-- =====================================================

CREATE TABLE Usuario (
    idUsuario INT PRIMARY KEY IDENTITY(1,1),
    nombreUsuario VARCHAR(50) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    nombreCompleto VARCHAR(150) NOT NULL,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('Administrador', 'Vendedor', 'Almacenero')),
    telefono VARCHAR(20),
    email VARCHAR(80),
    ultimoAcceso DATETIME,
    intentosFallidos INT DEFAULT 0,
    bloqueadoHasta DATETIME NULL,
    fechaCreacion DATETIME DEFAULT GETDATE(),
    estado VARCHAR(15) DEFAULT 'Activo' CHECK (estado IN ('Activo', 'Inactivo'))
);

-- =====================================================
-- MÓDULO 2: CLIENTES
-- =====================================================

CREATE TABLE Cliente (
    idCliente INT PRIMARY KEY IDENTITY(1,1),
    tipoCliente VARCHAR(20) NOT NULL CHECK (tipoCliente IN ('Mayorista', 'Minorista', 'Eventual')),
    razonSocial VARCHAR(150) NOT NULL,
    documento VARCHAR(11) NOT NULL UNIQUE, -- RUC o DNI
    tipoDocumento VARCHAR(10) CHECK (tipoDocumento IN ('RUC', 'DNI')),
    telefono VARCHAR(20),
    email VARCHAR(80),
    direccion VARCHAR(250),
    descuentoPersonalizado DECIMAL(5,2) DEFAULT 0 CHECK (descuentoPersonalizado >= 0 AND descuentoPersonalizado <= 100),
    fechaRegistro DATE DEFAULT CAST(GETDATE() AS DATE),
    estado VARCHAR(15) DEFAULT 'Activo' CHECK (estado IN ('Activo', 'Inactivo'))
);

-- =====================================================
-- MÓDULO 3: PROVEEDORES
-- =====================================================

CREATE TABLE Proveedor (
    idProveedor INT PRIMARY KEY IDENTITY(1,1),
    razonSocial VARCHAR(150) NOT NULL,
    ruc VARCHAR(11) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    email VARCHAR(80),
    direccion VARCHAR(250),
    contacto VARCHAR(100),
    fechaRegistro DATE DEFAULT CAST(GETDATE() AS DATE),
    estado VARCHAR(15) DEFAULT 'Activo' CHECK (estado IN ('Activo', 'Inactivo'))
);

-- =====================================================
-- MÓDULO 4: PRODUCTOS Y CATÁLOGO
-- =====================================================

CREATE TABLE Categoria (
    idCategoria INT PRIMARY KEY IDENTITY(1,1),
    nombreCategoria VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    estado VARCHAR(15) DEFAULT 'Activo' CHECK (estado IN ('Activo', 'Inactivo'))
);

CREATE TABLE Marca (
    idMarca INT PRIMARY KEY IDENTITY(1,1),
    nombreMarca VARCHAR(100) NOT NULL UNIQUE,
    estado VARCHAR(15) DEFAULT 'Activo' CHECK (estado IN ('Activo', 'Inactivo'))
);

CREATE TABLE Producto (
    idProducto INT PRIMARY KEY IDENTITY(1,1),
    codigoProducto VARCHAR(50) NOT NULL UNIQUE,
    nombreProducto VARCHAR(150) NOT NULL,
    descripcion VARCHAR(500),
    idCategoria INT NOT NULL,
    idMarca INT NOT NULL,
    edadRecomendada VARCHAR(20), -- Ej: "3-5 años"
    
    -- PRECIOS SEGÚN MODALIDAD DE VENTA
    precioUnidad DECIMAL(10,2) NOT NULL CHECK (precioUnidad > 0),
    precioDocena DECIMAL(10,2) NOT NULL CHECK (precioDocena > 0),
    precioMayorista DECIMAL(10,2) NOT NULL CHECK (precioMayorista > 0),
    cantidadMinimaMayorista INT DEFAULT 50, -- Mínimo para precio mayorista
    
    stockMinimo INT DEFAULT 10,
    pesoKg DECIMAL(8,3),
    
    fechaCreacion DATETIME DEFAULT GETDATE(),
    estado VARCHAR(15) DEFAULT 'Activo' CHECK (estado IN ('Activo', 'Inactivo')),
    
    FOREIGN KEY (idCategoria) REFERENCES Categoria(idCategoria),
    FOREIGN KEY (idMarca) REFERENCES Marca(idMarca)
);

-- =====================================================
-- MÓDULO 5: ALMACENES (251 Y 252)
-- =====================================================

CREATE TABLE Almacen (
    idAlmacen INT PRIMARY KEY,
    nombreAlmacen VARCHAR(100) NOT NULL,
    direccion VARCHAR(200),
    responsable VARCHAR(100),
    telefono VARCHAR(20),
    estado VARCHAR(15) DEFAULT 'Activo' CHECK (estado IN ('Activo', 'Inactivo')),
    CHECK (idAlmacen IN (251, 252)) -- Solo almacenes 251 y 252
);

CREATE TABLE Inventario (
    idInventario INT PRIMARY KEY IDENTITY(1,1),
    idProducto INT NOT NULL,
    idAlmacen INT NOT NULL,
    stockActual INT DEFAULT 0 CHECK (stockActual >= 0),
    ultimaActualizacion DATETIME DEFAULT GETDATE(),
    
    FOREIGN KEY (idProducto) REFERENCES Producto(idProducto),
    FOREIGN KEY (idAlmacen) REFERENCES Almacen(idAlmacen),
    UNIQUE(idProducto, idAlmacen)
);

CREATE TABLE MovimientoInventario (
    idMovimiento INT PRIMARY KEY IDENTITY(1,1),
    idProducto INT NOT NULL,
    idAlmacen INT NOT NULL,
    tipoMovimiento VARCHAR(30) NOT NULL CHECK (tipoMovimiento IN ('Entrada', 'Salida', 'Ajuste', 'Transferencia')),
    cantidad INT NOT NULL,
    stockAnterior INT NOT NULL,
    stockNuevo INT NOT NULL,
    motivo VARCHAR(200),
    referencia VARCHAR(100), -- Número de venta, compra, etc.
    idUsuario INT,
    fechaMovimiento DATETIME DEFAULT GETDATE(),
    
    FOREIGN KEY (idProducto) REFERENCES Producto(idProducto),
    FOREIGN KEY (idAlmacen) REFERENCES Almacen(idAlmacen),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

-- =====================================================
-- MÓDULO 6: PROMOCIONES Y OFERTAS
-- =====================================================

CREATE TABLE Promocion (
    idPromocion INT PRIMARY KEY IDENTITY(1,1),
    nombrePromocion VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    tipoPromocion VARCHAR(30) NOT NULL CHECK (tipoPromocion IN ('Descuento', '2x1', '3x2', 'Precio Especial', 'Regalo')),
    
    -- DESCUENTO
    porcentajeDescuento DECIMAL(5,2) CHECK (porcentajeDescuento >= 0 AND porcentajeDescuento <= 100),
    
    -- PARA OFERTAS 2x1, 3x2
    cantidadCompra INT, -- Compra X unidades
    cantidadPaga INT,   -- Paga Y unidades
    
    -- FILTROS
    aplicaProducto INT, -- NULL = todos los productos
    aplicaCategoria INT, -- NULL = todas las categorías
    aplicaMedioPago VARCHAR(20), -- NULL = todos, o específico: 'Yape', 'Plin'
    
    montoMinimo DECIMAL(10,2) DEFAULT 0, -- Monto mínimo de compra
    
    fechaInicio DATE NOT NULL,
    fechaFin DATE NOT NULL,
    activa BIT DEFAULT 1,
    estado VARCHAR(15) DEFAULT 'Activo' CHECK (estado IN ('Activo', 'Inactivo')),
    
    FOREIGN KEY (aplicaProducto) REFERENCES Producto(idProducto),
    FOREIGN KEY (aplicaCategoria) REFERENCES Categoria(idCategoria),
    CHECK (fechaFin >= fechaInicio)
);

-- =====================================================
-- MÓDULO 7: VENTAS
-- =====================================================

CREATE TABLE MedioPago (
    idMedioPago INT PRIMARY KEY IDENTITY(1,1),
    nombreMedio VARCHAR(50) NOT NULL UNIQUE,
    tipo VARCHAR(20) CHECK (tipo IN ('Digital', 'Efectivo', 'Transferencia')),
    activo BIT DEFAULT 1
);

CREATE TABLE Venta (
    idVenta INT PRIMARY KEY IDENTITY(1,1),
    numeroVenta VARCHAR(20) NOT NULL UNIQUE,
    idCliente INT NOT NULL,
    idUsuario INT NOT NULL,
    idMedioPago INT NOT NULL,
    
    fechaVenta DATETIME DEFAULT GETDATE(),
    
    -- MONTOS
    subtotal DECIMAL(12,2) NOT NULL DEFAULT 0,
    descuentoTotal DECIMAL(12,2) DEFAULT 0,
    igv DECIMAL(12,2) NOT NULL DEFAULT 0,
    total DECIMAL(12,2) NOT NULL DEFAULT 0,
    
    -- DATOS ADICIONALES
    modalidadVenta VARCHAR(20) CHECK (modalidadVenta IN ('Unidad', 'Docena', 'Mayorista', 'Mixta')),
    observaciones VARCHAR(500),
    
    estado VARCHAR(20) DEFAULT 'Completada' CHECK (estado IN ('Completada', 'Anulada')),
    
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idMedioPago) REFERENCES MedioPago(idMedioPago)
);

CREATE TABLE DetalleVenta (
    idDetalle INT PRIMARY KEY IDENTITY(1,1),
    idVenta INT NOT NULL,
    idProducto INT NOT NULL,
    idAlmacen INT NOT NULL,
    
    cantidad INT NOT NULL CHECK (cantidad > 0),
    modalidadVenta VARCHAR(20) NOT NULL CHECK (modalidadVenta IN ('Unidad', 'Docena', 'Mayorista')),
    precioUnitario DECIMAL(10,2) NOT NULL,
    
    -- DESCUENTOS
    descuentoPromocion DECIMAL(10,2) DEFAULT 0,
    descuentoAdicional DECIMAL(10,2) DEFAULT 0,
    idPromocionAplicada INT,
    
    subtotal DECIMAL(12,2) NOT NULL,
    
    FOREIGN KEY (idVenta) REFERENCES Venta(idVenta),
    FOREIGN KEY (idProducto) REFERENCES Producto(idProducto),
    FOREIGN KEY (idAlmacen) REFERENCES Almacen(idAlmacen),
    FOREIGN KEY (idPromocionAplicada) REFERENCES Promocion(idPromocion)
);

-- =====================================================
-- MÓDULO 8: COMPRAS
-- =====================================================

CREATE TABLE Compra (
    idCompra INT PRIMARY KEY IDENTITY(1,1),
    numeroCompra VARCHAR(20) NOT NULL UNIQUE,
    idProveedor INT NOT NULL,
    idAlmacen INT NOT NULL,
    idUsuario INT NOT NULL,
    
    fechaCompra DATE DEFAULT CAST(GETDATE() AS DATE),
    numeroFactura VARCHAR(50),
    
    subtotal DECIMAL(12,2) NOT NULL,
    igv DECIMAL(12,2) NOT NULL,
    total DECIMAL(12,2) NOT NULL,
    
    observaciones VARCHAR(500),
    estado VARCHAR(20) DEFAULT 'Completada' CHECK (estado IN ('Completada', 'Anulada')),
    
    FOREIGN KEY (idProveedor) REFERENCES Proveedor(idProveedor),
    FOREIGN KEY (idAlmacen) REFERENCES Almacen(idAlmacen),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

CREATE TABLE DetalleCompra (
    idDetalle INT PRIMARY KEY IDENTITY(1,1),
    idCompra INT NOT NULL,
    idProducto INT NOT NULL,
    
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precioUnitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    
    FOREIGN KEY (idCompra) REFERENCES Compra(idCompra),
    FOREIGN KEY (idProducto) REFERENCES Producto(idProducto)
);

-- =====================================================
-- ÍNDICES PARA OPTIMIZACIÓN
-- =====================================================

CREATE INDEX IX_Cliente_Documento ON Cliente(documento);
CREATE INDEX IX_Producto_Codigo ON Producto(codigoProducto);
CREATE INDEX IX_Venta_Fecha ON Venta(fechaVenta);
CREATE INDEX IX_Venta_Cliente ON Venta(idCliente);
CREATE INDEX IX_Inventario_Producto ON Inventario(idProducto);
CREATE INDEX IX_MovimientoInventario_Fecha ON MovimientoInventario(fechaMovimiento);
CREATE INDEX IX_Promocion_Fechas ON Promocion(fechaInicio, fechaFin, activa);

GO

-- =====================================================
-- TRIGGERS AUTOMÁTICOS
-- =====================================================

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
GO

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
GO

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
GO

-- =====================================================
-- VISTAS ÚTILES
-- =====================================================

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
    p.precioUnidad, p.precioDocena, p.precioMayorista;
GO

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
    AND pr.estado = 'Activo';
GO

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
    AND v.estado = 'Completada';
GO

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
ORDER BY totalVendido DESC;
GO

-- =====================================================
-- PROCEDIMIENTOS ALMACENADOS
-- =====================================================

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
GO

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
GO

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
GO

-- =====================================================
-- DATOS INICIALES
-- =====================================================

-- Usuarios
INSERT INTO Usuario (nombreUsuario, contrasena, nombreCompleto, rol, email) VALUES
('admin', 'admin123', 'Administrador del Sistema', 'Administrador', 'admin@jugueteria.com'),
('vendedor1', 'vend123', 'Juan Pérez López', 'Vendedor', 'juan.perez@jugueteria.com'),
('almacen1', 'alm123', 'María González Torres', 'Almacenero', 'maria.gonzalez@jugueteria.com');

-- Almacenes
INSERT INTO Almacen (idAlmacen, nombreAlmacen, direccion, responsable, telefono) VALUES
(251, 'Almacén Principal 251', 'Av. Industrial 1234, San Miguel', 'María González', '987654321'),
(252, 'Almacén Secundario 252', 'Jr. Comercio 567, Cercado', 'Carlos Ramírez', '987654322');

-- Categorías
INSERT INTO Categoria (nombreCategoria, descripcion) VALUES
('Muñecas y Accesorios', 'Muñecas de diferentes estilos y sus complementos'),
('Peluches', 'Animales y personajes de peluche'),
('Construcción', 'Sets de bloques y piezas para construir'),
('Vehículos', 'Carros, motos, aviones de juguete'),
('Juegos de Mesa', 'Juegos familiares y educativos'),
('Educativos', 'Juguetes que desarrollan habilidades'),
('Bebés', 'Juguetes para primera infancia'),
('Deportes', 'Juguetes para actividades físicas'),
('Electrónicos', 'Juguetes con tecnología y pilas'),
('Arte y Manualidades', 'Sets creativos y de expresión artística');

-- Marcas
INSERT INTO Marca (nombreMarca) VALUES
('LEGO'), ('Mattel'), ('Hasbro'), ('Fisher-Price'), 
('Hot Wheels'), ('Barbie'), ('Nerf'), ('Playmobil'),
('Melissa & Doug'), ('VTech'), ('Disney'), ('Marvel'),
('Play-Doh'), ('Paw Patrol'), ('LOL Surprise');

-- Medios de Pago
INSERT INTO MedioPago (nombreMedio, tipo) VALUES
('Efectivo', 'Efectivo'),
('Yape', 'Digital'),
('Plin', 'Digital'),
('Transferencia Bancaria', 'Transferencia'),
('Tarjeta Débito', 'Digital'),
('Tarjeta Crédito', 'Digital');

-- Clientes de ejemplo
INSERT INTO Cliente (tipoCliente, razonSocial, documento, tipoDocumento, telefono, direccion, descuentoPersonalizado) VALUES
('Mayorista', 'Distribuidora Juguetes SAC', '20123456789', 'RUC', '987111222', 'Av. Argentina 1234, Callao', 5.00),
('Mayorista', 'Comercial Los Andes EIRL', '20987654321', 'RUC', '987333444', 'Jr. Cusco 567, Lima', 3.00),
('Minorista', 'Bazar El Niño Feliz', '10234567890', 'RUC', '987555666', 'Av. Colonial 890, Lima', 0.00),
('Eventual', 'García Rodríguez, Ana María', '45678912', 'DNI', '987777888', 'Los Olivos', 0.00),
('Minorista', 'Juguetería La Estrella', '10345678901', 'RUC', '987999000', 'San Juan de Lurigancho', 0.00);

-- Proveedores
INSERT INTO Proveedor (razonSocial, ruc, telefono, email, direccion, contacto) VALUES
('Importadora Toy World SAC', '20111222333', '016541234', 'ventas@toyworld.com', 'Av. Argentina 2000, Callao', 'Roberto Sánchez'),
('Distribuidora Play & Fun EIRL', '20444555666', '015678900', 'contacto@playfun.pe', 'Jr. Paruro 1500, Lima', 'Carmen López'),
('Grupo Juguetes Perú SA', '20777888999', '014567890', 'info@gjperu.com', 'Av. Colonial 3400, Lima', 'Luis Morales');

-- Productos de ejemplo
INSERT INTO Producto (codigoProducto, nombreProducto, descripcion, idCategoria, idMarca, edadRecomendada, 
                     precioUnidad, precioDocena, precioMayorista, cantidadMinimaMayorista, stockMinimo, pesoKg) VALUES
-- Muñecas
('BAR001', 'Barbie Fashionista Original', 'Muñeca Barbie con vestido de gala', 1, 6, '3+ años', 85.00, 950.00, 75.00, 50, 20, 0.35),
('BAR002', 'Barbie Dreamhouse', 'Casa de ensueño con accesorios', 1, 6, '3+ años', 450.00, 4900.00, 380.00, 20, 5, 5.50),
('MAT001', 'Muñeca Baby Alive', 'Muñeca interactiva que come', 1, 2, '3+ años', 120.00, 1300.00, 100.00, 30, 15, 0.80),

-- Peluches
('PEL001', 'Oso de Peluche Grande 60cm', 'Oso suave color beige', 2, 9, '0+ años', 65.00, 720.00, 55.00, 40, 25, 0.90),
('PEL002', 'Peluche Unicornio Arcoíris', 'Unicornio colorido 40cm', 2, 9, '0+ años', 45.00, 500.00, 38.00, 50, 30, 0.40),
('DIS001', 'Peluche Mickey Mouse', 'Mickey oficial Disney 35cm', 2, 11, '0+ años', 55.00, 600.00, 45.00, 40, 20, 0.35),

-- Construcción
('LEG001', 'LEGO Classic Caja Grande', 'Set de 1500 piezas variadas', 3, 1, '4+ años', 250.00, 2700.00, 210.00, 30, 15, 2.20),
('LEG002', 'LEGO City Estación de Policía', 'Set temático con vehículos', 3, 1, '6+ años', 180.00, 1950.00, 150.00, 25, 10, 1.50),
('PLA001', 'Playmobil Casa Moderna', 'Casa con figuras y accesorios', 3, 8, '4+ años', 320.00, 3500.00, 270.00, 20, 8, 3.80),

-- Vehículos
('HOT001', 'Hot Wheels Pack 5 Autos', 'Set de 5 vehículos miniatura', 4, 5, '3+ años', 35.00, 380.00, 28.00, 100, 50, 0.25),
('HOT002', 'Hot Wheels Pista Mega Looping', 'Pista con loop gigante', 4, 5, '5+ años', 95.00, 1050.00, 80.00, 30, 12, 2.10),
('MAT002', 'Auto Control Remoto Turbo', 'Carro RC a batería', 4, 2, '6+ años', 110.00, 1200.00, 92.00, 40, 18, 1.20),

-- Juegos de Mesa
('HAS001', 'Monopoly Clásico', 'Juego de bienes raíces', 5, 3, '8+ años', 95.00, 1050.00, 80.00, 35, 15, 1.10),
('HAS002', 'Jenga Original', 'Torre de bloques de madera', 5, 3, '6+ años', 48.00, 520.00, 40.00, 50, 25, 0.75),
('MAT003', 'UNO Card Game', 'Juego de cartas familiar', 5, 2, '7+ años', 22.00, 240.00, 18.00, 100, 60, 0.15),

-- Educativos
('FIS001', 'Mesa de Actividades Bilingüe', 'Mesa interactiva con luces', 6, 4, '6-36 meses', 150.00, 1650.00, 125.00, 25, 10, 2.50),
('VTE001', 'Laptop Educativa Infantil', 'Computadora de juguete', 6, 10, '3+ años', 85.00, 930.00, 70.00, 40, 20, 0.60),
('MEL001', 'Rompecabezas Madera 100 Piezas', 'Puzzle educativo temático', 6, 9, '4+ años', 38.00, 410.00, 32.00, 60, 30, 0.55),

-- Bebés
('FIS002', 'Gimnasio de Actividades Bebé', 'Gimnasio con juguetes colgantes', 7, 4, '0+ meses', 120.00, 1300.00, 100.00, 30, 12, 1.80),
('FIS003', 'Sonajero Musical Electrónico', 'Sonajero con melodías', 7, 4, '3+ meses', 28.00, 300.00, 23.00, 80, 40, 0.20),
('MAT004', 'Móvil Musical para Cuna', 'Móvil giratorio con música', 7, 2, '0+ meses', 65.00, 710.00, 55.00, 40, 20, 0.85),

-- Deportes
('NER001', 'Nerf Elite Disruptor', 'Lanzador de dardos de espuma', 8, 7, '8+ años', 75.00, 820.00, 62.00, 40, 20, 0.65),
('NER002', 'Nerf Mega Pack 50 Dardos', 'Repuestos de dardos', 8, 7, '8+ años', 32.00, 350.00, 27.00, 80, 50, 0.30),
('HAS003', 'Frisbee Profesional', 'Disco volador resistente', 8, 3, '6+ años', 18.00, 195.00, 15.00, 100, 60, 0.18),

-- Electrónicos
('VTE002', 'Robot Interactivo Programable', 'Robot con control app', 9, 10, '5+ años', 180.00, 1950.00, 150.00, 25, 10, 1.10),
('MAR001', 'Figura Spider-Man Electrónica', 'Figura con sonidos y luces', 9, 12, '4+ años', 95.00, 1030.00, 78.00, 35, 18, 0.70),

-- Arte y Manualidades
('PLY001', 'Play-Doh Super Set Colores', 'Set de 20 botes de plastilina', 10, 13, '3+ años', 55.00, 600.00, 45.00, 50, 30, 1.50),
('MEL002', 'Set de Arte Completo', 'Crayones, marcadores, pinturas', 10, 9, '3+ años', 72.00, 780.00, 60.00, 40, 25, 1.20),
('HAS004', 'Pizarra Mágica Grande', 'Pizarra para dibujar y borrar', 10, 3, '3+ años', 42.00, 460.00, 35.00, 50, 28, 0.90);

-- Inventario inicial (distribuido en ambos almacenes)
INSERT INTO Inventario (idProducto, idAlmacen, stockActual) VALUES
-- Almacén 251 (Principal)
(1, 251, 45), (2, 251, 12), (3, 251, 28), (4, 251, 60), (5, 251, 75),
(6, 251, 38), (7, 251, 22), (8, 251, 15), (9, 251, 18), (10, 251, 120),
(11, 251, 35), (12, 251, 48), (13, 251, 95), (14, 251, 68), (15, 251, 140),
(16, 251, 32), (17, 251, 52), (18, 251, 88), (19, 251, 42), (20, 251, 110),
(21, 251, 28), (22, 251, 65), (23, 251, 45), (24, 251, 85), (25, 251, 130),
(26, 251, 38), (27, 251, 72), (28, 251, 55), (29, 251, 95),

-- Almacén 252 (Secundario)
(1, 252, 30), (2, 252, 8), (3, 252, 18), (4, 252, 40), (5, 252, 50),
(6, 252, 25), (7, 252, 15), (8, 252, 10), (9, 252, 12), (10, 252, 80),
(11, 252, 22), (12, 252, 32), (13, 252, 65), (14, 252, 45), (15, 252, 90),
(16, 252, 20), (17, 252, 35), (18, 252, 58), (19, 252, 28), (20, 252, 75),
(21, 252, 18), (22, 252, 42), (23, 252, 30), (24, 252, 55), (25, 252, 85),
(26, 252, 25), (27, 252, 48), (28, 252, 38), (29, 252, 65);

-- Promociones de ejemplo
INSERT INTO Promocion (nombrePromocion, descripcion, tipoPromocion, porcentajeDescuento, 
                      cantidadCompra, cantidadPaga, aplicaMedioPago, montoMinimo, fechaInicio, fechaFin) VALUES
-- Descuento por pago con Yape/Plin
('Promo Pago Digital', 'Descuento especial pagando con Yape o Plin', 'Descuento', 5.00, NULL, NULL, NULL, 50.00, '2025-01-01', '2025-12-31'),

-- Ofertas 2x1 y 3x2
('2x1 en Hot Wheels', 'Lleva 2 y paga 1 en Hot Wheels Pack', '2x1', NULL, 2, 1, NULL, 0.00, '2025-01-01', '2025-03-31'),
('3x2 en Peluches', 'Compra 3 peluches y paga solo 2', '3x2', NULL, 3, 2, NULL, 0.00, '2025-01-01', '2025-02-28'),

-- Descuentos por categoría
('15% OFF Play-Doh', 'Descuento en productos de arte', 'Descuento', 15.00, NULL, NULL, NULL, 100.00, '2025-01-15', '2025-02-15'),

-- Promo Yape específica
('10% Yape en Compras +500', 'Descuento extra pagando con Yape', 'Descuento', 10.00, NULL, NULL, 'Yape', 500.00, '2025-01-01', '2025-06-30');

GO

-- =====================================================
-- FUNCIONES ÚTILES
-- =====================================================

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
END;
GO

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
END;
GO

-- =====================================================
-- PROCEDIMIENTOS ADICIONALES
-- =====================================================

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
GO

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
GO

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
GO

-- =====================================================
-- MENSAJES FINALES
-- =====================================================

PRINT '====================================================';
PRINT 'BASE DE DATOS SIMPLIFICADA CREADA EXITOSAMENTE';
PRINT '====================================================';
PRINT '';
PRINT 'CARACTERÍSTICAS:';
PRINT '✓ Venta por Unidad, Docena y Mayorista';
PRINT '✓ 2 Almacenes (251 y 252)';
PRINT '✓ Medios de pago: Yape, Plin, Efectivo, Transferencia';
PRINT '✓ Sistema de promociones y ofertas';
PRINT '✓ Control de inventario automatizado';
PRINT '✓ 29 productos de ejemplo cargados';
PRINT '';
PRINT 'USUARIOS DE PRUEBA:';
PRINT '  admin / admin123 (Administrador)';
PRINT '  vendedor1 / vend123 (Vendedor)';
PRINT '  almacen1 / alm123 (Almacenero)';
PRINT '';
PRINT 'TABLAS PRINCIPALES:';
PRINT '  - Usuario (3 usuarios)';
PRINT '  - Cliente (5 clientes)';
PRINT '  - Proveedor (3 proveedores)';
PRINT '  - Producto (29 productos)';
PRINT '  - Almacen (2: 251 y 252)';
PRINT '  - Inventario (Stock distribuido)';
PRINT '  - Promocion (5 promociones activas)';
PRINT '  - Venta y DetalleVenta';
PRINT '  - Compra y DetalleCompra';
PRINT '';
PRINT 'PROCEDIMIENTOS ALMACENADOS:';
PRINT '  - SP_RegistrarVenta';
PRINT '  - SP_AplicarPromocion';
PRINT '  - SP_TransferirStock';
PRINT '  - SP_ReporteVentas';
PRINT '  - SP_ProductosStockBajo';
PRINT '  - SP_ConsultarPreciosProducto';
PRINT '';
PRINT 'VISTAS ÚTILES:';
PRINT '  - VW_StockConsolidado';
PRINT '  - VW_PromocionesActivas';
PRINT '  - VW_VentasHoy';
PRINT '  - VW_TopProductosVendidos';
PRINT '';
PRINT '====================================================';
PRINT 'EJEMPLO DE USO:';
PRINT '';
PRINT '-- Ver stock consolidado de ambos almacenes';
PRINT 'SELECT * FROM VW_StockConsolidado;';
PRINT '';
PRINT '-- Ver promociones activas';
PRINT 'SELECT * FROM VW_PromocionesActivas;';
PRINT '';
PRINT '-- Consultar precios de un producto';
PRINT 'EXEC SP_ConsultarPreciosProducto @codigoProducto = ''HOT001'';';
PRINT '';
PRINT '-- Ver productos con stock bajo';
PRINT 'EXEC SP_ProductosStockBajo;';
PRINT '';
PRINT '-- Reporte de ventas del mes';
PRINT 'EXEC SP_ReporteVentas @fechaInicio = ''2025-01-01'', @fechaFin = ''2025-01-31'';';
PRINT '====================================================';
GO