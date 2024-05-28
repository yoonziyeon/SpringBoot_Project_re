alter sequence gongji_no_seq
nocache; --nocache로 수정
 
alter sequence gongji_no_seq
nocycle; --nocycle로 수정

--gongji 테이블 생성 쿼리문
create table gongji(
  gongji_no number(38) primary key --공지번호
  ,gongji_name varchar2(100) not null --공지작성자
  ,gongji_title varchar2(200) not null --공지제목
  ,gongji_pwd varchar2(50) not null--비번
  ,gongji_cont varchar2(4000) not null --공지내용
  ,gongji_hit int default 0 --조회수
  ,gongji_date date --등록날짜
);

select * from gongji order by gongji_no desc;

--샘플 더미 데이터 저장
insert into gongji (gongji_no,gongji_name,gongji_title,gongji_pwd,gongji_cont,
gongji_hit,gongji_date)
values(gongji_no_seq.nextval,'관리자','공지제목','7777','공지 내용입니다',0,sysdate);

--gongji_no_seq 시퀀스 생성문
create sequence gongji_no_seq
start with 1 --1부터 시작
increment by 1 --1씩증가
nocache
nocycle;

--생성된 gongji_no_seq시퀀스 번호값 확인
select gongji_no_seq.nextval as "시퀀스 번호값" from dual;

/*  문제) 최신 공지글 5개를 검색하는 select 서브쿼리문을 만들어 보자 */
select * from (select gongji_no,gongji_title,gongji_date from gongji order by gongji_no desc)
where rownum < 6;




