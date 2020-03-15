// pages/anniversary/anniversary_content/anniversary_content.js
const app = getApp()
Page({
  data: {
    cur: '',
    cur_id: 0,
    motto: 'Hello World',
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    date: {
      'day': '',
      'month': '',
      'year': '',
      'week': ''
    },
    AnniversaryDetails: {
      'date': '2019-01-01',       //天
      'timeName': '生日', //时间名
      'passDay': '1120', //已过天数
      'nextAnniversary': '240', //下一个纪念日
      'type': '每年纪念',         //建立时间
      'description': "啊啊啊啊啊啊啊啊啊啊啊"           //当前进度
    },

    icon_url: {
      'icon_delete': '../../../images/icon/icon_delete.png',
      'icon_edit': '../../../images/icon/icon_edit.png',
    },
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log("message: ", options)
    this.setData({
      cur: options.content,
      cur_id: options.id,
      today: app.globalData.today
    })
    // 获取此纪念日详情数据
    var that = this;
    wx.request(
      {
      url: "https://172.19.241.77:443/project/anniversary/getAnniversaryByID",
      method: "POST",
      dataType: 'JSON',
      header: { 'Content-Type': 'application/x-www-form-urlencoded' },
      data: {
        id: this.data.cur_id
      },
      success: function (res) {
        console.log(res.data);
        var item = JSON.parse(res.data);
        var obj = {
          date: item.anniversary,      //计划天数
          timeName: item.anniversary_name,      //打卡天数
          passDay: item.restday,       //错过天数
          nextAnniversary: item.restday, //总计打卡天数
          type: item.anniversary_type, //当前连续时长
          description: item.anniversary_description, //最大连续时长
          nextAnniversary:item.to_next_anniversary,
        }
        that.setData({
          AnniversaryDetails: obj,
          date: app.getFormatDate(item.anniversary)
        })
      },
    })
    console.log(this.data.AnniversaryDetails.description);
    // this.setData({
    //   date: app.getFormatDate(this.data.date)
    // })
    console.log("date++" + this.data.date);
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
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

  //按下删除图标
  onClickDelete: function (e) {
    console.log("按下了删除图标");
    var pages = getCurrentPages();            //得到界面栈
    var currPage = pages[pages.length - 1];   //当前页面
    var prevPage = pages[pages.length - 2];  //上一个页面
    var that = this;
    wx.showModal({
      title: '确定删除',
      content: '是否确定删除该纪念日？',
      success: function (res) {
        if (res.confirm) {
          // 删除此打卡项
          wx.request({
            url: "https://172.19.241.77:443/project/anniversary/deleteAnniversary",
            method: "POST",
            dataType: 'JSON',
            header: { 'Content-Type': 'application/x-www-form-urlencoded' },
            data: {
              id: that.data.cur_id
            },
            success: function (res) {
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

})