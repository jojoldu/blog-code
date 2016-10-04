/**
 * Created by jojoldu@gmail.com on 2016-09-26.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

'use strict';
module.exports = function(grunt) {

    grunt.initConfig({
        pkg : grunt.file.readJSON('package.json'),
        
        //jquery와 requirejs를 copy하도록 지정
        copy : {
            jquery : {
                src : 'node_modules/jquery.1/node_modules/jquery/dist/jquery.min.js',
                dest : 'src/main/resources/static/js/lib/jquery.min.js'
            },
            require : {
                src : 'node_modules/requirejs/require.js',
                dest : 'src/main/resources/static/js/lib/require.js'
            }
        }
    });

    // 플러그인 load
    grunt.loadNpmTasks('grunt-contrib-copy');

    // Default task(s) : 즉, grunt 명령어로 실행할 작업
    grunt.registerTask('default', ['copy']);
};
