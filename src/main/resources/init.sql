CREATE TABLE userr(
                      user_id serial primary key ,
                      email varchar unique NOT NULL,
                      username varchar NOT NULL,
                      password varchar NOT NULL,
                      enabled int DEFAULT NULL
);

CREATE TABLE role (
                      role_id serial primary key ,
                      name varchar NOT NULL
);

CREATE TABLE users_roles (
                             user_id integer REFERENCES userr (user_id),
                             role_id integer REFERENCES role (role_id)
);

INSERT INTO role (name) VALUES ('USER');
INSERT INTO role (name) VALUES ('APP_ROLE');
INSERT INTO role (name) VALUES ('API_ROLE');
INSERT INTO role (name) VALUES ('ADMIN');

INSERT INTO userr (username, password, enabled, email) VALUES ('patrick', '$2a$10$cTUErxQqYVyU2qmQGIktpup5chLEdhD2zpzNEyYqmxrHHJbSNDOG.', '1', '1@mail.ru');
INSERT INTO userr (username, password, enabled, email) VALUES ('alex', '$2a$10$.tP2OH3dEG0zms7vek4ated5AiQ.EGkncii0OpCcGq4bckS9NOULu', '1', '2@mail.ru');
INSERT INTO userr (username, password, enabled, email) VALUES ('john', '$2a$10$E2UPv7arXmp3q0LzVzCBNeb4B4AtbTAGjkefVDnSztOwE7Gix6kea', '1', '3@mail.ru');
INSERT INTO userr (username, password, enabled, email) VALUES ('namhm', '$2a$10$GQT8bfLMaLYwlyUysnGwDu6HMB5G.tin5MKT/uduv2Nez0.DmhnOq', '1', '4@mail.ru');

INSERT INTO users_roles (user_id, role_id) VALUES (1, 1); -- user patrick has role USER
INSERT INTO users_roles (user_id, role_id) VALUES (2, 2); -- user alex has role ROLE_APP
INSERT INTO users_roles (user_id, role_id) VALUES (3, 3); -- user john has role ROLE_API
INSERT INTO users_roles (user_id, role_id) VALUES (4, 4); -- user namhm has role ADMIN
