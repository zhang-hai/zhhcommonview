# zhhcommonview
基础通用自定义view

库引入方式,在对应需要使用该库的工程目录的build.gradle配置中设置：
```
implementation 'com.github.zhang-hai:zhhcommonview:0.6.0'
```


1.ImageTextView
-------------

该View扩展了TextView，支持设置的icon的尺寸，可以做到icon大小自适应，支持上下左右设置icon。

可以完美替换ImageView + TextView组合。

使用方式：

	<com.zhh.commonview.view.ImageTextView
        android:id="@+id/itv_test"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="ImageTextView"
        android:textColor="#ff0000"
        android:textSize="20sp"
        app:drawableBottom="@mipmap/ic_launcher"
        app:drawableHeight="30dp"
        app:drawableLeft="@mipmap/ic_launcher"
        app:drawableRight="@mipmap/ic_launcher"
        app:drawableTop="@mipmap/ic_launcher"
        app:drawableWidth="30dp"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

2.RoundTextView
-------------

是一款多功能的自定义View，通过代码的方式设置各种drawable样式，免去创建多个不用样式的xml的苦恼。
该View同样扩展了TextView,除了带ImageTextView的功能外，还支持下设置

- 设置边框粗细、颜色、press和select样式
- 设置背景填充色、press和select样式
- 设置圆角
- 设置文本字体在press和select状态的颜色
	
使用方式如下：

    <com.zhh.commonview.view.RoundTextView
        android:id="@+id/rtv_stroke"
        android:layout_width="wrap_content"
        android:layout_height="40px"
        android:gravity="center"
        android:onClick="click"
        android:paddingLeft="30px"
        android:paddingRight="30px"
        android:text="圆角边框"
        app:corner="20px"
        app:press_stroke_color="@color/colorAccent"
        app:press_text_color="@color/colorAccent"
        app:solid_color="@android:color/transparent"
        app:stroke_color="@color/color_fill_01AC8E"
        app:stroke_width="2px"
        app:layout_constraintBottom_toBottomOf="@+id/itv_test"
        app:layout_constraintEnd_toStartOf="@+id/rtv_solid"
        app:layout_constraintStart_toEndOf="@+id/itv_test"
        app:layout_constraintTop_toTopOf="parent" />

支持以下自定义属性

- corner 圆角尺寸
- solid_color 正常状态背景填充色
- press_solid_color 按下时的背景填充颜色
- select_solid_color 选中时的背景填充颜色
- stroke_width 正常状态边框粗细
- stroke_color 正常状态的边框颜色
- press_stroke_color 按下时的背景边框颜色
- select_stroke_color 选中时的边框颜色
- press_text_color 按下时的字体颜色
- select_text_color 选中时的字体颜色


3.自定义图表组件
-------------



雷达图
-------------
    
使用方式：

XML中添加RadarChartView

    <com.zhh.commonview.view.chart.RadarChartView
        android:id="@+id/rcv_radar"
        android:layout_width="match_parent"
        android:layout_height="300px"
        app:areaColor="#3001AC8E"
        app:backgroundLineColor="#ccc"
        app:edgeTextColor="#666"
        app:edgeTextSize="26px"
        app:lineColor="#01AC8E"
        app:radius="110px"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

支持以下自定义属性

- radius 雷达最大半径，若设置了宽度和高度，请确保至少宽高 要大于radius * 2;
- backgroundLineColor 雷达蜘蛛网线条颜色
- edgeTextSize 指标文字大小
- edgeTextColor 指标文字颜色
- areaColor 数据区域颜色
- lineColor 数据折线颜色

代码中设置数据源及点击数据区域回调

    RadarChartView chartView = findViewById(R.id.rcv_radar);
	//addTestData()是获取数据源，返回List<RadarChartView.RadarItemData>列表
    chartView.setRadarChartData(addTestData());
	//回调接口，若想展示选中区域数据详情可以在onClick方法中做自己的UI展示，如弹窗
    chartView.setOnRadarChartViewListener(new OnCommonChartViewListener<RadarChartView.RadarItemData>() {
        @Override
        public void onCancel() {
            //取消事件，如果展示数据的UI，这里可以关闭

        }

        @Override
        public void onClick(RadarChartView.RadarItemData data, float ix, float iy) {
             //再这里可以展示数据
             Log.i("MainActivity",String.format(Locale.CHINESE,"data--->%s,ix-->%f,iy---->%f",data.toString(), ix,iy));
         }
     });


柱状图
-------------

支持组模式和单一柱图模式。

使用方式：

先在XML中添加BarChartView

    <com.zhh.commonview.view.chart.BarChartView
        android:id="@+id/barChartView"
        android:layout_width="0dp"
        android:layout_height="300px"
        android:layout_marginTop="10dp"
        app:groupPerBarSpace="5px"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView"
        app:legendIconShape="circle"
        app:legendIconWidth="14px"
        app:legendLabelColor="#555555"
        app:legendLabelTextSize="26px"
        app:perBarWidth="16px"
        app:xLabelColor="#222222"
        app:xLabelTextSize="26px"
        app:yAxisLineColor="#E5E5E5"
        app:yLabelColor="#555555"
        app:yLabelTextSize="24px" />


支持以下自定义属性

- perBarWidth 每个柱子的宽度;
- groupPerBarSpace 每组内部柱子之间的间距
- yAxisLineColor 横向线条颜色
- xLabelColor X轴标签文字颜色
- xLabelTextSize X轴标签文字大小
- yLabelColor Y轴标签文字颜色
- yLabelTextSize Y轴标签文字大小
- legendLabelColor 柱状图的图例文字颜色
- legendLabelTextSize 柱状图的图例文字大小
- legendIconWidth 柱状图的图例icon宽度
- legendIconShape 柱状图的用例icon样式，支持：square正方形、circle圆形，默认为square


在代码中添加数据源及回调接口

    BarChartView chart = findViewById(R.id.barChartView);
	//buildBarChartData()方法构建数据源并返回List<BarChartData>
    chart.setBarChartData(buildBarChartData());
	//回调接口，若想展示选中区域数据详情可以在onClick方法中做自己的UI展示，如弹窗
    chart.setOnChartViewListener(new OnCommonChartViewListener<BarChartData>() {
        @Override
        public void onCancel() {
        }

        @Override
        public void onClick(BarChartData data, float ix, float iy) {
            Log.i("MainActivity",String.format(Locale.CHINESE,"data--->%s,ix-->%f,iy---->%f",data.toString(), ix,iy));
        }
    });

