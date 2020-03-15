// pages/addAnniversary/addAnniversary.js
const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    today: '',
    date: '',
    types: ['非纪念日', '每月纪念', '每年纪念'],
    selectedType: '非纪念日',
    anniversary_title: "",
    anniversary_description: "",
    anniversary_type: "",
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let date = new Date();
    this.setData({
      today: date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
    })
    this.setData({
      date: this.data.today
    })
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

  selectType: function (event) {
    this.setData({
      selectedType: event.currentTarget.dataset.item
    })
  },


  /**
   * 点击保存按钮返回上一个界面
   */
  onClickSave: function (e) {
    console.log("新建一个纪念日");
    //console.log(this.selectComponent("#iconSelector").data.backgroundColor);

    var image_url = "../../images/clock/" + this.selectComponent("#iconSelector").data.imageUrl;
    var bg_color = this.selectComponent("#iconSelector").data.backgroundColor;
    console.log("pages" + pages);
    var pages = getCurrentPages();            //得到界面栈
    var currPage = pages[pages.length - 1];   //当前页面
    var prevPage = pages[pages.length - 2];  //上一个页面

    var list = prevPage.data.anniversaryLists;
    // var newID = parseInt(list[list.length - 1].id);
    // newID = newID + 1;
    // var newID_s = "" + newID;
    var obj = {
      // id: newID_s,
      name: this.data.anniversary_title,       //纪念日名称
      iconURL: image_url,          //指定图标
      background: "#" + bg_color,      // 背景颜色
      description: this.data.anniversary_description,    //描述
      type: this.data.selectedType,    //类型
      date: this.data.date,    //日期
      status: true,
    };
    console.log("新的纪念日：", obj);

    wx.request({
      url: "https://172.19.241.77:443/project/anniversary/createAnniversary",
      method: "POST",
      dataType: 'JSON',
      header: { 'Content-Type': 'application/x-www-form-urlencoded' },
      data: {
        user_id: app.globalData.openid,
        anniversary_name: this.data.anniversary_title,
        anniversary_description: this.data.anniversary_description,
        anniversary_type: this.data.selectedType,
        anniversary: this.data.date,
        icon_url: image_url,
        background: bg_color,
      },
      success: function (res) {
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
  onInput_title: function (e) {
    var value = e.detail.value;
    this.setData({
      anniversary_title: value
    })
  },

  //输入框[详情]输入绑定事件
  onInput_description: function (e) {
    var value = e.detail.value;
    this.setData({
      anniversary_description: value
    })
  },

  bindDateChange: function (e) {
    var date = e.detail.value;
    console.log(date)
    this.setData({
      date: date
    })
  }
})