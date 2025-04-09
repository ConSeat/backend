insert into member (member_id, uuid, social_id, nickname, src, role, provider_type, is_deleted, created_at, modified_at) values
    (1, 'c2141f87-30d7-4d2e-a9ef-3eff99acd957', 'test@test.com', 'test_nickname','src', 'ROLE_ADMIN', 'kakao', false, current_timestamp, current_timestamp);

insert into stadium (stadium_id, name, image, is_active) values
    (1, 'KSPO DOME(올림픽 체조경기장)', 'src', true),
    (2, '잠실 실내체육관', 'src', false),
    (3, '고척 스카이돔', 'src', false);

insert into concert (concert_id, stadium_id, name, is_deleted) values
   (1, 1, 'TOMORROW X TOGETHER WORLD TOUR 〈ACT : SWEET MIRAGE〉 IN SEOUL', false),
   (2, 1, 'TOMORROW X TOGETHER WORLD TOUR 〈ACT : PROMISE〉 IN SEOUL', false),
   (3, 1, 'TOMORROW X TOGETHER WORLD TOUR 〈ACT : PROMISE〉 ENCORE IN SEOUL', false),
   (4, 1, '2025 2NE1 CONCERT ［WELCOME BACK］ ENCORE IN SEOUL', false);

insert into floor (floor_id, stadium_id, name) values
   (1, 1, 'FLOOR'),
   (2, 1, '1F'),
   (3, 1, '2F');

insert into section (section_id, floor_id, name, image) values
    (1, 1, 'FLOOR A', 'src'),
    (2, 1, 'FLOOR B', 'src'),
    (3, 1, 'FLOOR C', 'src'),
    (4, 2, '1', 'src'),
    (5, 2, '2', 'src'),
    (6, 2, '3', 'src'),
    (7, 3, '24', 'src'),
    (8, 3, '25', 'src'),
    (9, 3, '26', 'src');

insert into seating (seating_id, section_id, name) values
   (1, 1, 'FLOOR'),
   (2, 2, 'FLOOR'),
   (3, 3, 'FLOOR'),
   (4, 4, '1열 ~ 5열'),
   (5, 4, '6열 ~ 11열'),
   (6, 4, '12열 ~ 15열'),
   (7, 5, '1열 ~ 5열'),
   (8, 5, '6열 ~ 11열'),
   (9, 5, '12열 ~ 15열'),
   (10, 6, '1열 ~ 5열'),
   (11, 6, '6열 ~ 11열'),
   (12, 6, '12열 ~ 15열'),
   (13, 7, '1열 ~ 5열'),
   (14, 7, '6열 ~ 11열'),
   (15, 7, '12열 ~ 15열'),
   (16, 8, '1열 ~ 5열'),
   (17, 8, '6열 ~ 11열'),
   (18, 8, '12열 ~ 15열'),
   (19, 9, '1열 ~ 5열'),
   (20, 9, '6열 ~ 11열'),
   (21, 9, '12열 ~ 15열');

insert into feature (feature_id, name) values
   (1, '돌출'),
   (2, '돌돌출'),
   (3, '돌출없음'),
   (4, '토롯코'),
   (5, '360'),
   (6, '통로'),
   (7, '의탠딩'),
   (8, '스탠딩'),
   (9, '시제석'),
   (10, '없음');

insert into obstruction (obstruction_id, name) values
   (1, '카메라에 가려요'),
   (2, '펜스 방해가 있어요'),
   (3, '단차가 있어요'),
   (4, '스피커에 가려요'),
   (5, '없음');

insert into review (review_id, member_id, seating_id, concert_id, contents, thumbnail,
                    stage_distance, thrust_stage_distance, screen_distance, is_approved, created_at, modified_at, is_deleted) values
  (1, 1, 1, 1, '테스트', 'src', 1, 1, 1, true, current_timestamp, current_timestamp, false),
  (2, 1, 2, 1, '테스트', 'src', 1, 1, 1, true, current_timestamp, current_timestamp, false),
  (3, 1, 3, 1, '테스트', 'src', 1, 1, 1, true, current_timestamp, current_timestamp, false),
  (4, 1, 4, 1, '테스트', 'src', 1, 1, 1, true, current_timestamp, current_timestamp, false),
  (5, 1, 4, 1, '테스트', 'src', 1, 1, 1, true, current_timestamp, current_timestamp, false),
  (6, 1, 5, 1, '테스트', 'src', 1, 1, 1, true, current_timestamp, current_timestamp, false),
  (7, 1, 5, 1, '테스트', 'src', 1, 1, 1, true, current_timestamp, current_timestamp, false),
  (8, 1, 6, 1, '테스트', 'src', 1, 1, 1, true, current_timestamp, current_timestamp, false),
  (9, 1, 6, 1, '테스트', 'src', 1, 1, 1, true, current_timestamp, current_timestamp, false),
  (10, 1, 7, 1, '테스트', 'src', 1, 1, 1, true, current_timestamp, current_timestamp, false),
  (11, 1, 7, 1, '테스트', 'src', 1, 1, 1, true, current_timestamp, current_timestamp, false),
  (12, 1, 7, 1, '테스트', 'src', 1, 1, 1, true, current_timestamp, current_timestamp, false),
  (13, 1, 8, 1, '테스트', 'src', 1, 1, 1, true, current_timestamp, current_timestamp, false);

insert into bookmark(review_id, member_id, created_at, modified_at, is_deleted) values
    (1, 1, DATEADD('MINUTE', -1, CURRENT_TIMESTAMP), DATEADD('MINUTE', -20, CURRENT_TIMESTAMP), false),
    (2, 1, DATEADD('MINUTE', -1, CURRENT_TIMESTAMP), DATEADD('MINUTE', -19, CURRENT_TIMESTAMP), false),
    (3, 1, DATEADD('MINUTE', -1, CURRENT_TIMESTAMP), DATEADD('MINUTE', -18, CURRENT_TIMESTAMP), false),
    (4, 1, DATEADD('MINUTE', -1, CURRENT_TIMESTAMP), DATEADD('MINUTE', -17, CURRENT_TIMESTAMP), false),
    (5, 1, DATEADD('MINUTE', -1, CURRENT_TIMESTAMP), DATEADD('MINUTE', -16, CURRENT_TIMESTAMP), false),
    (6, 1, DATEADD('MINUTE', -1, CURRENT_TIMESTAMP), DATEADD('MINUTE', -15, CURRENT_TIMESTAMP), false),
    (7, 1, DATEADD('MINUTE', -1, CURRENT_TIMESTAMP), DATEADD('MINUTE', -14, CURRENT_TIMESTAMP), false),
    (8, 1, DATEADD('MINUTE', -1, CURRENT_TIMESTAMP), DATEADD('MINUTE', -13, CURRENT_TIMESTAMP), false),
    (9, 1, DATEADD('MINUTE', -1, CURRENT_TIMESTAMP), DATEADD('MINUTE', -12, CURRENT_TIMESTAMP), false),
    (10, 1, DATEADD('MINUTE', -1, CURRENT_TIMESTAMP), DATEADD('MINUTE', -11, CURRENT_TIMESTAMP), false),
    (11, 1, DATEADD('MINUTE', -1, CURRENT_TIMESTAMP), DATEADD('MINUTE', -10, CURRENT_TIMESTAMP), false),
    (12, 1, DATEADD('MINUTE', -1, CURRENT_TIMESTAMP), DATEADD('MINUTE', -9, CURRENT_TIMESTAMP), false),
    (13, 1, DATEADD('MINUTE', -1, CURRENT_TIMESTAMP), DATEADD('MINUTE', -8, CURRENT_TIMESTAMP), false);
