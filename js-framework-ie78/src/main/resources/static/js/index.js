/**
 * Created by jojoldu@gmail.com on 2016-09-21.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

require(['Calculator', 'add/AddView', 'member/MemberView'], function(Calculator, AddView, MemberView) {

   //생성자 인자로 el을 넣어주면 AddView영역은 el에 할당된 dom 영역을 본인의 영역으로 지정하게 된다.
   var addView = new AddView({
      el : $('#userInput')
   });
   addView.render();

   var memberView = new MemberView({
      el : $('#member')
   });
   memberView.render();
});

