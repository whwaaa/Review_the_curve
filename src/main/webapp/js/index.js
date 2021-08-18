var url = "http://whw.free.idcfengye.com"
// var urlPara = "?jsoncallback=?"
// var url = "http://127.0.0.1:8089"
// var urlPara = "?jsoncallback=?"
var urlPara = ""

$(function () {
	/*----------- 全局变量 -----------*/
	var dateTop = 30; 				 	// 日期模块top最高点 (包含五个值:90(默认),30,-30,-90,-150)
	var dateMoveTop = 90 - dateTop; 	// 日期模块移动的距离 (0,60,120,180,240)
	var dateMid = 50;				 	// 日期模块top初始值
	var dateHeight = 60;			 	// 日期模块初始高度
	var dateHeightMax = 120;		 	// 日期模块最大高度
	var barTop = 150;							// 横条顶部marginTop值
	var barMid = dateHeight*5 + 90 + 10;		// 横条中部marginTop值(初始值)
	var barBot = barMid + 300;					// 横条底部marginTop值
	var barMidTop = barMid - barTop;			// 中到顶间距
	var barBotTop = barBot - barTop;			// 底到顶间距
	var barBotMid = barMid - barBot;			// 底到中间距*(-1)(取负数)
	var barPosition;		// 有三个值: 0 1 2, 表示bar在顶部,中部,底部
	var touchStartY;		// 开始触摸时记录触摸点y坐标
	var touchY;				// 实时滑动触摸点的y坐标
	var barMarginTopEnd;	// 一次调整bar最后的marginTop

	var date = new Date();
	var nowDate = new Date();
	var day = document.getElementsByClassName("date");
	var lastRow = document.getElementsByName("lastrow");
	var firstShowFlag = true;
	var timeList = new Array();
	var monthTask = new Array();
	var tonow_hidden = true;	// "今"图标显示或隐藏的标记 true表示显示
	
	layer.load(0)
	var uid = $.cookie("uid")
	// 1.查询所有学习任务
	$.post(url + "/task/getAllTask.do" + urlPara,{uid:uid},function (data) {
		layer.closeAll()
		if(data.status == 0){
			$.each(data.data,function(){
				var _this = this;
				$.each(this.timeList, function () {
					var time = this.time
					var status = this.status
					var tid = _this.tid;
					var tomkdown = markdown.toHTML(h2m(_this.content, {converter: 'CommonMark'}))
					var timeObj = {time:time,title:_this.title,status:status,content:tomkdown,tid:tid}
					timeList.push(timeObj)
				})
			})
			timeList.sort(function (a,b){
				return a.time - b.time
			})

			searchMonthTask(new Date())
			show()
		}else{
			// 还未登录，提示后跳转到登陆界面
			layer.msg(data.result,function(){})
			setTimeout(function(){
				window.location.href = "login.html"
			},500)
		}
	})


	/*----------- 监听date点击 -----------*/
	$(".date_div").on("click",function(){
		event.preventDefault();
		var _this = $(this).find(".date")
		$(".date").removeClass("date_color_now date_color")
		var month = parseInt($("#month").text().substring(5))
		if($(_this).text()==nowDate.getDate() && month==nowDate.getMonth()+1){
			$(_this).addClass("date_color")
			$(_this).addClass("date_color_now")
			$(".tonow").hide(100)
			tonow_hidden = true;
		}else{
			$(_this).removeClass("date_color_now")
			$(_this).addClass("date_color")
			$(".tonow").fadeIn(200)
			tonow_hidden = false;
		}
		$(".add").fadeIn(200)
		showTaskCon($(_this).text())
		moveDefault() // 2 -> 中
	})
	
	
	/*----------- 监听上一个月下一个月 -----------*/
	$("#next").on("click",showNext)
	$("#last").on("click",showLast)

	/*----------- 监听左右滑动屏幕 -----------*/
	var touchStartX;
	var oneTouchFlag = true;
	$(".cross_move").on("touchstart",function(){
		var touch = event.targetTouches[0];
		touchStartX = touch.pageX;
		
	}).on("touchmove",function(){
		// event.preventDefault();
		// 实时滑动触摸点的y坐标
		var touch = event.targetTouches[0];
		touchX = touch.pageX;
		if(oneTouchFlag){
			if(touchX - touchStartX > 80){
				// 右滑动
				showNext();
				touchStartX = touch.pageX;
				oneTouchFlag = false;
			}else if(touchStartX - touchX > 80){
				// 左滑动
				showLast();
				touchStartX = touch.pageX;
				oneTouchFlag = false;
			}
		}else{
			if(touchX - touchStartX > 10){
				// 右滑动
				showNext();
				touchStartX = touch.pageX;
			}else if(touchStartX - touchX > 10){
				// 左滑动
				showLast();
				touchStartX = touch.pageX;
			}
		}

		// event.preventDefault();
	}).on("touchcancel touchend",function(){
		oneTouchFlag = true;
	})

	/*----------- 监听跳转今日按钮 -----------*/	
	$(".tonow").on("click",function(){
		$(".date").removeClass("date_color")
		firstShowFlag = true;
		date.setFullYear(nowDate.getFullYear());
		date.setMonth(nowDate.getMonth());

		searchMonthTask(date)
		// 清空当天的task内容
		showTaskCon(-1)
		show()
		init()
		$(".tonow").hide(100)
	})
	/*----------- 监听添加任务 -----------*/
	$(".add").on("click", function(){
		var month = parseInt($("#month").text().substring(5))
		var year = parseInt($("#month").text().substring(0,4))
		var day = parseInt($(".date_color").text())
		window.location.href = "create.html?year="+year+"&month="+month+"&day="+day;
	})

	/*----------- 监听删除按钮 -----------*/
	$("body").on("click",".edit-delete", function(){
		var _this = this;
		layer.confirm('确定删除包含本次的完整学习计划？', {
			btn: ['删除','取消'] //按钮
		}, function(){
			layer.load(0)
			// 获取tid
			var tid = $(_this).parents(".task").find(".tid").val()
			$.post(url + "/task/delete.do" + urlPara,{tid:tid},function(data){
				layer.closeAll()
				if(data.status == 0){
					layer.msg(data.result)
					setTimeout(function(){
						window.location.href = "index.html"
					},500)
				}else{
					layer.msg(data.result, function(){})
				}
			})
		}, function(){
		});
	})
	/*----------- 监听编辑按钮 -----------*/
	$("body").on("click",".edit-edit", function(){
		var tid = $(this).parents(".task").find(".tid").val()
		window.location.href = "update.html?tid=" + tid
	})

	
	/*----------- 初始化参数 -----------*/
	init()
	function init(){
		$(".bar").css("margin-top", barMid)
		moveDefault() // 2 -> 中
	}

	/*----------- 监听任务模块展开 -----------*/
	var downNum = 0;
	$("body").on("click",".taskName",function(){
		// 1.改图标
		// 2.taskNote动画
		// 3."今"图标隐藏或显示
		console.log($(this).parents(".task").find(".taskNote").css("display"))
		if($(this).parents(".task").find(".taskNote").css("display") == "block"){
			// 改为隐藏
			$(this).parents(".task").find(".taskNote").slideUp("normal");
			$(this).find(".lnr").removeClass("lnr-chevron-down")
			$(this).find(".lnr").addClass("lnr-chevron-left")
			$(this).parents(".task").find(".edit-delete").hide(200)
			$(this).parents(".task").find(".edit-edit").hide(200)
			if(!tonow_hidden)
				$(".tonow").fadeIn(200)
			$(".add").fadeIn(200)
			downNum--;
			if(downNum == 0)
				moveDefault() // 2 -> 中
		}else{
			// 改为显示
			/*----------- 自适应宽度调整 -----------*/
			var wi = $(".taskName").width()
			console.log(wi)
			$(".taskNote").width(wi-15)
			$(this).parents(".task").find(".taskNote").slideDown("normal");
			$(this).find(".lnr").addClass("lnr-chevron-down")
			$(this).find(".lnr").removeClass("lnr-chevron-left")
			$(this).parents(".task").find(".edit-delete").fadeIn(200)
			$(this).parents(".task").find(".edit-edit").fadeIn(200)
			$(".tonow").hide(100)
			$(".add").hide(100)
			moveToUp() // 1 -> 顶
			downNum++;
		}
	})
	
	/*----------- 监听上下滑动屏幕 -----------*/
	$(".move").on("touchstart",function(){
		var nowBarMarTop = parseInt($(".bar").css("margin-top"))
		if(nowBarMarTop>barTop-10 && nowBarMarTop<barTop+10){
			// bar在顶部
			barPosition = 0
		}else if(nowBarMarTop>barMid-10 && nowBarMarTop<barMid+10){
			// bar在中部
			barPosition = 1
		}else if(nowBarMarTop>barBot-10 && nowBarMarTop<barBot+10){
			// bar在底部
			barPosition = 2
		}else{}
		// 记录开始触摸点y坐标
		var touch = event.targetTouches[0];
		touchStartY = touch.pageY;
		
	}).on("touchmove",move).on("touchcancel touchend",function(){
		$(".move").on("touchmove",move)
	})
	function move(){
		$(".move").off("touchmove")
		var touch = event.targetTouches[0];
		var touchY = touch.pageY;
		switch(barPosition){
			case 0:
				if(touchY > touchStartY + 5)
					moveDefault()	// 2 -> 中
			break;
			case 1:
				if(touchY > touchStartY + 5)
					moveToDown()	// 3 -> 底
				else if(touchY + 5 < touchStartY)
					moveToUp()		// 1 -> 顶
			break;
			case 2:
				if(touchY + 5 < touchStartY)
					moveDefault()	// 2 -> 中
			break;
		}
	}
	
	// 1 -> 顶
	function moveToUp(){	// moveToUp() // 1 -> 顶
		$(".bar").animate({'marginTop':barTop})
		$(".move_top").animate({'top':dateTop})
	}
	// 3 -> 底
	function moveToDown(){
		$(".bar").animate({'marginTop':barBot})
		$(".move_height").animate({"height":dateHeightMax})
		$(".tonow").hide(100)
		$(".add").hide(100)
		$(".tonow").hide(100)
		$(".add").hide(100)
	}
	// 2 -> 中
	function moveDefault(){ // moveDefault() // 2 -> 中
		$(".bar").animate({'marginTop':barMid})
		$(".move_top").animate({'top':100})
		$(".move_height").animate({"height":dateHeight})
		$(".taskNote").slideUp("normal");
		
		// 改为隐藏
		$(".taskNote").slideUp("normal");
		$(".lnr-chevron-down").addClass("lnr-chevron-left")
		$(".lnr-chevron-left").removeClass("lnr-chevron-down")
		$(".edit-delete").hide(200)
		$(".edit-edit").hide(200)
		if(!tonow_hidden)
			$(".tonow").fadeIn(200)
		$(".add").fadeIn(200)
		downNum = 0;
		
		if(!tonow_hidden)
			$(".tonow").fadeIn(200)
		$(".add").fadeIn(200)
	}
	
	/**
	 * 改变date的top
	 */
	function dateChangeTop(move){
		if(resetFlag){
			// 重置高度(重置为默认)
			$(".move_height").css("height", dateHeight)
			resetFlag = false;
		}
		// 调整top的表达式(根据触摸距离和bar的固定距离来计算date的偏移)
		var dateNewTop = move*dateMoveTop/parseFloat(barMidTop) + 100 
		$(".move_top").css('top', dateNewTop)
	}
	/**
	 * 改变date的height
	 */
	function dateChangeHeight(move){
		if(resetFlag){
			// 重置top(重置为默认)
			$(".move_top").css('top', 100)
			resetFlag = false;
		}
		// 根据触摸的距离计算模块的高度
		var dateNowHeight = dateHeight + move/5.0;
		console.log("模块的高度: " + dateNowHeight)
		if(dateNowHeight >= dateHeight)
			$(".move_height").css("height", dateNowHeight)
	}
	
	// 改变bar的marginTop
	function barChangeMarginTop(oldBarTop, move){
		barMarginTopEnd = oldBarTop + move;
		$(".bar").css('marginTop', barMarginTopEnd);
	}

	function searchMonthTask(date){
		// 获取当月月初和月末的时间戳
		var yuechu = new Date(new Date(new Date(new Date(date).setDate(1)).setHours(0)).setMinutes(0)).setSeconds(0)
		var yuemo1 = new Date(date).setDate(daysInMonth(new Date(date).getFullYear(),new Date(date).getMonth()))
		var yuemo = new Date(new Date(new Date(yuemo1).setHours(23)).setMinutes(59)).setSeconds(59)
	
		var startIndex;
		var endIndex;
		for(var i=0; i<timeList.length; i++){
			if(startIndex == undefined){
				if(timeList[i].time >= yuemo)
					// 从较低的time开始遍历,先遇到超过本月的time说明list中没有该月的区间
					break;
				if(timeList[i].time >= yuechu){	// 遇到第一个在本月的task
					startIndex = i;	// 标记开始,再次遍历寻找结束
					for(var j=startIndex; j<timeList.length; j++){
						endIndex = j;
						if(timeList[j].time > yuemo){	// 遇到第一个超过本月的task,说明前一个是本月最后一个task
							endIndex -= 1;
							break;
						}
					}
					break;
				}
			}
		}
		monthTask.length = 0;
		for(var i=startIndex; i<=endIndex; i++){
			timeList[i]["day"] = new Date(timeList[i].time).getDate()
			// 小于当天时间,添加before属性
			if(timeList[i].time < new Date()){
				timeList[i]["log"] = "taskDot_before"
			}else{
				// 大于等于当天时间,添加after属性
				timeList[i]["log"] = "taskDot_after";
			}
			monthTask.push(timeList[i])
		}
	}
	
	function showTaskCon( nday){
		$(".condiv2").empty()
		var dayTask = new Array();
		for(var i=0; i<monthTask.length; i++){
	
			if(monthTask[i]["day"] == nday){
				dayTask.push(monthTask[i]);
			}
			if(monthTask[i]["day"] > nday){
				break;
			}
		}
		for(var i=0; i<dayTask.length; i++) {
			var $div = $(
				"<div class=\"task\">\n" +
				"<input class='tid' type='hidden' value=" + dayTask[i]["tid"] + ">" +
				"\t\t\t<div class=\"taskName\">\n" +
				"\t\t\t\t<div class=\"text\">" + dayTask[i]["title"] + "   ——" + dayTask[i]["status"] + "</div>\n" +
				"\t\t\t\t<div class=\"right\"><span class=\"lnr lnr-chevron-left\"></span></div>\n" +
				"\t\t</div>\n" +
				"\t\t<div class=\"taskNote\">" + dayTask[i]["content"] + "</div>" +
				"<div class=\"edit-edit hidden\"><span class=\"lnr lnr-magic-wand \"></span>&nbsp;&nbsp;编辑</div>" +
				"<div class=\"edit-delete hidden\"><span class=\"lnr lnr-trash \"></span>&nbsp;&nbsp;删除</div>" +
			"</div>");
			$(".condiv2").append($div);
		}
	
		if(dayTask.length == 0){
			var $div =
				"<div class=\"task\">\n" +
				"\t\t\t<div class=\"taskName\">\n" +
				"\t\t\t\t<div class=\"text\">空</div>\n" +
				"\t\t\t\t<div class=\"right\"><span class=\"lnr lnr-chevron-left\"></span></span></div>\n" +
				"\t\t</div>\n" +
				"\t\t<div class=\"taskNote\">也是空的</div>" +
			"</div>";
			$(".condiv2").append($div);
		}
	}
	
	function show(){
		for(i=0;i<lastRow.length;i++){
			lastRow[i].style.display = "table-cell";  // 默认不隐藏最后一行
		}
		document.getElementById("month").innerHTML = date.getFullYear() + "年" + trans(date.getMonth());
		for(i=0;i<day.length;i++){
			day[i].innerText = "";	// 清空日期
		}
		var tempDate = new Date(date);
		var n = 1;
		$(".log").removeClass("taskDot_before")		// 清空下标
		$(".log").removeClass("taskDot_after")		// 清空下标
		var mindex = 0;
		var offday = tempDate.getDay(tempDate.setDate(1))
		for(i=offday; i<day.length; i++){
			if(mindex < monthTask.length){
				if(n == monthTask[mindex]["day"]){
					$(".log:eq("+i+")").addClass(monthTask[mindex]["log"])
					if(n === nowDate.getDate()){
						// 加载当天的task内容
						showTaskCon(n)
					}
					while(mindex<monthTask.length && monthTask[mindex]["day"] == n){
						mindex++
					}
				}
			}
			day[i].innerText = n;
			if(n === nowDate.getDate()){
				if(firstShowFlag){ 
					firstShowFlag = false;
					day[i].classList.add("date_color_now");		// 突出今天日期背景
					day[i].classList.add("date_color");
				}
				day[i].value="1";
			}
			if(n === daysInMonth(date.getFullYear(),date.getMonth()))
				break;
			n++;
		}
		if(lastRow[0].innerText==""){
			for(i=0;i<lastRow.length;i++){
				lastRow[i].style.display = "none";  //隐藏最后一行
			}
			barMid = dateHeight*5 + 90 + 10;		// 修改横条中部marginTop值(初始值)
			var nowBarMarTop = parseInt($(".bar").css("margin-top"))
			if(nowBarMarTop>(barTop+barMid)/2 && nowBarMarTop<(barMid+barBot)/2){
				$(".bar").animate({'marginTop':barMid},100)
			}
		}else{
			barMid = dateHeight*6 + 90 + 10;		// 修改横条中部marginTop值(初始值)
			var nowBarMarTop = parseInt($(".bar").css("margin-top"))
			if(nowBarMarTop>(barTop+barMid)/2 && nowBarMarTop<(barMid+barBot)/2){
				$(".bar").animate({'marginTop':barMid},100)
			}
		}
	}
	
	//显示下个月
	function showNext(){
		date.setDate(1);
		if(date.getMonth()<12){
			date.setMonth(date.getMonth()+1);
		}
		else{
			date.setFullYear(date.getFullYear+1);
			date.setMonth(0);
		}
		show();
		var month = parseInt($("#month").text().substring(5))
		var year = parseInt($("#month").text().substring(0,4))
		var day = parseInt($(".date_color").text())
		if(month!=nowDate.getMonth()+1 || year!=nowDate.getFullYear() || day!=nowDate.getDate()){
			// 非本月本年
			$(".date_color").removeClass("date_color_now")
			$(".tonow").removeClass("tonow_hidden")
		}else{
			// 本年本月
			$(".date_color").addClass("date_color_now")
			$(".tonow").addClass("tonow_hidden")
		}
	
		searchMonthTask(new Date(new Date(new Date().setFullYear(year)).setMonth(month-1)).setDate(day))
		// 清空当天的task内容
		showTaskCon(-1)
		show()
		moveDefault() // 2 -> 中
	}
	
	//显示上个月
	function showLast(){
		if(date.getMonth()>=0){
			date.setMonth(date.getMonth()-1);
		}
		else{
			date.setFullYear(date.getFullYear-1);
			date.setMonth(11);
		}
		show();
		var month = parseInt($("#month").text().substring(5))
		var year = parseInt($("#month").text().substring(0,4))
		var day = parseInt($(".date_color").text())
		if(month!=nowDate.getMonth()+1 || year!=nowDate.getFullYear() || day!=nowDate.getDate()){
			// 非本月本年
			$(".date_color").removeClass("date_color_now")
			$(".tonow").removeClass("tonow_hidden")
		}else{
			// 本年本月
			$(".date_color").addClass("date_color_now")
			$(".tonow").addClass("tonow_hidden")
		}
	
		searchMonthTask(new Date(new Date(new Date().setFullYear(year)).setMonth(month-1)).setDate(day))
		// 清空当天的task内容
		showTaskCon(-1)
		show()
		moveDefault() // 2 -> 中
	}
	
	//返回每月天数
	function daysInMonth(y,m){
		var month = [31,28,31,30,31,30,31,31,30,31,30,31];
		if((y%400==0) || (y%100!=0 && y%4==0)){
			month[1] = 29;
		}
		return month[m];
	}
	
	//月份名称转换
	function trans(m){
		switch(m){
			case 0: return "1月";
			case 1: return "2月";
			case 2: return "3月";
			case 3: return "4月";
			case 4: return "5月";
			case 5: return "6月";
			case 6: return "7月";
			case 7: return "8月";
			case 8: return "9月";
			case 9: return "10月";
			case 10:return "11月";
			case 11:return "12月";
		}
	}
	
})






