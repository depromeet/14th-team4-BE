INSERT INTO Category (categoryId, categoryName, createdAt)
VALUES (1, '일식', NOW());
INSERT INTO Category (categoryId, categoryName, createdAt)
VALUES (2, '카페', NOW());

INSERT INTO User (socialType, nickName, userRole, socialId, level, myReviewCount, createdAt)
VALUES ('KAKAO', '김철수', 'USER', 'kakao1234', 'LEVEL1', 0, NOW());

INSERT INTO User (socialType, nickName, userRole, socialId, level, myReviewCount, createdAt)
VALUES ('KAKAO', '이영희', 'USER', 'KAKAO11', 'LEVEL2', 3, NOW());

INSERT INTO User (socialType, nickName, userRole, socialId, level, myReviewCount, createdAt)
VALUES ('APPLE', '김동현', 'USER', 'kakao123411', 'LEVEL3', 11, NOW());

INSERT INTO User (socialType, nickName, userRole, socialId, level, myReviewCount, createdAt)
VALUES ('APPLE', '김나다', 'USER', 'kakao12341111', 'LEVEL4', 21, NOW());

INSERT INTO StoreMeta (totalRevisitedCount, totalReviewCount, mostVisitedCount,totalRating, createdAt, updatedAt)
VALUES (5, 14, 3, 4.5, NOW(), NOW()),
       (0, 4, 0, 5.0, NOW(), NOW()),
       (2, 8, 2, 4.0, NOW(), NOW()),
       (10, 50, 5, 3.5,NOW(), NOW()),
       (1, 3, 2, 3.0, NOW(), NOW());
-- Store 테이블에 데이터 삽입
INSERT INTO Store (category_id, store_meta_id, storeName, latitude, longitude, address, thumbnailUrl, kakaoStoreId, createdAt, updatedAt)
VALUES (1, 1, '맛집1', 37.3665, 123.9780, '서울특별시 중구 세종대로 110', 'thumbnail1.jpg', '234',NOW(), NOW()),
       (2, 2, '카페2', 37.6651, 126.98955, '서울특별시 중구 청계천로 100', 'thumbnail2.jpg', '3342',NOW(), NOW()),
       (1, 3, '맛집2', 43.5665, 130.9780, '서울특별시 관악구 봉천동 62-1', 'thumbnail3.jpg', '1234',NOW(), NOW()),
       (2, 4, '카페2', 45.5651, 131.98955, '서울특별시 강남구 테헤란로 21-10', 'thumbnail4.jpg', '3497',NOW(), NOW()),
       (1, 5, '티컵 스타필드 코엑스몰점', 37.5126847515106, 127.058938708812, '서울 강남구 삼성동 159', 'thumbnail1.jpg', '43234',NOW(), NOW());



INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
VALUES (1, 1, 4, '2022-01-01T12:00:00', 'http://example.com/image1.jpg', 1, '맛있었어요', '2022-01-01T12:00:00');
INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
VALUES (1, 1, 5, '2022-01-01T13:00:00', 'http://example.com/image2.jpg', 2, '맛있었어요', '2022-01-01T13:00:00');
INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
VALUES (1, 1, 3, '2022-01-01T14:00:00', 'http://example.com/image3.jpg', 3, '맛있었어요', '2022-01-01T14:00:00');
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (1, 2, 4, '2022-01-04T13:00:00', 1, '괜찮았어요1', now());
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (1, 2, 4, '2022-01-05T13:00:00', 2, '괜찮았어2', now());
INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
VALUES (1, 3, 3, '2022-01-01T12:00:00', 'http://example.com/image3.jpg', 1, '맛있었어요', '2022-01-01T17:00:00');
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (2, 1, 3, '2022-01-02T13:00:00', 1, '괜찮았어요', '2022-01-01T13:00:00');
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (2, 3, 2, '2022-01-02T13:00:00', 1, '괜찮았어요', '2022-01-01T15:00:00');
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (2, 3, 4, '2022-01-02T14:00:00', 2, '괜찮았어요', '2022-01-01T12:00:00');
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (3, 1, 4, '2022-01-02T13:00:00', 1, '괜찮았어요', '2022-01-01T12:00:00');
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (3, 2, 4, '2022-01-02T13:00:00', 1, '괜찮았어요', '2022-01-01T12:00:00');
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (4, 3, 4, '2022-01-02T13:00:00', 1, '요 식당 괜찮았어요1', now());
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (4, 3, 4, '2022-01-02T14:00:00', 2, '요 식당 괜찮았어요2', now());
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (4, 4, 4, '2022-01-03T13:00:00', 1, '진짜 맛있어요', now());

INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
VALUES (1, 1, 5, '2022-01-01T12:00:00', 'http://example.com/image2.jpg', 1, '맛있었어요', '2022-01-01T11:00:00');

INSERT INTO Bookmark (user_id, store_id, isDeleted, createdAt, updatedAt)
VALUES (1, 1, 0, now(), now());
INSERT INTO Bookmark (user_id, store_id, isDeleted, createdAt, updatedAt)
VALUES (1, 2, 0, now(), now());
INSERT INTO Bookmark (user_id, store_id, isDeleted, createdAt, updatedAt)
VALUES (1, 3, 0, now(), now());
INSERT INTO Bookmark (user_id, store_id, isDeleted, createdAt, updatedAt)
VALUES (2, 1, 0, now(), now());
INSERT INTO Bookmark (user_id, store_id, isDeleted, createdAt, updatedAt)
VALUES (2, 2, 0, now(), now());
INSERT INTO Bookmark (user_id, store_id, isDeleted, createdAt, updatedAt)
VALUES (3, 3, 0, now(), now());
INSERT INTO Bookmark (user_id, store_id, isDeleted, createdAt, updatedAt)
VALUES (4, 3, 0, now(), now());
INSERT INTO Bookmark (user_id, store_id, isDeleted, createdAt, updatedAt)
VALUES (4, 4, 0, now(), now());

INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
VALUES (1, 1, 3, '2022-01-01T12:00:00', 'http://example.com/image3.jpg', 1, '맛있었어요', '2022-01-01T17:00:00');

INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (2, 1, 4, '2022-01-02T13:00:00', 2, '괜찮았어요', '2022-01-01T12:00:00');
