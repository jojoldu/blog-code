<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>모던 IE78</title>
</head>
<body>
    <h1>모던하게 개발하는 IE 7/8 Javascript</h1>
    <div id="userInput" class="row">
        입력 1: <input type="text" class="inputs" id="input1" value="0"><br/>
        입력 2: <input type="text" class="inputs" id="input2" value="0">
        <div id="addResult" class="row">
        </div>

        <!--
            1. userInput div 안에 있어야만 AddView.js에서 찾을 수 있다.
            2. type은 text/template 이다. javascript가 아니다.
        -->
        <script id="underTemplate" type="text/template">
            <input type="text" id="result" value="<%= result %>">
        </script>

        <script id="overTemplate" type="text/template">
            <span>+ : <strong><%= result %></strong></span>
        </script>
    </div>
    <br/>

    <div class="list">

    </div>

    <h1>Member List</h1>
    <div id="member">
        <div id="memberList" class="list">

        </div>

        <script id="listTemplate" type="text/template">
            <h5>회원 리스트</h5>
            <ul>
                <% _.each(members, function(member) { %>
                <li><%= member.name %> : <%= member.email %></li>
                <% }); %>
            </ul>
        </script>
    </div>
    <script type="text/javascript" src="/js/lib/jquery.min.js"></script>
    <script type="text/javascript" src="/js/lib/underscore-min.js"></script>
    <script type="text/javascript" src="/js/lib/backbone-min.js"></script>
    <script type="text/javascript" src="/js/lib/require.js"></script>
    <script type="text/javascript" src="/js/main.js"></script>
    <script type="text/javascript" src="/js/index.js"></script>
</body>
</html>