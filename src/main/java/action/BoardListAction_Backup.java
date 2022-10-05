package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vo.ActionForward;

public class BoardListAction_Backup {
	
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("BoardListAction");
		
		// 글목록 작업..........................
		
		
		// 글목록 저장 객체를 가지고 qna_board_list.jsp 페이지로 포워딩
//		request.setAttribute("boardList", boardList);
		// Dispatcher 방식 포워딩 설정
		ActionForward forward = new ActionForward();
		forward.setPath("board/qna_board_list.jsp");
		forward.setRedirect(false);
		return forward;
		
	}
	
}













