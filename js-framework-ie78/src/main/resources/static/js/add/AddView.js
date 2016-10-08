/**
 * Created by jojoldu@gmail.com on 2016-10-07.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
define(["Calculator",
        "add/AddModel"], //require->define으로 변경했다. 즉시실행할 필요가 없어져서.
function(Calculator, AddModel) {
    return Backbone.View.extend({
        model : null,
        $inputs : null,

        // view 객체 생성시 진행할 코드들
        initialize: function () {
            this['events'] = {
                'keyup .inputs' : this.set
            };
            this.model = new AddModel();

            this.listenTo(this.model, "change", this.render); // view에 이벤트 바인드가 필요한 경우 listenTo를 사용한다

            $('#input1').val(this.model.get('input1'));
            $('#input2').val(this.model.get('input2'));
        },

        set : function() {
            this.model.set({'input1': $('#input1').val(), 'input2': $('#input2').val()});
        },

        render : function() {
            var sum = Calculator.add(this.model.get('input1'), this.model.get('input1'));
            $('#result').val(sum);
        }
    });
});