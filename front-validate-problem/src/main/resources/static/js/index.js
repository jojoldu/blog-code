/**
 * Created by jojoldu@gmail.com on 2017. 4. 25.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

var $tbody = $('#tbody');

var findAll = function () {
    $.ajax({
        url:'/members',
        method: 'GET',
        success: function (response) {
            var source   = $("#table-template").html();
            var template = Handlebars.compile(source);
            var html = template({members:response});
            $tbody.html('');
            $tbody.html(html);
        }
    });
};

$('#btnSave').click(function () {
    var member = {
        name: $('#name').val(),
        phoneNumber: $('#phoneNumber').val(),
        email: $('#email').val()
    };

    var isValid = true;

    if(!member.name){
        markingErrorMessage('#name', '이름을 작성해주세요');
        isValid = false;
    }

    if(!member.phoneNumber){
        markingErrorMessage('#phoneNumber', '휴대폰 번호를 작성해주세요');
        isValid = false;
    }

    if(!member.email){
        markingErrorMessage('#email', '이메일을 작성해주세요');
        isValid = false;
    }

    if(isValid){
        $.ajax({
            url:'/member',
            method: 'POST',
            data: JSON.stringify(member),
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                findAll();
            },
            error: function (response) {
                alert('오류 발생');
            }
        });
    }
});

var markingErrorMessage = function (targetElement, message) {
    var $targetElement = $(targetElement);
    $targetElement.siblings('.error-message').remove();
    $targetElement.after('<span class="error-message text-muted taxt-small text-danger">'+message+'</span>');
};

findAll();