/**
 * Created by jojoldu@gmail.com on 2016-09-23.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

define([], function() {
   return {
       add : function(a,b){
           return _.isNaN(a+b)? 0 : a+b;
       }
   };
});