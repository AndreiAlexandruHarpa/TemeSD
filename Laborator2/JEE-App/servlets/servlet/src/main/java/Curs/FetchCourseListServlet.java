package Curs;

import ejb.CourseEntity;
import ejb.StudentEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class FetchCourseListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManagerFactory factory =   Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        EntityManager em = factory.createEntityManager();

        StringBuilder responseText = new StringBuilder();
        responseText.append("<h2>Lista cursuri</h2>");
        responseText.append("<table border='1'><thead><tr><th>ID</th><th>Nume</th><th>Titular</th><th>Credite</th></thead>");
        responseText.append("<tbody>");

        TypedQuery<CourseEntity> query = em.createQuery("select curs from CourseEntity curs", CourseEntity.class);
        List<CourseEntity> results = query.getResultList();
        for (CourseEntity curs : results) {
            responseText.append("<tr><td>" + curs.getId() + "</td><td>" +
                    curs.getNume() + "</td><td>" + curs.getTitular() +
                    "</td><td>" + curs.getCredite() +
                    "</td><td><a href=\"./update-curs.jsp?id=" + curs.getId() +
                    "&nume=" + curs.getNume() +
                    "&titular=" + curs.getTitular() +
                    "&credite=" + curs.getCredite() + "\">Actualizeaza</a>" +
                    "</td><td><a href=\"./delete-course?id=" + curs.getId() + "\">Sterge</a></td></tr>");
        }

        responseText.append("</tbody></table><br /><br /><a href='./'>Inapoi la meniul principal</a>");

        em.close();
        factory.close();

        response.setContentType("text/html");
        response.getWriter().print(responseText.toString());
    }
}
