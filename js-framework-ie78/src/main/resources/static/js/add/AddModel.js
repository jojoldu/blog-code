/**
 * Created by jojoldu@gmail.com on 16. 10. 8.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

define([],
function() {
    return Backbone.Model.extend({

        defaults:{
            input1: 0,
            input2: 0
        },

        setInput1: function (num) {
            this.defaults.input1 = num;
        },
        setInput2: function (num) {
            this.defaults.input2 = num;
        },
        getInput1: function () {
            return this.defaults.input1;
        },
        getInput2: function () {
            return this.defaults.input2;
        }
    });
});