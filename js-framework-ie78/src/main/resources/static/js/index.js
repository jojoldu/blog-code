/**
 * Created by jojoldu@gmail.com on 2016-09-21.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

require(["Calculator"], function(Calculator) {
   var a = parseInt($('#input1').val()),
       b = parseInt($('#input2').val());

   var sum = Calculator.add(a,b);
   $('#result').val(sum);

});

