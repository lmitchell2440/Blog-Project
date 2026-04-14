import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/Home")
public class Home extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        
        PrintWriter out = response.getWriter();
        String contextPath = request.getContextPath();
        out.println("<html><head>"
            + "<link rel='stylesheet' href='" + contextPath + "/css/styles.css'/>"
            + "</head><body>");
        out.println("<p class='create-post'><a href='CreatePost'>Create New Post</a></p>");
        out.println("<h1>Featured Article</h1>");

        try (Connection conn = DBUtil.getConnection(getServletContext())) {
            // --- Featured article ---
            String featuredSql = "SELECT id, name, date FROM blogposts ORDER BY date DESC LIMIT 1";
            PreparedStatement featuredStmt = conn.prepareStatement(featuredSql);
            ResultSet featuredRs = featuredStmt.executeQuery();

            int featuredId = -1;
            if (featuredRs.next()) {
                featuredId = featuredRs.getInt("id");
                String featuredName = featuredRs.getString("name");
                String featuredDate = featuredRs.getString("date");

                out.println("<h2 class='featured-title'><a href='ViewPost?id=" + featuredId + "'>" + featuredName + "</a></h2>");
                out.println("<p class='featured-date'><em>" + featuredDate + "</em></p>");
                out.println("<hr>");
            }

            // --- Other articles table ---
            out.println("<h2>All Other Articles</h2>");
            out.println("<table border='1' cellpadding='5'>");
            out.println("<tr><th>Date</th><th>Name</th></tr>");

            String othersSql = "SELECT id, name, date FROM blogposts " +
                               (featuredId != -1 ? "WHERE id != ? " : "") +
                               "ORDER BY date DESC";
            PreparedStatement othersStmt = conn.prepareStatement(othersSql);

            if (featuredId != -1) {
                othersStmt.setInt(1, featuredId);
            }

            ResultSet othersRs = othersStmt.executeQuery();

            while (othersRs.next()) {
                int id = othersRs.getInt("id");
                String name = othersRs.getString("name");
                String date = othersRs.getString("date");

                out.println("<tr>");
                out.println("<td>" + date + "</td>");
                out.println("<td><a href='ViewPost?id=" + id + "'>" + name + "</a></td>");
                out.println("<td class='delete-col'>");
                out.println("<form method='post' action='DeletePost' style='display:inline;'>");
                out.println("<input type='hidden' name='id' value='" + id + "'/>");
                out.println("<input type='image' class='delete-col' src='" + contextPath + "/images/delete-icon.svg' alt='Delete' width='20' height='20'/>");
                out.println("</form>");
                out.println("</td>");
                out.println("</tr>");
            }

            out.println("</table>");

        } catch (Exception e) {
            e.printStackTrace(out);
        }

        out.println("</body></html>");
    }
}