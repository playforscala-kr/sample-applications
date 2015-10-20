# --- !Ups

create sequence s_product_id;
create sequence s_warehouses_id;
create sequence s_stockitems_id;

CREATE TABLE products (
    id long DEFAULT nextval('s_product_id'),
    ean long,
    name varchar,
    description varchar);
    
CREATE TABLE warehouses (
    id long DEFAULT nextval('s_warehouses_id'),
    name varchar);
    
CREATE TABLE stock_items (
    id long DEFAULT nextval('s_stockitems_id'),
    productId long,
    warehouseId long,
    quantity long);
    
# --- !Downs
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS warehouses;
DROP TABLE IF EXISTS stock_items;
DROP SEQUENCE IF EXISTS s_product_id;
DROP SEQUENCE IF EXISTS s_warehouses_id;
DROP SEQUENCE IF EXISTS s_stockitems_id;