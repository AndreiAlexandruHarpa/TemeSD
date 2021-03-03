<html xmlns:jsp="http://java.sun.com/JSP/Page">
	<head>
		<title>Informatii student</title>
		<meta charset="UTF-8" />
	</head>
	<body>
		<h3>Informatii student</h3>

		<!-- populare bean cu informatii din cererea HTTP -->
		<jsp:useBean id="studentBean" class="beans.StudentBean" scope="request" />
		<jsp:setProperty name="studentBean" property="nume" value='<%= request.getAttribute("nume") %>'/>
		<jsp:setProperty name="studentBean" property="prenume" value='<%= request.getAttribute("prenume") %>'/>
		<jsp:setProperty name="studentBean" property="varsta" value='<%= request.getAttribute("varsta") %>'/>
		<jsp:setProperty name="studentBean" property="telefon" value='<%= request.getAttribute("telefon") %>'/>
		<jsp:setProperty name="studentBean" property="email" value='<%= request.getAttribute("email") %>'/>

		<!-- folosirea bean-ului pentru afisarea informatiilor -->
		<p>Urmatoarele informatii au fost introduse:</p>
		<form action="./process-student" method="post">
		    Nume: <input type="text" name="nume" value='<%= request.getAttribute("nume") %>'/>
		    <br/>
		    Prenume: <input type="text" name="prenume" value='<%= request.getAttribute("prenume") %>'/>
		    <br/>
		    Varsta: <input type="text" name="varsta" value='<%= request.getAttribute("varsta") %>'/>
		    <br/>
		    Telefon: <input type="text" name="telefon" value='<%= request.getAttribute("telefon") %>'/>
		    <br/>
		    Email: <input type="text" name="email" value='<%= request.getAttribute("email") %>'/>
		    <br/><br/>
		    <button type="submit" name="submit">Actualizare</button>
		</form>

	</body>
</html>