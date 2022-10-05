package action;

import java.io.File;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import svc.BoardDeleteProService;
import svc.BoardModifyProService;
import vo.ActionForward;
import vo.BoardBean;

public class BoardModifyProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardModifyProAction");
		
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
		// 글번호, 페이지번호, 패스워드 파라미터 가져오기
		// => multipart/form-data 타입으로 전달되므로 MultipartRequest 객체로부터 가져오기
		int board_num = Integer.parseInt(multi.getParameter("board_num"));
		String pageNum = multi.getParameter("pageNum");
		String board_pass = multi.getParameter("board_pass");
//		System.out.println(board_num + ", " + pageNum + ", " + board_pass);
		
		// BoardModifyProService 클래스의 isBoardWriter() 메서드 호출하여 패스워드 일치 여부 확인
		// => 파라미터 : 글번호, 패스워드    리턴타입 : boolean(isBoardWriter)
		BoardModifyProService service = new BoardModifyProService();
		boolean isBoardWriter = service.isBoardWriter(board_num, board_pass);
		
		// 만약, 게시물 수정 권한이 없는 경우(= 패스워드 틀린 경우)
		// 자바스크립트 사용하여 "수정 권한이 없습니다!" 출력 후 이전 페이지로 돌아가기
		if(!isBoardWriter) { // 수정 권한이 없는 경우
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('수정 권한이 없습니다!')");
			out.println("history.back()");
			out.println("</script>");
		} else { // 수정 권한이 있는 경우
			// BoardBean 객체 생성 후 폼 파라미터 저장
			BoardBean board = new BoardBean();
			board.setBoard_num(board_num);
			board.setBoard_name(multi.getParameter("board_name"));
			board.setBoard_pass(board_pass);
			board.setBoard_subject(multi.getParameter("board_subject"));
			board.setBoard_content(multi.getParameter("board_content"));
			String fileElement = multi.getFileNames().nextElement().toString();
			board.setBoard_file(multi.getOriginalFileName(fileElement)); // 원본 파일명
			board.setBoard_real_file(multi.getFilesystemName(fileElement)); // 실제 업로드 된 파일명
//			System.out.println(board.getBoard_file() + ", " + board.getBoard_real_file());
			
			// Service 클래스의 modifyBoard() 메서드를 호출하여 게시물 수정 작업 요청
			// => 파라미터 : BoardBean 객체    리턴타입 : boolean(isModifySuccess)
			boolean isModifySuccess = service.modifyBoard(board);
			
			// 수정 요청 결과 판별
			// => 실패 시 자바스크립트를 통해 "수정 실패!" 출력 후 이전페이지로 돌아가기
			//    아니면, ActionForward 객체를 사용하여 BoardDetail.bo 서블릿 주소 요청(Redirect)
			//    (서블릿 주소 요청 시 글번호와 페이지번호를 파라미터로 함께 전달) 
			if(!isModifySuccess) {
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('수정 실패!')");
				out.println("history.back()");
				out.println("</script>");
			} else {
				// ----------- 기존 업로드 파일 삭제 작업 추가 -----------
				// board_file 이 null 이 아닐 경우에만 작업 수행
				if(board.getBoard_file() != null) {
					// java.io.File 객체 생성
					// => 파라미터 : 실제 업로드 폴더명, 삭제할 실제 파일명
					File f = new File(realFolder, multi.getParameter("board_real_file"));
					
					// 생성한 파일 객체에서 지정된 파일이 존재할 경우에만 삭제
					if(f.exists()) {
						f.delete();
					}
				}
				// -------------------------------------------------------
				
				forward = new ActionForward();
				forward.setPath("BoardDetail.bo?board_num=" + board_num + "&pageNum=" + pageNum);
				forward.setRedirect(true); // Redirect 방식
			}
		}
		
		
		return forward;
	}

}
