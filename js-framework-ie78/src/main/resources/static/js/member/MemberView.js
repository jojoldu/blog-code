/**
 * Created by jojoldu@gmail.com on 2016-10-11.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
define(['member/MemberModel', 'member/MemberCollection'],
function(MemberModel, MemberCollection){
    return Backbone.View.extend({
        model : null,
        collection : null,
        template : null,
        events : {
            'click #addMember' : 'save'
        },

        initialize: function () {
            this.model = new MemberModel();
            this.collection = new MemberCollection();
            this.collection.fetch();
            var templateHtml = this.$el.find('#listTemplate').html();
            var collectionHtml = this.$el.find('#collectionTemplate').html();
            this.template = _.template(collectionHtml);
        },

        render : function(){
            this.$el.find('.list').html(this.template(this.model.toJSON()));
        },

        save : function() {

        }
    });
});