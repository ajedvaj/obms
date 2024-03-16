-- data definition
CREATE TABLE users (
    id       BIGINT          AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled  BOOLEAN      NOT NULL
);

CREATE TABLE authorities (
    id        BIGINT         AUTO_INCREMENT PRIMARY KEY,
    username  VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users (username)
);
CREATE UNIQUE INDEX ix_auth_username ON authorities (username, authority);

-- data seed

-- admin:admin
INSERT INTO users(username, password, enabled) VALUES ('admin', '$2a$10$fbLYgWqjWqvOQRwNr9DO.eGR7vNHuH8smz/bH6szqwg6F6KOp7c4u', 1);
-- user:user
INSERT INTO users(username, password, enabled) VALUES ('user', '$2a$12$rNsVoabf677GRyasxzQuDOCNRD5zo39Ep6blZ8DwBIA9JK2cuXYxK', 1);

INSERT INTO authorities(username, authority) VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO authorities(username, authority) VALUES ('user', 'ROLE_USER');
