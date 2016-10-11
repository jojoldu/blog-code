/**
 * Created by jojoldu@gmail.com on 2016-10-11.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
define(['member/MemberModel'],
function(MemberModel){
    return Backbone.View.extend({
        model : null,
        template : null,

        initialize: function () {
            this.model = new MemberModel();
            var templateHtml = this.$el.find('#listTemplate').html();
            this.template = _.template(templateHtml);
        },

        render : function(){
            this.$el.find('.list').html(this.template(this.model.toJSON()));
        }
    });
});