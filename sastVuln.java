import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AdditionalVulnerabilities {

    // 1. Path Traversal (High)
    public String readConfig(HttpServletRequest request) throws Exception {

        String fileName = request.getParameter("file");

        File file = new File("/opt/app/config/" + fileName);

        return java.nio.file.Files.readString(file.toPath());
    }

    // 2. Unrestricted File Write (High)
    public void saveReport(HttpServletRequest request) throws Exception {

        String fileName = request.getParameter("name");

        FileWriter writer = new FileWriter("/tmp/" + fileName);

        writer.write("Report");

        writer.close();
    }

    // 3. Log Forging / Log Injection (Medium)
    public void logUser(HttpServletRequest request) throws Exception {

        String username = request.getParameter("username");

        PrintWriter log = new PrintWriter(new FileWriter("app.log", true));

        log.println("User login: " + username);

        log.close();
    }

    // 4. Second-Order SQL Injection (High)
    public void updateUser(HttpServletRequest request) throws Exception {

        String role = request.getParameter("role");

        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/test",
                "user",
                "pass");

        PreparedStatement ps = conn.prepareStatement(
                "UPDATE users SET role='" + role + "'");

        ps.execute();
    }
}