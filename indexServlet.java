
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class indexServlet
 */
@WebServlet("/")
public class indexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private attendanceWeb attend;
	private  String hello;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public indexServlet() {
        super();
        this.attend = new attendanceWeb();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		RequestDispatcher dispatcher = request.getRequestDispatcher("/view/index.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("id");	//入力されたデータ
		String seat = attend.getSeat(id);
		String name = attend.getName(id);
		String str = attend.attendedMember();
		String notAttendStr = attend.sortNotAttendedMember();
		this.hello = "<h2>こんにちは【" + name + "】さん。<br>あなたの座席番号は【" + seat + "】です。</h2>";

		request.setAttribute("seat", seat);
		request.setAttribute("name", name);
		request.setAttribute("hello", hello);
		request.setAttribute("str", str);
		request.setAttribute("notAttendStr", notAttendStr);

		request.getRequestDispatcher("view/index.jsp").forward(request, response);
	}

}
