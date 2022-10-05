package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import svc.BoardReplyProService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardReplyProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = null;
		
		// ----------------------------------------------------------------------------
		// 파일 수정 작업에 필요한 MultipartRequest 객체 작업
		int fileSize = 1024 * 1024 * 10; // 파일 업로드 최대 크기(10MB)
		String uploadFolder = "upload"; // 업로드 가상 경로(이클립스)
		String realFolder = ""; // 업로드 실제 경로(톰캣)
		realFolder = request.getServletContext().getRealPath(uploadFolder);
		
		MultipartRequest multi = new MultipartRequest(
				request, // 1) 실제 요청 정보(파라미터)가 포함된 request 객체
				realFolder, // 2) 실제 업로드 되는 폴더 경로
				fileSize, // 3) 업로드 파일 최대 크기
				"UTF-8", // 4) 한글 파일명을 처리하기 위한 인코딩 방식
				new DefaultFileRenamePolicy() // 5) 중복 파일명에 대한 기본 처리 담당 객체 생성(파일명 뒤에 숫자 붙임)
		);
		
		// ----------------------------------------------------------------------------
		// 페이지번호 파라미터 가져오기
		// => multipart/form-data 타입으로 전달되므로 MultipartRequest 객체로부터 가져오기
		String pageNum = multi.getParameter("pageNum");
		
		// BoardBean 객체 생성 후 폼 파라미터 저장
		BoardBean board = new BoardBean();
		board.setBoard_num(Integer.parseInt(multi.getParameter("board_num")));
		board.setBoard_name(multi.getParameter("board_name"));
		board.setBoard_pass(multi.getParameter("board_pass"));
		board.setBoard_subject(multi.getParameter("board_subject"));
		board.setBoard_content(multi.getParameter("board_content"));
		String fileElement = multi.getFileNames().nextElement().toString();
		board.setBoard_file(multi.getOriginalFileName(fileElement)); // 원본 파일명
		board.setBoard_real_file(multi.getFilesystemName(fileElement)); // 실제 업로드 된 파일명
		board.setBoard_re_ref(Integer.parseInt(multi.getParameter("board_re_ref"))); // 참조글번호
		board.setBoard_re_lev(Integer.parseInt(multi.getParameter("board_re_lev"))); // 들여쓰기레벨
		board.setBoard_re_seq(Integer.parseInt(multi.getParameter("board_re_seq"))); // 순서번호
//		System.out.println(board);
		
		// BoardReplyProService 객체의 registReplyBoard() 메서드를 호출하여 답글 등록 요청
		// => 파라미터 : BoardBean 객체     리턴타입 : boolean(isReplySuccess)
		BoardReplyProService service = new BoardReplyProService();
		boolean isReplySuccess = service.registReplyBoard(board);
		
		// 답글 등록 요청 결과 판별
		// => 실패 시 자바스크립트를 사용하여 "답글 등록 실패!" 출력 후 이전페이지로 돌아가기
		//    아니면, 글목록(BoardList.bo) 서블릿 주소 요청(파라미터 : 페이지번호)
		if(!isReplySuccess) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('답글 쓰기 실패!')");
			out.println("history.back()");
			out.println("</script>");
		} else {
			forward = new ActionForward();
			forward.setPath("BoardList.bo?pageNum=" + pageNum);
			forward.setRedirect(true);
		}
		
		return forward;
	}

}


















