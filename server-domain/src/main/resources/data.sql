INSERT INTO Category (categoryId, categoryName, createdAt) VALUES (1, '일식', NOW());
INSERT INTO Category (categoryId, categoryName, createdAt) VALUES (2, '카페', NOW());

INSERT INTO Store (storeId, category_id, storeName, latitude, longitude, thumbnailUrl, jibunAddress, roadAddress, addressDetail, totalRating, totalReviewCount, createdAt, updateAt)
VALUES (1, 1, '맛집1', 37.5665, 126.9780, 'thumbnail1.jpg', '서울특별시 중구 세종대로 110', '서울특별시 중구 세종대로 110', '101호', 4.5, 100, NOW(), NOW());

INSERT INTO Store (storeId, category_id, storeName, latitude, longitude, thumbnailUrl, jibunAddress, roadAddress, addressDetail, totalRating, totalReviewCount, createdAt, updateAt)
VALUES (2, 2, '카페2', 37.5651, 126.98955, 'thumbnail2.jpg', '서울특별시 중구 청계천로 100', '서울특별시 중구 청계천로 100', '201호', 4.0, 80, NOW(), NOW());
