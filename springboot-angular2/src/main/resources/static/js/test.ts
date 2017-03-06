/**
 * Created by jojoldu@gmail.com on 2017. 3. 6.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
let arr: string[] = [];
arr.push('a');
arr.push('b');
arr.push('c');
console.log(arr[0]);

const params = ['happy 동물원', 100];
let stringName = params[0];
let num = params[1];

console.log(stringName);
console.log(num);

const params2 = ['happy 동물원', 100];
let [name2, num2] = params2;
console.log(name2, num2);

const numArr = [1,2,3,4,5];
console.log(numArr.filter(n => {return n % 2 === 0}));