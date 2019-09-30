<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<body>
<section>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form method="post" action="meals">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt>Дата и время</dt>
            <dd><input type="datetime-local" name="localDateTime" size=25 value="${meal.dateTime}"></dd>
            <dt>Наименование блюда</dt>
            <dd><input type="text" name="description" size=50 value="${meal.description}"></dd>
            <dt>Количество калорий</dt>
            <dd><input type="number" name="calories" size=10 value="${meal.calories}"></dd>
        </dl>
        <button type="submit">Сохранить</button>
        <button type="reset" onclick="window.history.back()">Отменить</button>
    </form>
</section>
</body>
</html>