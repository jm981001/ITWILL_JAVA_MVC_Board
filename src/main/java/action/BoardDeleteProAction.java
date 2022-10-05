package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardDeleteProService;
import vo.ActionForward;

public class BoardDeleteProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardDeleteProAction");
		
		ActionForward forward = null;
		
		// 글번호, 페이지번호, 패스워드 파라미터 가져오기
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		String pageNum = request.getParameter("pageNum");
		String board_pass = request.getParameter("board_pass");
//		System.out.println(board_num + ", " + pageNum + ", " + board_pass);
		
		// BoardDeleteProService 클래스의 isBoardWriter() 메서드 호출하여 패스워드 일치 여부 확인
		// => 파라미터 : 글번호, 패스워드    리턴타입 : boolean(isBoardWriter)
		BoardDeleteProService service = new BoardDeleteProService();
		boolean isBoardWriter = service.isBoardWriter(board_num, board_pass);
		
		// 만약, 게시물 삭제 권한이 없는 경우(= 패스워드 틀린 경우)
		// 자바스크립트 사용하여 "삭제 권한이 없습니다!" 출력 후 이전 페이지로 돌아가기
		if(!isBoardWriter) { // 삭제 권한이 없는 경우
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('삭제 권한이 없습니다!')");
			out.println("history.back()");
			out.println("</script>");
		} else { // 삭제 권한이 있는 경우
			// Service 클래스의 removeBoard() 메서드를 호출하여 게시물 삭제 작업 요청
			// => 파라미터 : 글번호    리턴타입 : boolean(isDeleteSuccess)
			boolean isDeleteSuccess = service.removeBoard(board_num);
			
			// 삭제 요청 결과 판별
			// => 실패 시 자바스크립트를 통해 "삭제 실패!" 출력 후 이전페이지로 돌아가기
			//    아니면, ActionForward 객체를 사용하여 BoardList.bo 서블릿 주소 요청(Redirect)
			//    (서블릿 주소 요청 시 페이지번호를 파라미터로 함께 전달) 
			if(!isDeleteSuccess) {
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('삭제 실패!')");
				out.println("history.back()");
				out.println("</script>");
			} else {
				forward = new ActionForward();
				forward.setPath("BoardList.bo?pageNum=" + pageNum);
				forward.setRedirect(true); // Redirect 방식
			}
		}
		
		
		return forward;
	}

}












