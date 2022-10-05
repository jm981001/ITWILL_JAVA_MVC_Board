package svc;

import java.sql.Connection;

import dao.BoardDAO;
import db.JdbcUtil;
import vo.BoardBean;

public class BoardDetailService {

//	// 게시물 조회 요청
//	// => 파라미터 : 글번호(board_num)    리턴타입 : BoardBean(board)
//	public BoardBean getBoard(int board_num) {
//		BoardBean board = null;
//		
//		// 공통작업-1. Connection 객체 가져오기
//		Connection con = JdbcUtil.getConnection();
//		
//		// 공통작업-2. BoardDAO 객체 가져오기
//		BoardDAO dao = BoardDAO.getInstance();
//		
//		// 공통작업-3. BoardDAO 객체에 Connection 객체 전달하기
//		dao.setConnection(con);
//		
//		// BoardDAO 클래스의 updateReadcount() 메서드 호출하여 게시물 조회수 증가 작업 수행
//		// => 파라미터 : 글번호    리턴타입 : void
//		dao.updateReadcount(board_num);
//   
//		// BoardDAO 클래스의 selectBoard() 메서드 호출하여 게시물 상세 정보 조회 작업 수행 후
//        // 리턴되는 BoardBean 객체 전달받아 저장
//		// => 파라미터 : 글번호    리턴타입 : BoardBean(board)
//		board = dao.selectBoard(board_num);
//		
//		// 만약, 리턴받은 BoardBean 객체를 리턴받아 null 일 경우 rollback,
//		// 아니면 commit 작업 수행
//		if(board == null) {
//			JdbcUtil.rollback(con);
//		} else {
//			JdbcUtil.commit(con);
//		}
//		
//		// 공통작업-4. Connection 객체 반환
//		JdbcUtil.close(con);
//		
//		return board;
//	}
	
	// 게시물 조회 요청
	// => 파라미터 : 글번호(board_num)    리턴타입 : BoardBean(board)
	// => 추가사항 : 실제 조회와 수정 및 삭제를 위한 조회 구분을 위해 파라미터 Strign work 추가
	public BoardBean getBoard(int board_num, String work) {
		BoardBean board = null;
		
		// 공통작업-1. Connection 객체 가져오기
		Connection con = JdbcUtil.getConnection();
		
		// 공통작업-2. BoardDAO 객체 가져오기
		BoardDAO dao = BoardDAO.getInstance();
		
		// 공통작업-3. BoardDAO 객체에 Connection 객체 전달하기
		dao.setConnection(con);
		
		// BoardDAO 클래스의 updateReadcount() 메서드 호출하여 게시물 조회수 증가 작업 수행
		// => 파라미터 : 글번호    리턴타입 : void
//		dao.updateReadcount(board_num);
		
		// 추가사항 => 실제 조회 시에만 조회수 증가하도록 work 변수값이 "detail" 인지 판별
		if(work.equals("detail")) {
			dao.updateReadcount(board_num);
		}
   
		// BoardDAO 클래스의 selectBoard() 메서드 호출하여 게시물 상세 정보 조회 작업 수행 후
        // 리턴되는 BoardBean 객체 전달받아 저장
		// => 파라미터 : 글번호    리턴타입 : BoardBean(board)
		board = dao.selectBoard(board_num);
		
		// 만약, 리턴받은 BoardBean 객체를 리턴받아 null 일 경우 rollback,
		// 아니면 commit 작업 수행
		if(board == null) {
			JdbcUtil.rollback(con);
		} else {
			JdbcUtil.commit(con);
		}
		
		// 공통작업-4. Connection 객체 반환
		JdbcUtil.close(con);
		
		return board;
	}
	
}













