//获取功能列表（回显加载checkbox）;显示默认填充信息， hidden域用来传递当前点击的选项id；
$('.roleNameAuthority').click(function(){
	var authority = $(this);
	var roleId = authority.attr("roleid");
	$("#selectrole").html("当前配置角色为：" + authority.attr("rolename"));
	$("#roleidhide").val(roleId);
	//get functionList 
	$.ajax({
		url: '/SLSaleSystem/backend/functions.html',
		type: 'POST',
		data:{fid:roleId},
		dataType: 'json',
		timeout: 1000,
		error: function(){
			alert("加载功能列表失败！");
		},
		success: function(result){
			if(result == "nodata"){
				alert("对不起，功能列表获取失败，请重试。");
			}else{
				//var json = eval('(' + result + ')');
				//返回结果为html时转换
				listr = "";
				
				for(var i=0;i<result.length;i++){
					listr += "<li>";
					listr += "<ul id=\"subfuncul"+result[i].mainFunction.id+"\"  class=\"subfuncul\">";
					listr += "<li  class=\"functiontitle\" ><input id='functiontitle"+result[i].mainFunction.id+"' onchange='mainFunctionSelectChange(this,"+result[i].mainFunction.id+");' funcid=\""+result[i].mainFunction.id+"\" type='checkbox' />"+result[i].mainFunction.functionName+"</li>";
					for(j=0;j<result[i].subFunctions.length;j++){
						
						listr += "<li><input onchange='subFunctionSelectChange(this,"+result[i].mainFunction.id+");' funcid=\""+result[i].subFunctions[j].id+"\" type='checkbox' /> "+result[i].subFunctions[j].functionName+"</li>";
					}
					listr += "</ul></li>";
				}
				
				$("#functionList").html(listr);
				
				//get default value
				$("#functionList :checkbox").each(function () {  
					var checkbox = $(this);
					$.ajax({
						url: '/SLSaleSystem/backend/getAuthorityDefault.html',
						type: 'POST',
						data:{rid:$("#roleidhide").val(),fid:$(this).attr("funcid")},
						dataType: 'html',
						timeout: 1000,
						error: function(){
							alert("回显勾选失败");
						},
						success: function(result){
							if(result == "success"){
								//alert("ok");
								checkbox.attr("checked", true); 
							}else{
								//alert("no");
								checkbox.attr("checked", false);
							}
						}
						});
				});
			}
		}
		});
});

function subFunctionSelectChange(obj,id){
	if(obj.checked){
		$("#functiontitle"+id).attr("checked", true);  
	}
}

function mainFunctionSelectChange(obj,id){
	if(obj.checked){
		$("#subfuncul"+id+" :checkbox").attr("checked", true);  
	}else{
		$("#subfuncul"+id+" :checkbox").attr("checked", false);  
	}
	
	//alert($(this) +　id);
}

$("#selectAll").click(function () {//全选  
    $("#functionList :checkbox").attr("checked", true);  
});  

$("#unSelect").click(function () {//全不选  
    $("#functionList :checkbox").attr("checked", false);  
});  

$("#reverse").click(function () {//反选  
    $("#functionList :checkbox").each(function () {  
        $(this).attr("checked", !$(this).attr("checked"));  
    });  
});  


//提交修改
$("#confirmsave").click(function(){
	
	if(confirm("您确定要修改当前角色的权限吗？")){
	//使用“-”对角色权限进行分割，便于后台数组处理
	ids = $("#roleidhide").val()+"-";
	$("#functionList :checkbox").each(function () {
		if($(this).attr("checked") == 'checked'){
			ids += $(this).attr("funcid") + "-" ;
		}
    });
	//ajax异步提交
	alert(ids);
	$.ajax({
		url: '/SLSaleSystem/backend/modifyAuthority.html',
		type: 'POST',
		data:{ids:ids},
		dataType: 'html',
		timeout: 1000,
		error: function(){
			alert("修改权限失败！");
		},
		success: function(result){
			if(result == "nodata"){
				alert("对不起，功能列表获取失败，请重试。");
			}else{
				alert("恭喜您，权限修改成功。");
			}
		}
		});
	}
});