INSERT INTO brand (name, created_at, updated_at)
VALUES ('Brand1', NOW(), NOW()),
       ('Brand2', NOW(), NOW()),
       ('Brand3', NOW(), NOW()),
       ('Brand4', NOW(), NOW()),
       ('Brand5', NOW(), NOW());

INSERT INTO category (master_category_id, name, created_at, updated_at)
VALUES (NULL, 'RootCategory1', NOW(), NOW()),
       (NULL, 'RootCategory2', NOW(), NOW()),
       (NULL, 'RootCategory3', NOW(), NOW());

INSERT INTO category (master_category_id, name, created_at, updated_at)
VALUES (1, 'SubCategory1-1asdasdasdasdad', NOW(), NOW()),
       (1, 'Subcategory1-2', NOW(), NOW()),
       (2, 'Subcategory2-1', NOW(), NOW()),
       (2, 'Subcategory2-2', NOW(), NOW()),
       (3, 'Subcategory3-1', NOW(), NOW()),
       (3, 'Subcategory3-2', NOW(), NOW());

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
VALUES (1, 3, 'Product1testestestestestsetestsetset',  'P001', '/product-img/7ca72775-fa3c-49ef-8e62-34d1e2b67f30.avif',  100000, 'COMMON', 'NORMAL', NOW(), NOW()),
       (2, 3, 'Product2testestestestestsetestsetset',  'P002', '/product-img/deec2081-a85a-4cb5-871e-9dc26b37f775.jpg',  150000, 'MALE',   'NORMAL', NOW(), NOW()),
       (3, 3, 'Product3',  'P003', '/product-img/7ca72775-fa3c-49ef-8e62-34d1e2b67f30.avif',  200000, 'FEMALE', 'NORMAL', NOW(), NOW()),
       (4, 4, 'Product4',  'P004', '/product-img/deec2081-a85a-4cb5-871e-9dc26b37f775.jpg',  120000, 'COMMON', 'NORMAL', NOW(), NOW()),
       (5, 4, 'Product5',  'P005', '/product-img/7ca72775-fa3c-49ef-8e62-34d1e2b67f30.avif',  180000, 'MALE',   'NORMAL', NOW(), NOW()),
       (1, 4, 'Product6',  'P006', '/product-img/deec2081-a85a-4cb5-871e-9dc26b37f775.jpg',  130000, 'FEMALE', 'NORMAL', NOW(), NOW()),
       (2, 5, 'Product7',  'P007', '/product-img/deec2081-a85a-4cb5-871e-9dc26b37f775.jpg',  90000,  'COMMON', 'NORMAL', NOW(), NOW()),
       (3, 5, 'Product8',  'P008', '/product-img/deec2081-a85a-4cb5-871e-9dc26b37f775.jpg',  160000, 'MALE',   'NORMAL', NOW(), NOW()),
       (4, 6, 'Product9',  'P009', '/product-img/deec2081-a85a-4cb5-871e-9dc26b37f775.jpg',  220000, 'FEMALE', 'NORMAL', NOW(), NOW()),
       (5, 6, 'Product10', 'P010', '/product-img/deec2081-a85a-4cb5-871e-9dc26b37f775.jpg',  110000, 'COMMON', 'NORMAL', NOW(), NOW());
>>>>>>> Stashed changes

INSERT INTO product_stock (product_id, product_size_id, quantity)
VALUES (1, 1, 50), (2, 2, 30), (3, 1, 40), (4, 4, 25), (5, 3, 35),
       (6, 4, 20), (7, 5, 45), (8, 6, 60), (9, 7, 15), (10, 8, 55),
       (1, 2, 25), (2, 1, 40), (3, 2, 55), (4, 3, 30), (5, 4, 45),
       (6, 3, 20), (7, 6, 35), (8, 5, 50), (9, 8, 25), (10, 7, 40),
       (11, 7, 10);

INSERT INTO describe_image (product_id, img_url)
VALUES (1,  '/product-img/7ca72775-fa3c-49ef-8e62-34d1e2b67f30.avif'),
       (2,  '/product-img/deec2081-a85a-4cb5-871e-9dc26b37f775.jpg'),
       (3,  '/product-img/7ca72775-fa3c-49ef-8e62-34d1e2b67f30.avif'),
       (4,  '/product-img/deec2081-a85a-4cb5-871e-9dc26b37f775.jpg'),
       (5,  '/product-img/7ca72775-fa3c-49ef-8e62-34d1e2b67f30.avif'),
       (6,  '/product-img/deec2081-a85a-4cb5-871e-9dc26b37f775.jpg'),
       (7,  '/product-img/7ca72775-fa3c-49ef-8e62-34d1e2b67f30.avif'),
       (8,  '/product-img/deec2081-a85a-4cb5-871e-9dc26b37f775.jpg'),
       (9,  '/product-img/7ca72775-fa3c-49ef-8e62-34d1e2b67f30.avif'),
       (10, '/product-img/deec2081-a85a-4cb5-871e-9dc26b37f775.jpg');

INSERT INTO review_aggregate (product_id, avg_rating, review_count)
VALUES (1, 0, 0), (2, 0, 0),
       (3, 0, 0), (4, 0, 0),
       (5, 0, 0), (6, 0, 0),
       (7, 0, 0), (8, 0, 0),
       (9, 0, 0), (10, 0, 0),
       (11, 0, 0);