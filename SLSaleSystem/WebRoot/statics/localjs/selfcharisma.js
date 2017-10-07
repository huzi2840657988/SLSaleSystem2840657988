$(function(){
	/**
	 * modifyPassword:
	 * 1.登录密码；新密码；重复新密码；
	 * 2.新密码不能小于6位；
	 * 3.新旧密码相等
	 * 密码对应id:
	 * 		oldpassword
	 * 		newpassword
	 * 		aginpassword
	 * 提示信息id：modifypwdtip
	 * 按钮id(ajax提交):modifySavePassword
	 * 后台访问路径：/SLSaleSystem/backend/modifyPwd.html
	 * */
	$("#modifySavePassword").click(function(){
		//input messages judge
		//获取输入的信息
		var msgtip=$("#modifypwdtip");
		msgtip.html("");
		var oldpwd=$("#oldpassword").val();
		var newpwd=$("#newpassword").val();
		var aginpwd=$("#aginpassword").val();
		//信息验证 ""---length----==
		if(""==oldpwd){
			msgtip.css("color","red");
			msgtip.html("请输入原密码！");
			$("#oldpassword").focus();
		}else if(""==newpwd){
			msgtip.css("color","red");
			msgtip.html("请输入新密码！");
			$("#newpassword").focus();
		}else if(newpwd.length<6){    //判断密码长度
			msgtip.css("color","red");
			msgtip.html("请输入的新密码长度大于6！");
			$("#aginpassword").focus();
		}else if(""==aginpwd){
			msgtip.css("color","red");
			msgtip.html("请再次输入新密码！");
			$("#aginpassword").focus();
		}else if(newpwd !== aginpwd){  //密码比较确认
			msgtip.css("color","red");
			msgtip.html("两次输入的密码不相等！");
			$("#aginpassword").focus();
		}else{			//参数正确，ajax传值
			//新建user Json对象
			User user=new Object();
			user.password=oldpwd;
			user.password2=newpwd;
			$.ajax({
				url:"/SLSaleSystem/backend/modifyPwd.html",
				type:"post",
				data:{userJson:JSON.string.ify(user)},
				dataType:"html",
				timeout:1000,
				erro:function(){
					
				},
				success:function(result){
					if(result != "" && "success" == result){
						tip.css("color","green");
						tip.html("修改密码成功 ，下次登录记得使用新密码哦。^_^");
					}else if("failed" == result){
						tip.css("color","red");
						tip.html("修改密码失败！请重试。");
					}else if("oldpwdwrong" == result){
						tip.css("color","red");
						tip.html("原密码不正确！请重试。");
					}else if("nodata" == result){
						tip.css("color","red");
						tip.html("对不起，没有任何数据需要处理！请重试。");
					}
				}
				
			})
		}
		
	})
	
})


//add by bdqn_hl 2014-3-9
	$('.modifypwd').click(function(e){
		$('#oldpassword').val('');
		$('#newpassword').val('');
		$('#aginpassword').val('');
		$('#modifypwdtip').html('');
		e.preventDefault();
		$('#myModal').modal('show');
	});
	








/**
	 * menu add by bdqn_hl 2014-2-27 start
	 * 
	 * */
	var result = "";
	//使用该方法转换为可以直接使用的javascript代码！
	var json = eval('(' + tt + ')');
	alert(tt);
	//$("#jsonstr").append(tt);
//使用循环菜单列表（动态配置菜单）
	for(var i = 0;i<json.length;i++){
		/*
		<li class="nav-header hidden-tablet" onclick="$('#test1').toggle(500);">后台管理</li>
		<li>
			<ul class="nav nav-tabs nav-stacked" id="test1">
			<li><a class="ajax-link" href="index.html"><i class="icon-home"></i><span class="hidden-tablet">用户管理</span></a></li>
			<li><a class="ajax-link" href="ui.html"><i class="icon-eye-open"></i><span class="hidden-tablet">角色管理</span></a></li>
			</ul>
		</li>
		 */
		
		//config main menu
		result = result + '<li class="nav-header hidden-tablet" onclick="$(\'#test'+i+'\').toggle(500);" style="cursor:pointer;">'+json[i].mainMenu.functionName+'</li>';
		//config sub menus
		result = result + "<li><ul class=\"nav nav-tabs nav-stacked\" id=\"test"+i+"\">";
		
		for(var j=0;j<json[i].subMenu.length;j++){
			var pic;
			switch(j)
			{
			case 0:
				pic = "icon-home";break;
			case 1:
				pic = "icon-eye-open"; break;
			case 2:
				pic = "icon-edit";break;
			case 3:
				pic = "icon-list-alt";break;
			case 4:
				pic = "icon-font";break;
			case 5:
				pic = "icon-picture";break;
			default:
				pic = "icon-picture";break;
			}
			result = result + "<li><a class=\"ajax-link\" style=\"cursor:pointer;\" href=\""+json[i].subMenus[j].funcUrl +"\"><i class="+pic+"></i><span class=\"hidden-tablet\">"+json[i].subMenus[j].functionName + "</span></a></li>";
		}
		result = result +"</ul></li>";
	}
	$("#menus").append(result);
	/**menu add by bdqn_hl 2014-2-27 end*/
	
	
	
	
	
	
	
	
	
	
	