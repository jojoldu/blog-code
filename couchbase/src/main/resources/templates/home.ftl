<html>
    <head></head>
    <body>
        <h1>${hello}</h1>
        <h2>${uid}</h2>

        <form action="/user" method="post">
            <input type ="text" name="name">
            <input type ="text" name="email">
            <input type ="text" name="homepage">

            <button type="submit">등록</button>
        </form>
    </body>
</html>