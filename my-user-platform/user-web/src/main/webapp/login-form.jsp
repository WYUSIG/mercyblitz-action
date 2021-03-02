<head>
    <jsp:directive.include file="/WEB-INF/jsp/prelude/include-head-meta.jspf"/>
    <title>Sign In</title>
    <style>
        .bd-placeholder-img {
            font-size: 1.125rem;
            text-anchor: middle;
            -webkit-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;
        }

        @media (min-width: 768px) {
            .bd-placeholder-img-lg {
                font-size: 3.5rem;
            }
        }

        .container {
            margin-top: 50px;
        }

        .form-control{
            margin-top: 20px;
        }

        .footer{
            display: flex;
            flex-wrap: nowrap;
            flex-direction: row;
            justify-content: space-between;
            align-items: center;
        }
    </style>
</head>
<body>
<div class="container">
    <form class="form-signin" action="/userPage/loginForward">
        <h1 class="h3 mb-3 font-weight-normal">登录</h1>
        <label for="inputEmail" class="sr-only">请输出电子邮件</label>
        <input type="email" id="inputEmail" class="form-control" name="userNam"
               placeholder="请输入电子邮件" required autofocus>
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" id="inputPassword" class="form-control" name="password"
               placeholder="请输入密码" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit" style="margin-top: 20px;" id="signIn">
            Sign in
        </button>
        <p class="mt-5 mb-3 text-muted footer">&copy; 2017-2021
            <button class="btn btn-sm btn-primary" id="signUp" type="button">sign up</button>
        </p>
    </form>
    <!-- Modal -->
    <div class="modal fade" id="myModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">提示信息</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body" id="resultContent">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    window.onload = function (ev) {
        // $('#signIn').click(function () {
        //     $.ajax({
        //         type: 'get',
        //         url: '/userPage/loginForward',
        //         dateType: 'json',
        //         data: {
        //             userNam: $("#inputEmail").val(),
        //             password: $("#inputPassword").val()
        //         },
        //         success: function (result) {
        //             result = JSON.parse(result);
        //             $('#resultContent').text('code：'+result.code+', msg: '+result.msg)
        //             $('#myModal').modal('show')
        //         },
        //         error: function () {
        //             $('#resultContent').text('请求失败！')
        //             $('#myModal').modal('show')
        //         }
        //     })
        //     return false;
        // })
        $('#signUp').click(function () {
            window.location.href = "register";
            return false;
        })
    }
</script>
</body>