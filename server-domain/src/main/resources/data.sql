INSERT INTO Category (category_id, category_name, created_at)
VALUES (1, '일식', NOW());
INSERT INTO Category (category_id, category_name, created_at)
VALUES (2, '카페', NOW());

INSERT INTO User (social_type, profile_image_url, nick_name, email, user_role, social_id, created_at)
VALUES ('KAKAO', 'http://example.com/profile1.jpg', '김철수', 'kim@example.com', 'USER', 'kakao1234', NOW());

INSERT INTO User (social_type, profile_image_url, nick_name, email, user_role, social_id, created_at)
VALUES ('KAKAO', 'http://example.com/profile2.jpg', '이영희', 'lee@example.com', 'USER', 'KAKAO11', NOW());

INSERT INTO User (social_type, profile_image_url, nick_name, email, user_role, social_id, created_at)
VALUES ('APPLE', 'http://example.com/profile3.jpg', '김동현', 'email', 'USER', 'kakao123411', NOW());

INSERT INTO User (social_type, profile_image_url, nick_name, email, user_role, social_id, created_at)
VALUES ('APPLE', 'http://example.com/profile4.jpg', '김나다', 'email', 'USER', 'kakao12341111', NOW());

INSERT INTO Store (store_id, category_id, store_name, latitude, longitude, thumbnail_url, jibun_address, road_address,
                   address_detail, total_rating, total_review_count, created_at, updated_at)
VALUES (1, 1, '맛집1', 37.3665, 123.9780, 'thumbnail1.jpg', '서울특별시 중구 세종대로 110', '서울특별시 중구 세종대로 110', '101호', 4.5, 100,
        NOW(), NOW());
INSERT INTO Store (store_id, category_id, store_name, latitude, longitude, thumbnail_url, jibun_address, road_address,
                   address_detail, total_rating, total_review_count, created_at, updated_at)
VALUES (2, 2, '카페2', 37.6651, 126.98955, 'thumbnail2.jpg', '서울특별시 중구 청계천로 100', '서울특별시 중구 청계천로 100', '201호', 4.0, 80,
        NOW(), NOW());
INSERT INTO Store (store_id, category_id, store_name, latitude, longitude, thumbnail_url, jibun_address, road_address,
                   address_detail, total_rating, total_review_count, created_at, updated_at)
VALUES (3, 1, '맛집2', 43.5665, 130.9780, 'thumbnail3.jpg', '서울특별시 관악구 봉천동 62-1', '서울특별시 관악구 봉천동 62-1', '301호', 3.5, 100,
        NOW(), NOW());
INSERT INTO Store (store_id, category_id, store_name, latitude, longitude, thumbnail_url, jibun_address, road_address,
                   address_detail, total_rating, total_review_count, created_at, updated_at)
VALUES (4, 2, '카페2', 45.5651, 131.98955, 'thumbnail4.jpg', '서울특별시 강남구 테헤란로 21-10', '서울특별시 강남구 테헤란로 21-10', '401호', 4.0,
        70, NOW(), NOW());

INSERT INTO Review (store_id, user_id, rating, visited_at, image_url, visit_times, description, created_at)
VALUES (1, 1, 4, '2022-01-01T12:00:00', 'http://example.com/image1.jpg', 1, '맛있었어요', '2022-01-01T12:00:00');
INSERT INTO Review (store_id, user_id, rating, visited_at, image_url, visit_times, description, created_at)
VALUES (1, 1, 5, '2022-01-01T13:00:00', 'http://example.com/image2.jpg', 2, '맛있었어요', '2022-01-01T13:00:00');
INSERT INTO Review (store_id, user_id, rating, visited_at, image_url, visit_times, description, created_at)
VALUES (1, 1, 3, '2022-01-01T14:00:00', 'http://example.com/image3.jpg', 3, '맛있었어요', '2022-01-01T14:00:00');
INSERT INTO Review (store_id, user_id, rating, visited_at, visit_times, description, created_at)
VALUES (1, 2, 4, '2022-01-04T13:00:00', 1, '괜찮았어요1', now());
INSERT INTO Review (store_id, user_id, rating, visited_at, visit_times, description, created_at)
VALUES (1, 2, 4, '2022-01-05T13:00:00', 2, '괜찮았어2', now());
INSERT INTO Review (store_id, user_id, rating, visited_at, image_url, visit_times, description, created_at)
VALUES (1, 3, 3, '2022-01-01T12:00:00', 'http://example.com/image3.jpg', 1, '맛있었어요', '2022-01-01T17:00:00');
INSERT INTO Review (store_id, user_id, rating, visited_at, visit_times, description, created_at)
VALUES (2, 1, 3, '2022-01-02T13:00:00', 1, '괜찮았어요', '2022-01-01T13:00:00');
INSERT INTO Review (store_id, user_id, rating, visited_at, visit_times, description, created_at)
VALUES (2, 3, 2, '2022-01-02T13:00:00', 1, '괜찮았어요', '2022-01-01T15:00:00');
INSERT INTO Review (store_id, user_id, rating, visited_at, visit_times, description, created_at)
VALUES (2, 3, 4, '2022-01-02T14:00:00', 2, '괜찮았어요', '2022-01-01T12:00:00');
INSERT INTO Review (store_id, user_id, rating, visited_at, visit_times, description, created_at)
VALUES (3, 1, 4, '2022-01-02T13:00:00', 1, '괜찮았어요', '2022-01-01T12:00:00');
INSERT INTO Review (store_id, user_id, rating, visited_at, visit_times, description, created_at)
VALUES (3, 2, 4, '2022-01-02T13:00:00', 1, '괜찮았어요', '2022-01-01T12:00:00');
INSERT INTO Review (store_id, user_id, rating, visited_at, visit_times, description, created_at)
VALUES (4, 3, 4, '2022-01-02T13:00:00', 1, '요 식당 괜찮았어요1', now());
INSERT INTO Review (store_id, user_id, rating, visited_at, visit_times, description, created_at)
VALUES (4, 3, 4, '2022-01-02T14:00:00', 2, '요 식당 괜찮았어요2', now());
INSERT INTO Review (store_id, user_id, rating, visited_at, visit_times, description, created_at)
VALUES (4, 4, 4, '2022-01-03T13:00:00', 1, '진짜 맛있어요', now());


INSERT INTO Bookmark (user_id, store_id, is_deleted, created_at, updated_at)
VALUES (1, 1, 0, now(), now());
INSERT INTO Bookmark (user_id, store_id, is_deleted, created_at, updated_at)
VALUES (1, 2, 0, now(), now());
INSERT INTO Bookmark (user_id, store_id, is_deleted, created_at, updated_at)
VALUES (1, 3, 0, now(), now());
INSERT INTO Bookmark (user_id, store_id, is_deleted, created_at, updated_at)
VALUES (2, 1, 0, now(), now());
INSERT INTO Bookmark (user_id, store_id, is_deleted, created_at, updated_at)
VALUES (2, 2, 0, now(), now());
INSERT INTO Bookmark (user_id, store_id, is_deleted, created_at, updated_at)
VALUES (3, 3, 0, now(), now());
INSERT INTO Bookmark (user_id, store_id, is_deleted, created_at, updated_at)
VALUES (4, 3, 0, now(), now());
INSERT INTO Bookmark (user_id, store_id, is_deleted, created_at, updated_at)
VALUES (4, 4, 0, now(), now());
