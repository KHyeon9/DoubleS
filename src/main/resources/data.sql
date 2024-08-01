-- admin 추가
insert into user_account(user_id, password, role_type, email, nickname, memo, created_at, modified_at)
values ('admin', '$2a$10$Lzkcz7888H3xaQLg/KzTUeO37mmlkuby1EABVNuTb4/lB6zkpqgSK', 'ADMIN', 'admin@email.com', 'admin', null, now(), now()),
       ('tester', '$2a$10$hIDJIM6ab.OoT07Ub5a0ve7pAlgG3EbHBNGJy69ewtPrV5e6BZLIa', 'USER', 'tester@email.com', 'tester', null, now(), now()),
       ('tester2', '$2a$10$hIDJIM6ab.OoT07Ub5a0ve7pAlgG3EbHBNGJy69ewtPrV5e6BZLIa', 'USER', 'tester@email.com', 'tester', null, now(), now());

-- 공지사항
insert into notice_board(id, user_id, title, content, created_by, created_at, modified_by, modified_at)
values (1, 'admin', 'title1', 'content1', 'admin', now(), 'admin', now()),
       (2, 'admin', 'title2', 'content2', 'admin', now(), 'admin', now()),
       (3, 'admin', 'title3', 'content3', 'admin', now(), 'admin', now()),
       (4, 'admin', 'title4', 'content4', 'admin', now(), 'admin', now()),
       (5, 'admin', 'title5', 'content5', 'admin', now(), 'admin', now()),
       (6, 'admin', 'title6', 'content6', 'admin', now(), 'admin', now()),
       (7, 'admin', 'title7', 'content7', 'admin', now(), 'admin', now()),
       (8, 'admin', 'title8', 'content8', 'admin', now(), 'admin', now()),
       (9, 'admin', 'title9', 'content9', 'admin', now(), 'admin', now()),
       (10, 'admin', 'title10', 'content10', 'admin', now(), 'admin', now()),
       (11, 'admin', 'title11', 'content11', 'admin', now(), 'admin', now());

-- 질문 게시판
insert into question_board(id, user_id, title, content, tag, view_count, created_by, created_at, modified_by, modified_at)
values (1, 'tester', 'question title', 'content', null, 0, 'tester', now(), 'tester', now()),
       (2, 'tester', 'question title2', 'content2', null, 0, 'tester', now(), 'tester', now()),
       (3, 'tester', 'question title3', 'content3', null, 0, 'tester', now(), 'tester', now()),
       (4, 'tester', 'question title4', 'content4', null, 0, 'tester', now() - interval '1 day', 'tester', now() - interval '1 day'),
       (5, 'tester', 'question title5', 'content5', null, 0, 'tester', now(), 'tester', now()),
       (6, 'tester', 'question title6', 'content6', null, 0, 'tester', now(), 'tester', now());
