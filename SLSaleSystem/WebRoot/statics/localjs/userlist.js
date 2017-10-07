/**邮箱格式验证*/
function checkEmail(str){
	var reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	if(str == null || str == "" || reg.test(str))
		return true;
	else
		return false;
}
/*******************************添加用户弹框************************************************/
$('.adduser').click(function(e){
		$('#add_formtip').html('');
		e.preventDefault();
		$('#addUserDiv').modal('show');
	});
/**修改弹框**/
$('.modifyuser').click(function(e){
		$('#m_formtip').html('');
		e.preventDefault();
		$('#modifyUserDiv').modal('show');
	});
/**删除操作弹框**/

//*******************************************页面显示******************************************
//viewUserDiv
$('.viewuser').click(function(e){
	//获取id
	var m_id=$(this).attr('id');
	$.ajax({
		url:'/SLSaleSystem/backend/getUser.html',
		type:'post',
		data:{id:m_id},//key:value
		dataType:'json',
		timeout:1000,
		error:function(){
			alert("error!");
		},
		success:function(result){
			$("#v_id").val(result.id);
			$("#v_rolename").val(result.roleName);
			$("#v_usertypename").val(result.userTypeName);
			$("#v_logincode").val(result.loginCode);
			$("#v_username").val(result.userName);
			$("#v_sex").val(result.sex);
			$("#v_cardtypename").val(result.cardTypeName);
			$("#v_idcard").val(result.idCard);
			$("#v_birthday").val(result.birthday);
			$("#v_country").val(result.country);
			$("#v_mobile").val(result.mobile);
			$("#v_email").val(result.email);
			$("#v_postCode").val(result.postCode);
			$("#v_bankname").val(result.bankName);
			$("#v_bankaccount").val(result.bankAccount);
			$("#v_accountholder").val(result.accountHolder);
			$("#v_refercode").val(result.referCode);
			$("#v_createtime").val(result.createTime);
			//$("#v_isstart").val(result.isStart);
			var isstart = result.isStart;
			if(isstart == '1'){
				$("#v_isstart").append("<option value=\"1\" selected=\"selected\">启用</option><option value=\"2\">不启用</option>");
			}else{
				$("#v_isstart").append("<option value=\"1\">启用</option><option value=\"2\" selected=\"selected\">不启用</option>");
			}
			$("#v_useraddress").val(result.userAddress);
			
			$("#v_fileInputIDPath").val(result.idCardPicPath);
			var v_idcardpicpath = result.idCardPicPath;
			if(v_idcardpicpath == null || v_idcardpicpath == "" ){
				$("#v_idPic").append("暂无");
			}else{
				$("#v_idPic").append("<p><img src=\""+v_idcardpicpath+"?m="+Math.random()+"\" /></p>");
			}
			$("#v_fileInputBankPath").val(result.bankPicPath);
			var idPic=result.bankPicPath;
			if(idPic != null || idPic != ""){
				$("#v_bankPic").append("<p><img src=\""+idPic+"?m="+Math.random()+"\" /><p>");
			}else{
				$("#v_bankPic").append("暂无");
			}
			//<p><img src=\""+v_idcardpicpath+"?m="+Math.random()+"\" /></p>
			e.preventDefault();//屏蔽默认操作
			$('#viewUserDiv').modal('show');
		
		}
	})
});
//****************************************************
//modifyUserDiv
$('.modifyuser').click(function(e){
	//获取id
	var m_id=$(this).attr('id');
	$.ajax({
		url:'/SLSaleSystem/backend/getUser.html',
		type:'post',
		data:{id:m_id},//key:value
		dataType:'html',
		timeout:1000,
		error:function(){
			alert("error!");
		},
		success:function(result){
			if("failed" == result){
				alert("操作超时！");
			}else if("nodata" == result){
				alert("没有数据！");
			}else{
				m = eval('('+result+')');
				$("#m_id").val(m.id);
				$("#m_logincode").val(m.loginCode);
				$("#m_username").val(m.userName);
				$("#m_roleId").html('');
				$("#m_rolename").val(m.roleName);
				var roleId = m.roleId;
				var roleName = m.roleName;
				if(roleId == null || roleId == "")
					$("#m_roleId").append("<option value=\"\" selected=\"selected\" >--请选择--</option>");
				//遍历选项
					for(var i=0;i<roleListJson.length-1;i++){
						if(roleListJson[i].id == roleId){
							$("#m_roleId").append("<option value=\""+roleId+"\" selected=\"selected\" >"+roleName+"</option>");	
						}else{
							$("#m_roleId").append("<option value=\""+roleListJson[i].id+"\" selected=\"selected\" >"+roleListJson[i].roleName+"</option>");
						}
					}
				$("#m_selectusertypename").val(m.userTypeName);
				$("#m_selectusertype").html('');
				if(roleId == '2'){//管理员
					var userType = m.userType;
					var userTypeName = m.userTypeName;
					//是否存在用户类型；不存在===》选择  存在：显示并出现下拉菜单(json动态加载)
					if(userType == null || userType == "")
						$("#m_selectusertype").append("<option value=\"\" selected=\"selected\">--请选择--</option>");
						//$.post(url,[{"key":value,"key":value}],[function(){}],[type])
						$.post("/SLSaleSystem/backend/loadUserTypeList.html",
								{"s_role":roleId},function(result){
									if(result == null || result == ""){
										alert("用户类型加载失败！");
									}else{
										for(var i= 0; i<result.length;i++){
											if(result[i].valueId == userType){
												$("#m_selectusertype").append("<option value=\""+userType+"\" selected=\"selected\" >"+userTypeName+"</option>");	
											}else{
												$("#m_selectusertype").append("<option value=\""+result[i].valueId+"\" selected=\"selected\" >"+result[i].valueName+"</option>");
											}
										}
									}
								},'json');
					
				}else if(roleId == '1'){//会员
					$("#m_selectusertype").append("<option value=\"\" selected=\"selected\" >--请选择--</option>");;
				}
				
				var sex = m.sex;
				$("#m_sex").html('');
				if(sex == ''){
					$("#m_sex").append("<option value=\"\" selected=\"selected\">--请选择--</option><option value=\"男\">男</option><option value=\"女\">女</option>");
				}else if(sex == '男'){
					$("#m_sex").append("<option value=\"男\" selected=\"selected\">男<option value=\"女\">女</option>");
				}else if(sex == '女'){
					$("#m_sex").append("<option value=\"女\" selected=\"selected\">女<option value=\"男\">男</option>");
				}
				$("#m_cardtypename").val(m.cardTypeName);
				var cardType = m.cardType;
				var cardTypeName = m.cardTypeName;
				$("#m_cardtype").html('');
				if(cardType == null || cardType == "")
					$("#m_cardtype").append("<option value=\"\" selected=\"selected\">--请选择--</option>");
				for(var i=0;i<cardTypeListJson.length-1;i++){
					if(cardTypeListJson[i].valueId == cardType){
						$("#m_cardtype").append("<option value=\""+cardType+"\" selected=\"selected\">"+cardTypeName+"</option>");
					}else{
						$("#m_cardtype").append("<option value=\""+cardTypeListJson[i].valueId+"\">"+cardTypeListJson[i].valueName+"</option>");
					}
				}
				$("#m_idcard").val(m.idCard);
				$("#m_birthday").val(m.birthday);
				$("#m_country").val(m.country);
				$("#m_mobile").val(m.mobile);
				$("#m_email").val(m.email);
				$("#m_postcode").val(m.postCode);
				$("#m_bankname").val(m.bankName);
				$("#m_bankaccount").val(m.bankAccount);
				$("#m_accountholder").val(m.accountHolder);
				$("#m_refercode").val(m.referCode);
				$("#m_createtime").val(m.createTime);
				$("#m_isstart").val(m.isStart);
				var isstart = m.isStart;
				if(isstart == '1'){
					$("#m_isstart").append("<option value=\"1\" selected=\"selected\">启用</option><option value=\"2\">不启用</option>");
				}else{
					$("#m_isstart").append("<option value=\"1\">启用</option><option value=\"2\" selected=\"selected\">不启用</option>");
				}
				$("#m_useraddress").val(m.userAddress);
				
			
				$("#m_fileInputIDPath").val(m.idCardPicPath);
				var m_idcardpicpath = m.idCardPicPath;
				if(m_idcardpicpath == null || m_idcardpicpath == "" ){
					$("#m_uploadbtnID").show();
				}else{
					$("#m_idPic").append("<p><span onclick=\"delpic('"+m.id+"','m_idPic','m_uploadbtnID',this,'"+m_idcardpicpath+"','m_fileInputIDPath','m_fileInputID');\">x</span><img src=\""+m_idcardpicpath+"\" /></p>");
					$("#m_uploadbtnID").hide();
				}
				
				$("#m_fileInputBankPath").val(m.bankPicPath);
				var m_bankpicpath = m.bankPicPath;
				if(m_bankpicpath == null || m_bankpicpath == "" ){
					$("#m_uploadbtnBank").show();
					
				}else{
					$("#m_bankPic").append("<p><span onclick=\"delpic('"+m.id+"','m_bankPic','m_uploadbtnBank',this,'"+m_bankpicpath+"','m_fileInputBankPath','m_fileInputBank');\">x</span><img src=\""+m_bankpicpath+"\" /></p>");
					$("#m_uploadbtnBank").hide();
				}
				e.preventDefault();
				$('#modifyUserDiv').modal('show');
			}
		}
	});
});
//*******************************************清空操作******************************************
$('.modifyusercancel').click(function(e){
	$("#m_idPic").html('');
	$("#m_bankPic").html('');
	$("#m_formtip").html('');
	$("#m_isstart").html('');
});
//清空查看用户弹框
$('.viewusercancel').click(function(e){
	$("#v_isstart").html('');
	$("#v_idPic").html('');
	$("#v_bankPic").html('');
});
/**清空添加用户弹框*/
$('.addusercancel').click(function(e){
	$("#add_formtip").html('');
	$("#a_idPic").html('');
	$("#a_bankPic").html('');
	$("#selectrole").val('');
	$("#selectusertype").val('');
	$("#a_logincode").val('');
	$("#a_username").val('');
	$("#selectcardtype").val('');
	$("#a_idcard").val('');
	$("#a_mobile").val('');
	$("#a_email").val('');
	$("#a_postCode").val('');
	$("#a_bankname").val('');
	$("#a_bankaccount").val('');
	$("#a_accountholder").val('');
	$("#a_useraddress").val('');
	$("#selectusertype").html('<option value=\"\" selected=\"selected\" >--请选择--</option>');
});
/****************************通过选择角色来异步加载用户类型列表************************/
$("#selectrole").change(function(){
	$("#selectusertype").empty();
	$("#selectusertype").append("<option value=\"\" selected=\"selected\">--请选择--</option>");
	var sel_role = $("#selectrole").val();
	if(sel_role == 2){
		$.post("/SLSaleSystem/backend/loadUserTypeList.html",{'s_roleId':sel_role},function(result){
			if(result != ""){
				for(var i=0;i<result.length;i++){
					$("#selectusertype").append("<option value=\""+result[i].valueId+"\">"+result[i].valueName+"</option>");
				}
			}else{
				alert("用户类型加载失败！");
			}
		},'json');	
	}
});
/**********************判断用户添加时是否重名**********************/
$("#a_logincode").blur(function(){
	var alc = $("#a_logincode").val();
	if(alc != ""){
		//-1代表修改；非-1代表添加
		$.post("/SLSaleSystem/backend/logincodeisexit.html",{"loginCode":alc,"id":"-1"},function(result){
			if(result=="repeat"){
				$("#add_formtip").css("color","red");
				$("#add_formtip").html("<li>对不起，该用户名已存在。</li>");
				$("#add_formtip").attr("key",1);//重名标记
				result=false;
			}else if(result=="failed"){
				
				alert("操作操时！");
			}else if(result == "only"){
				$("#add_formtip").css("color","green");
				$("#add_formtip").html("<li>用户名正常！</li>");
				$("#add_formtip").attr("key",0);
			}
		},"html");
	}
	
});
//调用email失去焦点时验证
$("#a_email").blur(function(){
	var fiag = checkEmail($("#a_email").val());
	if(fiag == false){
		$("#add_formtip").css("color","red");
		$("#add_formtip").html("<li>邮箱格式输入错误！</li>");
	}else{
		$("#add_formtip").css("color","green");
		$("#add_formtip").html("<li>邮箱格式输入正确！</li>");
	}
});
/**************************判断用户修改时是否重名**********************/
$("#m_logincode").blur(function(){
	var mlc = $("#m_logincode").val();
	if(mlc != ""){
		//-1代表修改；非-1代表添加
		$.post("/SLSaleSystem/backend/logincodeisexit.html",{"loginCode":mlc,"id":$("#m_id").val()},function(result){
			if(result=="repeat"){
				$("#m_formtip").css("color","red");
				$("#m_formtip").html("<li>对不起，该用户名已存在。</li>");
				$("#m_formtip").attr("key",1);//重名标记
				result=false;
			}else if(result=="failed"){
				
				alert("操作操时！");
			}else if(result == "only"){
				$("#m_formtip").css("color","green");
				$("#m_formtip").html("<li>用户名正常！</li>");
				$("#m_formtip").attr("key",0);
			}
		},"html");
	}
	
});
//调用用户修改email失去焦点时验证
$("#m_email").blur(function(){
	var fiag = checkEmail($("#m_email").val());
	if(fiag == false){
		$("#m_formtip").css("color","red");
		$("#m_formtip").html("<li>邮箱格式输入错误！</li>");
	}else{
		$("#m_formtip").css("color","green");
		$("#m_formtip").html("<li>邮箱格式输入正确！</li>");
	}
});


//*********************添加用户的信息验证*****************************
function addUserFunction(){
	$("#add_formtip").html("");
	var result =true;
	if($("#selectrole").val() == ""){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>对不起，角色不能为空！！</li>");
		result = false;
	}
	if( $.trim($("#a_logincode").val()) =="" || $("#a_logincode").val() == null){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>对不起，用户名不能为空！！</li>");
		result = false;
	}else{//重名
		if($("#add_formtip").attr("key") == "1"){
			$("#add_formtip").css("color","red");
			$("#add_formtip").append("<li>对不起，用户名重复！！</li>");
			result = false;
		}
	}
	if($.trim($("#a_username").val()) == "" || $("#a_username").val() == null){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>对不起，真实姓名不能为空。</li>");
		result = false;
	}
	if($("#selectcardtype").val() == ""){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>对不起，证件类型不能为空。</li>");
		result = false;
	}
	if($.trim($("#a_idcard").val()) == "" || $("#a_idcard").val() == null){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>对不起，证件号码不能为空。</li>");
		result = false;
	}else{
		if($("#a_idcard").val().length < 6){
			$("#add_formtip").css("color","red");
			$("#add_formtip").append("<li>对不起，证件号码长度必须超过6位。</li>");
			result = false;
		}
	}
	if($.trim($("#a_mobile").val()) == "" || $("#a_mobile").val() == null){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>对不起，联系电话不能为空。</li>");
		result = false;
	}
	if($.trim($("#a_bankname").val()) == "" || $("#a_bankname").val() == null){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>对不起，开户行不能为空。</li>");
		result = false;
	}
	if($.trim($("#a_bankaccount").val()) == "" || $("#a_bankaccount").val() == null){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>对不起，开户卡号不能为空。</li>");
		result = false;
	}
	if($.trim($("#a_accountholder").val()) == "" || $("#a_accountholder").val() == null){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>对不起，开户人不能为空。</li>");
		result = false;
	}
	if($.trim($("#a_email").val()) != "" && $("#a_email").val() != null && $("#add_formtip").attr("email") == "1"){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>email格式不正确</li>");
		result = false;
	}
	if(result == true) {
		alert("添加成功 ^_^");
	}
	return result;
	
};
/*** **************************修改用户信息验证*************/
function modifyUserFunction(){
	$("#m_formtip").html("");
	var result =true;
	if($("#m_rolename").val() == ""){
		$("#m_formtip").css("color","red");
		$("#m_formtip").append("<li>对不起，角色不能为空！！</li>");
		result = false;
	}
	if( $.trim($("#m_logincode").val()) =="" || $("#m_logincode").val() == null){
		$("#m_formtip").css("color","red");
		$("#m_formtip").append("<li>对不起，用户名不能为空！！</li>");
		result = false;
	}else{//重名
		if($("#m_formtip").attr("key") == "1"){
			$("#m_formtip").css("color","red");
			$("#m_formtip").append("<li>对不起，用户名重复！！</li>");
			result = false;
		}
	}
	if($.trim($("#m_username").val()) == "" || $("#m_username").val() == null){
		$("#m_formtip").css("color","red");
		$("#m_formtip").append("<li>对不起，真实姓名不能为空。</li>");
		result = false;
	}
	if($("#m_selectcardtype").val() == ""){
		$("#m_formtip").css("color","red");
		$("#m_formtip").append("<li>对不起，证件类型不能为空。</li>");
		result = false;
	}
	if($.trim($("#m_idcard").val()) == "" || $("#m_idcard").val() == null){
		$("#m_formtip").css("color","red");
		$("#m_formtip").append("<li>对不起，证件号码不能为空。</li>");
		result = false;
	}else{
		if($("#m_idcard").val().length < 6){
			$("#m_formtip").css("color","red");
			$("#m_formtip").append("<li>对不起，证件号码长度必须超过6位。</li>");
			result = false;
		}
	}
	if($.trim($("#m_mobile").val()) == "" || $("#m_mobile").val() == null){
		$("#m_formtip").css("color","red");
		$("#m_formtip").append("<li>对不起，联系电话不能为空。</li>");
		result = false;
	}
	if($.trim($("#m_bankname").val()) == "" || $("#m_bankname").val() == null){
		$("#m_formtip").css("color","red");
		$("#m_formtip").append("<li>对不起，开户行不能为空。</li>");
		result = false;
	}
	if($.trim($("#m_bankaccount").val()) == "" || $("#m_bankaccount").val() == null){
		$("#m_formtip").css("color","red");
		$("#m_formtip").append("<li>对不起，开户卡号不能为空。</li>");
		result = false;
	}
	if($.trim($("#m_accountholder").val()) == "" || $("#m_accountholder").val() == null){
		$("#m_formtip").css("color","red");
		$("#m_formtip").append("<li>对不起，开户人不能为空。</li>");
		result = false;
	}
	if($.trim($("#m_email").val()) != "" && $("#m_email").val() != null && $("#m_formtip").attr("email") == "1"){
		$("#m_formtip").css("color","red");
		$("#m_formtip").append("<li>email格式不正确</li>");
		result = false;
	}
	if(result == true) {
		alert("修改成功 ^_^");
	}
	return result;
	
};

/*******************影藏域赋值；*************/
/*添加*/
$("#selectrole").change(function(){
	$("#selectrolename").val($("#selectrole").find("option:selected").text());
});
$("#selectusertype").change(function(){
	$("#selectusertypename").val($("#selectusertype").find("option:selected").text());
});
$("#selectcardtype").change(function(){
	$("#selectcardtypename").val($("#selectcardtype").find("option:selected").text()) ;
});
/**修改*/
$("#m_selectusertype").change(function(){
	$("#m_selectusertypename").val($("#m_selectusertype").find("option:selected").text()) ;
});
$("#m_selectcardtype").change(function(){
	$("#m_selectcardtypename").val($("#m_selectcardtype").find("option:selected").text());
});
/**************用户类型加载***************/
$("#m_roleId").change(function(){
	$("#m_rolename").val($("#m_roleId").find("option:selected").text()) ;
	$("#m_selectusertype").empty();
	$("#m_selectusertype").append("<option value=\"\" selected=\"selected\">--请选择--</option>");
	var sel_role = $("#m_roleId").val();
	if(sel_role == 2){
		$.post("/SLSaleSystem/backend/loadUserTypeList.html",{'s_role':sel_role},function(result){
			if(result != ""){
				for(var i=0;i<result.length;i++){
					$("#m_selectusertype").append("<option value=\""+result[i].valueId+"\">"+result[i].valueName+"</option>");
				}
			}else{
				alert("用户类型加载失败！");
			}
		},'json');	
	}else{
		$("#m_selectusertypename").val('');
	}
});

//图片上传添加、修改公共方法；
	//添加
$("#a_uploadbtnID").click(function(){
	TajaxFileUpload('0','a_fileInputID','a_uploadbtnID','a_idPic','a_fileInputIDPath');
});
$("#a_uploadbtnBank").click(function(){
	TajaxFileUpload('0','a_fileInputBank','a_uploadbtnBank','a_bankPic','a_fileInputBankPath');
});
	//修改
$("#m_uploadbtnID").click(function(){
	TajaxFileUpload($("#m_id").val(),'m_fileInputID','m_uploadbtnID','m_idPic','m_fileInputIDPath');
});
$("#m_uploadbtnBank").click(function(){
	TajaxFileUpload($("#m_id").val(),'m_fileInputBank','m_uploadbtnBank','m_bankPic','m_fileInputBankPath');
});

//文件上传方法调用；
	//'0','a_fileInputID','a_uploadbtnID','a_idPic','a_fileInputIDPath'
function TajaxFileUpload(flag,t1,t2,t3,t4){
	if($("#"+t1+"").val() =='' || $("#"+t1+"").val() == null   ){
		alert("请上传文件！！");
	}else{
		$.ajaxFileUpload({
			url: "/SLSaleSystem/backend/upload.html",
			secureuri:false,
			fileElementId :t1,
			dataType:'json',
			success:function(data){
				data = data.replace(/(^\s*)|(\s*$)/g, "");
				//data = data.replace(/(^\s*)|(|s*$)/g,"");
				//data判断：data==1,文件过大；
				if(data=="1"){ 
					//alert:提示
					alert("上传图片大小不得超过50K！");
					$("#uniform-"+t1+" span:first").html("无文件");
					//获取input标签：(标签属性选择器)
					//$("input[name!='basketball']")
					$("input[name-'"+t1+"']").change(function(){
						 //取出要上传的文件名
						var fn =$("input[name-'"+t1+"']").val();
						//IE兼容性处理
						if($.browser.mise){
							//截取路径
							fn=fn.subString(fn.lastIndexOf("\\")+1);
						}
						$("#uniform-"+t1+" span:first").html(fn);
					
					});
				}else if(data == "2"){
					//格式不正确：2---提示，同上
					alert("上传图片格式不正确！");
	     		   	$("#uniform-"+t1+" span:first").html('无文件');
	     		   	$("input[name='"+t1+"']").change(function(){
	     			var fn = $("input[name='"+t1+"']").val(); 
	     			   if($.browser.msie){
	     				   fn = fn.substring(fn.lastIndexOf("\\")+1);
	     			   }
	     			   $("#uniform-"+t1+" span:first").html(fn);
	     		   });
				
				}else{
					//?m="+Math.random() 是为了解决浏览器缓存,防止文件重复
					$("#"+t3+"").append("<p><span onclick=\"delpic('"+flag+"','"+t3+"','"+t2+"',this,'"+data+"','"+t4+"','"+t1+"');\">X</span><img src=\""+data+"?m="+Math.random()+"\" /><p>");
					$("#"+t2+"").hide();
					$("#"+t4+"").val(data);
	      		   	$("input[name='"+t1+"']").change(function(){
	      		   		var fn = $("input[name='"+t1+"']").val(); 
	      		   		if($.browser.msie){
	      		   			fn = fn.substring(fn.lastIndexOf("\\")+1);
	      		   		}
	      		   		$("#uniform-"+t1+" span:first").html(fn);
	      		   	});
					}
				},
				error:function() {
					alert("上传失败！");
				}
			});
	}
}
//********************图片删除*******************
function delpic(id,closeSpan,uploadBtn,obj,picpath,picText,fileinputid){
	//delete
	$.post("/SLSaleSystem/backend/delpic.html",{'id':id,'picpath':picpath},function(result){
		if("success" == result){
			alert("删除成功！");
			$('#'+picText).val('');
   			$("#uniform-"+fileinputid+" span:first").html('无文件');   			
   			document.getElementById(closeSpan).removeChild(obj.parentElement);
   			//$("#"+closeSpan).html('');这种方式跟上面一样效果
   			//最后再把上传的按钮显示
			$('#'+uploadBtn).show();
		}else
			alert("删除失败！");
	},'html');
	
}
/*************删除用户操作***************/
$('.deluser').click(function(e){
	//delete
	var d = $(this);
	var d_usertype = d.attr('usertype');
	var d_usertypename = d.attr('usertypename');
	var d_logincode = d.attr('logincode');
	var d_id = d.attr('id');
	var d_idcardpicpath = d.attr('idcardpicpath');
	var d_bankpicpath = d.attr('bankpicpath');
	if(confirm("您确定要删除【"+d_logincode+"】这个用户吗？")){
		//delete
		$.post("/SLSaleSystem/backend/deluser.html",{'delId':d_id,'delcardpic':d_idcardpicpath,'delbankpic':d_bankpicpath,'deluserType':d_usertype},function (res){
			if("success" == res){
				alert('删除成功！');
				window.location.href='/SLSaleSystem/backend/userlist.html';
			}else if("noallow" == res){
				alert("该用户类型为:【"+d_usertypename+"】，不允许被删除！");
			}else{
				alert("删除失败！");
			}
		},'html');
	}
});


