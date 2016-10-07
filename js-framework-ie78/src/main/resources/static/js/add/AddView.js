/**
 * Created by jojoldu@gmail.com on 2016-10-07.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
define(["Calculator"],
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