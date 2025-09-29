CREATE TABLE vehicles (
    id BINARY(16) PRIMARY KEY DEFAULT (UNHEX(REPLACE(UUID(), '-', ''))),
    veiculo VARCHAR(100) NOT NULL,
    brand_id BINARY(16) NOT NULL,
    ano INT NOT NULL,
    descricao TEXT NOT NULL,
    cor VARCHAR(50),
    vendido BOOLEAN NOT NULL DEFAULT FALSE,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_vehicles_brand FOREIGN KEY (brand_id) REFERENCES brands(id)
);

CREATE INDEX idx_vehicles_brand_id ON vehicles(brand_id);
CREATE INDEX idx_vehicles_ano ON vehicles(ano);
CREATE INDEX idx_vehicles_vendido ON vehicles(vendido);
CREATE INDEX idx_vehicles_created ON vehicles(created);
CREATE INDEX idx_vehicles_cor ON vehicles(cor);