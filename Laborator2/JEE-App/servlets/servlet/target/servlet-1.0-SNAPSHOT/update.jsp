<html xmlns:jsp="http://java.sun.com/JSP/Page">
	<head>
		<title>Formular student</title>
		<meta charset="UTF-8" />
	</head>
	<body>
		<h3>Formular student</h3>
		Date actuale:
		<br/>
		Nume: <%=request.getParameter("nume")%>
        <br />
        Prenume: <%=request.getParameter("prenume")%>
        <br />
        Varsta: <%=request.getParameter("varsta")%>
        <br/><br/>
		Introduceti datele despre student:
		<form action="./update-student" method="post">
			Nume: <input type="text" name="nume" value='<%=request.getParameter("nume")%>'/>
			<br />
			Prenume: <input type="text" name="prenume" value='<%=request.getParameter("prenume")%>'/>
			<br />
			Varsta: <input type="number" name="varsta" value='<%=request.getParameter("varsta")%>'/>
			<input type="hidden" name="id" value='<%=request.getParameter("id")%>'/>
			<br />
			<br />
			<button type="submit" name="submit">Actualizeaza</button>
		</form>
		<br/>
		<a href="./fetch-student-list">Inapoi la lista studentilor</a>
	</body>
</html>