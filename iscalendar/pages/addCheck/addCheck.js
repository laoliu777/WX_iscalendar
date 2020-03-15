// pages/addCheck/addCheck.js
const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    check_title: "",
    check_content: "",
    uid: "3",
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    this.setData({
      uid: app.globalData.openid
    })
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

  /**
   * 点击保存按钮返回上一个界面
   */
  onClickSave: function(e) {
    console.log("新建一个打卡项");
    //console.log(this.selectComponent("#iconSelector").data.backgroundColor);
    var that = this;
    var image_url = "../../images/clock/" + this.selectComponent("#iconSelector").data.imageUrl;
    var bg_color = this.selectComponent("#iconSelector").data.backgroundColor;
    console.log("pages" + pages);
    var pages = getCurrentPages(); //得到界面栈
    var currPage = pages[pages.length - 1]; //当前页面
    var prevPage = pages[pages.length - 2]; //上一个页面

    var list = prevPage.data.checkinLists;
    // var newID = parseInt(list[list.length - 1].id);
    // newID = newID + 1;
    // var newID_s = "" + newID;
    var obj = {
      // id: newID_s,
      name: this.data.check_title, //打卡名称
      iconURL: image_url, //指定图标
      background: "#" + bg_color, // 背景颜色
      stickDays: 0, //坚持日期
      details: this.data.check_content, //打卡详细内容
      status: true,
    };
    console.log("新的打卡项：", obj);

    wx.request({
      url: "https://172.19.241.77:443/project/checkin/createCheckin",
      method: "POST",
      dataType: 'JSON',
      header: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      data: {
        user_id: app.globalData.openid,
        checkin_name: this.data.check_title,
        checkin_description: this.data.check_content,
        icon_url: image_url,
        background: bg_color,
      },
      success: function(res) {
        console.log(res.data);
      }
    })

    // list.push(obj);
    // prevPage.setData({
    //   checkinLists: list
    // });

    // 父层界面进行刷新
    prevPage.getDatabaseData();

    wx.navigateBack({
      delta: 1
    });
  },

  //输入框[标题]输入绑定事件
  onInput_title: function(e) {
    var value = e.detail.value;
    this.setData({
      check_title: value
    })
  },

  //输入框[详情]输入绑定事件
  onInput_content: function(e) {
    var value = e.detail.value;
    this.setData({
      check_content: value
    })
  },
})