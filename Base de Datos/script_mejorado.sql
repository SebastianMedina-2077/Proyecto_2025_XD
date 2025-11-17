create table ALMACEN_SM
(
    COD_ALMACEN       varchar(10) not null
        constraint PK_ALMACEN_SM
            primary key,
    NOMBRE_ALMACEN    varchar(50) not null,
    DIRECCION_ALMACEN varchar(100),
    ACTIVO            bit default 1
)
go

create table CATEGORIA_SM
(
    ID_CATEGORIA int identity
        constraint PK_CATEGORIA_SM
            primary key,
    NOMBRE_CAT   varchar(50) not null,
    ACTIVO       bit default 1
)
go

create table DIRECCION_SM
(
    ID_DIRECCION       int identity
        constraint PK_DIRECCION_SM
            primary key,
    DIRECCION_COMPLETA varchar(200) not null,
    DEPARTAMENTO_DIR   varchar(30),
    PROVINCIA_DIR      varchar(30),
    DISTRITO_DIR       varchar(40),
    REFERENCIA         varchar(100)
)
go

create table CONTACTO_SM
(
    ID_CONTACTO  int identity
        constraint PK_CONTACTO_SM
            primary key,
    NUMERO_TEL   varchar(15),
    OPERADOR     varchar(20),
    CORREO       varchar(50),
    ID_DIRECCION int
        constraint FK_CONTACTO_DIRECCION
            references DIRECCION_SM,
    WHATSAPP     varchar(15)
)
go

create table ESTADO_SM
(
    ID_ESTADO    int identity
        constraint PK_ESTADO_SM
            primary key,
    DESCRIP_ESTA varchar(20) not null,
    ACTIVO       bit default 1
)
go

create table MARCA_SM
(
    ID_MARCA   int identity
        constraint PK_MARCA_SM
            primary key,
    NOMBRE_MAR varchar(50) not null,
    ACTIVO     bit default 1
)
go

create table PERSONA_SM
(
    DNI_SM           varchar(12) not null
        constraint PK_PERSONA_SM
            primary key,
    NOMBRE_SM        varchar(60) not null,
    APELLIDO_SM      varchar(60) not null,
    FECHA_NACIMIENTO date,
    GENERO           char,
    ID_CONTACTO      int
        constraint FK_PERSONA_CONTACTO
            references CONTACTO_SM,
    FECHA_REGISTRO   datetime default getdate()
)
go

create table TIPO_SM
(
    ID_TIPO        int identity
        constraint PK_TIPO_SM
            primary key,
    NOMBRE_TIP     varchar(20) not null,
    CATEGORIA_TIPO varchar(20) not null,
    ACTIVO         bit default 1
)
go

create table CLIENTE_SM
(
    ID_CLIENTE         varchar(20) not null
        constraint PK_CLIENTE_SM
            primary key,
    DNI_SM             varchar(12) not null
        constraint FK_CLIENTE_PERSONA
            references PERSONA_SM,
    CODIGO_CLI_SM      varchar(20),
    TIPO_CLIENTE_SM    varchar(20),
    FECHA_REGISTRO_SM  datetime      default getdate(),
    ID_TIPO            int         not null
        constraint FK_CLIENTE_TIPO
            references TIPO_SM,
    ID_ESTADO          int         not null
        constraint FK_CLIENTE_ESTADO
            references ESTADO_SM,
    DESCUENTO_ESPECIAL decimal(5, 2) default 0
)
go

create table PROVEEDOR_SM
(
    COD_PROV       int identity
        constraint PK_PROVEEDOR_SM
            primary key,
    RAZON_SOCIAL   varchar(100) not null,
    RUC_PROV       varchar(11)  not null,
    DNI_CONTACTO   varchar(12)
        constraint FK_PROVEEDOR_CONTACTO
            references PERSONA_SM,
    BANCO_PROV     varchar(50),
    NUMERO_CUENTA  varchar(50),
    ID_ESTADO      int          not null
        constraint FK_PROVEEDOR_ESTADO
            references ESTADO_SM,
    ID_TIPO        int          not null
        constraint FK_PROVEEDOR_TIPO
            references TIPO_SM,
    FECHA_REGISTRO datetime default getdate()
)
go

create table PRODUCTO_SM
(
    COD_PRODUCTO      varchar(20)    not null
        constraint PK_PRODUCTO_SM
            primary key,
    NOMBRE_PRO        varchar(100)   not null,
    DESCRIPCION       varchar(500),
    PRECIO_COMPRA     decimal(10, 2) not null,
    PRECIO_VENTA      decimal(10, 2) not null,
    STOCK_MINIMO      int      default 5,
    LOTE_PRO          varchar(20),
    FECHA_VENCIMIENTO date,
    IMAGEN_PATH       varchar(200),
    ID_CATEGORIA      int            not null
        constraint FK_PRODUCTO_CATEGORIA
            references CATEGORIA_SM,
    ID_MARCA          int            not null
        constraint FK_PRODUCTO_MARCA
            references MARCA_SM,
    ID_ESTADO         int            not null
        constraint FK_PRODUCTO_ESTADO
            references ESTADO_SM,
    COD_PROV          int            not null
        constraint FK_PRODUCTO_PROVEEDOR
            references PROVEEDOR_SM,
    FECHA_REGISTRO    datetime default getdate()
)
go

create index IX_PRODUCTO_CATEGORIA
    on PRODUCTO_SM (ID_CATEGORIA)
go

create index IX_PRODUCTO_MARCA
    on PRODUCTO_SM (ID_MARCA)
go

create table USUARIOS_SM
(
    ID_USUARIO        int identity
        constraint PK_USUARIOS_SM
            primary key,
    NOMBRE_US         varchar(30)  not null
        unique,
    CONTRASEÑA_US     varchar(100) not null,
    DNI_SM            varchar(12)  not null
        constraint FK_USUARIO_PERSONA
            references PERSONA_SM,
    ID_ESTADO         int          not null
        constraint FK_USUARIO_ESTADO
            references ESTADO_SM,
    ID_TIPO           int          not null
        constraint FK_USUARIO_TIPO
            references TIPO_SM,
    FECHA_REGISTRO_US datetime default getdate(),
    ULTIMO_INGRESO    datetime,
    INTENTOS_FALLIDOS int      default 0
)
go

create table INVENTARIO_SM
(
    ID_INVENTARIO      int identity
        constraint PK_INVENTARIO_SM
            primary key,
    COD_PRODUCTO       varchar(20)        not null
        constraint FK_INVENTARIO_PRODUCTO
            references PRODUCTO_SM,
    COD_ALMACEN        varchar(10)        not null
        constraint FK_INVENTARIO_ALMACEN
            references ALMACEN_SM,
    STOCK_ACTUAL       int      default 0 not null,
    STOCK_RESERVADO    int      default 0,
    FECHA_MODIFICACION datetime default getdate(),
    USUARIO_MODIFICA   int
        constraint FK_INVENTARIO_USUARIO
            references USUARIOS_SM,
    constraint UK_INVENTARIO
        unique (COD_PRODUCTO, COD_ALMACEN)
)
go

create index IX_INVENTARIO_PRODUCTO
    on INVENTARIO_SM (COD_PRODUCTO)
go

create table MOVIMIENTO_INVENTARIO
(
    ID_MOVIMIENTO      int identity
        constraint PK_MOVIMIENTO_INVENTARIO
            primary key,
    COD_PRODUCTO       varchar(20) not null
        constraint FK_MOVIMIENTO_PRODUCTO
            references PRODUCTO_SM,
    COD_ALMACEN        varchar(10) not null
        constraint FK_MOVIMIENTO_ALMACEN
            references ALMACEN_SM,
    TIPO_MOVIMIENTO    varchar(20) not null,
    CANTIDAD           int         not null,
    STOCK_ANTERIOR     int         not null,
    STOCK_NUEVO        int         not null,
    FECHA_MOVIMIENTO   datetime default getdate(),
    USUARIO_MOVIMIENTO int         not null
        constraint FK_MOVIMIENTO_USUARIO
            references USUARIOS_SM,
    REFERENCIA         varchar(50),
    OBSERVACIONES      varchar(200)
)
go

create index IX_MOVIMIENTO_FECHA
    on MOVIMIENTO_INVENTARIO (FECHA_MOVIMIENTO)
go

create table VENTA_CABECERA_SM
(
    COD_FACTURA     varchar(20)    not null
        constraint PK_VENTA_CABECERA_SM
            primary key,
    ID_CLIENTE      varchar(20)    not null
        constraint FK_VENTA_CLIENTE
            references CLIENTE_SM,
    ID_USUARIO      int            not null
        constraint FK_VENTA_USUARIO
            references USUARIOS_SM,
    FECHA_VENTA     datetime       default getdate(),
    SUBTOTAL        decimal(10, 2) not null,
    IGV_TOTAL       decimal(10, 2) not null,
    DESCUENTO_TOTAL decimal(10, 2) default 0,
    TOTAL_VENTA     decimal(10, 2) not null,
    METODO_PAGO     varchar(20)    not null,
    ESTADO_VENTA    varchar(20)    default 'COMPLETADA',
    QR_VENTA        varchar(200),
    OBSERVACIONES   varchar(200)
)
go

create index IX_VENTA_FECHA
    on VENTA_CABECERA_SM (FECHA_VENTA)
go

create index IX_VENTA_CLIENTE
    on VENTA_CABECERA_SM (ID_CLIENTE)
go

create table VENTA_DETALLE_SM
(
    ID_DETALLE         int identity
        constraint PK_VENTA_DETALLE_SM
            primary key,
    COD_FACTURA        varchar(20)    not null
        constraint FK_DETALLE_CABECERA
            references VENTA_CABECERA_SM,
    COD_PRODUCTO       varchar(20)    not null
        constraint FK_DETALLE_PRODUCTO
            references PRODUCTO_SM,
    CANTIDAD           int            not null,
    PRECIO_UNITARIO    decimal(10, 2) not null,
    DESCUENTO_UNITARIO decimal(10, 2) default 0,
    SUBTOTAL_DETALLE   decimal(10, 2) not null,
    COD_ALMACEN        varchar(10)    not null
        constraint FK_DETALLE_ALMACEN
            references ALMACEN_SM
)
go

CREATE PROCEDURE dbo.ActualizarInventario
    @p_id_producto      INT,
    @p_id_almacen       INT,
    @p_cantidad         DECIMAL(10,2),
    @p_tipo_movimiento  VARCHAR(10),
    @p_costo_unitario   DECIMAL(10,2),
    @p_precio_venta     DECIMAL(10,2)
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @stock_actual   DECIMAL(10,2) = 0;
    DECLARE @stock_nuevo    DECIMAL(10,2);
    DECLARE @registro_existe INT = 0;

    SELECT 
        @registro_existe = COUNT(*),
        @stock_actual    = ISNULL(stock_actual, 0)
    FROM dbo.inventario
    WHERE id_producto = @p_id_producto
      AND id_almacen  = @p_id_almacen;

    IF @p_tipo_movimiento = 'ENTRADA'
        SET @stock_nuevo = @stock_actual + @p_cantidad;
    ELSE IF @p_tipo_movimiento = 'SALIDA'
        SET @stock_nuevo = @stock_actual - @p_cantidad;
    ELSE IF @p_tipo_movimiento = 'AJUSTE'
        SET @stock_nuevo = @p_cantidad;
    ELSE
        SET @stock_nuevo = @stock_actual;

    IF @registro_existe > 0
    BEGIN
        UPDATE dbo.inventario
        SET 
            stock_actual        = @stock_nuevo,
            precio_costo        = @p_costo_unitario,
            precio_venta_actual = @p_precio_venta,
            fecha_actualizacion = GETDATE()
        WHERE id_producto = @p_id_producto
          AND id_almacen  = @p_id_almacen;
    END
    ELSE
    BEGIN
        INSERT INTO dbo.inventario (
            id_producto, 
            id_almacen, 
            stock_actual, 
            precio_costo, 
            precio_venta_actual
        )
        VALUES (
            @p_id_producto, 
            @p_id_almacen, 
            @stock_nuevo, 
            @p_costo_unitario, 
            @p_precio_venta
        );
    END
END
go


CREATE FUNCTION dbo.GenerarNumeroMovimiento
(
    @tipo VARCHAR(10)
)
RETURNS VARCHAR(50)
AS
BEGIN
    DECLARE @siguiente_numero   INT;
    DECLARE @prefijo            VARCHAR(5);
    DECLARE @numero_completo    VARCHAR(50);

    IF @tipo = 'ENTRADA' SET @prefijo = 'ENT';
    ELSE IF @tipo = 'SALIDA' SET @prefijo = 'SAL';
    ELSE IF @tipo = 'AJUSTE' SET @prefijo = 'AJU';
    ELSE SET @prefijo = 'MOV';

    SELECT 
        @siguiente_numero = ISNULL(
            MAX(CAST(SUBSTRING(numero_movimiento, LEN(@prefijo) + 1,  10) AS INT))
        , 0) + 1
    FROM dbo.movimientos_inventario
    WHERE numero_movimiento LIKE @prefijo + '%';

    SET @numero_completo = @prefijo 
        + RIGHT('000000' + CAST(@siguiente_numero AS VARCHAR(6)), 6);

    RETURN @numero_completo;
END
go


CREATE FUNCTION dbo.GenerarNumeroMovimientoCaja
(
    @tipo VARCHAR(10)
)
RETURNS VARCHAR(50)
AS
BEGIN
    DECLARE @siguiente_numero   INT;
    DECLARE @prefijo            VARCHAR(5);
    DECLARE @numero_completo    VARCHAR(50);

    IF @tipo = 'ENTRADA' SET @prefijo = 'ECAJ';
    ELSE IF @tipo = 'SALIDA' SET @prefijo = 'SCAJ';
    ELSE SET @prefijo = 'MCAJ';

    SELECT 
        @siguiente_numero = ISNULL(
            MAX(CAST(
                SUBSTRING(
                    numero_movimiento, 
                    LEN(@prefijo) + 1, 
                    10
                ) AS INT)
            )
        , 0) + 1
    FROM dbo.movimientos_caja
    WHERE numero_movimiento LIKE @prefijo + '%';

    SET @numero_completo = @prefijo 
        + RIGHT('000000' + CAST(@siguiente_numero AS VARCHAR(6)), 6);

    RETURN @numero_completo;
END
go


CREATE FUNCTION dbo.GenerarNumeroSesion()
RETURNS VARCHAR(50)
AS
BEGIN
    DECLARE @siguiente_numero   INT;
    DECLARE @fecha_actual       CHAR(8);
    DECLARE @numero_completo    VARCHAR(50);

    -- Formato: SESYYYYMMDD-NNNN
    SET @fecha_actual = CONVERT(CHAR(8), GETDATE(), 112);  

    SELECT 
        @siguiente_numero = ISNULL(
            MAX(CAST(RIGHT(numero_sesion, 4) AS INT))
        , 0) + 1
    FROM dbo.sesiones_caja
    WHERE numero_sesion LIKE 'SES' + @fecha_actual + '-%';

    SET @numero_completo = 'SES' 
        + @fecha_actual 
        + '-' 
        + RIGHT('0000' + CAST(@siguiente_numero AS VARCHAR(4)), 4);

    RETURN @numero_completo;
END
go


CREATE FUNCTION dbo.GenerarNumeroTicket()
RETURNS VARCHAR(50)
AS
BEGIN
    DECLARE @siguiente_numero   INT;
    DECLARE @mes_actual         CHAR(6);
    DECLARE @numero_completo    VARCHAR(50);

    -- Formato: YYYYMM-NNNNNN
    SET @mes_actual = SUBSTRING(CONVERT(CHAR(8), GETDATE(), 112), 1, 6);

    SELECT 
        @siguiente_numero = ISNULL(
            MAX(CAST(RIGHT(numero_ticket, 6) AS INT))
        , 0) + 1
    FROM dbo.ventas
    WHERE numero_ticket LIKE @mes_actual + '-%';

    SET @numero_completo = @mes_actual 
        + '-' 
        + RIGHT('000000' + CAST(@siguiente_numero AS VARCHAR(6)), 6);

    RETURN @numero_completo;
END
go

CREATE PROCEDURE SP_ActualizarPersona
    @Dni VARCHAR(12),
    @Nombre VARCHAR(60),
    @Apellido VARCHAR(60),
    @FechaNacimiento DATE = NULL,
    @Genero CHAR(1) = NULL,
    @Telefono VARCHAR(15) = NULL,
    @Whatsapp VARCHAR(15) = NULL,
    @Correo VARCHAR(50) = NULL,
    @DireccionCompleta VARCHAR(200) = NULL,
    @Departamento VARCHAR(30) = NULL,
    @Provincia VARCHAR(30) = NULL,
    @Distrito VARCHAR(40) = NULL,
    @Referencia VARCHAR(100) = NULL,
    @Resultado INT OUTPUT,
    @Mensaje VARCHAR(200) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Verificar que la persona existe
        IF NOT EXISTS (SELECT 1 FROM PERSONA_SM WHERE DNI_SM = @Dni)
            BEGIN
                SET @Resultado = 0;
                SET @Mensaje = 'La persona con DNI ' + @Dni + ' no existe';
                ROLLBACK TRANSACTION;
                RETURN;
            END

        DECLARE @IdContacto INT;
        DECLARE @IdDireccion INT;

        -- Obtener ID de contacto actual
        SELECT @IdContacto = ID_CONTACTO FROM PERSONA_SM WHERE DNI_SM = @Dni;

        -- Si tiene contacto, actualizar o crear
        IF @IdContacto IS NOT NULL
            BEGIN
                -- Obtener dirección actual
                SELECT @IdDireccion = ID_DIRECCION FROM CONTACTO_SM WHERE ID_CONTACTO = @IdContacto;

                -- Actualizar o crear dirección
                IF @DireccionCompleta IS NOT NULL
                    BEGIN
                        IF @IdDireccion IS NOT NULL
                            BEGIN
                                UPDATE DIRECCION_SM
                                SET DIRECCION_COMPLETA = @DireccionCompleta,
                                    DEPARTAMENTO_DIR = @Departamento,
                                    PROVINCIA_DIR = @Provincia,
                                    DISTRITO_DIR = @Distrito,
                                    REFERENCIA = @Referencia
                                WHERE ID_DIRECCION = @IdDireccion;
                            END
                        ELSE
                            BEGIN
                                INSERT INTO DIRECCION_SM (DIRECCION_COMPLETA, DEPARTAMENTO_DIR, PROVINCIA_DIR, DISTRITO_DIR, REFERENCIA)
                                VALUES (@DireccionCompleta, @Departamento, @Provincia, @Distrito, @Referencia);
                                SET @IdDireccion = SCOPE_IDENTITY();
                            END
                    END

                -- Actualizar contacto
                UPDATE CONTACTO_SM
                SET NUMERO_TEL = @Telefono,
                    WHATSAPP = @Whatsapp,
                    CORREO = @Correo,
                    ID_DIRECCION = @IdDireccion
                WHERE ID_CONTACTO = @IdContacto;
            END
        ELSE IF @Telefono IS NOT NULL OR @Correo IS NOT NULL OR @Whatsapp IS NOT NULL
            BEGIN
                -- Crear dirección si es necesario
                IF @DireccionCompleta IS NOT NULL
                    BEGIN
                        INSERT INTO DIRECCION_SM (DIRECCION_COMPLETA, DEPARTAMENTO_DIR, PROVINCIA_DIR, DISTRITO_DIR, REFERENCIA)
                        VALUES (@DireccionCompleta, @Departamento, @Provincia, @Distrito, @Referencia);
                        SET @IdDireccion = SCOPE_IDENTITY();
                    END

                -- Crear contacto
                INSERT INTO CONTACTO_SM (NUMERO_TEL, WHATSAPP, CORREO, ID_DIRECCION)
                VALUES (@Telefono, @Whatsapp, @Correo, @IdDireccion);
                SET @IdContacto = SCOPE_IDENTITY();
            END

        -- Actualizar persona
        UPDATE PERSONA_SM
        SET NOMBRE_SM = @Nombre,
            APELLIDO_SM = @Apellido,
            FECHA_NACIMIENTO = @FechaNacimiento,
            GENERO = @Genero,
            ID_CONTACTO = @IdContacto
        WHERE DNI_SM = @Dni;

        COMMIT TRANSACTION;
        SET @Resultado = 1;
        SET @Mensaje = 'Persona actualizada exitosamente';

    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        SET @Resultado = 0;
        SET @Mensaje = 'Error al actualizar persona: ' + ERROR_MESSAGE();
    END CATCH
END
go

-- SP para actualizar stock
CREATE PROCEDURE SP_ActualizarStock
    @CodigoProducto VARCHAR(20),
    @CodigoAlmacen VARCHAR(10),
    @TipoMovimiento VARCHAR(20), -- 'ENTRADA', 'SALIDA', 'AJUSTE'
    @Cantidad INT,
    @IdUsuario INT,
    @Referencia VARCHAR(50) = NULL,
    @Observaciones VARCHAR(200) = NULL,
    @Resultado INT OUTPUT,
    @Mensaje VARCHAR(200) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRANSACTION;

        DECLARE @StockActual INT;
        DECLARE @NuevoStock INT;

        -- Obtener stock actual
        SELECT @StockActual = STOCK_ACTUAL
        FROM INVENTARIO_SM
        WHERE COD_PRODUCTO = @CodigoProducto AND COD_ALMACEN = @CodigoAlmacen;

        IF @StockActual IS NULL
        BEGIN
            SET @Resultado = 0;
            SET @Mensaje = 'No existe registro de inventario para este producto y almacén';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Calcular nuevo stock según tipo de movimiento
        IF @TipoMovimiento = 'ENTRADA' OR @TipoMovimiento = 'AJUSTE'
        BEGIN
            SET @NuevoStock = @StockActual + @Cantidad;
        END
        ELSE IF @TipoMovimiento = 'SALIDA'
        BEGIN
            IF @StockActual < @Cantidad
            BEGIN
                SET @Resultado = 0;
                SET @Mensaje = 'Stock insuficiente. Stock actual: ' + CAST(@StockActual AS VARCHAR);
                ROLLBACK TRANSACTION;
                RETURN;
            END
            SET @NuevoStock = @StockActual - @Cantidad;
        END

        -- Actualizar inventario
        UPDATE INVENTARIO_SM
        SET STOCK_ACTUAL = @NuevoStock,
            FECHA_MODIFICACION = GETDATE(),
            USUARIO_MODIFICA = @IdUsuario
        WHERE COD_PRODUCTO = @CodigoProducto AND COD_ALMACEN = @CodigoAlmacen;

        -- Registrar movimiento
        INSERT INTO MOVIMIENTO_INVENTARIO (
            COD_PRODUCTO, COD_ALMACEN, TIPO_MOVIMIENTO, CANTIDAD,
            STOCK_ANTERIOR, STOCK_NUEVO, USUARIO_MOVIMIENTO, REFERENCIA, OBSERVACIONES
        )
        VALUES (
            @CodigoProducto, @CodigoAlmacen, @TipoMovimiento, @Cantidad,
            @StockActual, @NuevoStock, @IdUsuario, @Referencia, @Observaciones
        );

        COMMIT TRANSACTION;
        SET @Resultado = 1;
        SET @Mensaje = 'Stock actualizado exitosamente';

    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        SET @Resultado = 0;
        SET @Mensaje = 'Error al actualizar stock: ' + ERROR_MESSAGE();
    END CATCH
END
go

-- SP para buscar persona por criterio
CREATE PROCEDURE SP_BuscarPersonaPorCriterio
    @Criterio VARCHAR(50),
    @Valor VARCHAR(100)
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        IF @Criterio = 'DNI'
            BEGIN
                SELECT
                    p.DNI_SM,
                    p.NOMBRE_SM,
                    p.APELLIDO_SM,
                    p.FECHA_NACIMIENTO,
                    p.GENERO,
                    p.FECHA_REGISTRO,
                    c.NUMERO_TEL,
                    c.WHATSAPP,
                    c.CORREO,
                    d.DIRECCION_COMPLETA,
                    d.DEPARTAMENTO_DIR,
                    d.PROVINCIA_DIR,
                    d.DISTRITO_DIR,
                    d.REFERENCIA
                FROM PERSONA_SM p
                         LEFT JOIN CONTACTO_SM c ON p.ID_CONTACTO = c.ID_CONTACTO
                         LEFT JOIN DIRECCION_SM d ON c.ID_DIRECCION = d.ID_DIRECCION
                WHERE p.DNI_SM LIKE '%' + @Valor + '%';
            END
        ELSE IF @Criterio = 'NOMBRE'
            BEGIN
                SELECT
                    p.DNI_SM,
                    p.NOMBRE_SM,
                    p.APELLIDO_SM,
                    p.FECHA_NACIMIENTO,
                    p.GENERO,
                    p.FECHA_REGISTRO,
                    c.NUMERO_TEL,
                    c.WHATSAPP,
                    c.CORREO,
                    d.DIRECCION_COMPLETA,
                    d.DEPARTAMENTO_DIR,
                    d.PROVINCIA_DIR,
                    d.DISTRITO_DIR,
                    d.REFERENCIA
                FROM PERSONA_SM p
                         LEFT JOIN CONTACTO_SM c ON p.ID_CONTACTO = c.ID_CONTACTO
                         LEFT JOIN DIRECCION_SM d ON c.ID_DIRECCION = d.ID_DIRECCION
                WHERE p.NOMBRE_SM LIKE '%' + @Valor + '%'
                   OR p.APELLIDO_SM LIKE '%' + @Valor + '%';
            END

    END TRY
    BEGIN CATCH
        DECLARE @ErrorMessage NVARCHAR(4000) = ERROR_MESSAGE();
        RAISERROR (@ErrorMessage, 16, 1);
    END CATCH
END
go

-- SP para obtener movimientos de inventario
CREATE PROCEDURE SP_ConsultarMovimientosInventario
    @CodigoProducto VARCHAR(20) = NULL,
    @CodigoAlmacen VARCHAR(10) = NULL,
    @FechaInicio DATETIME = NULL,
    @FechaFin DATETIME = NULL,
    @TipoMovimiento VARCHAR(20) = NULL
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        mi.ID_MOVIMIENTO,
        mi.COD_PRODUCTO,
        p.NOMBRE_PRO,
        mi.COD_ALMACEN,
        a.NOMBRE_ALMACEN,
        mi.TIPO_MOVIMIENTO,
        mi.CANTIDAD,
        mi.STOCK_ANTERIOR,
        mi.STOCK_NUEVO,
        mi.FECHA_MOVIMIENTO,
        u.NOMBRE_US as USUARIO,
        mi.REFERENCIA,
        mi.OBSERVACIONES
    FROM MOVIMIENTO_INVENTARIO mi
    INNER JOIN PRODUCTO_SM p ON mi.COD_PRODUCTO = p.COD_PRODUCTO
    INNER JOIN ALMACEN_SM a ON mi.COD_ALMACEN = a.COD_ALMACEN
    INNER JOIN USUARIOS_SM u ON mi.USUARIO_MOVIMIENTO = u.ID_USUARIO
    WHERE (@CodigoProducto IS NULL OR mi.COD_PRODUCTO = @CodigoProducto)
    AND (@CodigoAlmacen IS NULL OR mi.COD_ALMACEN = @CodigoAlmacen)
    AND (@FechaInicio IS NULL OR mi.FECHA_MOVIMIENTO >= @FechaInicio)
    AND (@FechaFin IS NULL OR mi.FECHA_MOVIMIENTO <= @FechaFin)
    AND (@TipoMovimiento IS NULL OR mi.TIPO_MOVIMIENTO = @TipoMovimiento)
    ORDER BY mi.FECHA_MOVIMIENTO DESC;
END
go

-- SP para obtener información de productos con stock
CREATE PROCEDURE SP_ConsultarProductosConStock
    @CodigoProducto VARCHAR(20) = NULL,
    @IdCategoria INT = NULL,
    @IdMarca INT = NULL,
    @SoloConStock BIT = 1
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        p.COD_PRODUCTO,
        p.NOMBRE_PRO,
        p.DESCRIPCION,
        p.PRECIO_COMPRA,
        p.PRECIO_VENTA,
        p.STOCK_MINIMO,
        p.LOTE_PRO,
        p.FECHA_VENCIMIENTO,
        c.NOMBRE_CAT as CATEGORIA,
        m.NOMBRE_MAR as MARCA,
        pr.RAZON_SOCIAL as PROVEEDOR,
        e.DESCRIP_ESTA as ESTADO,
        -- Stock por almacén
        SUM(CASE WHEN i.COD_ALMACEN = 'ALM01' THEN i.STOCK_ACTUAL ELSE 0 END) as STOCK_ALM01,
        SUM(CASE WHEN i.COD_ALMACEN = 'ALM02' THEN i.STOCK_ACTUAL ELSE 0 END) as STOCK_ALM02,
        SUM(i.STOCK_ACTUAL) as STOCK_TOTAL,
        -- Alertas
        CASE
            WHEN SUM(i.STOCK_ACTUAL) <= p.STOCK_MINIMO THEN 'STOCK_BAJO'
            WHEN p.FECHA_VENCIMIENTO <= DATEADD(MONTH, 1, GETDATE()) THEN 'PROXIMO_VENCER'
            ELSE 'OK'
        END as ALERTA
    FROM PRODUCTO_SM p
    INNER JOIN CATEGORIA_SM c ON p.ID_CATEGORIA = c.ID_CATEGORIA
    INNER JOIN MARCA_SM m ON p.ID_MARCA = m.ID_MARCA
    INNER JOIN PROVEEDOR_SM pr ON p.COD_PROV = pr.COD_PROV
    INNER JOIN ESTADO_SM e ON p.ID_ESTADO = e.ID_ESTADO
    LEFT JOIN INVENTARIO_SM i ON p.COD_PRODUCTO = i.COD_PRODUCTO
    WHERE (@CodigoProducto IS NULL OR p.COD_PRODUCTO = @CodigoProducto)
    AND (@IdCategoria IS NULL OR p.ID_CATEGORIA = @IdCategoria)
    AND (@IdMarca IS NULL OR p.ID_MARCA = @IdMarca)
    AND p.ID_ESTADO = 1 -- Solo activos
    GROUP BY p.COD_PRODUCTO, p.NOMBRE_PRO, p.DESCRIPCION, p.PRECIO_COMPRA, p.PRECIO_VENTA,
             p.STOCK_MINIMO, p.LOTE_PRO, p.FECHA_VENCIMIENTO, c.NOMBRE_CAT, m.NOMBRE_MAR,
             pr.RAZON_SOCIAL, e.DESCRIP_ESTA
    HAVING (@SoloConStock = 0 OR SUM(i.STOCK_ACTUAL) > 0)
    ORDER BY p.NOMBRE_PRO;
END
go

-- SP para consultar ventas por período
CREATE PROCEDURE SP_ConsultarVentasPorPeriodo
    @FechaInicio DATE,
    @FechaFin DATE,
    @IdUsuario INT = NULL,
    @IdCliente VARCHAR(20) = NULL
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        v.COD_FACTURA,
        v.FECHA_VENTA,
        CONCAT(p.NOMBRE_SM, ' ', p.APELLIDO_SM) as CLIENTE,
        u.NOMBRE_US as VENDEDOR,
        v.SUBTOTAL,
        v.IGV_TOTAL,
        v.DESCUENTO_TOTAL,
        v.TOTAL_VENTA,
        v.METODO_PAGO,
        v.ESTADO_VENTA,
        -- Cantidad de items
        COUNT(vd.ID_DETALLE) as CANTIDAD_ITEMS,
        -- Total de productos vendidos
        SUM(vd.CANTIDAD) as TOTAL_PRODUCTOS
    FROM VENTA_CABECERA_SM v
    INNER JOIN CLIENTE_SM c ON v.ID_CLIENTE = c.ID_CLIENTE
    INNER JOIN PERSONA_SM p ON c.DNI_SM = p.DNI_SM
    INNER JOIN USUARIOS_SM u ON v.ID_USUARIO = u.ID_USUARIO
    LEFT JOIN VENTA_DETALLE_SM vd ON v.COD_FACTURA = vd.COD_FACTURA
    WHERE CAST(v.FECHA_VENTA AS DATE) BETWEEN @FechaInicio AND @FechaFin
    AND (@IdUsuario IS NULL OR v.ID_USUARIO = @IdUsuario)
    AND (@IdCliente IS NULL OR v.ID_CLIENTE = @IdCliente)
    GROUP BY v.COD_FACTURA, v.FECHA_VENTA, p.NOMBRE_SM, p.APELLIDO_SM,
             u.NOMBRE_US, v.SUBTOTAL, v.IGV_TOTAL, v.DESCUENTO_TOTAL,
             v.TOTAL_VENTA, v.METODO_PAGO, v.ESTADO_VENTA
    ORDER BY v.FECHA_VENTA DESC;
END
go

CREATE PROCEDURE SP_CrearProducto
    @CodigoProducto VARCHAR(20),
    @NombreProducto VARCHAR(100),
    @Descripcion VARCHAR(500) = NULL,
    @PrecioCompra DECIMAL(10,2),
    @PrecioVenta DECIMAL(10,2),
    @StockMinimo INT = 5,
    @Lote VARCHAR(20) = NULL,
    @FechaVencimiento DATE = NULL,
    @ImagenPath VARCHAR(200) = NULL,
    @IdCategoria INT,
    @IdMarca INT,
    @CodProveedor INT,
    @AlmacenElegido VARCHAR(10),
    @StockInicialAlm01 INT = 0,
    @StockInicialAlm02 INT = 0,
    @IdUsuario INT,
    @Resultado INT OUTPUT,
    @Mensaje VARCHAR(200) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Verificar que el código de producto no exista
        IF EXISTS (SELECT 1 FROM PRODUCTO_SM WHERE COD_PRODUCTO = @CodigoProducto)
        BEGIN
            SET @Resultado = 0;
            SET @Mensaje = 'El código de producto ya existe';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Verificar que existan las referencias
        IF NOT EXISTS (SELECT 1 FROM CATEGORIA_SM WHERE ID_CATEGORIA = @IdCategoria)
        BEGIN
            SET @Resultado = 0;
            SET @Mensaje = 'La categoría no existe';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        IF NOT EXISTS (SELECT 1 FROM MARCA_SM WHERE ID_MARCA = @IdMarca)
        BEGIN
            SET @Resultado = 0;
            SET @Mensaje = 'La marca no existe';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        IF NOT EXISTS (SELECT 1 FROM PROVEEDOR_SM WHERE COD_PROV = @CodProveedor)
        BEGIN
            SET @Resultado = 0;
            SET @Mensaje = 'El proveedor no existe';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Insertar producto
        INSERT INTO PRODUCTO_SM (
            COD_PRODUCTO, NOMBRE_PRO, DESCRIPCION, PRECIO_COMPRA, PRECIO_VENTA,
            STOCK_MINIMO, LOTE_PRO, FECHA_VENCIMIENTO, IMAGEN_PATH,
            ID_CATEGORIA, ID_MARCA, ID_ESTADO, COD_PROV
        )
        VALUES (
            @CodigoProducto, @NombreProducto, @Descripcion, @PrecioCompra, @PrecioVenta,
            @StockMinimo, @Lote, @FechaVencimiento, @ImagenPath,
            @IdCategoria, @IdMarca, 1, @CodProveedor
        );

        -- Crear registros en inventario para ambos almacenes
        INSERT INTO INVENTARIO_SM (COD_PRODUCTO, COD_ALMACEN, STOCK_ACTUAL, USUARIO_MODIFICA)
        VALUES (@CodigoProducto, @AlmacenElegido, @StockInicialAlm01, @IdUsuario);

        -- Registrar movimientos iniciales si hay stock
        IF @StockInicialAlm01 > 0
        BEGIN
            INSERT INTO MOVIMIENTO_INVENTARIO (
                COD_PRODUCTO, COD_ALMACEN, TIPO_MOVIMIENTO, CANTIDAD,
                STOCK_ANTERIOR, STOCK_NUEVO, USUARIO_MOVIMIENTO, REFERENCIA, OBSERVACIONES
            )
            VALUES (
                @CodigoProducto, 'ALM01', 'ENTRADA', @StockInicialAlm01,
                0, @StockInicialAlm01, @IdUsuario, 'INICIAL', 'Stock inicial del producto'
            );
        END

        IF @StockInicialAlm02 > 0
        BEGIN
            INSERT INTO MOVIMIENTO_INVENTARIO (
                COD_PRODUCTO, COD_ALMACEN, TIPO_MOVIMIENTO, CANTIDAD,
                STOCK_ANTERIOR, STOCK_NUEVO, USUARIO_MOVIMIENTO, REFERENCIA, OBSERVACIONES
            )
            VALUES (
                @CodigoProducto, 'ALM02', 'ENTRADA', @StockInicialAlm02,
                0, @StockInicialAlm02, @IdUsuario, 'INICIAL', 'Stock inicial del producto'
            );
        END

        COMMIT TRANSACTION;
        SET @Resultado = 1;
        SET @Mensaje = 'Producto creado exitosamente';

    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        SET @Resultado = 0;
        SET @Mensaje = 'Error al crear producto: ' + ERROR_MESSAGE();
    END CATCH
END
go

-- SP para desbloquear usuario (solo administrador)
CREATE PROCEDURE SP_DesbloquearUsuario
    @IdUsuarioAdmin INT,
    @IdUsuarioDesbloquear INT,
    @Resultado INT OUTPUT,
    @Mensaje VARCHAR(200) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    -- Verificar que el usuario que ejecuta sea administrador
    DECLARE @TipoAdmin VARCHAR(20);
    SELECT @TipoAdmin = t.NOMBRE_TIP
    FROM USUARIOS_SM u
    INNER JOIN TIPO_SM t ON u.ID_TIPO = t.ID_TIPO
    WHERE u.ID_USUARIO = @IdUsuarioAdmin;

    IF @TipoAdmin != 'ADMINISTRADOR'
    BEGIN
        SET @Resultado = 0;
        SET @Mensaje = 'Solo los administradores pueden desbloquear usuarios';
        RETURN;
    END

    -- Desbloquear usuario
    UPDATE USUARIOS_SM
    SET ID_ESTADO = 1,
        INTENTOS_FALLIDOS = 0
    WHERE ID_USUARIO = @IdUsuarioDesbloquear;

    SET @Resultado = 1;
    SET @Mensaje = 'Usuario desbloqueado exitosamente';
END
go

CREATE PROCEDURE SP_ListarPersonaPorDni
@Dni VARCHAR(12)
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        SELECT
            p.DNI_SM,
            p.NOMBRE_SM,
            p.APELLIDO_SM,
            p.FECHA_NACIMIENTO,
            p.GENERO,
            p.FECHA_REGISTRO,
            c.NUMERO_TEL,
            c.WHATSAPP,
            c.CORREO,
            d.DIRECCION_COMPLETA,
            d.DEPARTAMENTO_DIR,
            d.PROVINCIA_DIR,
            d.DISTRITO_DIR,
            d.REFERENCIA
        FROM PERSONA_SM p
                 LEFT JOIN CONTACTO_SM c ON p.ID_CONTACTO = c.ID_CONTACTO
                 LEFT JOIN DIRECCION_SM d ON c.ID_DIRECCION = d.ID_DIRECCION
        WHERE p.DNI_SM = @Dni;

    END TRY
    BEGIN CATCH
        DECLARE @ErrorMessage NVARCHAR(4000) = ERROR_MESSAGE();
        RAISERROR (@ErrorMessage, 16, 1);
    END CATCH
END
go

-- Procedimiento para listar todas las personas con su contacto y dirección
CREATE PROCEDURE SP_ListarPersonas
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        -- Selecciona todos los campos relevantes de las tablas unidas
        SELECT
            p.DNI_SM,
            p.NOMBRE_SM,
            p.APELLIDO_SM,
            p.FECHA_NACIMIENTO,
            p.GENERO,
            -- Campos de CONTACTO_SM
            c.NUMERO_TEL,
            c.WHATSAPP,
            c.CORREO,
            -- Campos de DIRECCION_SM
            d.DIRECCION_COMPLETA,
            d.DEPARTAMENTO_DIR,
            d.PROVINCIA_DIR,
            d.DISTRITO_DIR,
            d.REFERENCIA
        FROM
            PERSONA_SM p
                -- LEFT JOIN para incluir personas que no tengan contacto o dirección asociados
                LEFT JOIN CONTACTO_SM c ON p.ID_CONTACTO = c.ID_CONTACTO
                LEFT JOIN DIRECCION_SM d ON c.ID_DIRECCION = d.ID_DIRECCION;

    END TRY
    BEGIN CATCH
        -- Manejo de errores
        DECLARE @ErrorMessage NVARCHAR(4000) = ERROR_MESSAGE();
        DECLARE @ErrorSeverity INT = ERROR_SEVERITY();
        DECLARE @ErrorState INT = ERROR_STATE();

        RAISERROR (@ErrorMessage, @ErrorSeverity, @ErrorState);
    END CATCH

END
go

CREATE PROCEDURE SP_ListarTipoPorId
@IdTipo INT
AS
BEGIN
    SET NOCOUNT ON;
    SELECT ID_TIPO, NOMBRE_TIP, CATEGORIA_TIPO, ACTIVO
    FROM TIPO_SM
    WHERE ID_TIPO = @IdTipo;
END
go

CREATE PROCEDURE SP_ListarTipos
AS
BEGIN
    SET NOCOUNT ON;
    SELECT ID_TIPO, NOMBRE_TIP, CATEGORIA_TIPO, ACTIVO
    FROM TIPO_SM
    ORDER BY NOMBRE_TIP; -- O el orden que desees
END
go

CREATE PROCEDURE SP_ListarTiposActivos
AS
BEGIN
    SET NOCOUNT ON;
    SELECT ID_TIPO, NOMBRE_TIP, CATEGORIA_TIPO, ACTIVO
    FROM TIPO_SM
    WHERE ACTIVO = 1
    ORDER BY NOMBRE_TIP;
END
go

CREATE PROCEDURE SP_ListarUsuarioPorId
@IdUsuario INT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        SELECT
            u.ID_USUARIO,
            u.NOMBRE_US,
            u.CONTRASEÑA_US,
            u.DNI_SM,
            u.FECHA_REGISTRO_US,
            u.ULTIMO_INGRESO,
            u.INTENTOS_FALLIDOS,
            e.DESCRIP_ESTA,
            e.ACTIVO,
            t.NOMBRE_TIP,
            t.CATEGORIA_TIPO,
            p.NOMBRE_SM,
            p.APELLIDO_SM,
            p.FECHA_NACIMIENTO,
            p.GENERO
        FROM USUARIOS_SM u
                 INNER JOIN PERSONA_SM p ON u.DNI_SM = p.DNI_SM
                 INNER JOIN ESTADO_SM e ON u.ID_ESTADO = e.ID_ESTADO
                 INNER JOIN TIPO_SM t ON u.ID_TIPO = t.ID_TIPO
        WHERE u.ID_USUARIO = @IdUsuario;

    END TRY
    BEGIN CATCH
        DECLARE @ErrorMessage NVARCHAR(4000) = ERROR_MESSAGE();
        RAISERROR (@ErrorMessage, 16, 1);
    END CATCH
END
go

-- Procedimiento para listar todos los usuarios con su información de persona, estado y tipo
CREATE PROCEDURE SP_ListarUsuarios
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        -- Selecciona todos los campos relevantes de las tablas unidas
        SELECT
            -- Campos de USUARIOS_SM
            u.ID_USUARIO,
            u.NOMBRE_US,
            u.CONTRASEÑA_US,
            u.DNI_SM,
            u.FECHA_REGISTRO_US,
            u.ULTIMO_INGRESO,
            u.INTENTOS_FALLIDOS,
            -- Campos de ESTADO_SM
            e.DESCRIP_ESTA,
            e.ACTIVO,
            -- Campos de TIPO_SM
            t.NOMBRE_TIP,
            t.CATEGORIA_TIPO,
            t.ACTIVO,
            -- Campos de PERSONA_SM (heredados por Usuario)
            p.NOMBRE_SM,
            p.APELLIDO_SM,
            p.FECHA_NACIMIENTO,
            p.GENERO
        -- NOTA: No incluye contacto aquí, se haría en otro JOIN si es necesario
        -- o se manejaría en una consulta separada si la estructura lo requiere.
        -- Si contacto es parte de Persona, se debe incluir como en SP_ListarPersonas
        -- Suponiendo que ID_CONTACTO en PERSONA_SM apunta a CONTACTO_SM
        -- , c.NUMERO_TEL, c.WHATSAPP, c.CORREO, d.DIRECCION_COMPLETA, ...
        FROM
            USUARIOS_SM u
                INNER JOIN PERSONA_SM p ON u.DNI_SM = p.DNI_SM
                INNER JOIN ESTADO_SM e ON u.ID_ESTADO = e.ID_ESTADO
                INNER JOIN TIPO_SM t ON u.ID_TIPO = t.ID_TIPO
        -- Opcional: LEFT JOIN con CONTACTO_SM y DIRECCION_SM si la info de contacto
        -- está ligada a la persona y quieres incluirla aquí también.
        -- LEFT JOIN CONTACTO_SM c ON p.ID_CONTACTO = c.ID_CONTACTO_SM
        -- LEFT JOIN DIRECCION_SM d ON c.ID_DIRECCION = d.ID_DIRECCION_SM;

    END TRY
    BEGIN CATCH
        -- Manejo de errores
        DECLARE @ErrorMessage NVARCHAR(4000) = ERROR_MESSAGE();
        DECLARE @ErrorSeverity INT = ERROR_SEVERITY();
        DECLARE @ErrorState INT = ERROR_STATE();

        RAISERROR (@ErrorMessage, @ErrorSeverity, @ErrorState);
    END CATCH

END
go

-- SP para modificar usuario (solo administrador)
CREATE PROCEDURE SP_ModificarUsuario
    @IdUsuario INT,
    @NuevoNombreUsuario VARCHAR(30) = NULL,
    @NuevaContrasena VARCHAR(100) = NULL,
    @NuevoIdTipo INT = NULL,
    @NuevoIdEstado INT = NULL,
    @IdUsuarioAdmin INT,
    @Resultado INT OUTPUT,
    @Mensaje VARCHAR(200) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        -- Verificar que el usuario que ejecuta sea administrador
        DECLARE @TipoAdmin VARCHAR(20);
        SELECT @TipoAdmin = t.NOMBRE_TIP
        FROM USUARIOS_SM u
        INNER JOIN TIPO_SM t ON u.ID_TIPO = t.ID_TIPO
        WHERE u.ID_USUARIO = @IdUsuarioAdmin;

        IF @TipoAdmin != 'ADMINISTRADOR'
        BEGIN
            SET @Resultado = 0;
            SET @Mensaje = 'Solo los administradores pueden modificar usuarios';
            RETURN;
        END

        -- Verificar que el usuario a modificar existe
        IF NOT EXISTS (SELECT 1 FROM USUARIOS_SM WHERE ID_USUARIO = @IdUsuario)
        BEGIN
            SET @Resultado = 0;
            SET @Mensaje = 'El usuario no existe';
            RETURN;
        END

        -- Verificar nombre de usuario único si se va a cambiar
        IF @NuevoNombreUsuario IS NOT NULL
        BEGIN
            IF EXISTS (SELECT 1 FROM USUARIOS_SM WHERE NOMBRE_US = @NuevoNombreUsuario AND ID_USUARIO != @IdUsuario)
            BEGIN
                SET @Resultado = 0;
                SET @Mensaje = 'El nombre de usuario ya existe';
                RETURN;
            END
        END

        -- Actualizar campos no nulos
        UPDATE USUARIOS_SM
        SET
            NOMBRE_US = ISNULL(@NuevoNombreUsuario, NOMBRE_US),
            CONTRASEÑA_US = ISNULL(@NuevaContrasena, CONTRASEÑA_US),
            ID_TIPO = ISNULL(@NuevoIdTipo, ID_TIPO),
            ID_ESTADO = ISNULL(@NuevoIdEstado, ID_ESTADO)
        WHERE ID_USUARIO = @IdUsuario;

        SET @Resultado = 1;
        SET @Mensaje = 'Usuario modificado exitosamente';

    END TRY
    BEGIN CATCH
        SET @Resultado = 0;
        SET @Mensaje = 'Error al modificar usuario: ' + ERROR_MESSAGE();
    END CATCH
END
go

-- SP para obtener próximo código disponible
CREATE PROCEDURE SP_ObtenerProximoCodigo
    @TipoCodigo VARCHAR(20), -- 'PRODUCTO', 'CLIENTE', 'FACTURA'
    @ProximoCodigo VARCHAR(20) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @Contador INT;

    IF @TipoCodigo = 'PRODUCTO'
    BEGIN
        SELECT @Contador = ISNULL(MAX(CAST(SUBSTRING(COD_PRODUCTO, 4, 10) AS INT)), 0) + 1
        FROM PRODUCTO_SM
        WHERE COD_PRODUCTO LIKE 'PRO%' AND ISNUMERIC(SUBSTRING(COD_PRODUCTO, 4, 10)) = 1;

        SET @ProximoCodigo = 'PRO' + RIGHT('000000' + CAST(@Contador AS VARCHAR), 6);
    END
    ELSE IF @TipoCodigo = 'CLIENTE'
    BEGIN
        SELECT @Contador = ISNULL(MAX(CAST(SUBSTRING(CODIGO_CLI_SM, 4, 10) AS INT)), 0) + 1
        FROM CLIENTE_SM
        WHERE CODIGO_CLI_SM LIKE 'CLI%' AND ISNUMERIC(SUBSTRING(CODIGO_CLI_SM, 4, 10)) = 1;

        SET @ProximoCodigo = 'CLI' + RIGHT('0000' + CAST(@Contador AS VARCHAR), 4);
    END
    ELSE IF @TipoCodigo = 'FACTURA'
    BEGIN
        SELECT @Contador = ISNULL(MAX(CAST(SUBSTRING(COD_FACTURA, 2, 10) AS INT)), 0) + 1
        FROM VENTA_CABECERA_SM
        WHERE COD_FACTURA LIKE 'V%' AND ISNUMERIC(SUBSTRING(COD_FACTURA, 2, 10)) = 1;

        SET @ProximoCodigo = 'V' + RIGHT('000000' + CAST(@Contador AS VARCHAR), 6);
    END
    ELSE
    BEGIN
        SET @ProximoCodigo = 'ERROR: Tipo de código no válido';
    END
END
go

-- SP para realizar venta completa
CREATE PROCEDURE SP_RealizarVenta
    @IdCliente VARCHAR(20),
    @IdUsuario INT,
    @MetodoPago VARCHAR(20),
    @Observaciones VARCHAR(200) = NULL,
    @DetalleVenta NVARCHAR(MAX), -- JSON con detalles de venta
    @Resultado INT OUTPUT,
    @Mensaje VARCHAR(200) OUTPUT,
    @CodigoFactura VARCHAR(20) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Generar código de factura
        DECLARE @ContadorVentas INT;
        SELECT @ContadorVentas = ISNULL(MAX(CAST(SUBSTRING(COD_FACTURA, 2, 10) AS INT)), 0) + 1
        FROM VENTA_CABECERA_SM
        WHERE COD_FACTURA LIKE 'V%';

        SET @CodigoFactura = 'V' + RIGHT('000000' + CAST(@ContadorVentas AS VARCHAR), 6);

        -- Variables para totales
        DECLARE @Subtotal DECIMAL(10,2) = 0;
        DECLARE @IgvTotal DECIMAL(10,2) = 0;
        DECLARE @DescuentoTotal DECIMAL(10,2) = 0;
        DECLARE @TotalVenta DECIMAL(10,2) = 0;

        -- Crear tabla temporal para procesar detalles
        CREATE TABLE #DetalleTemp (
            CodigoProducto VARCHAR(20),
            Cantidad INT,
            PrecioUnitario DECIMAL(10,2),
            DescuentoUnitario DECIMAL(10,2),
            CodigoAlmacen VARCHAR(10)
        );

        -- Parsear JSON de detalles (simplificado, en producción usar JSON functions)
        -- Por ahora asumimos que @DetalleVenta viene como string separado por |
        -- Formato: COD_PRODUCTO,CANTIDAD,PRECIO,DESCUENTO,ALMACEN|...

        DECLARE @DetalleItem VARCHAR(200);
        DECLARE @Pos INT = 1;

        WHILE @Pos <= LEN(@DetalleVenta)
        BEGIN
            SET @DetalleItem = SUBSTRING(@DetalleVenta, @Pos, CHARINDEX('|', @DetalleVenta + '|', @Pos) - @Pos);

            DECLARE @CodProd VARCHAR(20) = SUBSTRING(@DetalleItem, 1, CHARINDEX(',', @DetalleItem) - 1);
            SET @DetalleItem = SUBSTRING(@DetalleItem, CHARINDEX(',', @DetalleItem) + 1, LEN(@DetalleItem));

            DECLARE @Cant INT = CAST(SUBSTRING(@DetalleItem, 1, CHARINDEX(',', @DetalleItem) - 1) AS INT);
            SET @DetalleItem = SUBSTRING(@DetalleItem, CHARINDEX(',', @DetalleItem) + 1, LEN(@DetalleItem));

            DECLARE @Precio DECIMAL(10,2) = CAST(SUBSTRING(@DetalleItem, 1, CHARINDEX(',', @DetalleItem) - 1) AS DECIMAL(10,2));
            SET @DetalleItem = SUBSTRING(@DetalleItem, CHARINDEX(',', @DetalleItem) + 1, LEN(@DetalleItem));

            DECLARE @Desc DECIMAL(10,2) = CAST(SUBSTRING(@DetalleItem, 1, CHARINDEX(',', @DetalleItem) - 1) AS DECIMAL(10,2));
            DECLARE @Almacen VARCHAR(10) = SUBSTRING(@DetalleItem, CHARINDEX(',', @DetalleItem) + 1, LEN(@DetalleItem));

            -- Verificar stock disponible
            DECLARE @StockDisponible INT;
            SELECT @StockDisponible = STOCK_ACTUAL
            FROM INVENTARIO_SM
            WHERE COD_PRODUCTO = @CodProd AND COD_ALMACEN = @Almacen;

            IF @StockDisponible < @Cant
            BEGIN
                SET @Resultado = 0;
                SET @Mensaje = 'Stock insuficiente para producto ' + @CodProd + ' en almacén ' + @Almacen;
                ROLLBACK TRANSACTION;
                RETURN;
            END

            -- Insertar en tabla temporal
            INSERT INTO #DetalleTemp VALUES (@CodProd, @Cant, @Precio, @Desc, @Almacen);

            SET @Pos = CHARINDEX('|', @DetalleVenta, @Pos) + 1;
            IF @Pos = 1 BREAK;
        END

        -- Insertar cabecera (inicialmente con totales en 0)
        INSERT INTO VENTA_CABECERA_SM (
            COD_FACTURA, ID_CLIENTE, ID_USUARIO, SUBTOTAL, IGV_TOTAL,
            DESCUENTO_TOTAL, TOTAL_VENTA, METODO_PAGO, OBSERVACIONES
        )
        VALUES (
            @CodigoFactura, @IdCliente, @IdUsuario, 0, 0, 0, 0, @MetodoPago, @Observaciones
        );

        -- Procesar cada detalle
        DECLARE detalle_cursor CURSOR FOR
        SELECT CodigoProducto, Cantidad, PrecioUnitario, DescuentoUnitario, CodigoAlmacen
        FROM #DetalleTemp;

        OPEN detalle_cursor;

        DECLARE @CodProducto VARCHAR(20), @Cantidad INT, @PrecioUnit DECIMAL(10,2),
                @DescuentoUnit DECIMAL(10,2), @CodAlmacen VARCHAR(10);

        FETCH NEXT FROM detalle_cursor INTO @CodProducto, @Cantidad, @PrecioUnit, @DescuentoUnit, @CodAlmacen;

        WHILE @@FETCH_STATUS = 0
        BEGIN
            DECLARE @SubtotalDetalle DECIMAL(10,2) = (@Cantidad * @PrecioUnit) - (@Cantidad * @DescuentoUnit);

            -- Insertar detalle
            INSERT INTO VENTA_DETALLE_SM (
                COD_FACTURA, COD_PRODUCTO, CANTIDAD, PRECIO_UNITARIO,
                DESCUENTO_UNITARIO, SUBTOTAL_DETALLE, COD_ALMACEN
            )
            VALUES (
                @CodigoFactura, @CodProducto, @Cantidad, @PrecioUnit,
                @DescuentoUnit, @SubtotalDetalle, @CodAlmacen
            );

            -- Actualizar stock
            EXEC SP_ActualizarStock
                @CodigoProducto = @CodProducto,
                @CodigoAlmacen = @CodAlmacen,
                @TipoMovimiento = 'SALIDA',
                @Cantidad = @Cantidad,
                @IdUsuario = @IdUsuario,
                @Referencia = @CodigoFactura,
                @Observaciones = 'Venta',
                @Resultado = @Resultado OUTPUT,
                @Mensaje = @Mensaje OUTPUT;

            IF @Resultado = 0
            BEGIN
                ROLLBACK TRANSACTION;
                RETURN;
            END

            -- Acumular totales
            SET @Subtotal = @Subtotal + @SubtotalDetalle;
            SET @DescuentoTotal = @DescuentoTotal + (@Cantidad * @DescuentoUnit);

            FETCH NEXT FROM detalle_cursor INTO @CodProducto, @Cantidad, @PrecioUnit, @DescuentoUnit, @CodAlmacen;
        END

        CLOSE detalle_cursor;
        DEALLOCATE detalle_cursor;

        -- Calcular IGV (18%)
        SET @IgvTotal = @Subtotal * 0.18;
        SET @TotalVenta = @Subtotal + @IgvTotal;

        -- Actualizar totales en cabecera
        UPDATE VENTA_CABECERA_SM
        SET SUBTOTAL = @Subtotal,
            IGV_TOTAL = @IgvTotal,
            DESCUENTO_TOTAL = @DescuentoTotal,
            TOTAL_VENTA = @TotalVenta
        WHERE COD_FACTURA = @CodigoFactura;

        DROP TABLE #DetalleTemp;
        COMMIT TRANSACTION;

        SET @Resultado = 1;
        SET @Mensaje = 'Venta realizada exitosamente';

    END TRY
    BEGIN CATCH
        IF CURSOR_STATUS('global', 'detalle_cursor') >= 0
        BEGIN
            CLOSE detalle_cursor;
            DEALLOCATE detalle_cursor;
        END

        IF OBJECT_ID('tempdb..#DetalleTemp') IS NOT NULL
            DROP TABLE #DetalleTemp;

        ROLLBACK TRANSACTION;
        SET @Resultado = 0;
        SET @Mensaje = 'Error al realizar venta: ' + ERROR_MESSAGE();
    END CATCH
END
go

-- SP para registrar cliente
CREATE PROCEDURE SP_RegistrarCliente
    @Dni VARCHAR(12),
    @TipoCliente VARCHAR(20) = 'REGULAR',
    @CodigoCliente VARCHAR(20) = NULL,
    @DescuentoEspecial DECIMAL(5,2) = 0,
    @Resultado INT OUTPUT,
    @Mensaje VARCHAR(200) OUTPUT,
    @IdCliente VARCHAR(20) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        -- Verificar que el DNI existe en PERSONA_SM
        IF NOT EXISTS (SELECT 1 FROM PERSONA_SM WHERE DNI_SM = @Dni)
        BEGIN
            SET @Resultado = 0;
            SET @Mensaje = 'El DNI no está registrado como persona';
            RETURN;
        END

        -- Verificar que no sea ya cliente
        IF EXISTS (SELECT 1 FROM CLIENTE_SM WHERE DNI_SM = @Dni)
        BEGIN
            SET @Resultado = 0;
            SET @Mensaje = 'Esta persona ya está registrada como cliente';
            RETURN;
        END

        -- Generar ID de cliente automático si no se proporciona
        IF @CodigoCliente IS NULL
        BEGIN
            DECLARE @ContadorClientes INT;
            SELECT @ContadorClientes = ISNULL(MAX(CAST(SUBSTRING(CODIGO_CLI_SM, 4, 10) AS INT)), 0) + 1
            FROM CLIENTE_SM
            WHERE CODIGO_CLI_SM LIKE 'CLI%';

            SET @CodigoCliente = 'CLI' + RIGHT('0000' + CAST(@ContadorClientes AS VARCHAR), 4);
        END

        -- Generar ID de cliente
        SET @IdCliente = 'C' + REPLACE(@Dni, ' ', '') + FORMAT(GETDATE(), 'yyyyMM');

        -- Obtener ID de tipo cliente
        DECLARE @IdTipoCliente INT;
        SELECT @IdTipoCliente = ID_TIPO
        FROM TIPO_SM
        WHERE NOMBRE_TIP = @TipoCliente AND CATEGORIA_TIPO = 'CLIENTE';

        -- Insertar cliente
        INSERT INTO CLIENTE_SM (ID_CLIENTE, DNI_SM, CODIGO_CLI_SM, TIPO_CLIENTE_SM, ID_TIPO, ID_ESTADO, DESCUENTO_ESPECIAL)
        VALUES (@IdCliente, @Dni, @CodigoCliente, @TipoCliente, @IdTipoCliente, 1, @DescuentoEspecial);

        SET @Resultado = 1;
        SET @Mensaje = 'Cliente registrado exitosamente';

    END TRY
    BEGIN CATCH
        SET @Resultado = 0;
        SET @Mensaje = 'Error al registrar cliente: ' + ERROR_MESSAGE();
    END CATCH
END
go

-- SP para registrar persona con validación de DNI único
CREATE PROCEDURE SP_RegistrarPersona
    @Dni VARCHAR(12),
    @Nombre VARCHAR(60),
    @Apellido VARCHAR(60),
    @FechaNacimiento DATE = NULL,
    @Genero CHAR(1) = NULL,
    @Telefono VARCHAR(15) = NULL,
    @Whatsapp VARCHAR(15) = NULL,
    @Correo VARCHAR(50) = NULL,
    @DireccionCompleta VARCHAR(200) = NULL,
    @Departamento VARCHAR(30) = NULL,
    @Provincia VARCHAR(30) = NULL,
    @Distrito VARCHAR(40) = NULL,
    @Referencia VARCHAR(100) = NULL,
    @Resultado INT OUTPUT,
    @Mensaje VARCHAR(200) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRANSACTION;

        -- Validar que el DNI no exista
        IF EXISTS (SELECT 1 FROM PERSONA_SM WHERE DNI_SM = @Dni)
        BEGIN
            SET @Resultado = 0;
            SET @Mensaje = 'El DNI ya está registrado';
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Variables para IDs
        DECLARE @IdDireccion INT = NULL;
        DECLARE @IdContacto INT = NULL;

        -- Insertar dirección si se proporciona
        IF @DireccionCompleta IS NOT NULL
        BEGIN
            INSERT INTO DIRECCION_SM (DIRECCION_COMPLETA, DEPARTAMENTO_DIR, PROVINCIA_DIR, DISTRITO_DIR, REFERENCIA)
            VALUES (@DireccionCompleta, @Departamento, @Provincia, @Distrito, @Referencia);
            SET @IdDireccion = SCOPE_IDENTITY();
        END

        -- Insertar contacto si se proporciona información
        IF @Telefono IS NOT NULL OR @Correo IS NOT NULL OR @Whatsapp IS NOT NULL
        BEGIN
            INSERT INTO CONTACTO_SM (NUMERO_TEL, WHATSAPP, CORREO, ID_DIRECCION)
            VALUES (@Telefono, @Whatsapp, @Correo, @IdDireccion);
            SET @IdContacto = SCOPE_IDENTITY();
        END

        -- Insertar persona
        INSERT INTO PERSONA_SM (DNI_SM, NOMBRE_SM, APELLIDO_SM, FECHA_NACIMIENTO, GENERO, ID_CONTACTO)
        VALUES (@Dni, @Nombre, @Apellido, @FechaNacimiento, @Genero, @IdContacto);

        COMMIT TRANSACTION;
        SET @Resultado = 1;
        SET @Mensaje = 'Persona registrada exitosamente';

    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        SET @Resultado = 0;
        SET @Mensaje = 'Error al registrar persona: ' + ERROR_MESSAGE();
    END CATCH
END
go

-- SP para registrar proveedor
CREATE PROCEDURE SP_RegistrarProveedor
    @RazonSocial VARCHAR(100),
    @Ruc VARCHAR(11),
    @DniContacto VARCHAR(12) = NULL,
    @Banco VARCHAR(50) = NULL,
    @NumeroCuenta VARCHAR(50) = NULL,
    @IdTipo INT,
    @Resultado INT OUTPUT,
    @Mensaje VARCHAR(200) OUTPUT,
    @CodProveedor INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        -- Verificar que el RUC no exista
        IF EXISTS (SELECT 1 FROM PROVEEDOR_SM WHERE RUC_PROV = @Ruc)
        BEGIN
            SET @Resultado = 0;
            SET @Mensaje = 'El RUC ya está registrado';
            RETURN;
        END

        -- Verificar DNI de contacto si se proporciona
        IF @DniContacto IS NOT NULL AND NOT EXISTS (SELECT 1 FROM PERSONA_SM WHERE DNI_SM = @DniContacto)
        BEGIN
            SET @Resultado = 0;
            SET @Mensaje = 'El DNI de contacto no está registrado como persona';
            RETURN;
        END

        -- Insertar proveedor
        INSERT INTO PROVEEDOR_SM (RAZON_SOCIAL, RUC_PROV, DNI_CONTACTO, BANCO_PROV, NUMERO_CUENTA, ID_ESTADO, ID_TIPO)
        VALUES (@RazonSocial, @Ruc, @DniContacto, @Banco, @NumeroCuenta, 1, @IdTipo);

        SET @CodProveedor = SCOPE_IDENTITY();
        SET @Resultado = 1;
        SET @Mensaje = 'Proveedor registrado exitosamente';

    END TRY
    BEGIN CATCH
        SET @Resultado = 0;
        SET @Mensaje = 'Error al registrar proveedor: ' + ERROR_MESSAGE();
    END CATCH
END
go

-- SP para registrar usuario (un DNI = un usuario)
CREATE PROCEDURE SP_RegistrarUsuario
    @NombreUsuario VARCHAR(30),
    @Contrasena VARCHAR(100),
    @Dni VARCHAR(12),
    @IdTipo INT,
    @IdUsuarioAdmin INT,
    @Resultado INT OUTPUT,
    @Mensaje VARCHAR(200) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        -- Verificar que el usuario que ejecuta sea administrador
        DECLARE @TipoAdmin VARCHAR(20);
        SELECT @TipoAdmin = t.NOMBRE_TIP
        FROM USUARIOS_SM u
        INNER JOIN TIPO_SM t ON u.ID_TIPO = t.ID_TIPO
        WHERE u.ID_USUARIO = @IdUsuarioAdmin;

        IF @TipoAdmin != 'ADMINISTRADOR'
        BEGIN
            SET @Resultado = 0;
            SET @Mensaje = 'Solo los administradores pueden crear usuarios';
            RETURN;
        END

        -- Verificar que el DNI existe en PERSONA_SM
        IF NOT EXISTS (SELECT 1 FROM PERSONA_SM WHERE DNI_SM = @Dni)
        BEGIN
            SET @Resultado = 0;
            SET @Mensaje = 'El DNI no está registrado como persona';
            RETURN;
        END

        -- Verificar que el DNI no tenga ya un usuario
        IF EXISTS (SELECT 1 FROM USUARIOS_SM WHERE DNI_SM = @Dni)
        BEGIN
            SET @Resultado = 0;
            SET @Mensaje = 'Esta persona ya tiene un usuario asignado';
            RETURN;
        END

        -- Verificar que el nombre de usuario no exista
        IF EXISTS (SELECT 1 FROM USUARIOS_SM WHERE NOMBRE_US = @NombreUsuario)
        BEGIN
            SET @Resultado = 0;
            SET @Mensaje = 'El nombre de usuario ya existe';
            RETURN;
        END

        -- Insertar usuario
        INSERT INTO USUARIOS_SM (NOMBRE_US, CONTRASEÑA_US, DNI_SM, ID_ESTADO, ID_TIPO)
        VALUES (@NombreUsuario, @Contrasena, @Dni, 1, @IdTipo);

        SET @Resultado = 1;
        SET @Mensaje = 'Usuario registrado exitosamente';

    END TRY
    BEGIN CATCH
        SET @Resultado = 0;
        SET @Mensaje = 'Error al registrar usuario: ' + ERROR_MESSAGE();
    END CATCH
END
go

-- SP para reporte de productos próximos a vencer
CREATE PROCEDURE SP_ReporteProductosProximosVencer
    @DiasAnticipacion INT = 30
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        p.COD_PRODUCTO,
        p.NOMBRE_PRO,
        p.LOTE_PRO,
        p.FECHA_VENCIMIENTO,
        DATEDIFF(DAY, GETDATE(), p.FECHA_VENCIMIENTO) as DIAS_RESTANTES,
        SUM(i.STOCK_ACTUAL) as STOCK_TOTAL,
        p.PRECIO_VENTA,
        (SUM(i.STOCK_ACTUAL) * p.PRECIO_VENTA) as VALOR_INVENTARIO
    FROM PRODUCTO_SM p
    INNER JOIN INVENTARIO_SM i ON p.COD_PRODUCTO = i.COD_PRODUCTO
    WHERE p.FECHA_VENCIMIENTO IS NOT NULL
    AND p.FECHA_VENCIMIENTO <= DATEADD(DAY, @DiasAnticipacion, GETDATE())
    AND p.ID_ESTADO = 1
    GROUP BY p.COD_PRODUCTO, p.NOMBRE_PRO, p.LOTE_PRO, p.FECHA_VENCIMIENTO, p.PRECIO_VENTA
    HAVING SUM(i.STOCK_ACTUAL) > 0
    ORDER BY p.FECHA_VENCIMIENTO ASC;
END
go

-- SP para reporte de productos con stock bajo
CREATE PROCEDURE SP_ReporteStockBajo
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        p.COD_PRODUCTO,
        p.NOMBRE_PRO,
        c.NOMBRE_CAT as CATEGORIA,
        p.STOCK_MINIMO,
        SUM(i.STOCK_ACTUAL) as STOCK_TOTAL,
        SUM(CASE WHEN i.COD_ALMACEN = 'ALM251' THEN i.STOCK_ACTUAL ELSE 0 END) as STOCK_ALM01,
        SUM(CASE WHEN i.COD_ALMACEN = 'ALM252' THEN i.STOCK_ACTUAL ELSE 0 END) as STOCK_ALM02,
        p.PRECIO_VENTA,
        pr.RAZON_SOCIAL as PROVEEDOR
    FROM PRODUCTO_SM p
    INNER JOIN CATEGORIA_SM c ON p.ID_CATEGORIA = c.ID_CATEGORIA
    INNER JOIN PROVEEDOR_SM pr ON p.COD_PROV = pr.COD_PROV
    INNER JOIN INVENTARIO_SM i ON p.COD_PRODUCTO = i.COD_PRODUCTO
    WHERE p.ID_ESTADO = 1 -- Solo activos
    GROUP BY p.COD_PRODUCTO, p.NOMBRE_PRO, c.NOMBRE_CAT, p.STOCK_MINIMO,
             p.PRECIO_VENTA, pr.RAZON_SOCIAL
    HAVING SUM(i.STOCK_ACTUAL) <= p.STOCK_MINIMO
    ORDER BY SUM(i.STOCK_ACTUAL) ASC;
END
go

-- SP para reporte de ventas por vendedor
CREATE PROCEDURE SP_ReporteVentasPorVendedor
    @FechaInicio DATE,
    @FechaFin DATE
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        u.ID_USUARIO,
        u.NOMBRE_US as VENDEDOR,
        CONCAT(p.NOMBRE_SM, ' ', p.APELLIDO_SM) as NOMBRE_COMPLETO,
        COUNT(v.COD_FACTURA) as CANTIDAD_VENTAS,
        SUM(v.TOTAL_VENTA) as TOTAL_VENDIDO,
        AVG(v.TOTAL_VENTA) as PROMEDIO_POR_VENTA,
        SUM(vd.CANTIDAD) as TOTAL_PRODUCTOS_VENDIDOS
    FROM USUARIOS_SM u
    INNER JOIN PERSONA_SM p ON u.DNI_SM = p.DNI_SM
    LEFT JOIN VENTA_CABECERA_SM v ON u.ID_USUARIO = v.ID_USUARIO
        AND CAST(v.FECHA_VENTA AS DATE) BETWEEN @FechaInicio AND @FechaFin
    LEFT JOIN VENTA_DETALLE_SM vd ON v.COD_FACTURA = vd.COD_FACTURA
    WHERE u.ID_ESTADO = 1 -- Solo usuarios activos
    GROUP BY u.ID_USUARIO, u.NOMBRE_US, p.NOMBRE_SM, p.APELLIDO_SM
    ORDER BY SUM(v.TOTAL_VENTA) DESC;
END
go

-- SP para validar disponibilidad de stock antes de venta
CREATE PROCEDURE SP_ValidarDisponibilidadStock
    @CodigoProducto VARCHAR(20),
    @CantidadRequerida INT,
    @CodigoAlmacen VARCHAR(10) = NULL,
    @Disponible BIT OUTPUT,
    @StockDisponible INT OUTPUT,
    @Mensaje VARCHAR(200) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    IF @CodigoAlmacen IS NOT NULL
    BEGIN
        -- Verificar en almacén específico
        SELECT @StockDisponible = STOCK_ACTUAL
        FROM INVENTARIO_SM
        WHERE COD_PRODUCTO = @CodigoProducto AND COD_ALMACEN = @CodigoAlmacen;

        IF @StockDisponible IS NULL
        BEGIN
            SET @Disponible = 0;
            SET @StockDisponible = 0;
            SET @Mensaje = 'Producto no encontrado en el almacén especificado';
            RETURN;
        END
    END
    ELSE
    BEGIN
        -- Verificar stock total en todos los almacenes
        SELECT @StockDisponible = SUM(STOCK_ACTUAL)
        FROM INVENTARIO_SM
        WHERE COD_PRODUCTO = @CodigoProducto;

        IF @StockDisponible IS NULL
        BEGIN
            SET @Disponible = 0;
            SET @StockDisponible = 0;
            SET @Mensaje = 'Producto no encontrado en inventario';
            RETURN;
        END
    END

    IF @StockDisponible >= @CantidadRequerida
    BEGIN
        SET @Disponible = 1;
        SET @Mensaje = 'Stock disponible';
    END
    ELSE
    BEGIN
        SET @Disponible = 0;
        SET @Mensaje = 'Stock insuficiente. Disponible: ' + CAST(@StockDisponible AS VARCHAR) +
                      ', Requerido: ' + CAST(@CantidadRequerida AS VARCHAR);
    END
END
go

CREATE PROCEDURE SP_ValidarLogin
    @NombreUsuario VARCHAR(30),
    @Contrasena VARCHAR(100),
    @Resultado INT OUTPUT,
    @Mensaje VARCHAR(200) OUTPUT,
    @IdUsuario INT OUTPUT,
    @TipoUsuario VARCHAR(20) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @IdEstadoActivo INT = 1;
    DECLARE @IdEstadoBloqueado INT = 2;
    DECLARE @MaxIntentos INT = 3;
    DECLARE @TiempoBloqueo INT = 30; -- minutos

    -- Verificar si el usuario existe
    IF NOT EXISTS (SELECT 1 FROM USUARIOS_SM WHERE NOMBRE_US = @NombreUsuario)
    BEGIN
        SET @Resultado = 0;
        SET @Mensaje = 'Usuario no existe';
        RETURN;
    END

    -- Obtener información del usuario
    SELECT
        @IdUsuario = u.ID_USUARIO,
        @TipoUsuario = t.NOMBRE_TIP
    FROM USUARIOS_SM u
    INNER JOIN TIPO_SM t ON u.ID_TIPO = t.ID_TIPO
    WHERE u.NOMBRE_US = @NombreUsuario;

    -- Verificar si está bloqueado y si ya pasó el tiempo
    DECLARE @EstadoUsuario INT, @UltimoIntento DATETIME, @IntentosFallidos INT;

    SELECT
        @EstadoUsuario = ID_ESTADO,
        @UltimoIntento = ULTIMO_INGRESO,
        @IntentosFallidos = INTENTOS_FALLIDOS
    FROM USUARIOS_SM
    WHERE ID_USUARIO = @IdUsuario;

    -- Si está bloqueado, verificar si ya pasaron 30 minutos
    IF @EstadoUsuario = @IdEstadoBloqueado
    BEGIN
        IF DATEDIFF(MINUTE, @UltimoIntento, GETDATE()) >= @TiempoBloqueo
        BEGIN
            -- Desbloquear usuario
            UPDATE USUARIOS_SM
            SET ID_ESTADO = @IdEstadoActivo,
                INTENTOS_FALLIDOS = 0
            WHERE ID_USUARIO = @IdUsuario;
        END
        ELSE
        BEGIN
            SET @Resultado = 0;
            SET @Mensaje = 'Usuario bloqueado. Espere ' +
                          CAST(@TiempoBloqueo - DATEDIFF(MINUTE, @UltimoIntento, GETDATE()) AS VARCHAR) +
                           N' minutos más';
            RETURN;
        END
    END

    -- Validar contraseña
    IF EXISTS (SELECT 1 FROM USUARIOS_SM WHERE ID_USUARIO = @IdUsuario AND CONTRASEÑA_US = @Contrasena)
    BEGIN
        -- Login exitoso
        UPDATE USUARIOS_SM
        SET ULTIMO_INGRESO = GETDATE(),
            INTENTOS_FALLIDOS = 0
        WHERE ID_USUARIO = @IdUsuario;

        SET @Resultado = 1;
        SET @Mensaje = 'Login exitoso';
    END
    ELSE
    BEGIN
        -- Incrementar intentos fallidos
        UPDATE USUARIOS_SM
        SET INTENTOS_FALLIDOS = INTENTOS_FALLIDOS + 1,
            ULTIMO_INGRESO = GETDATE()
        WHERE ID_USUARIO = @IdUsuario;

        -- Verificar si debe bloquearse
        IF @IntentosFallidos + 1 >= @MaxIntentos
        BEGIN
            UPDATE USUARIOS_SM
            SET ID_ESTADO = @IdEstadoBloqueado
            WHERE ID_USUARIO = @IdUsuario;

            SET @Mensaje = 'Usuario bloqueado por múltiples intentos fallidos';
        END
        ELSE
        BEGIN
            SET @Mensaje = 'Contraseña incorrecta. Intento ' +
                          CAST(@IntentosFallidos + 1 AS VARCHAR) + ' de ' +
                          CAST(@MaxIntentos AS VARCHAR);
        END

        SET @Resultado = 0;
    END
END
go

CREATE PROCEDURE sp_BuscarProductoPorCriterio
    @Criterio VARCHAR(50),
    @Valor VARCHAR(100)
AS
BEGIN
    SET NOCOUNT ON;

    IF @Criterio = 'codigo'
        BEGIN
            SELECT
                p.COD_PRODUCTO as id,
                p.COD_PRODUCTO as codigo,
                p.NOMBRE_PRO as nombre,
                p.DESCRIPCION as descripcion,
                p.PRECIO_COMPRA as precio_compra,
                p.PRECIO_VENTA as precio_venta,
                p.FECHA_REGISTRO as fecha_registro,
                p.FECHA_REGISTRO as fecha_modificacion,
                c.ID_CATEGORIA as categoria_id,
                c.NOMBRE_CAT as categoria_nombre,
                m.ID_MARCA as marca_id,
                m.NOMBRE_MAR as marca_nombre,
                p.ID_ESTADO as estado_id,
                e.DESCRIP_ESTA as estado_nombre,
                'UNIDAD' as unidad_medida,
                p.STOCK_MINIMO as stock_minimo,
                p.COD_PRODUCTO as codigo_barras,
                '' as observaciones,
                0 as es_pesable
            FROM PRODUCTO_SM p
                     INNER JOIN CATEGORIA_SM c ON p.ID_CATEGORIA = c.ID_CATEGORIA
                     INNER JOIN MARCA_SM m ON p.ID_MARCA = m.ID_MARCA
                     INNER JOIN ESTADO_SM e ON p.ID_ESTADO = e.ID_ESTADO
            WHERE p.COD_PRODUCTO = @Valor;
        END
    ELSE IF @Criterio = 'nombre'
        BEGIN
            SELECT
                p.COD_PRODUCTO as id,
                p.COD_PRODUCTO as codigo,
                p.NOMBRE_PRO as nombre,
                p.DESCRIPCION as descripcion,
                p.PRECIO_COMPRA as precio_compra,
                p.PRECIO_VENTA as precio_venta,
                p.FECHA_REGISTRO as fecha_registro,
                p.FECHA_REGISTRO as fecha_modificacion,
                c.ID_CATEGORIA as categoria_id,
                c.NOMBRE_CAT as categoria_nombre,
                m.ID_MARCA as marca_id,
                m.NOMBRE_MAR as marca_nombre,
                p.ID_ESTADO as estado_id,
                e.DESCRIP_ESTA as estado_nombre,
                'UNIDAD' as unidad_medida,
                p.STOCK_MINIMO as stock_minimo,
                p.COD_PRODUCTO as codigo_barras,
                '' as observaciones,
                0 as es_pesable
            FROM PRODUCTO_SM p
                     INNER JOIN CATEGORIA_SM c ON p.ID_CATEGORIA = c.ID_CATEGORIA
                     INNER JOIN MARCA_SM m ON p.ID_MARCA = m.ID_MARCA
                     INNER JOIN ESTADO_SM e ON p.ID_ESTADO = e.ID_ESTADO
            WHERE p.NOMBRE_PRO LIKE '%' + @Valor + '%';
        END
    ELSE IF @Criterio = 'categoria'
        BEGIN
            SELECT
                p.COD_PRODUCTO as id,
                p.COD_PRODUCTO as codigo,
                p.NOMBRE_PRO as nombre,
                p.DESCRIPCION as descripcion,
                p.PRECIO_COMPRA as precio_compra,
                p.PRECIO_VENTA as precio_venta,
                p.FECHA_REGISTRO as fecha_registro,
                p.FECHA_REGISTRO as fecha_modificacion,
                c.ID_CATEGORIA as categoria_id,
                c.NOMBRE_CAT as categoria_nombre,
                m.ID_MARCA as marca_id,
                m.NOMBRE_MAR as marca_nombre,
                p.ID_ESTADO as estado_id,
                e.DESCRIP_ESTA as estado_nombre,
                'UNIDAD' as unidad_medida,
                p.STOCK_MINIMO as stock_minimo,
                p.COD_PRODUCTO as codigo_barras,
                '' as observaciones,
                0 as es_pesable
            FROM PRODUCTO_SM p
                     INNER JOIN CATEGORIA_SM c ON p.ID_CATEGORIA = c.ID_CATEGORIA
                     INNER JOIN MARCA_SM m ON p.ID_MARCA = m.ID_MARCA
                     INNER JOIN ESTADO_SM e ON p.ID_ESTADO = e.ID_ESTADO
            WHERE p.ID_CATEGORIA = CAST(@Valor AS INT);
        END
    ELSE IF @Criterio = 'marca'
        BEGIN
            SELECT
                p.COD_PRODUCTO as id,
                p.COD_PRODUCTO as codigo,
                p.NOMBRE_PRO as nombre,
                p.DESCRIPCION as descripcion,
                p.PRECIO_COMPRA as precio_compra,
                p.PRECIO_VENTA as precio_venta,
                p.FECHA_REGISTRO as fecha_registro,
                p.FECHA_REGISTRO as fecha_modificacion,
                c.ID_CATEGORIA as categoria_id,
                c.NOMBRE_CAT as categoria_nombre,
                m.ID_MARCA as marca_id,
                m.NOMBRE_MAR as marca_nombre,
                p.ID_ESTADO as estado_id,
                e.DESCRIP_ESTA as estado_nombre,
                'UNIDAD' as unidad_medida,
                p.STOCK_MINIMO as stock_minimo,
                p.COD_PRODUCTO as codigo_barras,
                '' as observaciones,
                0 as es_pesable
            FROM PRODUCTO_SM p
                     INNER JOIN CATEGORIA_SM c ON p.ID_CATEGORIA = c.ID_CATEGORIA
                     INNER JOIN MARCA_SM m ON p.ID_MARCA = m.ID_MARCA
                     INNER JOIN ESTADO_SM e ON p.ID_ESTADO = e.ID_ESTADO
            WHERE p.ID_MARCA = CAST(@Valor AS INT);
        END
    ELSE IF @Criterio = 'estado'
        BEGIN
            SELECT
                p.COD_PRODUCTO as id,
                p.COD_PRODUCTO as codigo,
                p.NOMBRE_PRO as nombre,
                p.DESCRIPCION as descripcion,
                p.PRECIO_COMPRA as precio_compra,
                p.PRECIO_VENTA as precio_venta,
                p.FECHA_REGISTRO as fecha_registro,
                p.FECHA_REGISTRO as fecha_modificacion,
                c.ID_CATEGORIA as categoria_id,
                c.NOMBRE_CAT as categoria_nombre,
                m.ID_MARCA as marca_id,
                m.NOMBRE_MAR as marca_nombre,
                p.ID_ESTADO as estado_id,
                e.DESCRIP_ESTA as estado_nombre,
                'UNIDAD' as unidad_medida,
                p.STOCK_MINIMO as stock_minimo,
                p.COD_PRODUCTO as codigo_barras,
                '' as observaciones,
                0 as es_pesable
            FROM PRODUCTO_SM p
                     INNER JOIN CATEGORIA_SM c ON p.ID_CATEGORIA = c.ID_CATEGORIA
                     INNER JOIN MARCA_SM m ON p.ID_MARCA = m.ID_MARCA
                     INNER JOIN ESTADO_SM e ON p.ID_ESTADO = e.ID_ESTADO
            WHERE p.ID_ESTADO = CAST(@Valor AS INT);
        END
END
go

CREATE PROCEDURE sp_BuscarUsuarioPorCriterio
    @Criterio VARCHAR(50),
    @Valor VARCHAR(100)
AS
BEGIN
    SET NOCOUNT ON;

    IF @Criterio = 'dni'
        BEGIN
            SELECT
                u.ID_USUARIO as id,
                u.DNI_SM as dni,
                p.NOMBRE_SM as nombre,
                p.APELLIDO_SM as apellido,
                u.NOMBRE_US as nombre_usuario,
                u.CONTRASEÑA_US as contrasena,
                u.FECHA_REGISTRO_US as fecha_registro,
                u.FECHA_REGISTRO_US as fecha_modificacion,
                u.ID_ESTADO as estado_id,
                e.DESCRIP_ESTA as estado_nombre,
                p.FECHA_NACIMIENTO as fecha_nacimiento,
                co.NUMERO_TEL as telefono,
                co.CORREO as email,
                u.ULTIMO_INGRESO as ultimo_acceso,
                u.INTENTOS_FALLIDOS as intentos_fallidos,
                u.ID_TIPO as perfil_id,
                t.NOMBRE_TIP as perfil_nombre
            FROM USUARIOS_SM u
                     INNER JOIN PERSONA_SM p ON u.DNI_SM = p.DNI_SM
                     INNER JOIN ESTADO_SM e ON u.ID_ESTADO = e.ID_ESTADO
                     INNER JOIN TIPO_SM t ON u.ID_TIPO = t.ID_TIPO
                     LEFT JOIN CONTACTO_SM co ON p.ID_CONTACTO = co.ID_CONTACTO
            WHERE u.DNI_SM = @Valor;
        END
    ELSE IF @Criterio = 'nombre'
        BEGIN
            SELECT
                u.ID_USUARIO as id,
                u.DNI_SM as dni,
                p.NOMBRE_SM as nombre,
                p.APELLIDO_SM as apellido,
                u.NOMBRE_US as nombre_usuario,
                u.CONTRASEÑA_US as contrasena,
                u.FECHA_REGISTRO_US as fecha_registro,
                u.FECHA_REGISTRO_US as fecha_modificacion,
                u.ID_ESTADO as estado_id,
                e.DESCRIP_ESTA as estado_nombre,
                p.FECHA_NACIMIENTO as fecha_nacimiento,
                co.NUMERO_TEL as telefono,
                co.CORREO as email,
                u.ULTIMO_INGRESO as ultimo_acceso,
                u.INTENTOS_FALLIDOS as intentos_fallidos,
                u.ID_TIPO as perfil_id,
                t.NOMBRE_TIP as perfil_nombre
            FROM USUARIOS_SM u
                     INNER JOIN PERSONA_SM p ON u.DNI_SM = p.DNI_SM
                     INNER JOIN ESTADO_SM e ON u.ID_ESTADO = e.ID_ESTADO
                     INNER JOIN TIPO_SM t ON u.ID_TIPO = t.ID_TIPO
                     LEFT JOIN CONTACTO_SM co ON p.ID_CONTACTO = co.ID_CONTACTO
            WHERE p.NOMBRE_SM LIKE '%' + @Valor + '%';
        END
    ELSE IF @Criterio = 'apellido'
        BEGIN
            SELECT
                u.ID_USUARIO as id,
                u.DNI_SM as dni,
                p.NOMBRE_SM as nombre,
                p.APELLIDO_SM as apellido,
                u.NOMBRE_US as nombre_usuario,
                u.CONTRASEÑA_US as contrasena,
                u.FECHA_REGISTRO_US as fecha_registro,
                u.FECHA_REGISTRO_US as fecha_modificacion,
                u.ID_ESTADO as estado_id,
                e.DESCRIP_ESTA as estado_nombre,
                p.FECHA_NACIMIENTO as fecha_nacimiento,
                co.NUMERO_TEL as telefono,
                co.CORREO as email,
                u.ULTIMO_INGRESO as ultimo_acceso,
                u.INTENTOS_FALLIDOS as intentos_fallidos,
                u.ID_TIPO as perfil_id,
                t.NOMBRE_TIP as perfil_nombre
            FROM USUARIOS_SM u
                     INNER JOIN PERSONA_SM p ON u.DNI_SM = p.DNI_SM
                     INNER JOIN ESTADO_SM e ON u.ID_ESTADO = e.ID_ESTADO
                     INNER JOIN TIPO_SM t ON u.ID_TIPO = t.ID_TIPO
                     LEFT JOIN CONTACTO_SM co ON p.ID_CONTACTO = co.ID_CONTACTO
            WHERE p.APELLIDO_SM LIKE '%' + @Valor + '%';
        END
    ELSE IF @Criterio = 'nombreusuario'
        BEGIN
            SELECT
                u.ID_USUARIO as id,
                u.DNI_SM as dni,
                p.NOMBRE_SM as nombre,
                p.APELLIDO_SM as apellido,
                u.NOMBRE_US as nombre_usuario,
                u.CONTRASEÑA_US as contrasena,
                u.FECHA_REGISTRO_US as fecha_registro,
                u.FECHA_REGISTRO_US as fecha_modificacion,
                u.ID_ESTADO as estado_id,
                e.DESCRIP_ESTA as estado_nombre,
                p.FECHA_NACIMIENTO as fecha_nacimiento,
                co.NUMERO_TEL as telefono,
                co.CORREO as email,
                u.ULTIMO_INGRESO as ultimo_acceso,
                u.INTENTOS_FALLIDOS as intentos_fallidos,
                u.ID_TIPO as perfil_id,
                t.NOMBRE_TIP as perfil_nombre
            FROM USUARIOS_SM u
                     INNER JOIN PERSONA_SM p ON u.DNI_SM = p.DNI_SM
                     INNER JOIN ESTADO_SM e ON u.ID_ESTADO = e.ID_ESTADO
                     INNER JOIN TIPO_SM t ON u.ID_TIPO = t.ID_TIPO
                     LEFT JOIN CONTACTO_SM co ON p.ID_CONTACTO = co.ID_CONTACTO
            WHERE u.NOMBRE_US LIKE '%' + @Valor + '%';
        END
    ELSE IF @Criterio = 'email'
        BEGIN
            SELECT
                u.ID_USUARIO as id,
                u.DNI_SM as dni,
                p.NOMBRE_SM as nombre,
                p.APELLIDO_SM as apellido,
                u.NOMBRE_US as nombre_usuario,
                u.CONTRASEÑA_US as contrasena,
                u.FECHA_REGISTRO_US as fecha_registro,
                u.FECHA_REGISTRO_US as fecha_modificacion,
                u.ID_ESTADO as estado_id,
                e.DESCRIP_ESTA as estado_nombre,
                p.FECHA_NACIMIENTO as fecha_nacimiento,
                co.NUMERO_TEL as telefono,
                co.CORREO as email,
                u.ULTIMO_INGRESO as ultimo_acceso,
                u.INTENTOS_FALLIDOS as intentos_fallidos,
                u.ID_TIPO as perfil_id,
                t.NOMBRE_TIP as perfil_nombre
            FROM USUARIOS_SM u
                     INNER JOIN PERSONA_SM p ON u.DNI_SM = p.DNI_SM
                     INNER JOIN ESTADO_SM e ON u.ID_ESTADO = e.ID_ESTADO
                     INNER JOIN TIPO_SM t ON u.ID_TIPO = t.ID_TIPO
                     LEFT JOIN CONTACTO_SM co ON p.ID_CONTACTO = co.ID_CONTACTO
            WHERE co.CORREO LIKE '%' + @Valor + '%';
        END
    ELSE IF @Criterio = 'estado'
        BEGIN
            SELECT
                u.ID_USUARIO as id,
                u.DNI_SM as dni,
                p.NOMBRE_SM as nombre,
                p.APELLIDO_SM as apellido,
                u.NOMBRE_US as nombre_usuario,
                u.CONTRASEÑA_US as contrasena,
                u.FECHA_REGISTRO_US as fecha_registro,
                u.FECHA_REGISTRO_US as fecha_modificacion,
                u.ID_ESTADO as estado_id,
                e.DESCRIP_ESTA as estado_nombre,
                p.FECHA_NACIMIENTO as fecha_nacimiento,
                co.NUMERO_TEL as telefono,
                co.CORREO as email,
                u.ULTIMO_INGRESO as ultimo_acceso,
                u.INTENTOS_FALLIDOS as intentos_fallidos,
                u.ID_TIPO as perfil_id,
                t.NOMBRE_TIP as perfil_nombre
            FROM USUARIOS_SM u
                     INNER JOIN PERSONA_SM p ON u.DNI_SM = p.DNI_SM
                     INNER JOIN ESTADO_SM e ON u.ID_ESTADO = e.ID_ESTADO
                     INNER JOIN TIPO_SM t ON u.ID_TIPO = t.ID_TIPO
                     LEFT JOIN CONTACTO_SM co ON p.ID_CONTACTO = co.ID_CONTACTO
            WHERE u.ID_ESTADO = CAST(@Valor AS INT);
        END
END
go


