import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.sql.*;

@SuppressWarnings({ "serial", "unused" })
@WebServlet("/Deconnect")
public class Deconnect extends HttpServlet {
	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		String user = (String) session.getAttribute("user");
		if (session.getAttribute("user")==null){
			res.sendRedirect("../Dark/login.html");
		}else{
			System.out.println("Deconnexion de "+user);
			AdminLogs.add(user+" s'est déconnecté");
			session.invalidate();
			res.sendRedirect("../Dark/login.html");
		}
	}
}
