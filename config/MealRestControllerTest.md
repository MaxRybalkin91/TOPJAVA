getAll()

curl -s http://localhost:8080/topjava/rest/meals

get(100002)

curl -s http://localhost:8080/topjava/rest/meals/100002

getBetween()

curl -s "http://localhost:8080/topjava/rest/meals/between?startDate=2015-05-30&endDate=2015-05-30"

delete(100002)

curl -s -X DELETE http://localhost:8080/rest/meals/100002

create()

curl -s -X POST -d '{"dateTime":"2015-06-01T18:00","description":"Созданный ужин","calories":300}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/topjava/rest/meals


update()

curl -s -X PUT -d '{"dateTime":"2015-05-30T10:00", "description":"Обновленный завтрак", "calories":200}' -H 'Content-Type: application/json' http://localhost:8080/topjava/rest/meals/100002