/**
 * Created by jojoldu@gmail.com on 2016-10-12.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

define(['member/MemberModel'],
function (MemberModel) {
    return Backbone.Collection.extend({
        model : MemberModel,
        url : 'http://localhost:8080/members'
    });
});