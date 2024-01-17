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
VALUES (1, 'Subcategory1-1', NOW(), NOW()),
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
VALUES (1, 3, 'Product1',  'P001', '/product-img/default-product-img.png',  100000, 'COMMON', 'NORMAL', NOW(), NOW()),
       (2, 3, 'Product2',  'P002', '/product-img/default-product-img.png',  150000, 'MALE',   'NORMAL', NOW(), NOW()),
       (3, 3, 'Product3',  'P003', '/product-img/default-product-img.png',  200000, 'FEMALE', 'NORMAL', NOW(), NOW()),
       (4, 4, 'Product4',  'P004', '/product-img/default-product-img.png',  120000, 'COMMON', 'NORMAL', NOW(), NOW()),
       (5, 4, 'Product5',  'P005', '/product-img/default-product-img.png',  180000, 'MALE',   'NORMAL', NOW(), NOW()),
       (1, 4, 'Product6',  'P006', '/product-img/default-product-img.png',  130000, 'FEMALE', 'NORMAL', NOW(), NOW()),
       (2, 5, 'Product7',  'P007', '/product-img/default-product-img.png',  90000,  'COMMON', 'NORMAL', NOW(), NOW()),
       (3, 5, 'Product8',  'P008', '/product-img/default-product-img.png',  160000, 'MALE',   'NORMAL', NOW(), NOW()),
       (4, 6, 'Product9',  'P009', '/product-img/default-product-img.png',  220000, 'FEMALE', 'NORMAL', NOW(), NOW()),
       (5, 6, 'Product10', 'P010', '/product-img/default-product-img.png',  110000, 'COMMON', 'NORMAL', NOW(), NOW()),
       (5, 6, 'AuctionProduct',  'P011', '/product-img/default-product-img.png',  2000000, 'COMMON', 'AUCTION', NOW(), NOW());

INSERT INTO product_stock (product_id, product_size_id, quantity)
VALUES (1, 1, 50), (2, 2, 30), (3, 1, 40), (4, 4, 25), (5, 3, 35),
       (6, 4, 20), (7, 5, 45), (8, 6, 60), (9, 7, 15), (10, 8, 55),
       (1, 2, 25), (2, 1, 40), (3, 2, 55), (4, 3, 30), (5, 4, 45),
       (6, 3, 20), (7, 6, 35), (8, 5, 50), (9, 8, 25), (10, 7, 40),
       (11, 7, 10);

INSERT INTO describe_image (product_id, img_url)
VALUES (1,  '/product-img/default-product-img.png'),
       (2,  '/product-img/default-product-img.png'),
       (3,  '/product-img/default-product-img.png'),
       (4,  '/product-img/default-product-img.png'),
       (5,  '/product-img/default-product-img.png'),
       (6,  '/product-img/default-product-img.png'),
       (7,  '/product-img/default-product-img.png'),
       (8,  '/product-img/default-product-img.png'),
       (9,  '/product-img/default-product-img.png'),
       (10, '/product-img/default-product-img.png'),
       (11, '/product-img/default-product-img.png');

INSERT INTO review_aggregate (product_id, avg_rating, review_count)
VALUES (1, 3, 1), (2, 1.5, 6),
       (3, 4, 4), (4, 2.5, 2),
       (5, 5, 3), (6, 3.5, 1),
       (7, 2, 4), (8, 4.5, 3),
       (9, 1, 5), (10, 0.5, 8),
       (11, 0, 0);