<html xmlns:jsp="http://java.sun.com/JSP/Page">
	<head>
		<title>Formular curs</title>
		<meta charset="UTF-8" />
	</head>
	<body>
		<h3>Formular curs</h3>
		Date actuale:
		<br/>
		Nume: <%=request.getParameter("nume")%>
        <br />
        titular: <%=request.getParameter("titular")%>
        <br />
        credite: <%=request.getParameter("credite")%>
        <br/><br/>
		Introduceti datele despre curs:
		<form action="./update-course" method="post">
			Nume: <input type="text" name="nume" value='<%=request.getParameter("nume")%>'/>
			<br />
			Titular: <input type="text" name="titular" value='<%=request.getParameter("titular")%>'/>
			<br />
			credite: <input type="number" name="credite" value='<%=request.getParameter("credite")%>'/>
			<input type="hidden" name="id" value='<%=request.getParameter("id")%>'/>
			<br />
			<br />
			<button type="submit" name="submit">Actualizeaza</button>
		</form>
		<br/>
		<a href="./fetch-curs-list">Inapoi la cursuri</a>
	</body>
</html>