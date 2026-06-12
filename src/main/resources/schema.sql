-- ============================================================
-- Egg Management System - Database Schema (MySQL)
-- ============================================================

CREATE TABLE IF NOT EXISTS suppliers (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100)  NOT NULL,
    contact    VARCHAR(50)   DEFAULT NULL,
    phone      VARCHAR(20)   DEFAULT NULL,
    address    VARCHAR(255)  DEFAULT NULL,
    notes      TEXT          DEFAULT NULL,
    created_at DATETIME      DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_supplier_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 
ALTER TABLE purchases ADD COLUMN qr_image TEXT DEFAULT NULL;
ALTER TABLE purchases MODIFY COLUMN qr_image LONGTEXT DEFAULT NULL;
ALTER TABLE purchases ADD COLUMN payment_status VARCHAR(20) DEFAULT 'unpaid';
UPDATE purchases SET payment_status = 'unpaid' WHERE payment_status IS NULL;

CREATE TABLE IF NOT EXISTS customers (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100)  NOT NULL,
    contact    VARCHAR(50)   DEFAULT NULL,
    phone      VARCHAR(20)   DEFAULT NULL,
    address    VARCHAR(255)  DEFAULT NULL,
    notes      TEXT          DEFAULT NULL,
    created_at DATETIME      DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_customer_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS egg_qualities (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL UNIQUE,
    unit        VARCHAR(20)  DEFAULT NULL,
    description VARCHAR(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS purchases (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_id  BIGINT       NOT NULL,
    purchase_date DATE        NOT NULL,
    total_amount DECIMAL(12,2) DEFAULT 0.00,
    fuel_cost    DECIMAL(10,2) DEFAULT 0.00,
    crate_cost   DECIMAL(10,2) DEFAULT 0.00,
    bag_cost     DECIMAL(10,2) DEFAULT 0.00,
    other_cost   DECIMAL(10,2) DEFAULT 0.00,
    notes        TEXT         DEFAULT NULL,
    status       VARCHAR(20)  DEFAULT 'pending',
    created_at   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_purchase_supplier (supplier_id),
    INDEX idx_purchase_date (purchase_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS purchase_items (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    purchase_id BIGINT       NOT NULL,
    quality_id  BIGINT       NOT NULL,
    quantity    DECIMAL(12,2) DEFAULT 0.00,
    unit_price  DECIMAL(10,2) DEFAULT 0.00,
    subtotal    DECIMAL(12,2) DEFAULT 0.00,
    INDEX idx_purchase_item_purchase (purchase_id),
    INDEX idx_purchase_item_quality (quality_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sales (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id  BIGINT       NOT NULL,
    sale_date    DATE         NOT NULL,
    total_amount DECIMAL(12,2) DEFAULT 0.00,
    notes        TEXT         DEFAULT NULL,
    status       VARCHAR(20)  DEFAULT 'pending',
    created_at   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_sale_customer (customer_id),
    INDEX idx_sale_date (sale_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sale_items (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    sale_id     BIGINT       NOT NULL,
    quality_id  BIGINT       NOT NULL,
    quantity    DECIMAL(12,2) DEFAULT 0.00,
    unit_price  DECIMAL(10,2) DEFAULT 0.00,
    subtotal    DECIMAL(12,2) DEFAULT 0.00,
    INDEX idx_sale_item_sale (sale_id),
    INDEX idx_sale_item_quality (quality_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS payments (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_id  BIGINT       DEFAULT NULL,
    purchase_id  BIGINT       DEFAULT NULL,
    amount       DECIMAL(12,2) NOT NULL,
    payment_date DATE         NOT NULL,
    method       VARCHAR(20)  DEFAULT NULL,
    status       VARCHAR(20)  DEFAULT 'pending',
    qr_image     VARCHAR(255) DEFAULT NULL,
    notes        TEXT         DEFAULT NULL,
    created_at   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_payment_supplier (supplier_id),
    INDEX idx_payment_purchase (purchase_id),
    INDEX idx_payment_date (payment_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS receipts (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id  BIGINT       DEFAULT NULL,
    sale_id      BIGINT       DEFAULT NULL,
    amount       DECIMAL(12,2) NOT NULL,
    receipt_date DATE         NOT NULL,
    status       VARCHAR(20)  DEFAULT 'pending',
    notes        TEXT         DEFAULT NULL,
    created_at   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_receipt_customer (customer_id),
    INDEX idx_receipt_sale (sale_id),
    INDEX idx_receipt_date (receipt_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS cash_flow (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    flow_date    DATE         NOT NULL,
    type         VARCHAR(10)  NOT NULL COMMENT 'IN / OUT',
    amount       DECIMAL(12,2) NOT NULL,
    category     VARCHAR(50)  DEFAULT NULL,
    ref_type     VARCHAR(30)  DEFAULT NULL,
    ref_id       BIGINT       DEFAULT NULL,
    description  VARCHAR(255) DEFAULT NULL,
    balance_after DECIMAL(14,2) DEFAULT NULL,
    created_at   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_cash_flow_date (flow_date),
    INDEX idx_cash_flow_type (type),
    INDEX idx_cash_flow_ref (ref_type, ref_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 
 CREATE TABLE IF NOT EXISTS costs (
     id          BIGINT AUTO_INCREMENT PRIMARY KEY,
     cost_type   VARCHAR(20)  NOT NULL COMMENT 'fuel / crate / bag / other',
     amount      DECIMAL(10,2) NOT NULL,
     cost_date   DATE         NOT NULL,
     notes       TEXT         DEFAULT NULL,
     created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP,
     INDEX idx_cost_type (cost_type),
     INDEX idx_cost_date (cost_date)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE sales ADD COLUMN receipt_status VARCHAR(20) DEFAULT 'unreceived';
UPDATE sales SET receipt_status = 'unreceived' WHERE receipt_status IS NULL;
