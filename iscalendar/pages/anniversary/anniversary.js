// pages/anniversary/anniversary.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    isNetworkOn: true,
    // 所有的打卡列表
    // status字段代表此项状态，为true时代表创建并显示，为false时代表对其进行删除或屏蔽
    
    anniversaryLists: [],

    /* 左滑删除按钮 */
    slideButtons: [{
      type: 'warn',
      text: '删除',
      extClass: 'test',
      src: '/images/icon/slide_icon/icon_del.svg', // icon的路径
    }],

    // curID: 7,
    // Image_addItem_URL: "../../images/icon/icon_add.png",
    // Image_checkinItem_URL: "../../images/icon/icon_checkin_item.png",
    // Details_Page_URL: "./checkin_content/checkin_content",

    //是否需要隐藏弹窗
    hiddenModalPut: true,
    showInput: false, //控制输入栏

    isMaskWindowShow: false,
    maskWindowList: ['时间太早', '时间太晚', '距离太远', '交通不方便', '其他（输入）', '没有原因'],
    selectIndex: -1,
    isMaskWindowInputShow: false,
    maskWindowInputValue_title: "",
    maskWindowInputValue_content: "",
  },

  /**
  * 生命周期函数--监听页面加载
  */
  onLoad: function (options) {
    this.getDatabaseData();
  },
  getDatabaseData: function () {
    // 读取数据库内容
    // 传入：用户id
    // 传出：该用户的所有纪念日项列表
    var that = this;
    if (this.data.isNetworkOn) {
      var arr = new Array();
      wx.request({
        //url: "https://172.19.241.77:8080/project/user/getUserInfo",//url中接口前面加一下project项目名
        url: "https://172.19.241.77:443/project/anniversary/getAnniversariesAllByUser",
        method: "POST",
        dataType: 'JSON',
        header: { 'Content-Type': 'application/x-www-form-urlencoded' },
        data: {
          user_id: app.globalData.openid,
        },
        success: (res) => {
          console.log("用户的所有纪念日如下：", res.data)
          var item = JSON.parse(res.data);
          var i = 0;
          for (i = 0; i < item.length; i++) {
            var tmp = item[i];
            var obj = {
              id: tmp.id,
              name: tmp.anniversary_name,       //纪念日名称
              iconURL: tmp.icon_url,          //指定图标
              passDays: tmp.distday,    //已过时间
              createDate: tmp.create_at,       //纪念日创建日期
              background: tmp.background,
              status: true,
            };
            arr.push(obj);
          }

          // 重新刷新数组
          this.setData({
            anniversaryLists: arr
          });
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

  // 获取当前所有的打卡列表
  // 从数据库中进行获取
  getCheckinLists() {
    //TODO
  },


  //跳转到纪念日详情界面
  goDetails: function (e) {
    //实现界面的跳转
    console.log('点击纪念日详情')
    console.log(e)
    //TODO: 带参数跳转
    wx.navigateTo({
      // url:'/pages/anniversary/anniversary_content/anniversary_content'
      url: "./anniversary_content/anniversary_content?id=" + e.currentTarget.dataset.id + "&content=" + e.currentTarget.dataset.content
    })
  },

  //添加一个新的纪念日
  addAnniversary: function (e) {
    wx.navigateTo({
      url: "../addAnniversary/addAnniversary"
    })
  },

  //长按卡片事件
  onLongPressCard: function (e) {
    console.log("长按卡片");
  },


  //弹框以外区域点击
  maskWindowBgClick: function (e) {
    this.dismissMaskWindow();
  },

  //弹窗区域点击事件
  clickTap: function (e) {

  },

  //切换选择项事件
  maskWindowTableSelect: function (e) {
    var index = e.currentTarget.dataset.windowIndex;
    this.setData({
      selectIndex: e.currentTarget.dataset.windowIndex,
      isMaskWindowInputShow: index == 4
    })
  },

  //输入框[标题]输入绑定事件
  maskWindowInput_title: function (e) {
    var value = e.detail.value;
    this.setData({
      maskWindowInputValue_title: value
    })
  },

  //输入框[详情]输入绑定事件
  maskWindowInput_content: function (e) {
    var value = e.detail.value;
    this.setData({
      maskWindowInputValue_content: value
    })
  },

  maskWindowOk: function (e) {
    console.log("确定按钮");
    var index = this.data.selectIndex;
    var text_title = this.data.maskWindowInputValue_title;
    var text_content = this.data.maskWindowInputValue_content;

    //判断字符串是否为空
    if (typeof text_title == "undefined" || text_title == null || text_title == "") {
      this.dismissMaskWindow();
      return;
    }

    //添加一个新事项
    var list = this.data.checkinLists;
    var c_ID = ++this.data.curID;
    var obj = {
      id: c_ID,
      name: text_title,       //打卡名称
      iconURL: "../../images/icon/item/item_default.png",          //指定图标
      stickDays: "0",    //坚持日期
      details: text_content,       //打卡详细内容
    };
    list.push(obj);
    this.setData({
      checkinLists: list
    });
    console.log("添加ID", c_ID);
    this.dismissMaskWindow();
  },

  maskWindowCancel: function (e) {
    console.log("取消按钮");
    this.dismissMaskWindow();
  },

  // 显示蒙版弹窗
  showMaskWindow: function () {
    this.setData({
      isMaskWindowShow: true,
      selectIndex: -1,
      isMaskWindowInputShow: false,
      maskWindowInputValue: ""
    })
  },

  // 隐藏蒙版窗体
  dismissMaskWindow: function () {
    this.setData({
      isMaskWindowShow: false,
      selectIndex: -1,
      isMaskWindowInputShow: false,
      maskWindowInputValue: ""
    })
  },

  // 响应滑动按钮事件
  slideButtonTap(e) {
    console.log('slide button tap', e.detail);
    var that = this;

    // 模态弹窗确定是否要删除
    wx.showModal({
      title: '确定删除',
      content: '是否确定删除该纪念日？',
      success: function (res) {
        if (res.confirm) {
          console.log("删除id:", e.currentTarget.dataset.id);
          // // 删除这个id项
          // var del_ID = e.currentTarget.dataset.id;
          // var list = that.data.anniversaryLists;
          // for (var i = 0; i < list.length; i++) {
          //   console.log("cur id:", list[i].id)
          //   console.log("del id:", del_ID)
          //   if (list[i].id === del_ID) {
          //     list.splice(i, 1);
          //   }
          // }
          // // 重新刷新数组
          // that.setData({
          //   anniversaryLists: list
          // });

          wx.request({
            url: "https://172.19.241.77:443/project/anniversary/deleteAnniversary",
            method: "POST",
            dataType: 'JSON',
            header: { 'Content-Type': 'application/x-www-form-urlencoded' },
            data: {
              id: e.currentTarget.dataset.id
            },
            success: function (res) {
              console.log(res.data);
            }
          })
          that.getDatabaseData();
        }
      }
    })
  },


})