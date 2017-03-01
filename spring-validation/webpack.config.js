/**
 * Created by jojoldu@gmail.com on 2017.03.01
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
var path = require('path');

module.exports = {
    entry: { // 엔트리 파일 목록
        lib: ['./node_modules/webpack/bin/webpack.js', './node_modules/handlebars/dist/handlebars.runtime.js', './node_modules/jquery/dist/jquery.js']
    },
    output: {
        path: path.join(__dirname, '/src/main/resources/static/js/build/'), // 번들 파일 폴더
        filename: 'lib.bundle.js' // 번들 파일 이름 규칙
    }
};

