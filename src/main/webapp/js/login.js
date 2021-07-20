url = "http://whw.free.idcfengye.com"
urlPara = "?jsoncallback=?"
// var url = ""
// var urlPara = ""

$(function(){

    // 检测直链登录
    // token http://192.168.8.187:8080/login.html?token=abc
    var query = window.location.search.substring(1);

    var token = query.split("=")[1]
    $.getJSON(url+"/user/checkToken.do"+urlPara, {token:token},function(data){
        layer.closeAll();
        if(data.status == 0){
            // 直链登录成功
            $.cookie("uid", data.data.uid)
            layer.msg(data.result)
            setTimeout(function(){
                window.location.href = "index.html"
            },1000)
        }else{

        }
    })

    /*------- 监听点击获取验证码 -------*/
    $(".getVerCode").on("click",getVerCode)
	var varToken;
    function getVerCode() {
		ti = setTimeout(function(){
			clearTimeout(ti)
		},10)
		$(".bg-img").slideUp()
        // 先验证邮箱
        if(emailCheck()){
            // 防止多次点击
            $(".getVerCode").off("click")
            var num = 30;
            var it = setInterval(function () {
                $(".getVerCode").text(num)
                num--
                if (num < 0) {
                    clearInterval(it)
                    $(".getVerCode").text("点击获取")
                    $(".getVerCode").on("click",getVerCode)
                }
            }, 1000)
            // 发送验证码
            layer.load(0)
            var email = $(".email").val().trim()
            $.getJSON(url + "/user/getVerCode.do" + urlPara, {email:email},function(data){
                layer.closeAll();
                if(data.status == 0){
                    layer.msg(data.result)
                    varToken = data.data;
					console.log(varToken)
                }else{
                    layer.msg(data.result,function(){})
                }

            })
        }
    }


    /*------- 邮箱验证 -------*/
    $(".email").on("blur",function(){
        emailCheck()
    })
    function emailCheck(){
        var email = $(".email").val().trim()
        var reg = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
        if(!reg.test(email)){
            layer.msg("邮箱格式不正确",function(){})
			ti = setTimeout(function(){
				clearTimeout(ti)
			},10)
			$(".bg-img").slideUp()
            return false;
        }
        return true;
    }


    /*------- 监听登录提交 -------*/
    $(".sub").on("click", function(){
        // 登录前先验证验证码是否不为空
        var verCode = $(".vercode").val().trim();
        var email = $(".email").val().trim();
        if(!verCode == ""){
            layer.load(0)
            $.getJSON(url + "/user/login.do" + urlPara,{varToken:varToken,verCode:verCode,email:email},function(data){
                layer.closeAll()
                if(data.status == 0){
                    // 注册/登录成功
                    layer.msg(data.result)
                    // uid存入cookie
                    if($(".freeLogin").prop("checked")){
                        $.cookie('uid', data.data.uid, { expires: 30 });
                    }else{
                        $.cookie('uid', data.data.uid);
                    }
                    setTimeout(function(){
                        window.location.href = "index.html"
                    },1000)
                }else{
                    layer.msg(data.result,function(){})
					ti = setTimeout(function(){
						clearTimeout(ti)
					},10)
					$(".bg-img").slideUp()
                }
            })
        }else{
            layer.msg("请先填入验证码",function(){})
			ti = setTimeout(function(){
				clearTimeout(ti)
			},10)
			$(".bg-img").slideUp()
        }
    })
	
	 /*------- 监听输入 -------*/
	 var ti
	 $(".email").on("click",function(){
		 // 图片隐藏
		 $(".bg-img").slideUp()
		 return false;
	 })
	 $(".vercode").on("click",function(){
		 // 图片隐藏
		 $(".bg-img").slideUp()
		 return false;
	 })
	 $(".freeLogin").on("click",function(event){
		// 图片隐藏
		ti = setTimeout(function(){
			clearTimeout(ti)
		},10)
		
	 })
	
	 $("body").on("click",function(){
		 ti = setTimeout(function(){
			 $(".bg-img").slideDown();
		 },100)
	 })
	
})

