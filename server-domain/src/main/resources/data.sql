INSERT INTO Category (categoryId, categoryName, categoryType, createdAt)
VALUES (1, '한식', 'KOREAN', NOW()),
       (2, '중식', 'CHINESE', NOW()),
       (3, '일식', 'JAPANESE', NOW()),
       (4, '양식', 'WESTERN', NOW()),
       (5, '카페,디저트', 'CAFE', NOW()),
       (6, '술집', 'BARS', NOW()),
       (7, '분식', 'SCHOOLFOOD', NOW()),
       (8, '기타', 'ETC', NOW());

INSERT INTO User (socialType, nickName, userRole, socialId, level, myReviewCount, createdAt)
VALUES ('KAKAO', '김철수', 'USER', 'kakao1234', 'LEVEL1', 0, NOW()),
       ('KAKAO', '이영희', 'USER', 'KAKAO11', 'LEVEL2', 3, NOW()),
       ('APPLE', '김동현', 'USER', 'kakao123411', 'LEVEL3', 11, NOW()),
       ('APPLE', '김나다', 'USER', 'kakao12341111', 'LEVEL4', 21, NOW());

INSERT INTO StoreMeta (totalRevisitedCount, totalReviewCount, mostVisitedCount, totalRating, createdAt, updatedAt)
VALUES (5, 14, 3, 4.5, NOW(), NOW()),
       (0, 4, 0, 5.0, NOW(), NOW()),
       (2, 8, 2, 4.0, NOW(), NOW()),
       (10, 50, 5, 3.5, NOW(), NOW()),
       (1, 3, 2, 3.0, NOW(), NOW()),

       (3, 9, 3, 5, NOW(), NOW()),
       (3, 6, 2, 5, NOW(), NOW()),
       (1, 3, 3, 5, NOW(), NOW()),
       (1, 2, 2, 5, NOW(), NOW()),
       (2, 7, 4, 5, NOW(), NOW()),
       (2, 6, 4, 5, NOW(), NOW());
-- Store 테이블에 데이터 삽입
INSERT INTO Store (category_id, store_meta_id, storeName, latitude, longitude, address, thumbnailUrl, kakaoStoreId,
                   createdAt, updatedAt)
VALUES (1, 1, '맛집1', 37.3665, 123.9780, '서울특별시 중구 세종대로 110', 'thumbnail1.jpg', '234', NOW(), NOW()),
       (2, 2, '카페2', 37.6651, 126.98955, '서울특별시 중구 청계천로 100', 'thumbnail2.jpg', '3342', NOW(), NOW()),
       (1, 3, '맛집2', 43.5665, 130.9780, '서울특별시 관악구 봉천동 62-1', 'thumbnail3.jpg', '1234', NOW(), NOW()),
       (2, 4, '카페2', 45.5651, 131.98955, '서울특별시 강남구 테헤란로 21-10', 'thumbnail4.jpg', '3497', NOW(), NOW()),
       (1, 5, '티컵 스타필드 코엑스몰점', 37.5126847515106, 127.058938708812, '서울 강남구 삼성동 159', 'thumbnail1.jpg', '43234', NOW(),NOW()),

       (1, 6, '맛집6', 37.3665, 123.9780, '서울특별시 중구 세종대로 110', 'thumbnail1.jpg', '234', NOW(), NOW()),
       (1, 7, '맛집7', 37.3665, 123.9780, '서울특별시 중구 세종대로 110', 'thumbnail1.jpg', '234', NOW(), NOW()),
       (1, 8, '맛집8', 37.3665, 123.9780, '서울특별시 중구 세종대로 110', 'thumbnail1.jpg', '234', NOW(), NOW()),
       (1, 9, '맛집9', 37.3665, 123.9780, '서울특별시 중구 세종대로 110', 'thumbnail1.jpg', '234', NOW(), NOW()),
       (1, 10, '맛집10', 37.3665, 123.9780, '서울특별시 중구 세종대로 110', 'thumbnail1.jpg', '234', NOW(), NOW()),
       (1, 11, '맛집11', 37.3665, 123.9780, '서울특별시 중구 세종대로 110', 'thumbnail1.jpg', '234', NOW(), NOW());




INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
VALUES (1, 1, 4, '2022-01-01T12:00:00', 'http://example.com/image1.jpg', 1, '맛있었어요1', '2022-01-01T12:00:00');
INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
VALUES (1, 1, 5, '2022-01-01T13:00:00', 'http://example.com/image2.jpg', 2, '맛있었어요2', '2022-01-01T13:00:00');
INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
VALUES (1, 1, 3, '2022-01-01T14:00:00', 'http://example.com/image3.jpg', 3, '맛있었어요3', '2022-01-01T14:00:00');
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (1, 2, 4, '2022-01-04T13:00:00', 1, '괜찮았어요1', now());
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (1, 2, 4, '2022-01-05T13:00:00', 2, '괜찮았어2', now());
INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
VALUES (1, 3, 3, '2022-01-01T12:00:00', 'http://example.com/image3.jpg', 1, '맛있었어요4', '2022-01-01T17:00:00');
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (2, 1, 3, '2022-01-02T13:00:00', 1, '괜찮았어요3', '2022-01-01T13:00:00');
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (2, 3, 2, '2022-01-02T13:00:00', 1, '괜찮았어요4', '2022-01-01T15:00:00');
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (2, 3, 4, '2022-01-02T14:00:00', 2, '괜찮았어요5', '2022-01-01T12:00:00');
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (3, 1, 4, '2022-01-02T13:00:00', 1, '괜찮았어요6', '2022-01-01T12:00:00');
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (3, 2, 4, '2022-01-02T13:00:00', 1, '괜찮았어요7', '2022-01-01T12:00:00');
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (4, 3, 4, '2022-01-02T13:00:00', 1, '요 식당 괜찮았어요1', now());
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (4, 3, 4, '2022-01-02T14:00:00', 2, '요 식당 괜찮았어요2', now());
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (4, 4, 4, '2022-01-03T13:00:00', 1, '진짜 맛있어요11', now());
INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
VALUES (1, 1, 4, '2022-01-01T12:00:00', 'http://example.com/image1.jpg', 1, '맛있었어요12222', '2022-01-01T12:00:00');
INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
VALUES (1, 1, 5, '2022-01-01T13:00:00', 'http://example.com/image2.jpg', 2, '맛있었어요233333', '2022-01-01T13:00:00');
INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
VALUES (1, 1, 3, '2022-01-01T14:00:00', 'http://example.com/image3.jpg', 3, '맛있었어요344444', '2022-01-01T14:00:00');
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (1, 2, 4, '2022-01-04T13:00:00', 1, '괜찮았어요111111', now());
INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
VALUES (1, 2, 4, '2022-01-05T13:00:00', 2, '괜찮았어233333', now());
INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
VALUES (1, 3, 3, '2022-01-01T12:00:00', 'http://example.com/image3.jpg', 1, '맛있었어요4666666', '2022-01-01T17:00:00');


INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
VALUES (1, 1, 5, '2022-01-01T12:00:00', 'http://example.com/image2.jpg', 1, '맛있었어요121', '2022-01-01T11:00:00');

-- INSERT INTO Bookmark (user_id, store_id, isDeleted, createdAt, updatedAt)
-- VALUES (1, 1, 0, now(), now()),
--        (1, 2, 0, now(), now()),
--        (1, 3, 0, now(), now()),
--        (2, 1, 0, now(), now()),
--        (2, 2, 0, now(), now()),
--        (3, 3, 0, now(), now()),
--        (4, 3, 0, now(), now()),
--        (4, 4, 0, now(), now());
--
-- INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
-- VALUES (1, 1, 3, '2022-01-01T12:00:00', 'http://example.com/image3.jpg', 1, '맛있었어요13123', '2022-01-01T17:00:00');
--
-- INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
-- VALUES (2, 1, 4, '2022-01-02T13:00:00', 2, '괜찮았어요124141', '2022-01-01T12:00:00');
--
-- /* 리뷰 삭제 테스트
--    나는 1번 user
--    6번 store은 최다방문자가 3명이고(나도 최다 방문자), 3번씩 재방문한 경우
--    7번 store은 최다방문자가 2명이고, 3번씩 재방문한 상태, 내가 재방문 횟수가 2번인 경우
--    8번 store은 나만 최다 방문자고, 내가 재방문한 횟수가 3번 이상
--    9번 store은 나만 최다 방문자고, 내가 재방문한 횟수가 2번인 경우
--    10번 store은 최다방문자는 1명일때(나는 최다 방문자가 아님, 최다방문자는 4번 방문), 내 재방문 횟수가 3번 이상
--    11번 store은 최다방문자는 1명일때(나는 최다 방문자가 아님, 최다방문자는 4번 방문), 내 재방문 횟수가 2번
--  */
-- INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
-- VALUES (6, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (6, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (6, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (6, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (6, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (6, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (6, 3, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (6, 3, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (6, 3, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--
--        (7, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (7, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (7, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (7, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (7, 3, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (7, 3, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--
--        (8, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (8, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (8, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--
--        (9, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (9, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--
--        (10, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (10, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (10, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (10, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (10, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (10, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (10, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--
--        (11, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (11, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (11, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (11, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (11, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
--        (11, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now());
