import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings({ "serial", "unused" })
@WebServlet("/CheckIP")
public class CheckIP extends HttpServlet{
	String user;
	String IP;
	String IPregion;
	String regionAttendue;
	String redirect;
	public void service( HttpServletRequest req, HttpServletResponse res )
			throws ServletException, IOException{
		res.setContentType("text/html;charset=UTF-8");
		PrintWriter page = res.getWriter();
		HttpSession session = req.getSession(true);
		user = (String) session.getAttribute("user");
		if (session.getAttribute("user")==null){
			res.sendRedirect("../Dark/login.html?from=/Dark/Verif");
		}else{
			page.println("<!DOCTYPE html>"
					+ "<html lang=\"fr\"><head>"
					+ "<!--définition du codage des caractères-->"
					+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
					+"<!-- Donne le nom de l\'onglet -->"
					+"<title>Connexion</title>" 
					+"<!-- Donne le nom de l\'auteur-->"
					+"<meta name=\"auteur\" content=\"Anthony\" />"
					+"<!-- liens-->"
					+"<link rel= \"stylesheet\" href=\"data/style.css\" type=\"text/css\" media=\"screen\" />"
					+"<script src=\"Jquery-1.11.1.min.js\"></script>"
					+"</head>"
					+ "<body><center>");
			page.println("<h1>Mail de confirmation</h1>");
			Connection connection = null;
			redirect = req.getParameter("redirect");
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:login");
				PreparedStatement stmtm = connection.prepareStatement("select mail from mail where user=?");
				stmtm.setString(1, user);
				ResultSet rsm = stmtm.executeQuery();
				rsm.next();
				String mail = rsm.getString(1);
				VerifyIP checker = new VerifyIP();
				IP = req.getRemoteAddr();
				IPregion = checker.getRegion(IP);
				PreparedStatement stmt = connection.prepareStatement("select localise from IP where user=?");
				stmt.setString(1, user);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()){
					regionAttendue=rs.getString(1);
					if (!regionAttendue.equals(IPregion)){
						Mailing.sendWarningMail(mail, user);
						PreparedStatement stmtr = connection.prepareStatement("insert into warn values(?, ?)");
						stmtr.setString(1, mail);
						stmtr.setInt(2, 1);
						stmtr.executeUpdate();
					}else{
						res.sendRedirect(redirect);
					}
				}
			}catch (Exception e){
			}finally{
				try{
					res.sendRedirect(redirect);
					connection.close();
				}catch (Exception e){}
			}
		}
	}
	
}