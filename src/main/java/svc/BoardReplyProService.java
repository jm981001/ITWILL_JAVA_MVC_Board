package svc;

import java.sql.Connection;

import dao.BoardDAO;
import db.JdbcUtil;
import vo.BoardBean;

public class BoardReplyProService {

	public boolean registReplyBoard(BoardBean board) {
		// 1. 답글쓰기 작업 요청 처리 결과를 저장할 boolean 타입 변수 선언
		boolean isReplySuccess = false;
		
		// 2. JdbcUtil 객체로부터 Connection Pool 에 저장된 Connection 객체 가져오기(공통)
		Connection con = JdbcUtil.getConnection();
		
		// 3. BoardDAO 클래스로부터 BoardDAO 객체 가져오기(공통)
		BoardDAO dao = BoardDAO.getInstance();
		
		// 4. BoardDAO 객체의 setConnection() 메서드를 호출하여 Connection 객체 전달(공통)
		dao.setConnection(con);
		
		// 5. insertReplyBoard() 메서드를 호출하여 답글쓰기 작업 수행 및 결과 리턴받기
		// => 파라미터 : BoardBean 객체   리턴타입 : int(insertCount)
		int insertCount = dao.insertReplyBoard(board);
		
		// 6. 작업 처리 결과에 따른 트랜잭션 처리  
		if(insertCount > 0) { // 작업 성공했을 경우
			JdbcUtil.commit(con); // Service 클래스가 관리하는 Connection 객체 필요
			isReplySuccess = true;
		} else { // 작업 실패했을 경우
			JdbcUtil.rollback(con);
		}
		
		// 7. Connection Pool 로부터 가져온 Connection 객체 반환(공통)
		JdbcUtil.close(con);
		
		// 8. 작업 요청 처리 결과 리턴
		return isReplySuccess; 
	}

}
