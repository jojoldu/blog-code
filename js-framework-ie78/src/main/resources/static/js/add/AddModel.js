/**
 * Created by jojoldu@gmail.com on 16. 10. 8.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

define(["Calculator"],
function(Calculator) {

    return Backbone.Model.extend({
        // Model 객체 생성시 defaults를 기준으로 관리해야될 데이터(attributes)를 생성
        defaults: {
            input1: 0,
            input2: 0,
            result: 0
        },

        setInputs : function (obj) {
            var input1 = parseInt(obj.input1),
                input2 = parseInt(obj.input2),
                result = Calculator.add(input1, input2);

            // Model 객체의 attributes에 입력 받은 새로운 값을 set
            this.set({input1: input1, input2: input2, result:result});
        }
    });
});