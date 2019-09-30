<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
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
        <button name="action" type="submit" value="add">Создать запись</button>
    </form>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>Дата и время</th>
            <th>Блюдо</th>
            <th>Кол-во калорий</th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
            <style>
                .red {
                    color: red
                }

                .green {
                    color: green
                }
            </style>
            <tr class="${meal.excess ? 'red' : 'green'}">
                <td>${meal.dateTime.format(TimeUtil.DATE_TIME_FORMATTER)}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?id=${meal.id}&action=edit">Ред.</a></td>
                <td><a href="meals?id=${meal.id}&action=delete">Удалить</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>