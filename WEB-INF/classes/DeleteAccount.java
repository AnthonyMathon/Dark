import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings({ "serial", "unused" })
@WebServlet("/DeleteAccount")
public class DeleteAccount extends HttpServlet{
	String user;
	String login;
	String mdp;
	String site;
	String info;
	public void service( HttpServletRequest req, HttpServletResponse res )
			throws ServletException, IOException{
		res.setContentType("text/html;charset=UTF-8");
		PrintWriter page = res.getWriter();
		HttpSession session = req.getSession(true);
		if (session.getAttribute("user")==null){
			res.sendRedirect("../Dark/login.html");
		}else{
		Connection connection = null;
		user = (String) session.getAttribute("user");
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
		page.println("<h1>Creation d'un nouvel utilisateur</h1>");
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:login");
			PreparedStatement stmt = connection.prepareStatement("delete from login where user=?");
			stmt.setString(1, user);
			stmt.executeUpdate();
			stmt = connection.prepareStatement("delete from auth where user=?");
			stmt.setString(1, user);
			stmt.executeUpdate();
			connection.close();
			System.out.println("Reussite de la suppression de "+user);
			AdminLogs.add("Reussite de la suppression de "+user);
			page.println("<div>Suppression du compte effectuee.</div><br>");
		}catch (Exception e){
			page.println("<div>Erreur de suppression du compte...</div><br>"
					+ "<div>Contactez l'administrateur si vous pensez avoir vu ça par erreur</div><br>");
					System.out.println("Erreur de suppression du compte "+user);
					AdminLogs.add("Erreur de suppression du compte "+user);
			e.printStackTrace();
		}finally{
			session.invalidate();
			try {
				connection.close();
			} catch (SQLException e) {}
			page.println("<a href=\"../Dark/login.html\">Retour</a> ");
		}
	}
	}
	
}