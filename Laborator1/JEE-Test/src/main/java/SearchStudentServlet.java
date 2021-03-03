import beans.StudentBean;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SearchStudentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nume = request.getParameter("nume");
        File file = new File("/home/andy/SD/LAB1/JEE-Test/student.xml");
        if (!file.exists()) {
            response.sendError(404, "Nu a fost gasit niciun student serializat pe disc!");
            return;
        }
        Scanner sc = new Scanner(file);
        List<StudentBean> students = new ArrayList<>();
        String studentXml;
        XmlMapper xmlMapper = new XmlMapper();
        while(sc.hasNext()) {
            studentXml = sc.nextLine();
            if(studentXml.contains("<nume>" + nume + "</nume>"))
                students.add(xmlMapper.readValue(studentXml, StudentBean.class));
        }
        if(students.size() == 0) {
            response.sendError(404, "Nu exista niciun student cu numele " + nume + "!");
            return;
        }

        request.setAttribute("studenti", students);
        request.getRequestDispatcher("./view-students.jsp").forward(request, response);

    }
}
