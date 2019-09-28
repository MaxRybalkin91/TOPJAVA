<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<body>
<section>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal"/>
    <form method="post" action="" enctype="application/x-www-form-urlencoded">
        <dl>
            <dt>Дата</dt>
            <dt>Наименование блюда</dt>
            <dd><input type="text" name="description" size=50 value="${meal.description}"></dd>
            <dt>Количество калорий</dt>
            <dd><input type="text" name="calories" size=10 value="${meal.calories}"></dd>
        </dl>
        <button type="submit">Сохранить</button>
        <button type="reset" onclick="window.history.back()">Отменить</button>
    </form>
</section>
</body>
</html>