INSERT INTO brand (name, created_at, updated_at)
VALUES ('Brand1', NOW(), NOW()),
       ('Brand2', NOW(), NOW()),
       ('Brand3', NOW(), NOW()),
       ('Brand4', NOW(), NOW()),
       ('Brand5', NOW(), NOW());

INSERT INTO category (master_category_id, name, created_at, updated_at)
VALUES (NULL, 'RootCategory1', NOW(), NOW()),
       (NULL, 'RootCategory2', NOW(), NOW());

INSERT INTO category (master_category_id, name, created_at, updated_at)
VALUES (1, 'SubCategory1-1', NOW(), NOW()),
       (1, 'Subcategory1-2', NOW(), NOW()),
       (2, 'Subcategory2-1', NOW(), NOW()),
       (2, 'Subcategory2-2', NOW(), NOW());

INSERT INTO product_size (category_id, name, created_at, updated_at)
VALUES (3, 'Size1', NOW(), NOW()),
       (3, 'Size2', NOW(), NOW()),
       (4, 'Size1', NOW(), NOW()),
       (4, 'Size2', NOW(), NOW()),
       (5, 'Size1', NOW(), NOW()),
       (5, 'Size2', NOW(), NOW()),
       (6, 'Size1', NOW(), NOW()),
       (6, 'Size2', NOW(), NOW());

INSERT INTO product (brand_id, category_id, name, code, img_url, price, gender, type, created_at, updated_at)
VALUES (1, 3, 'Product1',  'P001', 'https://via.placeholder.com/400',  100000, 'COMMON', 'NORMAL', NOW(), NOW()),
       (2, 3, 'Product2',  'P002', 'https://via.placeholder.com/400',  150000, 'MALE',   'NORMAL', NOW(), NOW()),
       (3, 3, 'Product3',  'P003', 'https://via.placeholder.com/400',  200000, 'FEMALE', 'NORMAL', NOW(), NOW()),
       (4, 4, 'Product4',  'P004', 'https://via.placeholder.com/400',  120000, 'COMMON', 'NORMAL', NOW(), NOW()),
       (5, 4, 'Product5',  'P005', 'https://via.placeholder.com/400',  180000, 'MALE',   'NORMAL', NOW(), NOW()),
       (1, 4, 'Product6',  'P006', 'https://via.placeholder.com/400',  130000, 'FEMALE', 'NORMAL', NOW(), NOW()),
       (2, 5, 'Product7',  'P007', 'https://via.placeholder.com/400',  90000,  'COMMON', 'NORMAL', NOW(), NOW()),
       (3, 5, 'Product8',  'P008', 'https://via.placeholder.com/400',  160000, 'MALE',   'NORMAL', NOW(), NOW()),
       (4, 6, 'Product9',  'P009', 'https://via.placeholder.com/400',  220000, 'FEMALE', 'NORMAL', NOW(), NOW()),
       (5, 6, 'Product10', 'P010', 'https://via.placeholder.com/400',  110000, 'COMMON', 'NORMAL', NOW(), NOW());

INSERT INTO product_stock (product_id, product_size_id, quantity)
VALUES (1, 1, 50), (2, 2, 30), (3, 3, 40), (4, 4, 25), (5, 5, 35),
       (6, 6, 20), (7, 7, 45), (8, 8, 60), (9, 1, 15), (10, 2, 55),
       (1, 5, 25), (2, 6, 40), (3, 7, 55), (4, 8, 30), (5, 1, 45),
       (6, 2, 20), (7, 3, 35), (8, 4, 50), (9, 5, 25), (10, 6, 40);

INSERT INTO describe_image (product_id, img_url)
VALUES (1, 'https://via.placeholder.com/500'),
       (2, 'https://via.placeholder.com/500'),
       (3, 'https://via.placeholder.com/500'),
       (4, 'https://via.placeholder.com/500'),
       (5, 'https://via.placeholder.com/500'),
       (6, 'https://via.placeholder.com/500'),
       (7, 'https://via.placeholder.com/500'),
       (8, 'https://via.placeholder.com/500'),
       (9, 'https://via.placeholder.com/500'),
       (10, 'https://via.placeholder.com/500');

INSERT INTO review_aggregate (product_id, avg_rating, review_count)
VALUES (1, 0, 0), (2, 0, 0),
       (3, 0, 0), (4, 0, 0),
       (5, 0, 0), (6, 0, 0),
       (7, 0, 0), (8, 0, 0),
       (9, 0, 0), (10, 0, 0);