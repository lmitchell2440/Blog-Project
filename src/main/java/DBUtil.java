import java.sql.Connection;
import java.sql.DriverManager;
import java.io.File;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletContext;
public class DBUtil {

	private static final String URL = "jdbc:sqlite:C:/temp/blogposts.db";

	public static Connection getConnection(ServletContext context) throws Exception {

	    String path = context.getRealPath("/WEB-INF/blogposts.db");

	    System.out.println("DB Path: " + path);

	    Class.forName("org.sqlite.JDBC");
	    return DriverManager.getConnection("jdbc:sqlite:" + path);
	}
}
