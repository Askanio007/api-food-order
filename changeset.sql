CREATE EXTENSION pgcrypto;

INSERT INTO role(id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO role(id, name) VALUES (2, 'ROLE_USER');
INSERT INTO role(id, name) VALUES (3, 'ROLE_MANAGER');
INSERT INTO role(id, name) VALUES (4, 'ROLE_COOK');

INSERT INTO food_type(type) VALUES ('SALAD');
INSERT INTO food_type(type) VALUES ('OTHER');
INSERT INTO food_type(type) VALUES ('SOUP');
INSERT INTO food_type(type) VALUES ('HOT');
INSERT INTO food_type(type) VALUES ('DRINK');
INSERT INTO food_type(type) VALUES ('BREAD');
INSERT INTO food_type(type) VALUES ('DESSERT');

INSERT INTO user_fo(enabled, login, password, role_id) VALUES (true, 'cook', crypt('123456', gen_salt('bf')), 4);
INSERT INTO user_fo(enabled, login, password, role_id) VALUES (true, 'manager', crypt('123456', gen_salt('bf')), 3);


INSERT INTO transaction_status(id, status) VALUES (1, 'SEND_TO_MM');
INSERT INTO transaction_status(id, status) VALUES (2, 'COMPLETE');
INSERT INTO transaction_status(id, status) VALUES (3, 'PAYMENT_ORDER');

INSERT INTO user_fo(enabled, login, password, role_id, balance) VALUES (true, 'admin', crypt('123456', gen_salt('bf')), 1, 0.00);

INSERT INTO setting(name, value) VALUES ('city', 'Тула');
INSERT INTO setting(name, value) VALUES ('street', 'Николая Руднева');
INSERT INTO setting(name, value) VALUES ('house', '28а');
INSERT INTO setting(name, value) VALUES ('floor', '3');
INSERT INTO setting(name, value) VALUES ('flat', '31');
INSERT INTO setting(name, value) VALUES ('phone', '9207471983');
INSERT INTO setting(name, value) VALUES ('api', 'http://91.193.237.45:2000/FastOperator.asmx/AddOrder');
INSERT INTO setting(name, value) VALUES ('timeAttempt', '10');


UPDATE user_fo SET balance=0.00;
UPDATE food SET active=true;
UPDATE food SET available_every_day=true WHERE type_id = 25;
UPDATE order_user SET accept=true;
UPDATE order_user SET date_delivered_order=date;

delete all

DELETE FROM menu_foods;
DELETE FROM menu;
DELETE FROM order_foods;
DELETE FROM order_user;
DELETE FROM transaction;
UPDATE user_fo SET balance=0.00 WHERE role_id = 1;

