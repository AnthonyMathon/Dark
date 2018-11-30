import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.sql.*;


@SuppressWarnings("serial")
@WebServlet("/Voir")
public class Voir extends HttpServlet {
	String user;
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
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
		HttpSession session = req.getSession(true);
		if (session.getAttribute("user")==null){
			res.sendRedirect("../Dark/login.html?from=/Dark/Voir");
		}else{
		user = (String) session.getAttribute("user");
		page.println("<div class='page-header'>"
				+ "<script src=\"dist/clipboard.min.js\"></script>");
		page.println("<h1>Vos informations</h1>");
		page.println("</div>");
		  
		
		page.println("<div class='row'>");
		page.println("<div class='col-xs-12'>");
		Connection con =null;
		try {
		    
		    // enregistrement du driver
		    Class.forName("org.sqlite.JDBC");
		    
		    // connexion a la base
		    con = DriverManager.getConnection("jdbc:sqlite:login");
		    
		    // execution de la requete
		    PreparedStatement stmt = con.prepareStatement("Select * from login where user=? order by site asc");
		    stmt.setString(1, user);
		    ResultSet rs = stmt.executeQuery();	    
		    page.println("<table class='table table-bordered table-striped'>");
		    
		    page.print("<thead>");
			page.print("<tr>");
		    page.print("<th>jeu/site</th>");
		    page.print("<th>infos</th>");
		    page.print("<th>Pseudo/mail</th>");
		    page.print("<th>mot de passe</th>");
		    //page.print("<th>Copier le mdp</th>"); 
		    page.print("<th>Suppression</th>");
		    page.print("<th>Partage</th>");
		    page.println("</tr>");
		    page.print("</thead>");
		    
		    page.print("<tbody>");
		    
		    while(rs.next())
			{
			    page.println("<tr>");
			    page.print("<td>"+rs.getString(3)+"</td>");
			    page.print("<td>"+rs.getString(4)+"</td>");
			    page.print("<td>"+rs.getString(2)+"</td>");
			    page.print("<td>"+rs.getString(5)+"</td>");
			    //page.print("<td>"+"<button id=\"copy-button\" type=\"button\" class=\"btn btn-lg btn-default dropdown-toggle red-btn zclip\" data-clipboard-text=\""+rs.getString(5)+"\">Copie</button>"+"</td>");
			    page.print("<td><a href=\"Suppr?site="+rs.getString(3)+"&info="+rs.getString(4)+"&login="+rs.getString(2)+"&mdp="+rs.getString(5)+"\">Supprimer</a></td>");
			    page.print("<td><a href=\"Share?site="+rs.getString(3)+"&info="+rs.getString(4)+"&login="+rs.getString(2)+"&mdp="+rs.getString(5)+"\">Partager</a></td>");
			    page.println("</tr>");
			}
		    
			page.print("</tbody>");
		    page.println("</table>");
		   /* if (cpt>0){
		    	page.println("<br><br><br>");
			    page.println("<div>Supprimer une donnee :"
			    + "<form method=\"post\" action=\"../Dark/Suppr\">"
				+ "<div class=\"form-group\">"
				+ "<label for=\"site\" class=\"control-label\">Site/Jeu</label>"
				+ "<input type=\"text\" class=\"form-control\" id=\"site\" name=\"site\" value=\"\">"
				+ "</div>"
				+ "<div class=\"form-group\">"
				+ "<label for=\"info\" class=\"control-label\">Infos supplementaires</label>"
				+ "<input type=\"text\" class=\"form-control\" id=\"info\" name=\"info\" value=\"\">"
				+ "</div>"
				+ "<div class=\"form-group\">"
				+ "<label for=\"login\" class=\"control-label\">Pseudo/mail</label>"
				+ "<input type=\"text\" class=\"form-control\" id=\"login\" name=\"login\" value=\"\">"
				+ "</div>"
				+ "<div class=\"form-group\">"
				+ "<label for=\"mdp\" class=\"control-label\">Mot de passe</label>"
				+ "<input type=\"text\" class=\"form-control\" id=\"mdp\" name=\"mdp\" value=\"\">"
				+ "</div>"
				+ "<button type=\"submit\" class=\"btn btn-success btn-block\">Supprimer</button>"
				+ "</form>");
		    }
		    */
		    page.println("<br><br><br>");
		    page.println("<h2>Infos partagées avec vous</h2>");
		    stmt = con.prepareStatement("select * from share where with=?");
		    stmt.setString(1, user);
		    rs = stmt.executeQuery();
		    page.println("<table class='table table-bordered table-striped'>");
		    
		    page.print("<thead>");
			page.print("<tr>");
			page.print("<th>Propriétaire</th>");
		    page.print("<th>jeu/site</th>");
		    page.print("<th>infos</th>");
		    page.print("<th>Pseudo/mail</th>");
		    page.print("<th>mot de passe</th>");
		    //page.print("<th>Copier le mdp</th>");
		    page.print("<th>STOP</th>");
		    page.println("</tr>");
		    page.print("</thead>");
		    int i = 0;
		    while(rs.next())
			{
		    	i++;
		    	page.println("<input id=\""+i+"\" value=\""+rs.getString(5)+"\">");
			    page.println("<tr>");
			    page.print("<td>"+rs.getString(1)+"</td>");
			    page.print("<td>"+rs.getString(3)+"</td>");
			    page.print("<td>"+rs.getString(4)+"</td>");
			    page.print("<td>"+rs.getString(2)+"</td>");
			    page.print("<td>"+rs.getString(5)+"</td>");
			    
			   // page.print("<td>"+"<button class=\"button\" id=\"copy-button\" data-clipboard-target="+i+">Copier</button>"+"</td>");
			    page.print("<td><a href=\"Unshare?site="+rs.getString(3)+"&info="+rs.getString(4)+"&login="+rs.getString(2)+"&mdp="+rs.getString(5)+"&with="+rs.getString(6)+"&from="+rs.getString(1)+"\">Supprimer</a></td>");
			    page.println("</tr>");
			}
		    
			page.print("</tbody>");
		    page.println("</table>");
		    
		    page.println("<br><br><br>");
		    page.println("<h2>Infos que vous avez partagées</h2>");
		    stmt = con.prepareStatement("select * from share where user=?");
		    stmt.setString(1, user);
		    rs = stmt.executeQuery();
		    page.println("<table class='table table-bordered table-striped'>");
		    
		    page.print("<thead>");
			page.print("<tr>");
			page.print("<th>Pour</th>");
		    page.print("<th>jeu/site</th>");
		    page.print("<th>infos</th>");
		    page.print("<th>Pseudo/mail</th>");
		    page.print("<th>mot de passe</th>");
		    //page.print("<th>Copier le mdp</th>");
		    page.print("<th>STOP</th>");
		    page.println("</tr>");
		    page.print("</thead>");
		    
		    while(rs.next())
			{
			    page.println("<tr>");
			    page.print("<td>"+rs.getString(6)+"</td>");
			    page.print("<td>"+rs.getString(3)+"</td>");
			    page.print("<td>"+rs.getString(4)+"</td>");
			    page.print("<td>"+rs.getString(2)+"</td>");
			    page.print("<td>"+rs.getString(5)+"</td>");
			    //page.print("<td>"+"<button id=\"copy-button\" type=\"button\" class=\"btn btn-lg btn-default dropdown-toggle red-btn zclip\" data-clipboard-text=\""+rs.getString(5)+"\">Copie</button>"+"</td>");
			    page.print("<td><a href=\"Unshare?site="+rs.getString(3)+"&info="+rs.getString(4)+"&login="+rs.getString(2)+"&mdp="+rs.getString(5)+"&with="+rs.getString(6)+"&from="+rs.getString(1)+"\">Supprimer</a></td>");
			    page.println("</tr>");
			}
		    
			page.print("</tbody>");
		    page.println("</table>");
		    
		    page.print("<tbody>");
		    page.println("<br><br><br>");
		    page.println("<a href=\"Menu\">Retour au Menu principal</a> ");
		    AdminLogs.add(user+" vient d'accéder à ses données");
		}
		catch (Exception e) {			
			System.out.println("Erreur soulevée pour "+user+" :");
			e.printStackTrace();
		}
		finally
		    {
			try{con.close();} catch (Exception e){}
		    }
				
				page.println("</div>");
			page.println("</div>");
		page.println("</div>");
		page.println("<script src=\"../Dark/dist/clipboard.min.js\"></script>");
		page.println("</body></html>");
		page.close();
	    }
	}
}
