<html xmlns:jsp="http://java.sun.com/JSP/Page">
	<head>
		<title>Informatii student</title>
		<meta charset="UTF-8" />
	</head>
	<body>
		<h3>Informatii student</h3>
		<!-- populare bean cu informatii din cererea HTTP -->
		<%@ page import="beans.StudentBean" %>
		<%@ page import="java.util.ArrayList" %>
		<ul>
		<%
		    ArrayList<StudentBean> students = new ArrayList<StudentBean>();
		    students = (ArrayList<StudentBean>) request.getAttribute("studenti");
		%>
		    <ol>
		<%
		    for(int i = 0; i < students.size(); i++) {
		%>
		        <li>
                    <ul type="bullet">
                        <li>Nume: <%=students.get(i).getNume()%> </li>
                        <li>Prenume: <%=students.get(i).getPrenume()%></li>
                        <li>Varsta: <%=students.get(i).getVarsta()%></li>
                        <li>Telefon: <%=students.get(i).getTelefon()%></li>
                        <li>Email: <%=students.get(i).getEmail()%></li>
                    </ul>
                </li>
            <br/><br/>
		<%
		    }
		%>
		    </ol>
	</body>
</html>