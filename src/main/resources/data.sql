-- admin 추가
insert into user_account(user_id, password, role_type, email, nickname, memo, created_at, modified_at)
values ('admin', '$2a$10$MRoAwXIsWzYskEaj1U9lOOoM84B6EPoR.yj5tQyR72iSgVp2HayG6', 'ADMIN', 'admin@email.com', 'admin', null, now(), now()),
       ('tester', '$2a$10$MRoAwXIsWzYskEaj1U9lOOoM84B6EPoR.yj5tQyR72iSgVp2HayG6', 'USER', 'tester@email.com', '닉네임', null, now(), now()),
       ('tester2', '$2a$10$MRoAwXIsWzYskEaj1U9lOOoM84B6EPoR.yj5tQyR72iSgVp2HayG6', 'USER', 'tester2@email.com', '닉네임2', null, now(), now()),
       ('tester3', '$2a$10$MRoAwXIsWzYskEaj1U9lOOoM84B6EPoR.yj5tQyR72iSgVp2HayG6', 'USER', 'tester3@email.com', '닉네임3', null, now(), now()),
       ('tester4', '$2a$10$MRoAwXIsWzYskEaj1U9lOOoM84B6EPoR.yj5tQyR72iSgVp2HayG6', 'USER', 'tester4@email.com', '닉네임4', null, now(), now()),
       ('tester5', '$2a$10$MRoAwXIsWzYskEaj1U9lOOoM84B6EPoR.yj5tQyR72iSgVp2HayG6', 'USER', 'tester5@email.com', '닉네임5', null, now(), now()),
       ('tester6', '$2a$10$MRoAwXIsWzYskEaj1U9lOOoM84B6EPoR.yj5tQyR72iSgVp2HayG6', 'USER', 'tester6@email.com', '닉네임6', null, now(), now());

-- 공지사항
insert into notice_board(user_id, title, content, created_by, created_at, modified_by, modified_at)
values ('admin', 'title1', 'content1', 'admin', DATE_ADD(NOW(), INTERVAL -12 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -12 DAY)),
       ('admin', 'title2', 'content2', 'admin', DATE_ADD(NOW(), INTERVAL -12 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -12 DAY)),
       ('admin', 'title3', 'content3', 'admin', DATE_ADD(NOW(), INTERVAL -12 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -12 DAY)),
       ('admin', 'title4', 'content4', 'admin', DATE_ADD(NOW(), INTERVAL -12 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -12 DAY)),
       ('admin', 'title5', 'content5', 'admin', DATE_ADD(NOW(), INTERVAL -11 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -11 DAY)),
       ('admin', 'title6', 'content6', 'admin', DATE_ADD(NOW(), INTERVAL -11 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -11 DAY)),
       ('admin', 'title7', 'content7', 'admin', DATE_ADD(NOW(), INTERVAL -11 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -11 DAY)),
       ('admin', 'title8', 'content8', 'admin', DATE_ADD(NOW(), INTERVAL -11 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -11 DAY)),
       ('admin', 'title9', 'content9', 'admin', DATE_ADD(NOW(), INTERVAL -11 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -11 DAY)),
       ('admin', 'title10', 'content10', 'admin', DATE_ADD(NOW(), INTERVAL -11 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -11 DAY)),
       ('admin', 'title11', 'content11', 'admin', DATE_ADD(NOW(), INTERVAL -11 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -11 DAY)),
       ('admin', 'title12', 'content12', 'admin', DATE_ADD(NOW(), INTERVAL -10 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -10 DAY)),
       ('admin', 'title13', 'content13', 'admin', DATE_ADD(NOW(), INTERVAL -10 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -10 DAY)),
       ('admin', 'title14', 'content14', 'admin', DATE_ADD(NOW(), INTERVAL -10 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -10 DAY)),
       ('admin', 'title15', 'content15', 'admin', DATE_ADD(NOW(), INTERVAL -10 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -10 DAY)),
       ('admin', 'title16', 'content16', 'admin', DATE_ADD(NOW(), INTERVAL -10 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -10 DAY)),
       ('admin', 'title17', 'content17', 'admin', DATE_ADD(NOW(), INTERVAL -10 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -10 DAY)),
       ('admin', 'title18', 'content18', 'admin', DATE_ADD(NOW(), INTERVAL -9 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -9 DAY)),
       ('admin', 'title19', 'content19', 'admin', DATE_ADD(NOW(), INTERVAL -9 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -9 DAY)),
       ('admin', 'title20', 'content20', 'admin', DATE_ADD(NOW(), INTERVAL -9 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -9 DAY)),
       ('admin', 'title21', 'content21', 'admin', DATE_ADD(NOW(), INTERVAL -9 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -9 DAY)),
       ('admin', 'title22', 'content22', 'admin', DATE_ADD(NOW(), INTERVAL -8 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -8 DAY)),
       ('admin', 'title23', 'content23', 'admin', DATE_ADD(NOW(), INTERVAL -8 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -8 DAY)),
       ('admin', 'title24', 'content24', 'admin', DATE_ADD(NOW(), INTERVAL -8 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -8 DAY)),
       ('admin', 'title25', 'content25', 'admin', DATE_ADD(NOW(), INTERVAL -8 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -8 DAY)),
       ('admin', 'title26', 'content26', 'admin', DATE_ADD(NOW(), INTERVAL -8 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -8 DAY)),
       ('admin', 'title27', 'content27', 'admin', DATE_ADD(NOW(), INTERVAL -8 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -8 DAY)),
       ('admin', 'title28', 'content28', 'admin', DATE_ADD(NOW(), INTERVAL -7 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -7 DAY)),
       ('admin', 'title29', 'content29', 'admin', DATE_ADD(NOW(), INTERVAL -7 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -7 DAY)),
       ('admin', 'title30', 'content30', 'admin', DATE_ADD(NOW(), INTERVAL -7 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -7 DAY)),
       ('admin', 'title31', 'content31', 'admin', DATE_ADD(NOW(), INTERVAL -7 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -7 DAY)),
       ('admin', 'title32', 'content32', 'admin', DATE_ADD(NOW(), INTERVAL -7 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -7 DAY)),
       ('admin', 'title33', 'content33', 'admin', DATE_ADD(NOW(), INTERVAL -6 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -6 DAY)),
       ('admin', 'title34', 'content34', 'admin', DATE_ADD(NOW(), INTERVAL -6 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -6 DAY)),
       ('admin', 'title35', 'content35', 'admin', DATE_ADD(NOW(), INTERVAL -6 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -6 DAY)),
       ('admin', 'title36', 'content36', 'admin', DATE_ADD(NOW(), INTERVAL -6 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -6 DAY)),
       ('admin', 'title37', 'content37', 'admin', DATE_ADD(NOW(), INTERVAL -6 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -6 DAY)),
       ('admin', 'title38', 'content38', 'admin', DATE_ADD(NOW(), INTERVAL -6 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -6 DAY)),
       ('admin', 'title39', 'content39', 'admin', DATE_ADD(NOW(), INTERVAL -5 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -5 DAY)),
       ('admin', 'title40', 'content40', 'admin', DATE_ADD(NOW(), INTERVAL -5 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -5 DAY)),
       ('admin', 'title41', 'content41', 'admin', DATE_ADD(NOW(), INTERVAL -5 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -5 DAY)),
       ('admin', 'title42', 'content42', 'admin', DATE_ADD(NOW(), INTERVAL -5 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -5 DAY)),
       ('admin', 'title43', 'content43', 'admin', DATE_ADD(NOW(), INTERVAL -5 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -5 DAY)),
       ('admin', 'title44', 'content44', 'admin', DATE_ADD(NOW(), INTERVAL -4 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -4 DAY)),
       ('admin', 'title45', 'content45', 'admin', DATE_ADD(NOW(), INTERVAL -4 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -4 DAY)),
       ('admin', 'title46', 'content46', 'admin', DATE_ADD(NOW(), INTERVAL -4 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -4 DAY)),
       ('admin', 'title47', 'content47', 'admin', DATE_ADD(NOW(), INTERVAL -3 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -3 DAY)),
       ('admin', 'title48', 'content48', 'admin', DATE_ADD(NOW(), INTERVAL -3 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -3 DAY)),
       ('admin', 'title49', 'content49', 'admin', DATE_ADD(NOW(), INTERVAL -3 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -3 DAY)),
       ('admin', 'title50', 'content50', 'admin', DATE_ADD(NOW(), INTERVAL -3 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -3 DAY)),
       ('admin', 'title51', 'content51', 'admin', DATE_ADD(NOW(), INTERVAL -3 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -3 DAY)),
       ('admin', 'title52', 'content52', 'admin', DATE_ADD(NOW(), INTERVAL -3 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -3 DAY)),
       ('admin', 'title53', 'content53', 'admin', DATE_ADD(NOW(), INTERVAL -2 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -2 DAY)),
       ('admin', 'title54', 'content54', 'admin', DATE_ADD(NOW(), INTERVAL -2 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -2 DAY)),
       ('admin', 'title55', 'content55', 'admin', DATE_ADD(NOW(), INTERVAL -2 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -2 DAY)),
       ('admin', 'title56', 'content56', 'admin', DATE_ADD(NOW(), INTERVAL -2 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -2 DAY)),
       ('admin', 'title57', 'content57', 'admin', DATE_ADD(NOW(), INTERVAL -2 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -2 DAY)),
       ('admin', 'title58', 'content58', 'admin', DATE_ADD(NOW(), INTERVAL -2 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -2 DAY)),
       ('admin', 'title59', 'content59', 'admin', DATE_ADD(NOW(), INTERVAL -2 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -2 DAY)),
       ('admin', 'title60', 'content60', 'admin', DATE_ADD(NOW(), INTERVAL -1 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -1 DAY)),
       ('admin', 'title61', 'content61', 'admin', DATE_ADD(NOW(), INTERVAL -1 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -1 DAY)),
       ('admin', 'title62', 'content62', 'admin', DATE_ADD(NOW(), INTERVAL -1 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -1 DAY)),
       ('admin', 'title63', 'content63', 'admin', DATE_ADD(NOW(), INTERVAL -1 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -1 DAY)),
       ('admin', 'title64', 'content64', 'admin', DATE_ADD(NOW(), INTERVAL -1 DAY), 'admin', DATE_ADD(NOW(), INTERVAL -1 DAY)),
       ('admin', 'title65', 'content65', 'admin', now(), 'admin', now());

-- 질문 게시글
insert into question_board(user_id, title, content, tag, view_count, created_by, created_at, modified_by, modified_at)
values ('tester', 'question title', 'content', 'Free', 0, 'tester', DATE_ADD(NOW(), INTERVAL -12 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -12 DAY)),
       ('tester', 'question title2', 'content2', 'Free', 0, 'tester', DATE_ADD(NOW(), INTERVAL -12 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -12 DAY)),
       ('tester', 'question title3', 'content3', 'Free', 0, 'tester', DATE_ADD(NOW(), INTERVAL -12 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -12 DAY)),
       ('tester', 'question title4', 'content4', 'Free', 0, 'tester', DATE_ADD(NOW(), INTERVAL -12 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -12 DAY)),
       ('tester', 'question title5', 'content5', 'Free', 0, 'tester', DATE_ADD(NOW(), INTERVAL -11 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -11 DAY)),
       ('tester', 'question title6', 'content6', 'Free', 0, 'tester', DATE_ADD(NOW(), INTERVAL -11 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -11 DAY)),
       ('tester', 'question title7', 'content7', 'Free', 0, 'tester', DATE_ADD(NOW(), INTERVAL -11 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -11 DAY)),
       ('tester', 'question title8', 'content8', 'Free', 0, 'tester', DATE_ADD(NOW(), INTERVAL -11 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -11 DAY)),
       ('tester', 'question title9', 'content9', 'Free', 0, 'tester', DATE_ADD(NOW(), INTERVAL -10 DAY), 'tester',DATE_ADD(NOW(), INTERVAL -10 DAY)),
       ('tester', 'question title10', 'content10', 'Free', 0, 'tester', DATE_ADD(NOW(), INTERVAL -10 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -10 DAY)),
       ('tester', 'question title11', 'content11', 'Free', 0, 'tester', DATE_ADD(NOW(), INTERVAL -10 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -10 DAY)),
       ('tester', 'question title12', 'content12', 'Free', 0, 'tester', DATE_ADD(NOW(), INTERVAL -10 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -10 DAY)),
       ('tester', 'question title13', 'content13', 'Free', 0, 'tester', DATE_ADD(NOW(), INTERVAL -9 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -9 DAY)),
       ('tester', 'question title14', 'content14', 'English', 0, 'tester', DATE_ADD(NOW(), INTERVAL -9 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -9 DAY)),
       ('tester', 'question title15', 'content15', 'English', 0, 'tester', DATE_ADD(NOW(), INTERVAL -9 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -9 DAY)),
       ('tester', 'question title16', 'content16', 'English', 0, 'tester', DATE_ADD(NOW(), INTERVAL -8 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -8 DAY)),
       ('tester', 'question title17', 'content17', 'IT', 0, 'tester', DATE_ADD(NOW(), INTERVAL -7 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -7 DAY)),
       ('tester', 'question title18', 'content18', 'IT', 0, 'tester', DATE_ADD(NOW(), INTERVAL -6 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -6 DAY)),
       ('tester', 'question title19', 'content19', 'IT', 0, 'tester', DATE_ADD(NOW(), INTERVAL -5 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -5 DAY)),
       ('tester', 'question title20', 'content20', 'IT', 0, 'tester', DATE_ADD(NOW(), INTERVAL -4 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -4 DAY)),
       ('tester', 'question title21', 'content21', 'IT', 0, 'tester', DATE_ADD(NOW(), INTERVAL -3 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -3 DAY)),
       ('tester', 'question title22', 'content22', 'IT', 0, 'tester', DATE_ADD(NOW(), INTERVAL -2 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -2 DAY)),
       ('tester', 'question title23', 'content23', 'IT', 0, 'tester', DATE_ADD(NOW(), INTERVAL -1 DAY), 'tester', DATE_ADD(NOW(), INTERVAL -1 DAY)),
       ('tester', 'question title24', 'content content content content content content content content content content content content', 'Free', 0, 'tester', now(), 'tester', now());

-- 질문 게시글 댓글
insert into question_board_comment(user_id, comment, question_board_id, created_by, created_at, modified_by, modified_at)
values ('tester2', 'comment1', 24, 'tester2', now(), 'tester2', now()),
       ('tester2', 'comment2', 24, 'tester2', now(), 'tester2', now()),
       ('tester', 'comment3', 24, 'tester2', now(), 'tester2', now()),
       ('tester', 'comment4', 24, 'tester2', now(), 'tester2', now()),
       ('tester', 'comment5', 24, 'tester2', now(), 'tester2', now()),
       ('tester2', 'comment6', 24, 'tester2', now(), 'tester2', now()),
       ('tester2', 'comment7', 24, 'tester2', now(), 'tester2', now()),
       ('tester2', 'comment8', 24, 'tester2', now(), 'tester2', now()),
       ('tester2', 'comment9', 24, 'tester2', now(), 'tester2', now()),
       ('tester2', 'comment10', 24, 'tester2', now(), 'tester2', now()),
       ('tester2', 'comment11', 24, 'tester2', now(), 'tester2', now()),
       ('tester2', 'comment12', 24, 'tester2', now(), 'tester2', now()),
       ('tester2', 'comment13', 24, 'tester2', now(), 'tester2', now()),
       ('tester2', 'comment14', 24, 'tester2', now(), 'tester2', now()),
       ('tester2', 'comment15', 24, 'tester2', now(), 'tester2', now()),
       ('tester2', 'comment16', 23, 'tester2', now(), 'tester2', now()),
       ('tester2', 'comment17', 23, 'tester2', now(), 'tester2', now()),
       ('tester2', 'comment18', 23, 'tester2', now(), 'tester2', now()),
       ('tester2', 'comment19', 23, 'tester2', now(), 'tester2', now()),
       ('tester2', 'comment20', 23, 'tester2', now(), 'tester2', now());

-- todolist
insert into todo(user_id, content, importance_type, is_completed, created_at, modified_at)
values ('tester', 'Java 공부하기', 'Middle', false, now(), now()),
       ('tester', 'AWS 공부하기', 'Middle', false, now(), now()),
       ('tester', 'CS 공부하기', 'High', true, now(), now()),
       ('tester', '코테 공부하기', 'Middle', false, now(), now()),
       ('tester', '블로그 정리하기', 'Middle', false, now(), now()),
       ('tester', '회사 지원하기', 'Middle', false, now(), now()),
       ('tester', '운동하기', 'Low', true, now(), now()),
       ('tester', '영어 공부하기', 'Middle', false, now(), now());

-- 스터디 그룹
insert into study_group(study_group_name, description, created_at, created_by, modified_at, modified_by)
values ('test group', 'description', now(), 'tester', now(), 'tester');

-- 유저 스터디 그룹
insert into user_study_group(joined_at, study_group_id, user_id, position)
values (now(), 1, 'tester', 'Leader'),
       (now(), 1, 'tester3', 'Member'),
       (now(), 1, 'tester4', 'Member'),
       (now(), 1, 'tester5', 'Member'),
       (now(), 1, 'tester6', 'Member');

-- 스터디 그룹 게시글
insert into study_group_board(user_id, study_group_id, title, content, created_at, created_by, modified_at, modified_by)
values ('tester', 1, 'title_test1', 'content', now(), 'tester', now(), 'tester'),
       ('tester6', 1, 'title_test2', 'content', now(), 'tester6', now(), 'tester6'),
       ('tester3', 1, 'title_test3', 'content', now(), 'tester3', now(), 'tester3'),
       ('tester', 1, 'title_test4', 'content', now(), 'tester', now(), 'tester'),
       ('tester', 1, 'title_test5', 'content', now(), 'tester', now(), 'tester'),
       ('tester', 1, 'title_test6', 'content', now(), 'tester', now(), 'tester'),
       ('tester', 1, 'title_test7', 'content', now(), 'tester', now(), 'tester'),
       ('tester', 1, 'title_test8', 'content', now(), 'tester', now(), 'tester'),
       ('tester', 1, 'title_test9', 'content', now(), 'tester', now(), 'tester'),
       ('tester', 1, 'title_test10', 'content', now(), 'tester', now(), 'tester'),
       ('tester', 1, 'title_test11', 'content', now(), 'tester', now(), 'tester'),
       ('tester', 1, 'title_test12', 'content', now(), 'tester', now(), 'tester'),
       ('tester3', 1, 'title_test13', 'content', now(), 'tester3', now(), 'tester3'),
       ('tester', 1, 'title_test14', 'content', now(), 'tester', now(), 'tester'),
       ('tester4', 1, 'title_test15', 'content', now(), 'tester4', now(), 'tester4'),
       ('tester', 1, 'title_test16', 'content', now(), 'tester', now(), 'tester'),
       ('tester', 1, 'title_test17', 'content', now(), 'tester', now(), 'tester'),
       ('tester5', 1, 'title_test18', 'content', now(), 'tester5', now(), 'tester5'),
       ('tester4', 1, 'title_test19', 'content', now(), 'tester4', now(), 'tester4'),
       ('tester', 1, 'title_test20', 'content', now(), 'tester', now(), 'tester');

-- 그룹 스터디 게시글 댓글
insert into study_group_board_comment(user_id, comment, study_group_board_id, created_at, created_by, modified_at, modified_by)
values ('tester3', 'test_comment1', 20, now(), 'tester3', now(), 'tester3'),
       ('tester3', 'test_comment2', 20, now(), 'tester3', now(), 'tester3'),
       ('tester3', 'test_comment3', 20, now(), 'tester3', now(), 'tester3'),
       ('tester4', 'test_comment4', 20, now(), 'tester4', now(), 'tester4'),
       ('tester4', 'test_comment5', 20, now(), 'tester4', now(), 'tester4'),
       ('tester4', 'test_comment6', 20, now(), 'tester4', now(), 'tester4'),
       ('tester', 'test_comment7', 20, now(), 'tester', now(), 'tester'),
       ('tester', 'test_comment8', 20, now(), 'tester', now(), 'tester'),
       ('tester', 'test_comment9', 20, now(), 'tester', now(), 'tester'),
       ('tester', 'test_comment10', 20, now(), 'tester', now(), 'tester'),
       ('tester', 'test_comment11', 20, now(), 'tester', now(), 'tester'),
       ('tester', 'test_comment12', 20, now(), 'tester', now(), 'tester'),
       ('tester', 'test_comment13', 20, now(), 'tester', now(), 'tester'),
       ('tester', 'test_comment14', 18, now(), 'tester', now(), 'tester'),
       ('tester', 'test_comment15', 18, now(), 'tester', now(), 'tester'),
       ('tester', 'test_comment16', 18, now(), 'tester', now(), 'tester'),
       ('tester', 'test_comment17', 18, now(), 'tester', now(), 'tester'),
       ('tester', 'test_comment18', 18, now(), 'tester', now(), 'tester'),
       ('tester', 'test_comment19', 20, now(), 'tester', now(), 'tester'),
       ('tester', 'test_comment20', 20, now(), 'tester', now(), 'tester');

-- 채팅룸
insert into chat_room(user1_id, user2_id, created_at)
values ('tester', 'tester2', now()),
       ('tester', 'tester3', now()),
       ('tester', 'tester4', now()),
       ('tester', 'tester5', now());

-- 채팅 메세지
insert into chat_message(room_id, user_id, message, created_at)
values (1, 'tester', '안녕하세요', DATE_ADD(NOW(), INTERVAL -2 DAY)),
       (1, 'tester2', '안녕하세요!', DATE_ADD(NOW(), INTERVAL -1 DAY));