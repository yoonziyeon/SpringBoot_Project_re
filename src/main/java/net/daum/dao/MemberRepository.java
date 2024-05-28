package net.daum.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.daum.vo.MemberVO;

public interface MemberRepository extends JpaRepository<MemberVO, String> {

	@Query("select m from MemberVO m where m.mem_id=?1 and m.mem_name=?2")
	public MemberVO pwdFind(String id,String name);//?2는 두번째로 전달되어지는 인자값, 아이디와 회원이름을 기준
	//으로 비번을 검색
	
	@Modifying //@Query 애노테이션은 select문만 가능하지만 @Modifying을 이용해서 DML(update,insert,delete)
	//문 작업 처리가 가능하다.
	@Query("update MemberVO m set m.mem_pwd=?1 where m.mem_id=?2")
	public void updatePwd(String pwd,String id); //아이디를 기준으로 암호화된 임시비번으로 수정
	
    @Query("select m from MemberVO m where m.mem_id=?1 and m.mem_state=1")
    public MemberVO loginCheck(String id);//아이디와 가입회원 1인 경우만 로그인 인증 처리
    
    @Modifying
    @Query("update MemberVO m set m.mem_pwd=?1, m.mem_name=?2, m.mem_zip=?3, m.mem_zip2=?4,"
    		+"m.mem_addr=?5,m.mem_addr2=?6,mem_phone01=?7,mem_phone02=?8,mem_phone03=?9,"
    		+ "mail_id=?10,mail_domain=?11 where mem_id=?12")
    public void updateMember(String pwd, String name,String zip,String zip2,String addr,
    		String addr2,String phone01,String phone02,String phone03,String mail_id,
    		String mail_domain,String id);//회원 정보 수정
    
}







