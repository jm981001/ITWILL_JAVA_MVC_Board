package svc;

import java.sql.Connection;
import java.util.List;

import dao.BoardDAO;
import db.JdbcUtil;
import vo.BoardBean;

public class BoardListService {
	// 전체 게시물 수 조회 작업을 요청하는 getListCount() 메서드 정의
	public int getListCount() {
//		System.out.println("BoardListService - getListCount()");
		
		int listCount = 0;
		
		// 공통작업-1. Connection 객체 가져오기
		Connection con = JdbcUtil.getConnection();
		
		// 공통작업-2. BoardDAO 객체 가져오기
		BoardDAO dao = BoardDAO.getInstance();
		
		// 공통작업-3. BoardDAO 객체에 Connection 객체 전달하기
		dao.setConnection(con);
		
		// BoardDAO 객체의 selectListCount() 메서드를 호출하여 게시물 총 갯수 가져오기
		// => 파라미터 : 없음   리턴타입 : int(listCount)
		listCount = dao.selectListCount();
		
		// 게시물 수 조회 작업의 경우 commit or rollback 이 불필요하므로 트랜잭션 처리 과정 없음
		
		// 공통작업-4. Connection 객체 반환
		JdbcUtil.close(con);
		
		return listCount;
	}

	// 전체 게시물 목록 조회 작업을 요청하는 getBoardList() 메서드 정의
	// => 파라미터 : 페이지번호(pageNum), 페이지 당 목록 갯수(listLimit)
	//    리턴타입 : List<BoardBean>(boardList)
	public List<BoardBean> getBoardList(int pageNum, int listLimit) {
		List<BoardBean> boardList = null;
		
		// 공통작업-1. Connection 객체 가져오기
		Connection con = JdbcUtil.getConnection();
		
		// 공통작업-2. BoardDAO 객체 가져오기
		BoardDAO dao = BoardDAO.getInstance();
		
		// 공통작업-3. BoardDAO 객체에 Connection 객체 전달하기
		dao.setConnection(con);
		
		// BoardDAO 객체의 selectBoardList() 메서드를 호출하여 게시물 목록 가져오기
		// => 파라미터 : 페이지번호(pageNum), 페이지 당 목록 갯수(listLimit)
		//    리턴타입 : List<BoardBean>(boardList)
		boardList = dao.selectBoardList(pageNum, listLimit);
		
		// 게시물 목록 조회 작업의 경우 commit or rollback 이 불필요하므로 트랜잭션 처리 과정 없음
		
		// 공통작업-4. Connection 객체 반환
		JdbcUtil.close(con);
		
		return boardList;
	}

}













