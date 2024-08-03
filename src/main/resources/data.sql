-- admin 추가
insert into user_account(user_id, password, role_type, email, nickname, memo, created_at, modified_at)
values ('admin', '$2a$10$MRoAwXIsWzYskEaj1U9lOOoM84B6EPoR.yj5tQyR72iSgVp2HayG6', 'ADMIN', 'admin@email.com', 'admin', null, now(), now()),
       ('tester', '$2a$10$hIDJIM6ab.OoT07Ub5a0ve7pAlgG3EbHBNGJy69ewtPrV5e6BZLIa', 'USER', 'tester@email.com', 'tester', null, now(), now()),
       ('tester2', '$2a$10$hIDJIM6ab.OoT07Ub5a0ve7pAlgG3EbHBNGJy69ewtPrV5e6BZLIa', 'USER', 'tester@email.com', 'tester', null, now(), now());

-- 공지사항
insert into notice_board(id, user_id, title, content, created_by, created_at, modified_by, modified_at)
values (1, 'admin', 'title1', 'content1', 'admin', now() - interval '23 day', 'admin', now() - interval '23 day'),
       (2, 'admin', 'title2', 'content2', 'admin', now() - interval '22 day', 'admin', now() - interval '22 day'),
       (3, 'admin', 'title3', 'content3', 'admin', now() - interval '21 day', 'admin', now() - interval '21 day'),
       (4, 'admin', 'title4', 'content4', 'admin', now() - interval '20 day', 'admin', now() - interval '20 day'),
       (5, 'admin', 'title5', 'content5', 'admin', now() - interval '19 day', 'admin', now() - interval '19 day'),
       (6, 'admin', 'title6', 'content6', 'admin', now() - interval '18 day', 'admin', now() - interval '18 day'),
       (7, 'admin', 'title7', 'content7', 'admin', now() - interval '17 day', 'admin', now() - interval '17 day'),
       (8, 'admin', 'title8', 'content8', 'admin', now() - interval '16 day', 'admin', now() - interval '16 day'),
       (9, 'admin', 'title9', 'content9', 'admin', now() - interval '15 day', 'admin', now() - interval '15 day'),
       (10, 'admin', 'title10', 'content10', 'admin', now() - interval '14 day', 'admin', now() - interval '14 day'),
       (11, 'admin', 'title11', 'content11', 'admin', now() - interval '13 day', 'admin', now() - interval '13 day'),
       (12, 'admin', 'title12', 'content12', 'admin', now() - interval '12 day', 'admin', now() - interval '12 day'),
       (13, 'admin', 'title13', 'content13', 'admin', now() - interval '11 day', 'admin', now() - interval '11 day'),
       (14, 'admin', 'title14', 'content14', 'admin', now() - interval '10 day', 'admin', now() - interval '10 day'),
       (15, 'admin', 'title15', 'content15', 'admin', now() - interval '9 day', 'admin', now() - interval '9 day'),
       (16, 'admin', 'title16', 'content16', 'admin', now() - interval '8 day', 'admin', now() - interval '8 day'),
       (17, 'admin', 'title17', 'content17', 'admin', now() - interval '7 day', 'admin', now() - interval '7 day'),
       (18, 'admin', 'title18', 'content18', 'admin', now() - interval '6 day', 'admin', now() - interval '6 day'),
       (19, 'admin', 'title19', 'content19', 'admin', now() - interval '5 day', 'admin', now() - interval '5 day'),
       (20, 'admin', 'title20', 'content20', 'admin', now() - interval '4 day', 'admin', now() - interval '4 day'),
       (21, 'admin', 'title21', 'content21', 'admin', now() - interval '3 day', 'admin', now() - interval '3 day'),
       (22, 'admin', 'title22', 'content22', 'admin', now() - interval '2 day', 'admin', now() - interval '2 day'),
       (23, 'admin', 'title23', 'content23', 'admin', now() - interval '1 day', 'admin', now() - interval '1 day'),
       (24, 'admin', 'title24', 'content24', 'admin', now(), 'admin', now());

-- 질문 게시판
insert into question_board(id, user_id, title, content, tag, view_count, created_by, created_at, modified_by, modified_at)
values (1, 'tester', 'question title', 'content', null, 0, 'tester', now(), 'tester', now()),
       (2, 'tester', 'question title2', 'content2', null, 0, 'tester', now(), 'tester', now()),
       (3, 'tester', 'question title3', 'content3', null, 0, 'tester', now(), 'tester', now()),
       (4, 'tester', 'question title4', 'content4', null, 0, 'tester', now() - interval '1 day', 'tester', now() - interval '1 day'),
       (5, 'tester', 'question title5', 'content5', null, 0, 'tester', now(), 'tester', now()),
       (6, 'tester', 'question title6', 'content6', null, 0, 'tester', now(), 'tester', now());
