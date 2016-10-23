/**
 * Created by jojoldu@gmail.com on 2016-10-11.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
define(['member/MemberCollection'],
function(MemberCollection){
    return Backbone.View.extend({
        collection : null,
        $memberList : null,
        events : {
            'click #addMember' : 'save'
        },

        initialize: function () {
            this.collection = new MemberCollection();
            this.$memberList = this.$el.find('#memberList');

            this.collection.fetch();

            //collection.reset 이벤트 발생시 view.rednerAll 이벤트 실행
            this.listenTo(this.collection, 'reset', this.render);

            //collection.add 이벤트 발생시 view.render 이벤트 실행
            this.listenTo(this.collection, 'add', this.render);
        },

        render : function(){
            var data = {
                members : this.collection.toJSON()
            };

            this.$memberList.html('');
            this.$memberList.html(Handlebars.templates.memberList(data));
        },

        save : function() {
            var name = this.$el.find('#name').val(),
                email = this.$el.find('#email').val();

            this.collection.create({name : name, email: email});
        }
    });
});