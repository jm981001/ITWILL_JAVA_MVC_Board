package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.BoardListAction_Backup;
import action.BoardWriteProAction_Backup;
import vo.ActionForward;

// 요청 및 응답에 대한 흐름을 제어하는 FrontController 클래스 정의
//@WebServlet("*.bo")
public class BoardFrontController_Backup extends HttpServlet {
	
	// GET 방식 & POST 방식 요청에 대한 공통 작업을 수행하는 doProcess() 메서드 정의
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println("BoardFrontController");
		
		// POST 방식에 대한 한글 처리
		request.setCharacterEncoding("UTF-8");
		
		// 서블릿 주소 추출
		String command = request.getServletPath();
		System.out.println("command : " + command);
		
		// 공통으로 사용할 변수 선언
		ActionForward forward = null; // 포워딩 정보를 저장할 ActionForward 타입
		
		// 요청받은 서블릿 주소 판별을 통해 각각 다른 작업 수행
		// BoardWriteForm.bo => qna_board_write.jsp
		// BoardWritePro.bo
		// BoardList.bo => qna_board_list.jsp
		if(command.equals("/BoardWriteForm.bo")) {
			// 글쓰기 폼 페이지 요청
			// 포워딩 대상이 뷰페이지(*.jsp)인 경우 Dispatcher 방식으로 포워딩
			// (별다른 비즈니스 로직이 필요하지 않을 경우)
			// 현재 서블릿 주소(= 요청 주소)가 루트(http://localhost:8080/MVC_Board/BoardWriteForm.bo) 이다.
			// => 주의! webapp 폴더(루트) 내의 board 폴더에 있는 qna_board_write.jsp 페이지
//			RequestDispatcher dispatcher = request.getRequestDispatcher("board/qna_board_write.jsp");
//			dispatcher.forward(request, response);
			
			// 공통으로 포워딩 작업을 처리하기 위해 ActionForward 객체 생성
			forward = new ActionForward();
			forward.setPath("board/qna_board_write.jsp");
			forward.setRedirect(false);
		} else if(command.equals("/BoardWritePro.bo")) {
			// 글쓰기 비즈니스 작업 요청
			// 비즈니스 작업을 처리할 Action 클래스의 인스턴스 생성 후 execute() 메서드를 호출
			// => 파라미터 : HttpServletRequest 객체, HttpServletResponse 객체
			BoardWriteProAction_Backup action = new BoardWriteProAction_Backup();
			forward = action.execute(request, response);
			
			// 포워딩 방식 판별
//			if(forward.isRedirect()) { // Redirect 방식
//				response.sendRedirect(forward.getPath());
//			} else { // Dispatcher 방식
//				RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
//				dispatcher.forward(request, response);
//			}
		} else if(command.equals("/BoardList.bo")) {
			// 글목록 비즈니스 작업 요청 
			// 비즈니스 작업을 처리할 Action 클래스의 인스턴스 생성 후 execute() 메서드를 호출
			BoardListAction_Backup action = new BoardListAction_Backup();
			forward = action.execute(request, response);
		}
		
		// --------------------------------------------------------------------------------
		// ActionForward 객체 내용에 따라 각각 다른 포워딩 작업 수행(포워딩 작업 공통 처리)
		// 1. ActionForward 객체가 null 이 아닐 경우 판별
		if(forward != null) {
			// 2. ActionForward 객체에 저장된 포워딩 방식 판별
			if(forward.isRedirect()) { // Redirect 방식
				response.sendRedirect(forward.getPath());
			} else { // Dispatcher 방식
				RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
				dispatcher.forward(request, response);
			}
		}
		// --------------------------------------------------------------------------------
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

}





