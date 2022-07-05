<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">

    <%--引入依赖的jQuery--%>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <%--引入开发包插件--%>
    <script type="text/javascript" src="jquery/echarts/echarts.min.js"></script>
    <title>演示Echarts插件</title>

    <script type="text/javascript">

        $(function () {
            //  发起请求
            $.ajax({
                url:'workbench/chart/transaction/queryCountOfTranGroupByStages.do',
                type:'post',
                dataType:'json',
                success:function (data) {
                    //  调用echarts工具函数，生成图表
                    var myChart = echarts.init(document.getElementById('main'));
                    //  漏斗图
                    option = {
                        title: {
                            text: '交易统计图表',
                            subtext:'交易表中各个阶段的数量'
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: '{a} <br/>{b} : {c}'
                        },
                        toolbox: {
                            feature: {
                                dataView: { readOnly: false },
                                restore: {},
                                saveAsImage: {}
                            }
                        },
                        series: [
                            {
                                name: '数据量',
                                type: 'funnel',
                                left: '10%',
                                width: '80%',
                                label: {
                                    formatter: '{b}'
                                },
                                labelLine: {
                                    show: true
                                },
                                itemStyle: {
                                    opacity: 0.8
                                },
                                emphasis: {
                                    label: {
                                        position: 'inside',
                                        formatter: '{b}: {c}'
                                    }
                                },
                                data: data
                            }
                        ]
                    };

                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);
                }
            });

        });
    </script>
</head>
<body>
<!-- 为 ECharts 准备一个定义了宽高的 DOM -->
<div id="main" style="width: 600px;height:400px;"></div>
</body>
</html>
