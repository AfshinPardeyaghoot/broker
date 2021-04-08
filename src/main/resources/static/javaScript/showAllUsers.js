$(document).ready(function () {
    $("#table tr").click(function () {
        $(this).addClass('selected').siblings().removeClass('selected');
        var value = $(this).find('td:last').html();
        $(".userNationalIdForRequest").val(value);
        $(".userNationalIdForEdit").val(value);
        $(".userNationalIdForChangePass").val(value);
        $(".userNationalIdForLockStatus").val(value);
    });
})
