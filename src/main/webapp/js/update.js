var url = "http://whw.free.idcfengye.com"
// var urlPara = "?jsoncallback=?"
// var url = "http://127.0.0.1:8089"
// var urlPara = "?jsoncallback=?"
var urlPara = ""

$(function (){
    /*-------- 获取get参数 --------*/
    var query = window.location.search.substring(1);
    var vars = query.split("&")
    var tid = vars[0].split("=")[1]
    var year;
    var month;
    var date;
    var hour;
    var min;
    $.post(url+"/task/getTaskByTid.do"+urlPara,{tid:tid},function(data){
        console.log(data);
        task = data.data;
        time = task.timeList[0].time
        year = new Date(time).getFullYear()
        month = new Date(time).getMonth()+1
        date = new Date(time).getDate()
        hour = new Date(time).getHours()
		min = new Date(time).getMinutes()
        // min = (new Date(time).getMinutes()/5).toFixed() * 5;

        if(data.status == 0){
            // 查询到数据,填充数据
            $(".ctime-msg").text(year + "年" + month + "月" + date + "日" + "    " + hour + "时" + min + "分")
            $(".form-control").val(task.title)
            $("#content").val(task.content)

        }else{
            layer.msg(data.result, function(){})
        }
    })


    /*-------- 监听texteare获取失去焦点 --------*/
    $('#content').on("focus", function(){
        // tips隐藏
        if($(this).val() == "这里可以填入具体的学习内容"){
            $(this).css("color","black")
            $(this).val("")
        }
        $(this).animate({"top":"-200px","height":280})
        $(".div-bottom").animate({"top":"-200px"})
    });
    $('#content').blur(function(){
        // 检查content是否有内容,没有内容显示tips
        if($(this).val().length == 0){
            $(this).css("color","rgb(153,153,153)")
            $(this).val("这里可以填入具体的学习内容")
        }
		$(this).animate({"height":80,"top":"0"},500)
		$(".div-bottom").animate({"top":"0"})
    });

    /*-------- 监听返回 --------*/
    $(".left").on("click",function(){
        window.location.href = "index.html"
    })

    /*-------- 监听确认修改 --------*/
    $(".right").on("click",function(){
        layer.load(0);
        setTimeout(function(){
            // 获取时间
            var zhour = $(".hour").scrollTop()/50
            var zmin = $(".min").scrollTop()/10
            var temp1 = new Date().setFullYear(year)
            var temp2 = new Date(temp1).setMonth(month-1)
            var temp3 = new Date(temp2).setDate(date)
            var temp4 = new Date(temp3).setHours(zhour)
            var time = new Date(temp4).setMinutes(zmin)
            // 获取title
            var title = $("#title").val();
            // 获取content
            var content = $("#content").val();
            // mailRe : (邮件提醒) false or true
            var isActive = document.getElementById("mySwitch").classList.contains("mui-active")
            // series :
            var series = $(".series-input").val();
            var uid = $.cookie("uid")

            $.post(url + "/task/update.do" + urlPara,{uid:uid,time:time,title:title,content:content,isActive:isActive,series:series,tid:tid},function (data) {
                layer.closeAll()
                if(data.status != -1){
                    // 修改成功
                    layer.msg(data.result)
                    setTimeout(function () {
                        window.location.href = "index.html"
                    }, 300)
                }else{
                    layer.msg(data.result, function(){})
                }
            })
        },300)
    })

})









