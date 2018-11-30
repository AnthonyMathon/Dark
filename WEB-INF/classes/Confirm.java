import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings({ "serial", "unused" })
@WebServlet("/Confirm")
public class Confirm extends HttpServlet{
	String mail;
	public void service( HttpServletRequest req, HttpServletResponse res )
			throws ServletException, IOException{
		res.setContentType("text/html;charset=UTF-8");
		PrintWriter page = res.getWriter();
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
		try {
			mail = req.getParameter("mail");
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:login");
			PreparedStatement stmtmail = connection.prepareStatement("select user from mail where mail=?");
			stmtmail.setString(1, mail);
			ResultSet rs = stmtmail.executeQuery();
			rs.next();
			PreparedStatement stmt = connection.prepareStatement("update verify set verif=1 where user=?");
			String user = rs.getString(1);
			stmt.setString(1, user);
			stmt.executeUpdate();
			page.println("<div>Mail Confirmé, vous pouvez vous connecter.</div>");				
			page.println("<a href=\"../Dark/login.html\">Retour</a> ");
		}catch (Exception e){
			e.printStackTrace();
			page.println("<div>Mail non confirmé. Contactez l'administrateur.</div>");				
			page.println("<a href=\"../Dark/login.html\">Retour</a> ");
		}finally{
			try{
				connection.close();
			}catch (Exception e){}
		}
		
	}
	
}