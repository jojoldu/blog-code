/**
 * Created by jojoldu@gmail.com on 2016-09-26.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

'use strict';
module.exports = function(grunt) {

    grunt.initConfig({
        pkg : grunt.file.readJSON('package.json'),

        // build 전 이전 resource들 정리
        clean: [
            'src/main/resources/static/build/*',
            'src/main/resources/static/js/templates.js'
        ],
        //uglify 설정
        uglify: {
            options: {
                banner: '/* <%= grunt.template.today("yyyy-mm-dd") %> / ' //파일의 맨처음 붙는 banner 설정
            },
            build: {
                src: 'public/build/result.js', //uglify할 대상 설정
                dest: 'public/build/result.min.js' //uglify 결과 파일 설정
            }
        },
        //concat 설정
        concat: {
            lib : {
                src : [ //concat 타겟 설정(앞에서부터 순서대로 합쳐진다.)
                    'node_modules/backbone/backbone-min.js',
                    'node_modules/backbone/node_modules/underscore-min.js',
                    'node_modules/jquery.1/node_modules/jquery/dist/jquery.min.js', // IE 하위버전 호환을 위해 jquery는 1.x 버전을 사용
                    'node_modules/json2/lib/JSON2/static/json2.js',
                    'node_modules/handlebars/dist/handlebars.min.js',
                    'node_modules/requirejs/require.js'
                ],
                dest : 'src/main/resources/static/build/lib.js' //concat 결과 파일
            }
        }
    });

    // Load the plugin that provides the "uglify", "concat" tasks.
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-concat');

    // Default task(s).
    grunt.registerTask('default', ['clean', 'concat', 'uglify']); //grunt 명령어로 실행할 작업
};
