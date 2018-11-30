import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings({ "serial", "unused" })
@WebServlet("/NewMail")
public class NewMail extends HttpServlet{
	String user;
	String mail;
	public void service( HttpServletRequest req, HttpServletResponse res )
			throws ServletException, IOException{
		res.setContentType("text/html;charset=UTF-8");
		PrintWriter page = res.getWriter();
		HttpSession session = req.getSession(true);
		user = (String) session.getAttribute("user");
		if (session.getAttribute("user")==null){
			res.sendRedirect("../Dark/login.html");
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
			page.println("<h1>Modification du mail</h1>");
			Connection connection = null;
			mail = req.getParameter("mail");
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:login");
				PreparedStatement stmt = connection.prepareStatement("update mail set mail=? where user=?");
				stmt.setString(1, mail);
				stmt.setString(2, user);
				stmt.executeUpdate();
				connection.close();
				System.out.println("Reussite de changement de mail pour "+user+": défini avec "+mail);
				page.println("<div>Modification du mail effectuée. Pensez à bien le noter.</div><br>");
				AdminLogs.add("Modification du mail de "+user+" effectuée.");
				page.println("<a href=\"Menu\">Retour</a> ");
			}catch (Exception e){
				page.println("<div>Erreur dans la modification du mail.</div><br>"
						+ "<div>Contactez l'administrateur si vous pensez avoir vu ça par erreur</div><br>");
				page.println("<a href=\"Menu\">Retour</a> ");
				System.out.println("Erreur de changement de mail pour "+user);
				AdminLogs.add("Erreur de changement de mail pour "+user);
				e.printStackTrace();
				
			}finally{
				try{
					connection.close();
				}catch (Exception e){}
			}
		}
	}
}