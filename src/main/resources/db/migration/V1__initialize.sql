drop table if exists documents cascade;

create table documents (
    id      bigserial,
    title    varchar(255),
    path    varchar(255),
    data    bytea,
--    unique  (title),
    primary key (id)
);

--insert into documents (title) values
--    ('sample.txt'),
--    ('sample2.txt'),
--    ('audio.mp4');

drop table IF exists users;
create table users (
  id                    bigserial,
  login                 VARCHAR(30) NOT NULL,
  password              VARCHAR(80),
  email                 VARCHAR(50),
  first_name            VARCHAR(50),
  last_name             VARCHAR(50),
  UNIQUE (login, email),
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS roles;
CREATE TABLE roles (
  id                    serial,
  title                  VARCHAR(50) NOT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS users_roles;
CREATE TABLE users_roles (
  user_id               INT NOT NULL,
  role_id               INT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id)
  REFERENCES users (id),
  FOREIGN KEY (role_id)
  REFERENCES roles (id)
);

DROP TABLE IF EXISTS users_documents;
CREATE TABLE users_documents (
  user_id               INT NOT NULL,
  document_id           INT NOT NULL,
  PRIMARY KEY (user_id, document_id),
  FOREIGN KEY (user_id)
  REFERENCES users (id),
  FOREIGN KEY (document_id)
  REFERENCES documents (id)
);

INSERT INTO roles (title)
VALUES
('ROLE_CUSTOMER'), ('ROLE_MANAGER'), ('ROLE_ADMIN'), ('ROLE_USER');

INSERT INTO users (login, password, first_name, last_name, email)
VALUES
('11111111','$2y$10$QIh8y336wTyILMURfN6QE.oQp2sGQ.KLsPULlbylkBmOHpVAG277a','Admin','Admin','derden2000@mail.ru'),
('22222222','$2y$10$knh2hFHVHAeHs4VwLBkkk.MfWAEDyb7UrmJKixcMdE4gaZ/iQirC2','Manager','Manager','222@222.ru'),
('Anton.Shu','$2y$12$UQSZzm5MHp6IFMMggq.cTexxn/RqBQ6IJng32xmBeBwb1sPN4c3V.','User','User','333@333.ru');

INSERT INTO users_roles (user_id, role_id)
VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 1),
(2, 2),
(3, 4);
