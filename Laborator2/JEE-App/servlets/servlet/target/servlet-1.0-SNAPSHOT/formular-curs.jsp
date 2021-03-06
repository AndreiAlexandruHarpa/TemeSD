<html xmlns:jsp="http://java.sun.com/JSP/Page">
	<head>
		<title>Formular curs</title>
		<meta charset="UTF-8" />
	</head>
	<body>
		<h3>Formular curs</h3>
		Introduceti datele despre curs:
		<form action="./process-curs" method="post">
			Nume: <input type="text" name="nume" />
			<br />
			Titular: <input type="text" name="titular" />
			<br />
			Credite: <input type="number" name="credite" />
			<br />
			<br />
			<button type="submit" name="submit">Trimite</button>
		</form>
	</body>
</html>