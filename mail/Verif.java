import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@SuppressWarnings({ "serial" })
@WebServlet("/Verif")
public class Verif extends HttpServlet{
	String user;
	String mail;
	public void service( HttpServletRequest req, HttpServletResponse res )
			throws ServletException, IOException{
		res.setContentType("text/html;charset=UTF-8");
		PrintWriter page = res.getWriter();
		HttpSession session = req.getSession(true);
		user = (String) session.getAttribute("user");
		if (session.getAttribute("user")==null){
			res.sendRedirect("../Dark/login.html?from=/Dark/Verif");
		}else{
			page.println("<!DOCTYPE html><head><meta charset=\"utf-8\" />"
					+ "<style>\n"
					+ "body{\n"
					+ "color:#C35B00\n"
					+ "}\n"
					+ "a{\n"
					+ "color:yellow\n"
					+ "}\n"
					+ "h1{\n"
					+ "color:red\n"
					+ "}\n"
					+ "h2{\n"
					+ "color:red\n"
					+ "}\n"
					+ "th{\n"
					+ "color:#129133\n"
					+ "}\n"
					+ "label{\n"
					+ "color:red\n"
					+ "}\n"
					+ "</style>"
					+ "</head>"
					+ "<body background=\"http://www.pix-me.fr/wp-content/uploads/2016/08/Quel-interet-d-externaliser-la-securite-informatique-de-son-entreprise-.jpg\">"
					+ "<body><center>");
			page.println("<h1>Mail de confirmation</h1>");
			Connection connection = null;
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:login");
				PreparedStatement stmt = connection.prepareStatement("select mail from mail where user=?");
				stmt.setString(1, user);
				ResultSet rs = stmt.executeQuery();
				rs.next();
				mail = rs.getString(1);
				System.out.println("(verif)Tentative d'envoi de mail pour "+user+" sur "+mail);
				Mailing.sendConfirmMail(mail, user);
				//AdminLogs.add("Envoi de mail de confirmation pour "+user+" effectué");
				page.println("<div>Mail envoyé, vérifiez votre boite de réception et éventuellement vos spams.</div>");				
				page.println("<a href=\"Menu\">Retour</a> ");
				page.println("</center></body></html>");
				connection.close();
			}catch (Exception e){
				//AdminLogs.add("Erreur de l'envoi de mail de confirmation pour "+user);
				page.println("<div>Mail non envoyé. Contactez l'administrateur.</div>");				
				page.println("<a href=\"Menu\">Retour</a> ");
				page.println("</center></body></html>");
				e.printStackTrace();
			}
		}
	}
	
}