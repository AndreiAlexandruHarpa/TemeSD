import beans.StudentBean;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProcessStudentServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // se citesc parametrii din cererea de tip POST
        String nume = request.getParameter("nume");
        String prenume = request.getParameter("prenume");
        String telefon = request.getParameter("telefon");
        String email = request.getParameter("email");
        int varsta = Integer.parseInt(request.getParameter("varsta"));
        /*
        procesarea datelor - calcularea anului nasterii
         */

        int anCurent = Year.now().getValue();
        int anNastere = anCurent - varsta;

        // initializare serializator Jackson
        XmlMapper mapper = new XmlMapper();

        // creare bean si populare cu date

        StudentBean bean = new StudentBean();
        bean.setNume(nume);
        bean.setPrenume(prenume);
        bean.setVarsta(varsta);
        bean.setTelefon(telefon);
        bean.setEmail(email);

        // serializare bean sub forma de string XML

        FileWriter fw = new FileWriter("/home/andy/SD/LAB1/JEE-Test/student.xml", true);
        String studentBeanXml = mapper.writeValueAsString(bean) + "\n";
        fw.append(studentBeanXml);
        fw.close();

        // se trimit datele primite si anul nasterii catre o alta pagina JSP pentru afisare
        request.setAttribute("nume", nume);
        request.setAttribute("prenume", prenume);
        request.setAttribute("varsta", varsta);
        request.setAttribute("anNastere", anNastere);
        request.setAttribute("telefon", telefon);
        request.setAttribute("email", email);
        request.getRequestDispatcher("./info-student.jsp").forward(request, response);
    }
}