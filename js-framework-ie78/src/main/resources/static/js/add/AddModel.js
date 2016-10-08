/**
 * Created by jojoldu@gmail.com on 16. 10. 8.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

define([],
function() {
    return Backbone.Model.extend({

        defaults: {
            input1: 0,
            input2: 0
        },

        set : function (obj) {
            var self = this;
            _.each(obj, function(value, key){
                self.defaults[key] = parseInt(value);
            });
        },

        get : function (key){
            return this.defaults[key];
        }
    });
});