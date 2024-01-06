INSERT INTO Category (categoryId, categoryName, createdAt) VALUES (1, '일식', NOW());
INSERT INTO Category (categoryId, categoryName, createdAt) VALUES (2, '카페', NOW());

INSERT INTO User (socialType, profileImageUrl, nickName, email, userRole, socialId, createdAt)
VALUES ('KAKAO', 'http://example.com/profile1.jpg', '김철수', 'kim@example.com', 'USER', 'kakao1234', NOW());

INSERT INTO User (socialType, profileImageUrl, nickName, email, userRole, socialId, createdAt)
VALUES ('KAKAO', 'http://example.com/profile2.jpg', '이영희', 'lee@example.com', 'USER', 'KAKAO11', NOW());

INSERT INTO User (socialType, profileImageUrl, nickName, email, userRole, socialId, createdAt)
VALUES ('APPLE', 'http://example.com/profile3.jpg', '김동현', 'email', 'USER', 'kakao123411', NOW());

INSERT INTO User (socialType, profileImageUrl, nickName, email, userRole, socialId, createdAt)
VALUES ('APPLE', 'http://example.com/profile4.jpg', '김나다', 'email', 'USER' ,'kakao12341111', NOW());


INSERT INTO Store (storeId, category_id, storeName, latitude, longitude, thumbnailUrl, jibunAddress, roadAddress, addressDetail, totalRating, totalReviewCount, createdAt, updateAt)
VALUES (1, 1, '맛집1', 37.5665, 126.9780, 'thumbnail1.jpg', '서울특별시 중구 세종대로 110', '서울특별시 중구 세종대로 110', '101호', 4.5, 100, NOW(), NOW());

INSERT INTO Store (storeId, category_id, storeName, latitude, longitude, thumbnailUrl, jibunAddress, roadAddress, addressDetail, totalRating, totalReviewCount, createdAt, updateAt)
VALUES (2, 2, '카페2', 37.5651, 126.98955, 'thumbnail2.jpg', '서울특별시 중구 청계천로 100', '서울특별시 중구 청계천로 100', '201호', 4.0, 80, NOW(), NOW());

INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
VALUES (1, 1, 4, '2022-01-01T12:00:00', 'http://example.com/image1.jpg', 1, '맛있었어요', '2022-01-01T12:00:00');

INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (2, 1, 3, '2022-01-02T13:00:00', 2, '괜찮았어요', '2022-01-01T13:00:00');

INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
VALUES (1, 1, 5, '2022-01-01T12:00:00', 'http://example.com/image2.jpg', 1, '맛있었어요', '2022-01-01T11:00:00');

INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (2, 1, 2, '2022-01-02T13:00:00', 2, '괜찮았어요', '2022-01-01T15:00:00');

INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
VALUES (1, 1, 3, '2022-01-01T12:00:00', 'http://example.com/image3.jpg', 1, '맛있었어요', '2022-01-01T17:00:00');

INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (2, 1, 4, '2022-01-02T13:00:00', 2, '괜찮았어요', '2022-01-01T12:00:00');