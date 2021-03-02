<head>
    <jsp:directive.include
            file="/WEB-INF/jsp/prelude/include-head-meta.jspf"/>
    <title>Sign Up</title>
    <style>
        .container-lg {
            margin-top: 50px;
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
<div class="container-lg">
    <h1 class="h3 mb-3 font-weight-normal">注册</h1>
    <form id="form">
        <div class="form-group">
            <label for="name">姓名</label>
            <input type="text" class="form-control" id="name">
        </div>
        <div class="form-group">
            <label for="phoneNumber">电话</label>
            <input type="text" class="form-control" id="phoneNumber">
        </div>
        <div class="form-group">
            <label for="email">邮箱</label>
            <input type="text" class="form-control" id="email">
        </div>
        <div class="form-group">
            <label for="password">密码</label>
            <input type="password" class="form-control" id="password">
        </div>
        <button type="button" class="btn btn-lg btn-primary btn-block" id="signUp">sign up</button>
        <p class="mt-5 mb-3 text-muted footer">&copy; 2017-2021
            <button class="btn btn-sm btn-primary" id="signIn" type="button">sign in</button>
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
        $('#signUp').click(function () {
            if($("#name").val() === ''){
                $('#resultContent').text('请填写姓名！')
                $('#myModal').modal('show')
                return false
            }
            if($("#phoneNumber").val() === ''){
                $('#resultContent').text('请填写电话！')
                $('#myModal').modal('show')
                return false
            }
            if($("#email").val() === ''){
                $('#resultContent').text('请填写邮箱！')
                $('#myModal').modal('show')
                return false
            }
            if($("#password").val() === ''){
                $('#resultContent').text('请填写密码！')
                $('#myModal').modal('show')
                return false
            }
            $.ajax({
                type: 'post',
                url: '/user/signUp',
                dateType: 'json',
                data: {
                    name: $("#name").val(),
                    phoneNumber: $("#phoneNumber").val(),
                    email: $("#email").val(),
                    password: $("#password").val()
                },
                success: function (result) {
                    result = JSON.parse(result);
                    $('#resultContent').text('code：'+result.code+', msg: '+result.msg)
                    $('#myModal').modal('show')
                },
                error: function () {
                    $('#resultContent').text('请求失败！')
                    $('#myModal').modal('show')
                }
            })
            return false;
        })
        $('#signIn').click(function () {
            window.location.href = "loginForm";
            return false;
        })
    }
</script>
</body>
