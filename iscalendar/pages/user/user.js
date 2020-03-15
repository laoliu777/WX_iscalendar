const app = getApp()

Page({
  data: {
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    list: [{
        id: 'check',
        name: '打卡统计',
        open: false
      },
      {
        id: 'anniversary',
        name: '纪念日统计',
        open: false
      },
      {
        id: 'setting',
        name: '个人信息',
        open: false
      }
    ],
    date: '请选择',
    phone: '',
    today: ''
  },
  //事件处理函数
  bindViewTap: function() {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },
  bindDateChange: function (e) {
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      date: e.detail.value
    })
    wx.request({
      url: "https://172.19.241.77:443/project/user/modifyBirthday",
      method: "POST",
      dataType: 'JSON',
      header: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      data: {
        id: '3',
        birthday: e.detail.value,
      },
      success: (res) => {
        wx.showToast({
          icon: 'success',
          title: '修改成功！',
          duration: 2000
        })
      },
      fail: (res) => {
        console.log(res);
      }
    })
  },
  bindblur: function (e) {
    let tmp = e.detail.value
    let reg = /^1(3[0-9]|4[5,7]|5[0,1,2,3,5,6,7,8,9]|6[2,5,6,7]|7[0,1,7,8]|8[0-9]|9[1,8,9])\d{8}$/;
    if (reg.test(tmp)) {
      this.setData({
        phone: e.detail.value
      })
      wx.request({
        url: "https://172.19.241.77:443/project/user/modifyPhone",
        method: "POST",
        dataType: 'JSON',
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: {
          id: '3',
          phone: e.detail.value,
        },
        success: (res) => {
          wx.showToast({
            icon: 'success',
            title: '修改成功！',
            duration: 2000
          })
        },
        fail: (res) => {
          console.log(res);
        }
      })
    } else {
      wx.showToast({
        icon: 'none',
        title: '请输入正确的手机号',
        duration: 2000
      })
      this.setData({
        phone: ''
      })
    }
  },
  onLoad: function() {
    let date = new Date();
    this.setData({
      today: date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
    })
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
      })
      this.getUserInfo()
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
  getUserInfo: function(e) {
    wx.request({
      url: "https://172.19.241.77:443/project/user/getUserInfo",
      method: "POST",
      dataType: 'JSON',
      header: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      data: {
        id: '3',
      },
      success: (res) => {
        var item = JSON.parse(res.data)
        if (item.phone) {
          this.setData({
            phone: item.phone
          })
        } 
        if (item.birthday) {
          this.setData({
            date: item.birthday
          })
        } 
        console.log(res)
      },
      fail: (res) => {
        console.log(res);
      }
    })
  }
})