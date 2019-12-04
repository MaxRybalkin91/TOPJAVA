// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/meals/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "desc"
                    ]
                ]
            })
        }
    );
});

function filter() {
    $.ajax({
        type: "GET",
        url: context.ajaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableWithFilter);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(context.ajaxUrl, updateTableWithFilter);
}