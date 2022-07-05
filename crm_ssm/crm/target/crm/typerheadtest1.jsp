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
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
    <title>演示自动补全</title>

    <script type="text/javascript">
        $(function () {
            //  当容器加载完成之后对容器调用工具函数
            $("#customerName").typeahead({
                source:function (jquery,process) {  //  每次键盘弹起都会自动触发本函数，可以向后台发送请求，查询数据库中所有的名称，以 json 的形式返回，赋值给 source
                    $.ajax({

                    });
                }
            });
        });
    </script>
</head>
<body>
<input type="text" id="customerName">

</body>
</html>

















