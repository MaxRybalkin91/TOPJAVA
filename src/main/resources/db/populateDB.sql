DELETE
FROM user_roles;
DELETE
FROM meals;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_USER', 100000),
       ('ROLE_ADMIN', 100001);

INSERT INTO meals (id, user_id, date_time, description, calories)
VALUES (100000, 100000, '2015-05-30 10:00:00', 'breakfast-user', 1000),
       (99990, 100000, '2015-05-30 13:00:00', 'lunch-user', 1000),
       (99991, 100001, '2015-05-30 10:00:00', 'breakfast-admin', 1000),
       (99992, 100001, '2015-05-30 13:00:00', 'lunch-admin', 1000)