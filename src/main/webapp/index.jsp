<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <script src="${pageContext.request.contextPath}/js/jquery-3.4.0.min.js"></script>
    <title>SpringBoot + websocket + jsp</title>
</head>
<body>
<h4>在线聊天室</h4>
<div>
    <label for="content"></label>
    <textarea id="content" readonly="readonly" cols="80" rows="15"></textarea>
</div>
<div>
    <textarea id="message" cols="80" rows="5" placeholder="请输入消息"></textarea>
    <div>
        <button id="toSend" class="button btn-info">发送</button>
        <button id="user_exit" class="button btn-danger">离开</button>
        <input id="username" value="${username}">
    </div>
</div>
<script type="text/javascript">
    $(function () {
        var ws;
        if ("WebSocket" in window) {
            var baseUrl = 'ws://localhost:8080/websocket/';
            var username = $('#username').val();
            ws = new WebSocket(baseUrl + username);

            ws.onopen = function () {
                console.log("建立 websocket 连接 ......");
            };

            ws.onmessage = function (event) {
                $('#content').append(event.data + '\n\n');
                console.log("接收到服务端发送的消息......" + event.data + '\n');
            };

            ws.onclose = function () {
                $('#content').append('[' + username + ']' + '已离开！');
                console.log("关闭websocket连接.......");
            };

            ws.onerror = function (event) {
                console.log("websocket发生错误......." + event + "\n");
            };
        } else {
            alert("您的浏览器不支持WebSocket！");
        }

        $('#toSend').click(function () {
            sendMsg();
        })

        $(document).keyup(function (event) {
            if (event.keyCode == 13) {
                sendMsg();
            }
        })

        function sendMsg() {
            ws.send($('#message').val());
        }

        $('#user_exit').click(function () {
            if (ws) {
                ws.close();
            }
        })
    })
</script>
</body>
</html>