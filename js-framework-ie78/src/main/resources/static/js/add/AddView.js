/**
 * Created by jojoldu@gmail.com on 2016-10-07.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
define(["Calculator"], //require->define으로 변경했다. 즉시실행할 필요가 없어져서.
function(Calculator) {
    return Backbone.View.extend({

        // view 객체 생성시 진행할 코드들
        initialize: function () {
            $('.inputs').on('keyup', this.render);
        },

        render : function() {
            var a = $('#input1').val(),
                b = $('#input2').val();
            var sum = Calculator.add(parseInt(a), parseInt(b));

            $('#result').val(sum);
        }
    });
});