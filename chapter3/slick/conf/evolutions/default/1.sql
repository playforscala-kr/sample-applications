# Users schema
 
# --- !Ups
 
CREATE TABLE products (
    ean bigint(20) NOT NULL,
    name varchar(255) NOT NULL,
    description varchar(255) NOT NULL
);

insert into products (ean, name, description) values (5010255079763, 'Paperclips Large', 'Large Plain Pack of 1000');
insert into products (ean, name, description) values (5018206244666, 'Giant Paperclips', 'Giant Plain 51mm 100 pack');
insert into products (ean, name, description) values (5018306332812, 'Paperclip Giant Plain', 'Giant Plain Pack of 10000');
insert into products (ean, name, description) values (5018306312913, 'No Tear Paper Clip', 'No Tear Extra Large Pack of 1000');
insert into products (ean, name, description) values (5018206244611, 'Zebra Paperclips', 'Zebra Length 28mm Assorted 150 Pack');

# --- !Downs
 
DROP TABLE User;

