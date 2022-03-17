-- 테스트 아이디
insert into member (nickname,oauth_id,password,provider,role) values ('test2','1111111','1111111','GOOGLE','USER');

-- 게시판 카테고리
insert into board_category (category_name) values ('공지사항');
insert into board_category (category_name) values ('커뮤니티');

-- 해시태그 카테고리
insert into tag_category (tag_name) values ('직장인');
insert into tag_category (tag_name) values ('공대생');
insert into tag_category (tag_name) values ('취준생');
insert into tag_category (tag_name) values ('고민');
insert into tag_category (tag_name) values ('질문');
insert into tag_category (tag_name) values ('개발자');
insert into tag_category (tag_name) values ('학생');
insert into tag_category (tag_name) values ('비전공자');
insert into tag_category (tag_name) values ('전공자');
insert into tag_category (tag_name) values ('토이프로젝트');
insert into tag_category (tag_name) values ('공모전');
insert into tag_category (tag_name) values ('취업');
insert into tag_category (tag_name) values ('취준');
insert into tag_category (tag_name) values ('면접');
insert into tag_category (tag_name) values ('디자이너');
insert into tag_category (tag_name) values ('기획자');
insert into tag_category (tag_name) values ('프론트');
insert into tag_category (tag_name) values ('백엔드');

-- 공지사항 게시글
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '공지사항첫번째글제목', '첫번째글내용', 1, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '공지사항두번째글제목', '두번째글제목', 1, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '공지사항두번째글제목', '세번째글제목', 1, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '글제목입니다', '글내용', 1, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '글제목입니당', '글내용', 1, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '제목', '글내용', 1, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '공지사항글', '글내용', 1, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '글제목', '글내용', 1, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '타이틀', '글내용', 1, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, 'ㅇㅇㅇㅇㅇ', '글내용', 1, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '제목', '글내용', 1, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '공지사항글', '글내용', 1, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '글제목', '글내용', 1, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '타이틀', '글내용', 1, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, 'ㅇㅇㅇㅇㅇ', '글내용', 1, 1);

-- 커뮤니티 게시글
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '커뮤니티첫번째글제목', '첫번째글내용', 2, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '커뮤니티두번째글제목', '두번째글제목', 2, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '공지사항세번째글제목', '세번째글제목', 2, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '글제목입니다', '글내용', 2, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '글제목입니당', '글내용', 2, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '제목', '글내용', 2, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '글', '글내용', 2, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '글제목', '글내용', 2, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '타이틀', '글내용', 2, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, 'ㅇㅇㅇㅇ', '글내용', 2, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '글', '글내용', 2, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '글제목', '글내용', 2, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '타이틀', '글내용', 2, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, 'ㅇㅇㅇㅇ', '글내용', 2, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '글제목', '글내용', 2, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, '타이틀', '글내용', 2, 1);
insert into board (board_cnt, board_title, board_content, category_id, member_id) values (0, 'ㅇㅇㅇㅇ', '글내용', 2, 1);

-- 해시태그 등록
insert into board_tag (board_id, tag) values (1, '직장인');
insert into board_tag (board_id, tag) values (1, '공대생');
insert into board_tag (board_id, tag) values (1, '취준생');
insert into board_tag (board_id, tag) values (2, '직장인');
insert into board_tag (board_id, tag) values (2, '공대생');
insert into board_tag (board_id, tag) values (2, '취준생');
insert into board_tag (board_id, tag) values (3, '직장인');
insert into board_tag (board_id, tag) values (3, '공대생');
insert into board_tag (board_id, tag) values (4, '취준생');



