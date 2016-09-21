/**
 * Created by jojoldu@gmail.com on 2016-09-21
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

module.exports = {
    entry: {
        lib: ["./node_modules/backbone/backbone-min.js",
            "./node_modules/underscore/underscore-min.js",
            "./node_modules/jquery.1/node_modules/jquery/dist/jquery.min.js",
            "./node_modules/requirejs/require.js",
            "./node_modules/handlebars/dist/handlebars.runtime.min.js",
            "./node_modules/json2/lib/JSON2.js"
        ]
    },
    output: {
        path: "./src/main/resources/static/build",
        filename: "[name].js"
    }
};