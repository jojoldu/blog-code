/**
 * Created by jojoldu@gmail.com on 2016-09-21
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
var webpack = require('webpack');
var glob = require("glob"); //js 호출시 와일드카드(*) 로 가져오기 위해 추가

module.exports = {
    //브라우저에서 코드를 읽고 쉽게 디버그 하기 위해 소스맵 추가
    devtool: 'eval-source-map',

    /*
     JSON2 : ie7에선 JSON 객체가 없다. 그래서 IE7에서도 기존처럼 JSON.parse(), JSON.stringify() 를 하기 위해서 추가
     */
    entry: {
        lib: ["./node_modules/backbone/backbone-min.js",
            "./node_modules/underscore/underscore-min.js",
            "./node_modules/jquery.1/node_modules/jquery/dist/jquery.min.js",
            "./node_modules/requirejs/require.js",
            "./node_modules/handlebars/dist/handlebars.runtime.min.js",
            "./node_modules/json2/lib/JSON2.js"
        ],
        service: glob.sync("./src/main/resources/static/js/**/*.js")
    },
    output: {
        path: "./src/main/resources/static/build/js",
        filename: "[name].js"
    },

    /*
     OccurenceOrderPlugin : 자주이용하는 모듈에 가장 작은 Id할당
     UglifyJsPlugin : JS를 압축한다.
     */
    plugins: [
        new webpack.optimize.OccurenceOrderPlugin(),
        new webpack.optimize.UglifyJsPlugin()
    ]

};