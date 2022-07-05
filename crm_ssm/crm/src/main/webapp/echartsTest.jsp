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
          /*
            //  柱状图
            //  当容器加载完成之后，对容器调用工具函数：
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('main'));

            // 指定图表的配置项和数据
            var option = {
                //  标题
                title: {
                    text: 'ECharts 入门示例'
                },

                //  提示框
                tooltip: {},

                //  图例
                legend: {
                    data: ['销量']
                },

                //  x轴显示的数据：数据项轴
                xAxis: {
                    data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子']
                },

                //  y轴显示的数据：数据量轴
                yAxis: {},

                //  系列
                series: [
                    {
                        name: '销量',     //  系列的名称
                        type: 'bar',    //  谢列的类型：bar---柱状图，line---折线图
                        data: [5, 20, 36, 10, 10, 20]   //  系列的数据
                    }
                ]
            };
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);

*/

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
                        data: [
                            { value: 60, name: '访问' },
                            { value: 40, name: '咨询' },
                            { value: 20, name: '订单' },
                            { value: 80, name: '点击' },
                            { value: 100, name: '展示' }
                        ]
                    }
                ]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        });

    </script>

</head>
<body>
<!-- 为 ECharts 准备一个定义了宽高的 DOM -->
<div id="main" style="width: 600px;height:400px;"></div>
</body>
</html>

<%--
    echarts的使用：
        1.引入开发包
        2.创建容器
        3.当容器加载完成之后，对容器调用工具函数
--%>