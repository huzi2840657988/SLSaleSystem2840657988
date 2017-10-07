<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/pages/common/head.jsp" %>

<div>
	<ul class="breadcrumb">
		<li>
			<a href="#">后台管理</a> <span class="divider">/</span>
		</li>
		<li>
			<a href="/SLSaleSystem/backend/rolelist.html">角色管理</a>
		</li>
	</ul>
</div>

<div class="row-fluid sortable">		
	<div class="box span12">
		<div class="box-header well" data-original-title>
			<h2><i class="icon-user"></i>角色列表</h2>
			<div class="box-icon">
				<span data-original-title="新增角色" data-rel="tooltip" class="icon32 icon-color icon-add custom-setting addrole"/>
			</div>
		</div>
 <div class="box-content">
 
	<table class="table table-striped table-bordered bootstrap-datatable datatable">
	  <thead>
		  <tr>
			  <th>角色代码</th>
			  <th>角色名称</th>
			  <th>创建时间</th>
			  <th>是否启用</th>
			  <th>创建者</th>
			  <th>操作</th>
		  </tr>
	  </thead>   
	  <tbody>
		
		  <c:forEach items="${roleList}" var="role">
			<tr>
				<td class="center"><input id="roleCode${role.id}" type="text" value="${role.roleCode}"/></td>
				<td class="center"><input id="roleName${role.id}" type="text" value="${role.roleName}"/></td>
				<td class="center"><fmt:formatDate value="${role.createDate}" pattern="yyyy-MM-dd"/></td>
				<td class="center">
				<input type="checkbox" title="直接勾选修改状态，立即生效" data-rel="tooltip" class="modifyIsStart" isstart="${role.isStart}" roleid="${role.id}" <c:if test="${role.isStart == 1}">checked="true"</c:if>/>
				</td>
				<td class="center">
					<a class="btn btn-info modifyrole" rolecode="${role.roleCode}" rolename="${role.roleName}" roleid="${role.id}" href="#">
						<i class="icon-edit icon-white"></i>  
						修改                                            
					</a>
					<a class="btn btn-danger delrole" rolename="${role.roleName}" roleid="${role.id}" href="#">
						<i class="icon-trash icon-white"></i> 
						删除
					</a>
				</td>
			</tr>
		  </c:forEach>
		
		</tbody>
	</table>
	<%-- <div class="pagination pagination-centered">
	  <ul>
	  <c:choose>
	  	<c:when test="${page.page == 1}">
	  	<li class="active"><a href="javascript:void();" title="首页">首页</a></li>
	  	</c:when>
	  	<c:otherwise>
	  	<li><a href="/SLSaleSystem/backend/userlist.html?current=1 & s_loginCode=${s_loginCode} & s_referCode=${s_referCode} & s_roleId=${s_roleId} & s_isStart=${s_isStart}" title="首页">首页</a></li>
	  	</c:otherwise>
	  </c:choose>
		<c:if test="${page.prevPages != null}">
			<c:forEach items="${page.prevPages}" var="num">
				<li><a href="/SLSaleSystem/backend/userlist.html?current=${num} & s_loginCode=${s_loginCode} & s_referCode=${s_referCode} & s_roleId=${s_roleId} & s_isStart=${s_isStart}"
					class="number" title="${num}">${num}</a></li>
			</c:forEach>
		</c:if>
		<li class="active">
		  <a href="#" title="${page.page}">${page.page}</a>
		</li>
		<c:if test="${page.nextPages!=null}">
			<c:forEach items="${page.nextPages}" var="num">
				<li><a href="/SLSaleSystem/backend/userlist.html?current=${num} & s_loginCode=${s_loginCode} & s_referCode=${s_referCode} & s_roleId=${s_roleId} & s_isStart=${s_isStart}" title="${num}">
				${num} </a></li>
			</c:forEach>
		</c:if>
		<c:if test="${page.pageCount !=null}">
			<c:choose>
		  	<c:when test="${page.page == page.pageCount}">
		  	<li class="active"><a href="javascript:void();" title="尾页">尾页</a></li>
		  	</c:when>
		  	<c:otherwise>
		  	<li><a href="/SLSaleSystem/backend/userlist.html?current=${page.pageCount}&s_loginCode=${s_loginCode}&s_referCode=${s_referCode}&s_roleId=${s_roleId}&s_isStart=${s_isStart}" title="尾页">尾页</a></li>
		  	</c:otherwise>
		    </c:choose>
	    </c:if>
		<c:if test="${page.pageCount == null}">
		<li class="active"><a href="javascript:void();" title="尾页">尾页</a></li>
	  	</c:if>
	  </ul>
	</div>  --%>  
	
	
  </div>
</div>
</div>

		<div class="modal hide fade" id="addRoleDiv">
				<div class="modal-header">
					<button type="button" id="addRoleClose" class="close" data-dismiss="modal">×</button>
				<h3>添加角色信息</h3>
				</div>
				<div class="modal-body">
					<ul class="topul">
						<li>
						  <label>角色代码：</label><input type="text" id="roleCode" />
						</li>
						<li>
						  <label>角色名称：</label><input type="text" id="roleName" />
						</li>
						<li id="formtip"></li>
					</ul>
					<div class="clear"></div>
				</div>
				<div class="modal-footer">
					<a href="#" class="btn addRoleCancel" data-dismiss="modal">关闭</a>
					<input type="button" id="addRoleBtn" class="btn btn-primary" value="保存">
				</div>
		
		</div>

<%@include file="/WEB-INF/pages/common/foot.jsp" %>
<script src="/SLSaleSystem/statics/localjs/rolelist.js"></script>


	