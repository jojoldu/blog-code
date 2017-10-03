/**
 * Created by jojoldu@gmail.com on 2017. 10. 3.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */


// bad
const bad = function () {
    console.log('bad');
};

// good
function good() {
    console.log('good');
}

console.log(bad());
console.log(good());
