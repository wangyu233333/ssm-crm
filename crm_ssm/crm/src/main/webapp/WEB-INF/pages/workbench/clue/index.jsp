<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css"
          href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

    <script type="text/javascript">

        $(function () {


            //  给“创建”按钮添加单击事件
            $("#createCuleBtn").click(function () {
                //  初始化工作
                $("#createClueForm")[0].reset();       //   清空表单
                //  弹出模态窗口
                $("#createClueModal").modal("show");
            });

            //  给“保存”按钮添加单击事件
            $("#saveCreateClueBtn").click(function () {
                var fullname = $.trim($("#create-fullname").val());
                var appellation = $("#create-appellation").val();
                var owner = $("#create-owner").val();
                var company = $.trim($("#create-company").val());
                var job = $.trim($("#create-job").val());
                var email = $.trim($("#create-email").val());
                var phone = $.trim($("#create-phone").val());
                var website = $.trim($("#create-website").val());
                var mphone = $.trim($("#create-mphone").val());
                var state = $("#create-state").val();
                var source = $("#create-source").val();
                var description = $.trim($("#create-description").val());
                var contactSummary = $.trim($("#create-contactSummary").val());
                var nextContactTime = $.trim($("#create-nextContactTime").val());
                var address = $.trim($("#create-address").val());

                //  表单验证
                if (company == "") {
                    alert("公司不能为空");
                    return;
                }
                if (fullname == "") {
                    alert("姓名不能为空");
                    return;
                }
                var pathn2 = /^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/;
                if (!pathn2.exec(mphone)) {
                    alert("手机号码不符合")
                    return;
                }


                //  发起 请求
                $.ajax({
                    url: 'workbench/clue/saveCreateClue.do',
                    data: {
                        fullname: fullname,
                        appellation: appellation,
                        owner: owner,
                        company: company,
                        job: job,
                        email: email,
                        phone: phone,
                        website: website,
                        mphone: mphone,
                        state: state,
                        source: source,
                        description: description,
                        contactSummary: contactSummary,
                        nextContactTime: nextContactTime,
                        address: address
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == "1") {
                            $("#createClueModal").modal("hide");
                            //	刷新市场活动，显示第一页数据，保持每页显示条数不变（保留）
                            queryClueByConditionForPage(1, $("#demo_page1").bs_pagination('getOption', 'rowsPerPage'));

                        } else {
                            alert(data.message);
                            $("#createClueModal").modal("show");
                        }
                    }
                });

            });


            //	当页面加载完成显示所有数据的分页查询
            queryClueByConditionForPage(1, 10);

            //	给 "查询" 按钮添加单击事件
            $("#queryClueBtn").click(function () {
                queryClueByConditionForPage(1, $("#demo_page1").bs_pagination('getOption', 'rowsPerPage'));
            });


            //  给“全选”按钮添加单击事件
            $("#checkboxAll").click(function () {
                //  如果“全选”按钮是选中状态，则列表中所有的checkbox都选择
                $("#tBody input[type='checkbox']").prop("checked", this.checked);
            });

            $("#tBody").on("click", "input[type='checkbox']", function () {
                //  如果列表中所有checkbox都选中了，则全选按钮也选中
                if ($("#tBody input[type='checkbox']").size() == $("#tBody input[type='checkbox']:checked").size()) {
                    $("#checkboxAll").prop("checked", true);
                } else {
                    $("#checkboxAll").prop("checked", false);
                }
            });


            //  给“修改”按钮添加单击事件
            $("#editClueBtn").click(function () {
                //  收集参数
                var checkedIds = $("#tBody input[type='checkbox']:checked");

                if (checkedIds.size() == 0) {
                    alert("请选择要修改的市场活动");
                    return;
                }
                if (checkedIds.size() > 1) {
                    alert("一次只能选中一条记录");
                    return;
                }
                var id = checkedIds.val();
                //  发起请求
                $.ajax({
                    url: 'workbench/clue/queryClueById.do',
                    data: {
                        id: id
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        //  把市场活动的信息显示在修改的模态窗口上
                        $("#edit_id").val(data.id)
                        $("#edit-fullname").val(data.fullname);
                        $("#edit-company").val(data.company);
                        $("#edit-job").val(data.job);
                        $("#edit-email").val(data.email);
                        $("#edit-phone").val(data.phone);
                        $("#edit-website").val(data.website);
                        $("#edit-mphone").val(data.mphone);
                        $("#edit-description").val(data.description);
                        $("#edit-contactSummary").val(data.contactSummary);
                        $("#edit-nextContactTime").val(data.nextContactTime);
                        $("#edit-address").val(data.address);
                        //  弹出模态窗口
                        $("#editClueModal").modal("show");
                    }
                });

            });

            //  给“更新”按钮添加单击事件
            $("#saveClueBtn").click(function () {
                //  收集参数
                var id = $("#edit_id").val()
                var fullname = $.trim($("#edit-fullname").val());
                var appellation = $("#edit-appellation option:selected").val();
                var owner = $("#edit-owner option:selected").val();
                var company = $.trim($("#edit-company").val());
                var job = $.trim($("#edit-job").val());
                var email = $.trim($("#edit-email").val());
                var phone = $.trim($("#edit-phone").val());
                var website = $.trim($("#edit-website").val());
                var mphone = $.trim($("#edit-mphone").val());
                var state = $("#edit-state option:selected").val();
                var source = $("#edit-source option:selected").val();
                var description = $.trim($("#edit-description").val());
                var contactSummary = $.trim($("#edit-contactSummary").val());
                var nextContactTime = $.trim($("#edit-nextContactTime").val());
                var address = $.trim($("#edit-address").val());
                //  表单验证
                if (fullname == "" && company == "") {
                    alert("所有者或名称不能为空");
                    return;
                }
                //  发起请求
                $.ajax({
                    url: 'workbench/clue/saveEditClue.do',
                    data: {
                        id: id,
                        fullname: fullname,
                        appellation: appellation,
                        owner: owner,
                        company: company,
                        job: job,
                        email: email,
                        phone: phone,
                        website: website,
                        mphone: mphone,
                        state: state,
                        source: source,
                        description: description,
                        contactSummary: contactSummary,
                        nextContactTime: nextContactTime,
                        address: address
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == "1") {
                            //  关闭模态窗口
                            $("#editClueModal").modal("hide");
                            //  刷新列表，保持页号和每页显示条数不变
                            queryClueByConditionForPage($("#demo_page1").bs_pagination('getOption', 'currentPage'), $("#demo_page1").bs_pagination('getOption', 'rowsPerPage'));
                        } else {
                            //  提示信息
                            alert(data.message);
                            //  模态窗口不关闭
                            $("#editClueModal").modal("show");
                        }
                    }
                });

            });

            $("#deleteClueBtn").click(function() {
                var checkedIds = $("#tBody input[type='checkbox']:checked");
                if (checkedIds.size() == 0 ){
                    alert("请选择要删除的线索！！！");
                    return;
                }
                if (window.confirm("确定删除吗？")){
                    var ids = "";
                    //  遍历 checkedIds 获取每个被选中对象的id
                    $.each(checkedIds,function () {
                        ids+="ids="+this.value+"&";
                    });
                    ids= ids.substr(0, ids.length - 1);

                    //  发起请求
                    $.ajax({
                        url:'workbench/clue/deleteClueByIds.do',
                        data:ids,
                        type:"post",
                        dataType:"json",
                        success:function (data) {
                            if (data.code == "1"){
                                //	刷新市场活动，显示第一页数据，保持每页显示条数不变（保留）
                                queryClueByConditionForPage(1,  $("#demo_page1").bs_pagination('getOption','rowsPerPage'));
                            }else{
                                alert(data.message);
                            }
                        }
                    });
                }
            });


        });


        //	封装分页查询函数
        function queryClueByConditionForPage(pageNo, pageSize) {
            //	收集参数
            var name = $("#name").val();
            var owner = $("#owner").val();
            var company = $("#company").val();
            var phone = $("#phone").val();
            var source = $("#source").val();
            var mphone = $("#mphone").val();
            var state = $("#qstatus").val();

            //  var pageNo = 1;
            //  var pageSize = 10;

            //	发送请求
            $.ajax({
                url: 'workbench/activity/queryClueByConditionForPage.do',
                data: {
                    name: name,
                    owner: owner,
                    company: company,
                    phone: phone,
                    source: source,
                    mphone: mphone,
                    state: state,
                    pageNo: pageNo,
                    pageSize: pageSize
                },
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    //	显示总条数
                    // $("#totalRowsB").html(data.totalRows)
                    //	显示市场活动的列表
                    //	遍历activityList，拼接所有行数据
                    var htmlStr = "";
                    $.each(data.clueList, function (index, obj) {
                        htmlStr += "<tr>";
                        htmlStr += "<td><input type=\"checkbox\" value=\"" + obj.id + "\" /></td>";
                        htmlStr += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/clue/detailClue.do?id=" + obj.id + "';\">" + obj.fullname + obj.appellation + "</a></td>";
                        htmlStr += "<td>" + obj.company + "</td>";
                        htmlStr += "<td>" + obj.phone + "</td>";
                        htmlStr += "<td>" + obj.mphone + "</td>";
                        htmlStr += "<td>" + obj.source + "</td>";
                        htmlStr += "<td>" + obj.owner + "</td>";
                        htmlStr += "<td>" + obj.state + "</td>";
                        htmlStr += "</tr>";
                    });
                    $("#tBody").html(htmlStr);

                    //  取消全选按钮
                    $("#checkboxAll").prop("checked", false);

                    //  总页数
                    var totalPages = 1;
                    if (data.totalRows % pageSize == 0) {
                        totalPages = data.totalRows / pageSize;
                    } else {
                        totalPages = parseInt(data.totalRows / pageSize) + 1;
                    }


                    //调用工具函数，显示翻页信息
                    $("#demo_page1").bs_pagination({
                        currentPage: pageNo,//当前页号,相当于pageNo

                        rowsPerPage: pageSize,//每页显示条数,相当于pageSize
                        totalRows: data.totalRows,//总条数
                        totalPages: totalPages,  //总页数,必填参数.

                        visiblePageLinks: 5,//最多可以显示的卡片数

                        showGoToPage: true,//是否显示"跳转到"部分,默认true--显示
                        showRowsPerPage: true,//是否显示"每页显示条数"部分。默认true--显示
                        showRowsInfo: true,//是否显示记录的信息，默认true--显示

                        //用户每次切换页号，都自动触发本函数;
                        //每次返回切换页号之后的pageNo和pageSize
                        onChangePage: function (event, pageObj) { // returns page_num and rows_per_page after a link has clicked
                            //js代码
                            //alert(pageObj.currentPage);// 变化后的pageNo
                            //alert(pageObj.rowsPerPage);// 变化后的pageSize
                            queryClueByConditionForPage(pageObj.currentPage, pageObj.rowsPerPage);
                        }
                    });
                }
            });


        }


    </script>
</head>
<body>

<!-- 创建线索的模态窗口 -->
<div class="modal fade" id="createClueModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 90%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">创建线索</h4>
            </div>
            <div class="modal-body">
                <form id="createClueForm" class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="create-owner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-owner">
                                <c:forEach items="${userList}" var="user">
                                    <option value="${user.id}">${user.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="create-company" class="col-sm-2 control-label">公司<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-company">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-appellation" class="col-sm-2 control-label">称呼</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-appellation">
                                <option></option>
                                <c:forEach items="${appellationList}" var="a">
                                    <option value="${a.id}">${a.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="create-fullname" class="col-sm-2 control-label">姓名<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-fullname">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-job" class="col-sm-2 control-label">职位</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-job">
                        </div>
                        <label for="create-email" class="col-sm-2 control-label">邮箱</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-email">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-phone" class="col-sm-2 control-label">公司座机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-phone">
                        </div>
                        <label for="create-website" class="col-sm-2 control-label">公司网站</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-website">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-mphone" class="col-sm-2 control-label">手机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-mphone">
                        </div>
                        <label for="create-state" class="col-sm-2 control-label">线索状态</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-state">
                                <option></option>
                                <c:forEach items="${clueStateList}" var="cs">
                                    <option value="${cs.id}">${cs.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-source" class="col-sm-2 control-label">线索来源</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-source">
                                <option></option>
                                <c:forEach items="${sourceList}" var="s">
                                    <option value="${s.id}">${s.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="create-description" class="col-sm-2 control-label">线索描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-description"></textarea>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                    <div style="position: relative;top: 15px;">
                        <div class="form-group">
                            <label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-nextContactTime">
                            </div>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                    <div style="position: relative;top: 20px;">
                        <div class="form-group">
                            <label for="create-address" class="col-sm-2 control-label">详细地址</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="1" id="create-address"></textarea>
                            </div>
                        </div>
                    </div>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveCreateClueBtn">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改线索的模态窗口 -->
<div class="modal fade" id="editClueModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 90%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">修改线索</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <input type="hidden" id="edit_id">
                    <div class="form-group">
                        <label for="edit-owner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-owner">
                                <c:forEach items="${userList}" var="user">
                                    <option value="${user.id}">${user.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="edit-company" class="col-sm-2 control-label">公司<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-company" value="动力节点">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-appellation" class="col-sm-2 control-label">称呼</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-appellation">
                                <option></option>
                                <c:forEach items="${appellationList}" var="a">
                                    <option value="${a.id}">${a.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="edit-fullname" class="col-sm-2 control-label">姓名<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-fullname" value="李四">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-job" class="col-sm-2 control-label">职位</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-job" value="CTO">
                        </div>
                        <label for="edit-email" class="col-sm-2 control-label">邮箱</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-email" value="lisi@bjpowernode.com">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-phone" value="010-84846003">
                        </div>
                        <label for="edit-website" class="col-sm-2 control-label">公司网站</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-website"
                                   value="http://www.bjpowernode.com">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-mphone" class="col-sm-2 control-label">手机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-mphone" value="12345678901">
                        </div>
                        <label for="edit-state" class="col-sm-2 control-label">线索状态</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-state">
                                <option></option>
                                <c:forEach items="${clueStateList}" var="cs">
                                    <option value="${cs.id}">${cs.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-source" class="col-sm-2 control-label">线索来源</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-source">
                                <option></option>
                                <c:forEach items="${sourceList}" var="s">
                                    <option value="${s.id}">${s.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-description" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-description">这是一条线索的描述信息</textarea>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                    <div style="position: relative;top: 15px;">
                        <div class="form-group">
                            <label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="edit-contactSummary">这个线索即将被转换</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-nextContactTime" value="2017-05-01">
                            </div>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                    <div style="position: relative;top: 20px;">
                        <div class="form-group">
                            <label for="edit-address" class="col-sm-2 control-label">详细地址</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="1" id="edit-address">北京大兴区大族企业湾</textarea>
                            </div>
                        </div>
                    </div>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveClueBtn">更新</button>
            </div>
        </div>
    </div>
</div>


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>线索列表</h3>
        </div>
    </div>
</div>

<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input class="form-control" type="text" id="name">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">公司</div>
                        <input class="form-control" type="text" id="company">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">公司座机</div>
                        <input class="form-control" type="text" id="phone">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">线索来源</div>
                        <select id="source" class="form-control">
                            <option></option>
                            <c:forEach items="${sourceList}" var="s">
                                <option value="${s.id}">${s.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">线索状态</div>
                        <select id="status" class="form-control">
                            <option></option>
                            <c:forEach items="${clueStateList}" var="cs">
                                <option value="${cs.id}">${cs.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <br>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="owner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">手机</div>
                        <input class="form-control" type="text" id="mphone">
                    </div>
                </div>


                <button type="submit" id="queryClueBtn" class="btn btn-default">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="createCuleBtn"><span
                        class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button type="button" class="btn btn-default" id="editClueBtn"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button"  id="deleteClueBtn" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
            </div>


        </div>


        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="checkboxAll"/></td>
                    <td>名称</td>
                    <td>公司</td>
                    <td>公司座机</td>
                    <td>手机</td>
                    <td>线索来源</td>
                    <td>所有者</td>
                    <td>线索状态</td>
                </tr>
                </thead>
                <tbody id="tBody">
                </tbody>
            </table>
        </div>


        <div id="demo_page1"></div>


        <%--    <div style="height: 50px; position: relative;top: 60px;">
                <div>
                    <button type="button" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>
                </div>
                <div class="btn-group" style="position: relative;top: -34px; left: 110px;">
                    <button type="button" class="btn btn-default" style="cursor: default;">显示</button>
                    <div class="btn-group">
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                            10
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="#">20</a></li>
                            <li><a href="#">30</a></li>
                        </ul>
                    </div>
                    <button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
                </div>
                <div style="position: relative;top: -88px; left: 285px;">
                    <nav>
                        <ul class="pagination">
                            <li class="disabled"><a href="#">首页</a></li>
                            <li class="disabled"><a href="#">上一页</a></li>
                            <li class="active"><a href="#">1</a></li>
                            <li><a href="#">2</a></li>
                            <li><a href="#">3</a></li>
                            <li><a href="#">4</a></li>
                            <li><a href="#">5</a></li>
                            <li><a href="#">下一页</a></li>
                            <li class="disabled"><a href="#">末页</a></li>
                        </ul>
                    </nav>
                </div>
            </div>--%>
    </div>

</div>
</body>
</html>