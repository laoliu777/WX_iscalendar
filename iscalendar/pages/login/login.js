//获取应用实例
const app = getApp()

Page({
  data: {
    canIUse: wx.canIUse('button.open-type.getUserInfo')
  },

  onLoad: function() {
    wx.hideHomeButton()
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
  getUserInfo: function(e) {
    app.globalData.userInfo = e.detail.userInfo
    this.setData({
      userInfo: e.detail.userInfo,
      hasUserInfo: true
    })
  },
  //按钮的点击事件
  bindGetUserInfo: function(res) {
    let info = res;
    console.log(info);
    if (info.detail.userInfo) {
      console.log("点击了同意授权");
      var that = this
      wx.login({
        success: function(res) {
          if (res.code) {
            wx.request({
              url: 'https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code',
              data: {
                appid: 'wx362a2203222dc677',
                secret: '6e671520a379fff31c83c1b1f3393510',
                js_code: res.code,
                grant_type: 'authorization_code'
              },
              header: {
                'content-type': 'application/json' // 默认值
              },
              success: function(res) {
                console.log(res)
              }
            })
            // wx.request({
            //   url: 'http://fa.com/api/schoolreserve/login',
            //   data: {
            //     code: res.code,
            //     user_info: info.detail.userInfo
            //   },
            //   header: {
            //     'content-type': 'application/json' // 默认值
            //   },
            //   success: function (res) {
            //     var userinfo = {};
            //     userinfo['id'] = res.data.id;
            //     userinfo['nickName'] = info.detail.userInfo.nickName;
            //     userinfo['avatarUrl'] = info.detail.userInfo.avatarUrl;
            //     userinfo['user_data'] = res.data;
            //     wx.setStorageSync('userinfo', userinfo)
            //     that.setData({
            //       userInfo: info.detail.userInfo
            //     })
            //     wx.switchTab({
            //       url: '../toast/toast',
            //     })
            //   }
            // })
          } else {
            console.log("授权失败");
          }
        },
      })

    } else {
      //用户按了拒绝按钮
      wx.showModal({
        title: '授权失败',
        content: '您拒绝了此次授权，将无法进入小程序...',
        showCancel: false,
        confirmText: '返回授权',
        success: function(res) {
          if (res.confirm) {
            console.log('用户点击了“返回授权”')
          }
        }
      })
    }
  }
})