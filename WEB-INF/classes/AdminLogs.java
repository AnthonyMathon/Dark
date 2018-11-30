import java.io.*;
import java.text.*;
import java.util.*;

public class AdminLogs {

	private static File log = new File("log.txt");
	private static PrintWriter out;
	
	public static void add(String s) throws IOException{
		String base = "";
		InputStream ips = new FileInputStream(log); 
		InputStreamReader ipsr = new InputStreamReader(ips);
		BufferedReader logs = new BufferedReader(ipsr);
		String ligne;
		ligne=logs.readLine();
		while (ligne!=null){
			base = base + ligne + "\n";
			ligne=logs.readLine();
		}
		logs.close();
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat ("dd/MM/yyyy - HH:mm");
		String dat = formatter.format(date);
		out = new PrintWriter(log);
		out.println(base);
		out.println(dat+" - "+s);
		out.close();
	}
	
	public static String getlogs() throws IOException{
		String s = "<div>";
		InputStream ips = new FileInputStream(log); 
		InputStreamReader ipsr = new InputStreamReader(ips);
		BufferedReader logs = new BufferedReader(ipsr);
		String ligne;
		ligne=logs.readLine();
		while (ligne!=null){
			s = s + ligne + "<br>";
			ligne=logs.readLine();
		}
		logs.close();
		s = s + "</div>";
		return s;
	}
	
}