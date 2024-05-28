create table member( --회원 관리 테이블
  mem_id varchar2(50) primary key --회원 아이디
  ,mem_pwd varchar2(200) not null --비밀번호
  ,mem_name varchar2(50) not null --회원이름
  , mem_zip varchar2(10) not null --우편번호
  , mem_zip2 varchar2(10) not null -- 우편번호
  , mem_addr varchar2(200) not null --주소
  , mem_addr2 varchar2(100) not null -- 나머지 주소
  , mem_phone01 varchar2(10) --첫번째 자리 폰번호
  , mem_phone02 varchar2(10) --두번째 자리 폰번호
  , mem_phone03 varchar2(10) --세번째 자리 폰번호
  , mail_id varchar2(100) -- 메일 아이디
  , mail_domain varchar2(100) --메일 도메인
  , mem_date date --가입날짜
  , mem_state number(38) --가입회원 1, 탈퇴회원 2
  , mem_delcont varchar2(4000) -- 탈퇴사유
  , mem_deldate date -- 탈퇴날짜
  );
  
  select * from member order by mem_id asc;
  
  update member set mem_deldate=null, mem_delcont=null, mem_state=1 where mem_id='bbbbb';
  
  commit;
  
  drop table member;
  
  delete from member where mem_id='ccccc';
  commit;
  
  ALTER TABLE member MODIFY (mem_delcont VARCHAR2(4000));
 
 --회원가입폼에서 아이디 중복 검색을 위한 샘플 더비 데이터 회원 저장
 insert into member (mem_id, mem_pwd,mem_name,mem_zip,mem_zip2,mem_addr,mem_addr2,mem_phone01,
 mem_phone02,mem_phone03,mail_id,mail_domain,mem_date,mem_state) values('aaaaa','77777','홍길동','123',
 '678','서울시 강남구 테헤란로길','00빌딩 00호','010','999','7777','aaaaa','hanmail.net',sysdate,1);
 
 commit;
 --이클립스는 오토 커밋이지만 SQL Development는 오토 커밋이 아니다.그러므로 insert,delete,update 쿼리문 수행
 --후 반드시 commit을 해야지만 회원가입폼 아이디 중복검색등의 인터넷 웹에서 제대로 된 테스트를 할 수 있다.
  
--우편/주소 테이블(zipcode)
create table zipcode(
 no number(38) primary key
 ,zipcode varchar2(20) --우편번호
 ,sido varchar2(50) --시도
 ,gugun varchar2(50) --구군
 ,dong varchar2(50) --읍면동,길주소
 ,bunji varchar2(50) --번지
);

insert into zipcode (no,zipcode,sido,gugun,dong,bunji) values(
zip_no_seq.nextval,'047-69','서울시','성동구','왕십리로','83-21 에스엠엔터테인먼트');

select * from zipcode;

-- 서울 성동구 왕십리로 83-21 에스엠엔터테인먼트

delete from zipcode;

alter sequence zip_no_seq
nocache; --nocache로 수정
 
alter sequence zip_no_seq
nocycle; --nocycle로 수정
 




