url = "http://whw.free.idcfengye.com"
urlPara = "?jsoncallback=?"
// var url = ""
// var urlPara = ""

$(function(){
	/*-------- 获取get参数并修改时间 --------*/
	var query = window.location.search.substring(1);
	var vars = query.split("&")
	var timeArr = new Array()
	for (var i=0;i<vars.length;i++) {
		timeArr[i] = vars[i].split("=")[1]
	}
	$(".ctime-msg").text(timeArr[0] + "年" + timeArr[1] + "月" + timeArr[2] + "日");
	var nowHour = new Date().getHours();
	var nowMin = (new Date().getMinutes()/5).toFixed() * 5;
	$(".hour").animate({"scrollTop":nowHour*50})
	$(".min").animate({"scrollTop":nowMin*10})


	/*-------- 监听返回 --------*/
	$(".left").on("click",function(){
		window.location.href = "index.html";
	})


	/*-------- 监听texteare获取失去焦点 --------*/
	$('#content').on("focus", function(){
		// tips隐藏
		if($(this).val() == "这里可以填入具体的学习内容"){
		    $(this).css("color","#555")
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


	/*-------- 监听提交 --------*/
	$(".right").on("click",function(){
		layer.load(0);
		setTimeout(function(){
			// 获取时间
			var hour = $(".hour").scrollTop()/50
			var min = $(".min").scrollTop()/10
			var temp1 = new Date().setFullYear(timeArr[0])
			var temp2 = new Date(temp1).setMonth(timeArr[1]-1)
			var temp3 = new Date(temp2).setDate(timeArr[2])
			var temp4 = new Date(temp3).setHours(hour)
			var time = new Date(temp4).setMinutes(min)
			// 获取title
			var title = $("#title").val();
			// 获取content
			var content = $("#content").val();
			if(content == "这里可以填入具体的学习内容")
				content = ""
			// mailRe : (邮件提醒) false or true
			var mailRe = document.getElementById("mySwitch").classList.contains("mui-active")
			// series :
			var series = $(".series-input").val();
			var uid = $.cookie("uid")

			$.getJSON(url+"/task/addTask.do"+urlPara,{uid:uid,time:time,title:title,content:content,mailRe:mailRe,series:series},function (data) {
				layer.closeAll()
				if(data.status == 0){
					// 提交成功
					layer.msg(data.result)
					setTimeout(function () {
						window.location.href = "index.html"
					}, 300)
				}else if(data.status == -2){
					// 未登录非正常访问
					layer.msg(data.result, function(){})
					setTimeout(function(){
						window.location.href = "login.html"
					},1000)
				}else{
					layer.msg(data.result, function(){})
				}
			})


		},300)

	})
	
	
	/*-------- 创建时间text --------*/
	$(".min").append("<div class='min-first min-text'>00</div>")
	$(".min").append("<div class='min-text'>05</div>")
	for(var i=0; i<=9; i++){
		if(i==0){
			$(".hour").append("<div class='hour-first hour-text'>00</div>")
		}else{
			$(".hour").append("<div class='hour-text'>0"+i+"</div>")
		}
		
		$(".min").append("<div class='min-text'>"+(i+2)*5+"</div>")
	}
	for(var i=10; i<=23; i++){
		$(".hour").append("<div class='hour-text'>"+i+"</div>")
	}

	
	/*-------- 监听时间滑动 --------*/
	var ti1;
	var ti2;
	$(".hour").on("touchstart",function(){
	    clearTimeout(ti1)
	}).on("touchmove",function(event){
	}).on("touchcancel touchend",function(event){
		// 获取scroll
		var hour;
		ti1 = setTimeout(function(){
			var margintop = $(".hour").scrollTop()
			hour = parseInt((margintop+25)/50)
			$(".hour").animate({"scrollTop":hour*50})
		},900)
	})
	$(".min").on("touchstart",function(){
	    clearTimeout(ti2)
	}).on("touchmove",function(event){
	}).on("touchcancel touchend",function(event){
		// 获取scroll 
		var min;	// 真实值 = min*5
		ti2 = setTimeout(function(){
			var margintop = $(".min").scrollTop()
			min = parseInt((margintop+25)/50)
			$(".min").animate({"scrollTop":min*50})
		},900)
	})


	/*-------- 监听学习天数 --------*/
	$(".series-input").on("blur", function(){
		if($(this).val() == "" || $(this).val() <= 0)
			$(this).val(1)
		else if($(this).val() > 100)
			$(this).val(100)
		else if($(this.val()>0 && $(this).val()<=100)){}
		else
			$(this).val(1)
	})




})

			
