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
        $memberList : null,
        events : {
            'click #addMember' : 'save'
        },

        initialize: function () {
            this.model = new MemberModel();
            this.collection = new MemberCollection();
            var collectionHtml = this.$el.find('#collectionTemplate').html();
            this.template = _.template(collectionHtml);
            this.$memberList = this.$el.find('#memberList');

            this.collection.fetch();

            this.listenTo(this.collection, 'reset', this.renderAll);
            this.listenTo(this.collection, 'add', this.render);
        },

        render : function(member){
            this.$memberList.append(this.template(member.toJSON()));
        },

        renderAll : function() {
            /*
             forEach의 내부 function에서는 this가 MemberView가 아니다.
             MemberView를 사용하기 위해 this를 self로 변수할당 후 사용한다.
             */
            var self = this;
            self.collection.forEach(function(member){ //member는 MemberModel 객체이다.
                self.render(member);
            });
        },

        save : function() {
            var name = this.$el.find('#name').val(),
                email = this.$el.find('#email').val();

            this.collection.create({name : name, email: email});
        }
    });
});