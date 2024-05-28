package net.daum.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.daum.vo.MemberVO;

public interface AdminMemberRepository extends JpaRepository<MemberVO, String> {

	@Modifying
	@Query("update MemberVO m set m.mem_pwd=?1,m.mem_name=?2,m.mem_zip=?3, m.mem_zip2=?4,"
			+"mem_addr=?5,mem_addr2=?6,mem_phone01=?7,mem_phone02=?8,mem_phone03=?9, "
			+"mail_id=?10,mail_domain=?11,m.mem_state=?12,m.mem_delcont=?13 "
			+" where mem_id=?14")
	public void adminMember_Update(String pwd,String name,String zip,String zip2,String addr,
			String addr2,String phone01,String phone02,String phone03,String mail_id,
			String mail_domain,int state,String delcont,String id); //아이디를 기준으로 관리자로 사용자
	//회원정보 수정
}
