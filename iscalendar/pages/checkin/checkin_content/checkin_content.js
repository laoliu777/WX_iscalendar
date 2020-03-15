// pages/checkin/checkin_content/checkin_content.js
// TODO: 日历上显示所有已打卡的日期

const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    cur: '', // 当前名称
    cur_id: 0, // 当前id
    uid: "3", // 用户ID
    motto: 'Hello World',
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    calendarConfig: {
      /**
       * 初始化日历时指定默认选中日期，如：'2018-3-6' 或 '2018-03-06'
       * 初始化时不默认选中当天，则将该值配置为false。
       */
      multi: true, // 是否开启多选,
      highlightToday: true, // 是否高亮显示当天，区别于选中样式（初始化时当天高亮并不代表已选中当天）
      takeoverTap: true, // 是否完全接管日期点击事件（日期不会选中），配合 onTapDay() 使用
      disablePastDay: false, // 是否禁选当天之前的日期
      disableLaterDay: true, // 是否禁选当天之后的日期
      firstDayOfWeek: 'Mon', // 每周第一天为周一还是周日，默认按周日开始
      onlyShowCurrentMonth: false, // 日历面板是否只显示本月日期
      hideHeadOnWeekMode: false, // 周视图模式是否隐藏日历头部
      showHandlerOnWeekMode: true // 周视图模式是否显示日历头部操作栏，hideHeadOnWeekMode 优先级高于此配置
    },
    today: {
      'day': '',
      'month': '',
      'year': '',
      'week': ''
    },

    checkinItem: {
      plannedDays: 'NaN1', //计划天数
      checkinDays: 'NaN2', //打卡天数
      missedDays: 'NaN3', //错过天数
      totalCheckedDays: 'NaN4', //总计打卡天数
      curConsecutiveDays: 'NaN5', //当前连续时长
      maxConsecutiveDays: 'NaN6', //最大连续时长
      createDay: 'NaN7', //建立时间
      checkinProgess: "0" //当前进度
    },

    icon_url: {
      'icon_delete': '../../../images/icon/icon_delete.png',
      'icon_edit': '../../../images/icon/icon_edit.png',
    },

    //status字段代表此项状态，为true时代表创建并显示，为false时代表对其进行删除或屏蔽
    clocks: [{
        id: '1232131',
        name: '跑步',
        image: '../../images/clock/1.png',
        background: '#d6c6de',
        days: 1,
        checked: false,
        status: true,
      },
      {
        id: '1232132',
        name: '早起',
        image: '../../images/clock/2.png',
        background: '#5626e530',
        days: 2,
        checked: true,
        status: true,
      },
      {
        id: '1232133',
        name: '跑步',
        image: '../../images/clock/3.png',
        background: '#d6c6de',
        days: 1,
        checked: true,
        status: true,
      },
      {
        id: '1232134',
        name: '跑步',
        image: '../../images/clock/4.png',
        background: '#d6c6de',
        days: 1,
        checked: false,
        status: true,
      },
      {
        id: '1232135',
        name: '跑步',
        image: '../../images/clock/5.png',
        background: '#d6c6de',
        days: 1,
        checked: false,
        status: true,
      }
    ]
  },

  //事件处理函数
  bindViewTap: function() {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    console.log("message: ", options)
    this.setData({
      cur: options.content,
      cur_id: options.id,
      today: app.globalData.today
    })
    // 日历：禁止选择日期
    //console.log(this.calendar)

    // 获取此打卡项数据
    var that = this;
    wx.request({
      url: "https://172.19.241.77:443/project/checkin/getCheckinByID",
      method: "POST",
      dataType: 'JSON',
      header: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      data: {
        checkin_id: this.data.cur_id
      },
      success: function(res) {
        console.log("getcheckinbyID: ",res.data);
        var item = JSON.parse(res.data);

        // 计算打卡进度百分比
        var pc = 100;
        if (parseInt(item.historyday)+1 == 0){
          pc = 0;
        }else{
          if (item.totalcheckinday == null || item.historyday == null){
            pc = 100;
          }else{
            var a = item.totalcheckinday;
            var b = parseInt(item.historyday) + 1;
            pc = a/b * 100;
            console.log("百分比：",pc)
          }
        }

        // historyday 返回的值少1
        var pd = (item.historyday == null) ? item.totalcheckinday : parseInt(item.historyday)+1;

        var md = (item.missday == null) ? 0 : (item.missday < 0 ? 0 : item.missday);

        var obj = {
          plannedDays: pd, //计划天数
          checkinDays: item.totalcheckinday, //打卡天数
          missedDays: md, //错过天数
          totalCheckedDays: item.totalcheckinday, //总计打卡天数
          curConsecutiveDays: item.stick_days, //当前连续时长
          maxConsecutiveDays: item.stick_days, //最大连续时长
          createDay: item.created_at, //建立时间
          checkinProgess: pc, //当前进度
        }
        that.setData({
          checkinItem: obj,
        })
      }
    })
    // 获取该打卡项的创建日期，/checkin/getCheckinByID中并没有返回此字段，需要另外进行查找
    wx.request({
      url: "https://172.19.241.77:443/project/checkin/getCheckinsAllByUser",
      method: "POST",
      dataType: 'JSON',
      header: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      data: {
        user_id: that.data.uid
      },
      success: function (res) {
        //console.log("JSON: ",JSON.parse(res.data));
        var lists = JSON.parse(res.data);
        for(var i = 0; i < lists.length; i++){
          var id_i = lists[i].id;
          if (id_i == that.data.cur_id){
            that.setData({
              "checkinItem.createDay": lists[i].created_at
            })
            break;
          }
        }
      }
    })

    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
      })
    } else if (this.data.canIUse) {
      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
      // 所以此处加入 callback 以防止这种情况
      app.userInfoReadyCallback = res => {
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
    } else {
      // 在没有 open-type=getUserInfo 版本的兼容处理
      wx.getUserInfo({
        success: res => {
          app.globalData.userInfo = res.userInfo
          this.setData({
            userInfo: res.userInfo,
            hasUserInfo: true
          })
        }
      })
    }
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  },

  getUserInfo: function(e) {
    console.log(e)
    app.globalData.userInfo = e.detail.userInfo
    this.setData({
      userInfo: e.detail.userInfo,
      hasUserInfo: true
    })
    //TODO:在这里加载个人数据？
  },
  /**
   * 选择日期后执行的事件
   * currentSelect 当前点击的日期
   * allSelectedDays 选择的所有日期（当mulit为true时，allSelectedDays有值）
   */
  afterTapDay(e) {
    console.log('afterTapDay', e.detail); // => { currentSelect: {}, allSelectedDays: [] }
  },
  /**
   * 当日历滑动时触发(适用于周/月视图)
   * 可在滑动时按需在该方法内获取当前日历的一些数据
   */
  onSwipe(e) {
    console.log('onSwipe', e.detail);
    const dates = this.calendar.getCalendarDates();
  },
  /**
   * 当改变月份时触发
   * => current 当前年月 / next 切换后的年月
   */
  whenChangeMonth(e) {
    console.log('whenChangeMonth', e.detail);
    // => { current: { month: 3, ... }, next: { month: 4, ... }}
  },
  /**
   * 周视图下当改变周时触发
   * => current 当前周信息 / next 切换后周信息
   */
  whenChangeWeek(e) {
    console.log('whenChangeWeek', e.detail);
    // {
    //    current: { currentYM: {year: 2019, month: 1 }, dates: [{}] },
    //    next: { currentYM: {year: 2019, month: 1}, dates: [{}] },
    //    directionType: 'next_week'
    // }
  },
  /**
   * 日期点击事件（此事件会完全接管点击事件），需自定义配置 takeoverTap 值为真才能生效
   * currentSelect 当前点击的日期
   */
  onTapDay(e) {
    console.log('onTapDay', e.detail); // => { year: 2019, month: 12, day: 3, ...}
  },
  /**
   * 日历初次渲染完成后触发事件，如设置事件标记
   */
  afterCalendarRender(e) {
    /* 多选所有的已打卡日期 */
    // 请求获取数据库此打卡项的所有打卡日期
    var checkinList = new Array();
    var that = this;
    var arr_month = new Array('Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec');
    var int_month = arr_month.indexOf(app.globalData.today.month) + 1;
    var time_str = app.globalData.today.year + "-" + int_month;
    console.log(time_str);
    wx.request({
      url: "https://172.19.241.77:443/project/checkin/getMonthCheckin",
      method: "POST",
      dataType: 'JSON',
      header: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      data: {
        //id: that.data.cur_id
        user_id: that.data.uid,
        this_month: time_str,
      },
      success: function(res) {
        var tmpList = JSON.parse(res.data);
        // 进行深度拷贝
        var ll = new Array();
        var i = 0;
        for (i = 0; i < tmpList.length; i++) {
          if (tmpList[i].checkin_id == that.data.cur_id) {
            let split1 = tmpList[i].checkin_date.trim().split(" ")[0];
            let split2 = split1.trim().split("-");
            var obj = {
              year: split2[0],
              month: split2[1],
              day: split2[2],
            }
            checkinList.push(obj);
          }
        }
        console.log("checkinlist1", checkinList)
        that.calendar.setSelectedDays(checkinList)
      }
    })
  },

  //按下删除图标
  onClickDelete: function(e) {
    console.log("按下了删除图标");
    var that = this;

    var pages = getCurrentPages(); //得到界面栈
    var currPage = pages[pages.length - 1]; //当前页面
    var prevPage = pages[pages.length - 2]; //上一个页面

    wx.showModal({
      title: '确定删除',
      content: '是否确定删除该打卡项？',
      success: function(res) {
        if (res.confirm) {
          // 删除此打卡项
          wx.request({
            url: "https://172.19.241.77:443/project/checkin/deleteCheckin",
            method: "POST",
            dataType: 'JSON',
            header: {
              'Content-Type': 'application/x-www-form-urlencoded'
            },
            data: {
              id: that.data.cur_id
            },
            success: function(res) {
              console.log(res.data);
              // 父层界面进行刷新
              prevPage.getDatabaseData();

              wx.navigateBack({
                delta: 1
              });
            }
          })
        }
      }
    })


  },

  //按下编辑图标
  onClickEdit: function(e) {
    console.log("按下了编辑图标");
  },


})