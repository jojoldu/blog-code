/**
 * Created by jojoldu@gmail.com on 2016-10-11.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
define([],
function () {
    return Backbone.Model.extend({
        defaults: {
            name : null,
            email : null
        }
    });
});