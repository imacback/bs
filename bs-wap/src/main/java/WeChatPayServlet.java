import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class WeChatPayServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8735995591700388812L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doProcess(req,resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	doProcess(req,resp);
	}

	private void doProcess(HttpServletRequest req, HttpServletResponse resp) {


	}

}
