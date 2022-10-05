package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.JdbcUtil;
import vo.BoardBean;
import vo.MemberBean;

public class MemberDAO {
	private MemberDAO() {}
	
	private static MemberDAO instance = new MemberDAO();

	public static MemberDAO getInstance() {
		return instance;
	}
	// ----------------------------------------------------------------------------------------
	// 데이터베이스 접근에 사용될 Connection 객체를 Service 클래스로부터 전달받기 위한
	// 멤버변수 및 Setter 메서드 정의
	private Connection con;

	public void setConnection(Connection con) {
		this.con = con;
	}
	// Service 클래스로부터 BoardBean 객체를 전달받아 글쓰기 작업 수행 및 결과 리턴
	// => 파라미터 : BoardBean 객체   리턴타입 : int(insertCount)
	public int insertMember(MemberBean member) {
		System.out.println("MemberDAO - insertMember()");
		
		// INSERT 작업 결과를 리턴받아 저장할 변수 선언
		int insertCount = 0;
		
		// 데이터베이스 작업에 필요한 변수 선언
		PreparedStatement pstmt = null, pstmt2 = null;
		ResultSet rs = null;
		
		try {
			// board 테이블의 게시물 최대 번호를 조회하여 새 글번호 결정(+1)
			int idx = 1; // 새 글 번호를 저장할 변수 선언(기본값으로 1 설정)
			String sql = "SELECT MAX(idx) FROM member";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			// 기존 게시물이 존재하여 최대 번호가 조회되었을 경우
			if(rs.next()) {
				// 새 글 번호 = 현재 최대 글 번호 + 1
				idx = rs.getInt(1) + 1;
				System.out.println("새 글 번호 : " + idx);
			}
			
			// 전달받은 데이터(BoardBean 객체)를 사용하여 INSERT 작업 수행
			// => 참조글번호(board_re_ref)는 새 글 번호와 동일한 번호로 지정
			// => 들여쓰기레벨(board_re_lev)과 순서번호(board_re_seq)는 0으로 지정
			sql = "INSERT INTO member VALUES (?,?,?,?,?,?,now())";
			pstmt2 = con.prepareStatement(sql);
			pstmt2.setInt(1, idx); // 글번호(board_num)
			pstmt2.setString(2, member.getName()); // 작성자(board_name)
			pstmt2.setString(3, member.getId()); // 작성자(board_name)
			pstmt2.setString(4, member.getPasswd()); // 제목(board_subject)
			pstmt2.setString(5, member.getEmail()); // 실제 업로드 파일명(board_file)
			pstmt2.setString(6, member.getGender()); // 실제 업로드 파일명(board_file)
		
			
			// INSERT 구문 실행 후 리턴값을 insertCount 변수에 저장
			insertCount = pstmt2.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("SQL 구문 오류 발생! - insertBoard()");
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
			JdbcUtil.close(pstmt2);
//			JdbcUtil.close(con); // 주의! DAO 객체 내에서 Connection 객체 반환하지 않도록 주의할 것!
			// => Service 클래스에서 commit, rollback 작업을 수행하기 위해 접근해야 하기 때문
		}
		
		return insertCount; // BoardWriteProService 로 리턴
	}
	
	
	
	
}
