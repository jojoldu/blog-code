/**
 * Created by jojoldu@gmail.com on 2016-09-21.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
require(['/build/js/utils.js'],
function(Utils){
   $(function() {
      var sum = Utils.add(1+2);
      alert(sum);
   });
});
