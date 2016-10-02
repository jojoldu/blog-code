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
        //concat 설정
        concat: {
            /*
                외부 라이브러리 통합
                단, handlebar의 경우 pre-compile 해서 사용하니 굳이 concat할 필요 없다.
              */
            lib : {
                src : [ //concat 타겟 설정(앞에서부터 순서대로 합쳐진다.)
                    'node_modules/jquery.1/node_modules/jquery/dist/jquery.min.js', // IE 하위버전 호환을 위해 jquery는 1.x 버전을 사용
                    'node_modules/backbone/backbone-min.js',
                    'node_modules/backbone/node_modules/underscore-min.js',
                    'node_modules/json2/lib/JSON2/static/json2.js',
                    'node_modules/requirejs/require.js'
                ],
                dest : 'src/main/resources/static/build/js/lib.js' //concat 결과 파일
            },
            //직접 작성한 javascript 통합
            service : {
                src : 'src/main/resources/static/js/*',
                dest : 'src/main/resources/static/build/js/service.js'
            }
        },
        /*
            uglify 설정
            lib를 다시 uglify할 경우 jquery가 충돌난다.
            어차피 minify되었으니 uglify는 제외하고 직접 제작된
         */
        uglify: {
            options: {
                sourceMap: true
            },
            build: {
                files: [{
                    expand : true,
                    cwd: 'src/main/resources/static/build/js', // parent 폴더 지정
                    src: ['**/*.js', '!lib.js'], // parent 폴더 아래에 있는 모든 js 확장자 파일들을 선택하되, lib.js는 제외
                    dest: 'src/main/resources/static/build/js/', // uglify 결과를 저장할 폴더 지정
                    ext: '.min.js' // uglify 결과로 나온 js파일들에 붙일 확장자명
                }]
            }
        }
    });

    // 플러그인 load
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    
    // Default task(s) : 즉, grunt 명령어로 실행할 작업
    grunt.registerTask('default', ['clean', 'concat', 'uglify']);
};
