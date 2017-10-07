<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/pages/common/head.jsp" %>
	
	<!-- content starts -->
			

			<div>
				<ul class="breadcrumb">
					<li>
						<a href="#">后台管理</a> <span class="divider">/</span>
					</li>
					<li>
						<a href="/backend/userList.html">用户管理</a>
					</li>
				</ul>
			</div>
			
			<div class="row-fluid sortable">		
				<div class="box span12">
					<div class="box-header well" data-original-title>
						<h2><i class="icon-user"></i> 用户列表</h2>
						<div class="box-icon">
							<span class="icon-32 icon-color icon-add adduser" />
						</div>
					</div>
					<div class="box-content">
						<!-- 模糊查询 -->
						<form  action="/backend/userList.html"  method="post">
							<div class="searcharea">
								用户名称：
								<input type="text" name="" value="${s_loginCode}" >
							
							
							
							</div>
						
						
						</form>
						
						<!-- table start -->
						<table class="table table-striped table-bordered bootstrap-datatable datatable">
						  <thead>
							  <tr>
								  <th>Username</th>
								  <th>Date registered</th>
								  <th>Role</th>
								  <th>Status</th>
								  <th>Actions</th>
							  </tr>
						  </thead>   
						  <tbody>
							<tr>
								<td>David R</td>
								<td class="center">2012/01/01</td>
								<td class="center">Member</td>
								<td class="center">
									<span class="label label-success">Active</span>
								</td>
								<td class="center">
									<a class="btn btn-success" href="#">
										<i class="icon-zoom-in icon-white"></i>  
										View                                            
									</a>
									<a class="btn btn-info" href="#">
										<i class="icon-edit icon-white"></i>  
										Edit                                            
									</a>
									<a class="btn btn-danger" href="#">
										<i class="icon-trash icon-white"></i> 
										Delete
									</a>
								</td>
							</tr>
						</tbody>
					</table>  
					<!-- table end -->
					<div class="pagination pagination-centered">
						  <ul>
							<li><a href="#">Prev</a></li>
							<li class="active">
							  <a href="#">1</a>
							</li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">Next</a></li>
						  </ul>
					</div> 
				</div>
			</div>
		</div>

<%@include file="/WEB-INF/pages/common/foot.jsp" %>


<div class="modal hide fade" id="viewUserDiv">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">×</button>
					<h3>查看用户信息</h3>
				</div>
				<div class="modal-body">
					<input id="v_id" type="hidden" value=""/>
					<ul class="topul">
						<li><label>角色：</label> 
						<input id="v_rolename" type="text" value="" >
						</li>
						
						<li><label>会员类型：</label> 
						<input id="v_usertypename" type="text"  value="" >
						
						</li>
						<li><label>用户名：</label> 
							<input id="v_logincode" type="text"  value=""  >
						</li>
						<li><label>姓名：</label> 
							<input id="v_username" type="text" value="" >
						</li>
						<li>
						  <label>性别：</label>
			 			  <input id="v_sex"  type="text" value="" >
			 			    	 
						</li>
						<li><label>证件类型：</label> 
						<input id="v_cardtypename" type="text"  value="" >
						</li>
						<li><label>证件号码：</label> 
							<input id="v_idcard" type="text" value="" >
						</li>
						
						<li>
						  <label>生日：</label>
						 <input type="text" id="v_birthday" value=""/>
						</li>
						<li>
						  <label>收货国家：</label>
						  <input type="text" id="v_country" value=""/>
						</li>
						<li>
						  <label>联系电话：</label>
						  <input type="text" id="v_mobile" value="" />
						</li>
						<li>
						  <label>Email：</label>
						  <input type="text" id="v_email" value=""/>
						</li>
						<li>
						  <label>邮政编码：</label>
						  <input type="text" id="v_postCode" name="postCode" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
						</li>
						<li>
						  <label>开户行：</label>
						  <input type="text" id="v_bankname" name="bankName"/>
						  <span style="color:red;font-weight: bold;">*</span>
						</li>
						<li>
						  <label>开户卡号：</label>
						  <input type="text" id="v_bankaccount" value=""/>
						</li>
						<li>
						  <label>开户人：</label>
						  <input type="text" id="v_accountholder" value=""/>
						</li>
						<li>
						  <label>推荐人：</label>
						  <input type="text" value="" id="v_refercode" />
						</li>
						<li>
						  <label>注册时间：</label>
						   <input type="text" id="v_createtime" value="" />
						   </li>
						<li>
						  <label>是否启用：</label>
			 			  <select id="v_isstart" style="width:100px;" disabled="disabled">
					  </select>
						</li>
						<li class="lastli">
						  <label>收货地址：</label>
						  <textarea id="v_useraddress" name="userAddress"></textarea>
						</li>
					</ul>
					<div class="clear"></div>
					<ul class="downul">
						<li>
							<label>上传身份证图片：</label> 
							<input type="hidden" id="v_fileInputIDPath" value=""/>
							<div id="v_idPic"></div>
						</li>
						<li>
							<label>上传银行卡图片：</label> 
							<input type="hidden" id="v_fileInputBankPath" value=""/>
							<div id="v_bankPic"></div>
						</li>
					</ul>
				</div>
				<div class="modal-footer">
					<a href="javascript:void();" class="btn  viewusercancel" data-dismiss="modal">关闭</a>
				</div>
		</div>


