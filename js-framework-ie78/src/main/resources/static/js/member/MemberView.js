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

            //reset: true 옵션이 없으면 model 갯수만큼 add이벤트가 발생한다.
            this.collection.fetch({reset: true});

            //collection.reset 이벤트 발생시 view.rednerAll 이벤트 실행
            this.listenTo(this.collection, 'reset', this.render);

            //collection.add 이벤트 발생시 view.render 이벤트 실행
            this.listenTo(this.collection, 'add', this.render);
        },

        render : function(){
            var data = {
                members : this.collection.toJSON()
            };
            //기존 화면 초기화
            this.$memberList.html('');
            //미리 템플릿된 memberList를 호출하여 템플릿 작업
            this.$memberList.html(Handlebars.templates.memberList(data));
        },

        save : function() {
            var name = this.$el.find('#name').val(),
                email = this.$el.find('#email').val();

            this.collection.create({name : name, email: email});
        }
    });
});