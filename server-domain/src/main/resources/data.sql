-- # drop table Bookmark ;
-- # drop table Review;
-- # drop table Store;
-- # drop table StoreMeta ;
-- # drop table Category ;
-- # drop table User;
--
-- INSERT INTO Category (categoryId, categoryName, categoryType, createdAt)
-- VALUES (1, '한식', 'KOREAN', NOW()),
--        (2, '중식', 'CHINESE', NOW()),
--        (3, '일식', 'JAPANESE', NOW()),
--        (4, '양식', 'WESTERN', NOW()),
--        (5, '카페,디저트', 'CAFE', NOW()),
--        (6, '술집', 'BARS', NOW()),
--        (7, '분식', 'SCHOOLFOOD', NOW()),
--        (8, '기타', 'ETC', NOW());
--
INSERT INTO User (myFeedCnt, createdAt, deletedAt, updatedAt, level, nickName, profileImageUrl, socialId, socialType, userRole)
VALUES
    (0, NOW(), NULL, NULL, 'LEVEL1', 'JohnDoe', 'http://example.com/profile1.jpg', 'john.doe@example.com', 'APPLE', 'USER'),
    (0, NOW(), NULL, NULL, 'LEVEL2', 'JaneDoe', 'http://example.com/profile2.jpg', 'jane.doe@example.com', 'KAKAO', 'USER'),
    (0, NOW(), NULL, NULL, 'LEVEL3', 'Alice', 'http://example.com/profile3.jpg', 'alice@example.com', 'APPLE', 'USER'),
    (0, NOW(), NULL, NULL, 'LEVEL4', 'Bob', 'http://example.com/profile4.jpg', 'bob@example.com', 'KAKAO', 'USER');

INSERT INTO Feed (storeId, userId, rating, imageUrl, description, likeCnt, commentCnt, createdAt)
VALUES
    (1, 1, 5, 'image_url_1.jpg', 'Description 1', 10, 20, now()),
    (2, 1, 4, 'image_url_2.jpg', 'Description 2', 15, 25, now()),
    (3, 2, 3, 'image_url_3.jpg', 'Description 3', 20, 30, now()),
    (4, 2, 2, 'image_url_4.jpg', 'Description 4', 25, 35, now()),
    (5, 3, 1, 'image_url_5.jpg', 'Description 5', 30, 40, now()),
    (6, 3, 5, 'image_url_6.jpg', 'Description 6', 35, 45, now()),
    (7, 4, 4, 'image_url_7.jpg', 'Description 7', 40, 50, now()),
    (8, 4, 3, 'image_url_8.jpg', 'Description 8', 45, 55, now()),
    (9, 4, 2, 'image_url_9.jpg', 'Description 9', 50, 60, now()),
    (10, 4, 1, 'image_url_10.jpg', 'Description 10', 55, 65, now());

INSERT INTO Store (storeId, storeName, latitude, longitude, address, thumbnailUrl, kakaoStoreId, totalRating, kakaoCategoryName, totalFeedCnt, createdAt, updatedAt)
VALUES
    (1, 'Store 1', 37.123456, 127.123456, '서울특별시 강남구 1 2', 'thumbnail_url_1.jpg', 123456789, 4.5, 'Category 1', 100, NOW(), NULL),
    (2, 'Store 2', 37.234567, 127.234567, '서울특별시 강남구 2 3', 'thumbnail_url_2.jpg', 234567890, 4.2, 'Category 2', 200, NOW(), NULL),
    (3, 'Store 3', 37.345678, 127.345678, '서울특별시 강남구 3 4', 'thumbnail_url_3.jpg', 345678901, 4.7, 'Category 3', 150, NOW(), NULL),
    (4, 'Store 4', 37.456789, 127.456789, '서울특별시 강남구 4 5', 'thumbnail_url_4.jpg', 456789012, 4.0, 'Category 4', 180, NOW(), NULL),
    (5, 'Store 5', 37.567890, 127.567890, '서울특별시 강남구 5 6', 'thumbnail_url_5.jpg', 567890123, 4.9, 'Category 5', 220, NOW(), NULL),
    (6, 'Store 6', 37.678901, 127.678901, '서울특별시 강남구 6 7', 'thumbnail_url_6.jpg', 678901234, 4.3, 'Category 6', 130, NOW(), NULL),
    (7, 'Store 7', 37.789012, 127.789012, '서울특별시 강남구 7 8', 'thumbnail_url_7.jpg', 789012345, 4.6, 'Category 7', 190, NOW(), NULL),
    (8, 'Store 8', 37.890123, 127.890123, '서울특별시 강남구 8 6', 'thumbnail_url_8.jpg', 890123456, 4.1, 'Category 8', 170, NOW(), NULL),
    (9, 'Store 9', 37.901234, 127.901234, '서울특별시 강남구 9 7', 'thumbnail_url_9.jpg', 901234567, 4.8, 'Category 9', 210, NOW(), NULL),
    (10, 'Store 10', 37.012345, 127.012345, '서울특별시 강남구 10 1', 'thumbnail_url_10.jpg', 123456789, 4.4, 'Category 10', 160, NOW(), NULL);

INSERT INTO Heart (createdAt, deletedAt, feedId, updatedAt, userId)
VALUES
    (NOW(), NULL, 1, NULL, 1),
    (NOW(), NULL, 2, NULL, 2),
    (NOW(), NULL, 3, NULL, 3),
    (NOW(), NULL, 4, NULL, 4),
    (NOW(), NULL, 5, NULL, 1),
    (NOW(), NULL, 6, NULL, 2),
    (NOW(), NULL, 7, NULL, 3),
    (NOW(), NULL, 8, NULL, 4),
    (NOW(), NULL, 9, NULL, 1),
    (NOW(), NULL, 10, NULL, 2);

-- -- INSERT INTO Bookmark (user_id, store_id, isDeleted, createdAt, updatedAt)
-- -- VALUES (1, 1, 0, now(), now()),
-- --        (1, 2, 0, now(), now()),
-- --        (1, 3, 0, now(), now()),
-- --        (2, 1, 0, now(), now()),
-- --        (2, 2, 0, now(), now()),
-- --        (3, 3, 0, now(), now()),
-- --        (4, 3, 0, now(), now()),
-- --        (4, 4, 0, now(), now());
-- --
-- -- INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
-- -- VALUES (1, 1, 3, '2022-01-01T12:00:00', 'http://example.com/image3.jpg', 1, '맛있었어요13123', '2022-01-01T17:00:00');
-- --
-- -- INSERT INTO Review (store_id, user_id, rating, visitedAt, visitTimes, description, createdAt)
-- -- VALUES (2, 1, 4, '2022-01-02T13:00:00', 2, '괜찮았어요124141', '2022-01-01T12:00:00');
-- --
-- -- /* 리뷰 삭제 테스트
-- --    나는 1번 user
-- --    6번 store은 최다방문자가 3명이고(나도 최다 방문자), 3번씩 재방문한 경우
-- --    7번 store은 최다방문자가 2명이고, 3번씩 재방문한 상태, 내가 재방문 횟수가 2번인 경우
-- --    8번 store은 나만 최다 방문자고, 내가 재방문한 횟수가 3번 이상
-- --    9번 store은 나만 최다 방문자고, 내가 재방문한 횟수가 2번인 경우
-- --    10번 store은 최다방문자는 1명일때(나는 최다 방문자가 아님, 최다방문자는 4번 방문), 내 재방문 횟수가 3번 이상
-- --    11번 store은 최다방문자는 1명일때(나는 최다 방문자가 아님, 최다방문자는 4번 방문), 내 재방문 횟수가 2번
-- --  */
-- -- INSERT INTO Review (store_id, user_id, rating, visitedAt, imageUrl, visitTimes, description, createdAt)
-- -- VALUES (6, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (6, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (6, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (6, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (6, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (6, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (6, 3, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (6, 3, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (6, 3, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --
-- --        (7, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (7, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (7, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (7, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (7, 3, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (7, 3, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --
-- --        (8, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (8, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (8, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --
-- --        (9, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (9, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --
-- --        (10, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (10, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (10, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (10, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (10, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (10, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (10, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --
-- --        (11, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (11, 1, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (11, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (11, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (11, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now()),
-- --        (11, 2, 5, '2022-01-01T12:00:00', 'exam', 1, '1번 유저가 5번 음식점에 첫방문', now());
