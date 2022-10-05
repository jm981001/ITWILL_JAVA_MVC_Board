package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class JdbcUtil {
	
	public static Connection getConnection() {
		// DBCP 를 활용한 Connection 객체 객체 리턴하기
		// DB 연결 정보를 저장할 Connection 타입 변수 선언
		Connection con = null;
		
		// ---------------------------------------------------------------------------------------------------
		try {
			// context.xml 에 설정된 DBCP(커넥션풀)로부터 Connection 객체 가져오기
			// 1. 톰캣으로부터 context.xml 파일의 <Context> 태그 부분 객체 가져오기
			// => InitialContext 객체 생성하여 Context 타입으로 업캐스팅
			Context initCtx = new InitialContext();
			
			// 2. 생성된 Context 객체(initCtx)로부터 context.xml 파일의 <Resource> 태그 부분 객체 가져오기
			// => Context 객체의 lookup() 메서드를 호출하여 찾아올 리소스 지정
			// => 파라미터로 "java:comp/env" 문자열 전달
			// => 리턴되는 Object 타입 객체를 Context 타입 객체로 다운캐스팅하여 저장
//			Context envCtx = (Context)initCtx.lookup("java:comp/env");
			
			// 3. <Resource> 태그가 복수개 있을 경우 각각의 리소스를 구분하기 위해 지정한 이름(JNDI)을
			//    사용하여 해당 리소스 가져오기
			// => 생성된 Context 객체(envCtx)의 lookup() 메서드를 호출하여 찾아올 리소스의 이름(name 속성값) 지정
			// => 주의! context.xml 파일에 지정된 이름에 따라 문자열 내용은 변함
			// => 리턴되는 Object 타입 객체를 javax.sql.DataSource 타입으로 다운캐스팅하여 저장
//			DataSource ds = (DataSource)envCtx.lookup("jdbc/MySQL");
			
			// -- < 참고 > 위의 2번과 3번 작업을 하나로 결합 가능 --
			// 2번과 3번에서 지정한 문자열을 결합하여 하나의 lookup() 메서드를 통해 접근
			DataSource ds = (DataSource)initCtx.lookup("java:comp/env/jdbc/MySQL");
			
			// 4. 커넥션 풀을 관리하는 DataSource 객체로부터 미리 생성되어 있는 Connection 객체 가져오기
			// => DataSource 객체의 getConnection() 메서드 호출(리턴타입 : java.sql.Connection)
			con = ds.getConnection();
			
			// -------------------------- 추가사항(옵션) --------------------------
			// 트랜잭션 처리를 위해 데이터베이스(MySQL)의 Auto Commit 기능 해제
			// => Connection 객체의 setAutoCommit() 메서드를 호출하여 false 값 전달
			con.setAutoCommit(false);
			// => 주의! 이 이후로 DML(INSERT, UPDATE, DELETE) 및 DDL 등의 작업 수행 후
			//    반드시 commit 작업을 수동으로 실행해야함!
			//    (Connection 객체의 commit() 메서드 호출)
			// => 또한, 이전 상태로 되돌리려면 rollback 작업을 수동으로 실행해야함!
			//    (Connection 객체의 rollback() 메서드 호출)
			
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// --------------------------------------------------------------------------------------------------
		
		// DB 연결 정보가 저장된 Connection 객체 리턴
		return con;
	}
	
	public static void close(Connection con) {
		try {
			if(con != null) { con.close(); }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(PreparedStatement pstmt) {
		try {
			if(pstmt != null) { pstmt.close(); }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(ResultSet rs) {
		try {
			if(rs != null) { rs.close(); }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// -----------------------------------------------------------------
	// 트랜잭션 처리에 필요한 commit, rollback 작업을 수행할 메서드 정의
	// => 단, Connection 객체에 대해 Auto Commit 기능 해제 선행되어야 함
	// => 파라미터 : Connection 객체(con)
	public static void commit(Connection con) {
		try {
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void rollback(Connection con) {
		try {
			con.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
















