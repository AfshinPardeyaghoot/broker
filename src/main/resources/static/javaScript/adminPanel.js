$(document).ready(function () {
    $("#table tr").click(function () {
        $(this).addClass('selected').siblings().removeClass('selected');
        var value = $(this).find('td:first').html();
        $(".request-id-show").val(value);
        $(".request-id-answer").val(value);
    });
})
