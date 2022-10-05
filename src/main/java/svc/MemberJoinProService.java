package svc;

import java.sql.Connection;

import dao.BoardDAO;
import dao.MemberDAO;
import db.JdbcUtil;
import vo.MemberBean;

public class MemberJoinProService {

	public boolean registMember(MemberBean member) {
		boolean isRegistSuccess=false;
		// 공통작업-1. Connection 객체 가져오기
		Connection con = JdbcUtil.getConnection();

		// 공통작업-2. BoardDAO 객체 가져오기
		MemberDAO dao = MemberDAO.getInstance();

		// 공통작업-3. BoardDAO 객체에 Connection 객체 전달하기
		dao.setConnection(con);


		int Count=dao.insertMember(member);

		// 게시물 수 조회 작업의 경우 commit or rollback 이 불필요하므로 트랜잭션 처리 과정 없음
		
		// 트랜잭션 처리
		if(Count > 0) {
			JdbcUtil.commit(con);
			// isModifySuccess 를 true 로 변경
			isRegistSuccess = true;
		} else {
			JdbcUtil.rollback(con);
		}
		// 공통작업-4. Connection 객체 반환
		JdbcUtil.close(con);

		return isRegistSuccess;
	}
}
