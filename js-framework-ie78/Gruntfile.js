/**
 * Created by jojoldu@gmail.com on 2016-09-26.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

'use strict';
module.exports = function(grunt) {

    grunt.initConfig({
        pkg : grunt.file.readJSON('package.json'),
        
        //jquery와 requirejs, underscorejs, backbonejs, json2를 copy하도록 지정
        copy : {
            jquery : {
                src : 'node_modules/jquery.1/node_modules/jquery/dist/jquery.min.js',
                dest : 'src/main/resources/static/js/lib/jquery.min.js'
            },
            require : {
                src : 'node_modules/requirejs/require.js',
                dest : 'src/main/resources/static/js/lib/require.js'
            },
            underscore : {
                src : 'node_modules/backbone/node_modules/underscore/underscore-min.js',
                dest : 'src/main/resources/static/js/lib/underscore-min.js'
            },
            backbone : {
                src : 'node_modules/backbone/backbone-min.js',
                dest : 'src/main/resources/static/js/lib/backbone-min.js'
            },
            json2 : {
                src : 'node_modules/json2/lib/jSON2/static/json2.js',
                dest : 'src/main/resources/static/js/lib/json2.js'
            },
            almond : {
                src : 'node_modules/almond/almond.js',
                dest : 'src/main/resources/static/js/lib/almond.js'
            }
        },

        concat: {
            lib: {
                //순서가 중요하다. 꼭 라이브러리 순서를 지켜서 작성하자.
                src:[
                    'src/main/resources/static/js/lib/jquery.min.js',
                    'src/main/resources/static/js/lib/underscore-min.js',
                    'src/main/resources/static/js/lib/backbone-min.js',
                    'src/main/resources/static/js/lib/require.js',
                    'src/main/resources/static/js/lib/json2.js'
                ],
                dest: 'src/main/resources/static/build/js/lib.js' //concat 결과 파일
            }
        },

        requirejs: {
            build: {
                options: {
                    baseUrl : 'src/main/resources/static/js',
                    name : 'index',
                    mainConfigFile : 'src/main/resources/static/js/main.js',
                    optimize : 'uglify',
                    out : 'src/main/resources/static/optimized/js/service.js'
                }
            }
        }
    });

    // 플러그인 load
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-requirejs');
    grunt.loadNpmTasks('grunt-contrib-concat');

    /*
        Default task(s) : 즉, grunt 명령어로 실행할 작업
        copy -> concat 진행
    */
    grunt.registerTask('default', ['copy', 'concat']);
};
