-- ----------------------------------------------------------------------------
-- Ofertas Model
-------------------------------------------------------------------------------

-- Indexes for primary keys have been explicitly created.

-- ---------- Table for validation queries from the connection pool -----------

DROP TABLE PingTable;
CREATE TABLE PingTable (foo CHAR(1));

-- -----------------------------------------------------------------------------
-- Drop tables. NOTE: before dropping a table (when re-executing the script),
-- the tables having columns acting as foreign keys of the table to be dropped,
-- must be dropped first (otherwise, the corresponding checks on those tables
-- could not be done).

DROP TABLE Reserva;
DROP TABLE Oferta;

-- --------------------------------- Oferta ------------------------------------
CREATE TABLE Oferta ( ofertaId BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(255) COLLATE latin1_bin NOT NULL,
    descripcion VARCHAR(1024) COLLATE latin1_bin NOT NULL,
    iniReserva TIMESTAMP DEFAULT 0 NOT NULL,
    limReserva TIMESTAMP DEFAULT 0 NOT NULL,
    limOferta TIMESTAMP DEFAULT 0 NOT NULL,
    precioReal FLOAT NOT NULL,
    precioRebajado FLOAT NOT NULL,
	maxPersonas BIGINT,
    estado VARCHAR(15) COLLATE latin1_bin NOT NULL,
    numReservas BIGINT NOT NULL,
    numUsedReservas BIGINT NOT NULL,
    CONSTRAINT OfertaPK PRIMARY KEY(ofertaId) ) ENGINE = InnoDB;

CREATE INDEX OfertaIndexByOfertaId ON Oferta (ofertaId);
CREATE INDEX OfertaIndexByTitle ON Oferta (titulo);

-- --------------------------------- Reserva ------------------------------------

CREATE TABLE Reserva ( reservaId BIGINT NOT NULL AUTO_INCREMENT,
    ofertaId BIGINT NOT NULL,
    emailUsuario VARCHAR(40) COLLATE latin1_bin NOT NULL,
    numeroTarjeta VARCHAR(16),
    estado VARCHAR(15) COLLATE latin1_bin NOT NULL,
    fechaReserva TIMESTAMP DEFAULT 0 NOT NULL,
    CONSTRAINT ReservaPK PRIMARY KEY(ReservaId),
    CONSTRAINT ReservaOfertaIdFK FOREIGN KEY(ofertaId)
        REFERENCES Oferta(ofertaId) ON DELETE CASCADE ) ENGINE = InnoDB;

CREATE INDEX ReservaIndexByReservaId ON Reserva (reservaId);