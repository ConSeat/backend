insert into member (member_id, uuid, social_id, nickname, src, role, provider_type, is_deleted, created_at, modified_at) values
    (1, 'c2141f87-30d7-4d2e-a9ef-3eff99acd957', 'test@test.com', 'test_nickname','src', 'ROLE_ADMIN', 'kakao', false, current_timestamp, current_timestamp);

insert into stadium (stadium_id, name, image, is_active) values
    (1, '잠실 실내체육관', 'src', true);

insert into concert (concert_id, stadium_id, name, is_deleted) values
   (1, 1, 'TOMORROW X TOGETHER WORLD TOUR 〈ACT : SWEET MIRAGE〉 IN SEOUL', false),
   (2, 1, 'TOMORROW X TOGETHER WORLD TOUR 〈ACT : PROMISE〉 IN SEOUL', false),
   (3, 1, 'TOMORROW X TOGETHER WORLD TOUR 〈ACT : PROMISE〉 ENCORE IN SEOUL', false),
   (4, 1, '2025 2NE1 CONCERT ［WELCOME BACK］ ENCORE IN SEOUL', false);
