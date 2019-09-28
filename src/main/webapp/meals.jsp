<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Список блюд</title>
</head>
<body>
<section>
    <form method="get">
        Дневная норма калорий
        <input type="number" name="calories" size=10 value="${MealTo.getCaloriesPerDay()}">
        <input type="hidden" name="action" value="setCalories">
        <button type="submit">Сохранить</button>
    </form>
    <form method="get">
        <input type="hidden" name="action" value="fill">
        <button type="submit">Заполнить таблицу</button>
    </form>
    <form method="get">
        <button name="action" type="submit" value="add">Создать запись</button>
    </form>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>Дата и время</th>
            <th>Блюдо</th>
            <th>Кол-во калорий</th>
        </tr>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
            <c:set var="color" value='<%=meal.isExcess() ? "red" : "green"%>'/>
            <style>
                table {
                    color: black;
                }

                td {
                    color: ${color};
                }
            </style>
            <tr>
                <td>${meal.getStringDateTime()}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>