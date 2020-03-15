//index.js
//获取应用实例
const app = getApp()

Page({ //页面的生命周期钩子、事件处理函数、页面的默认数据
  data: {
    content: "",
    picture: "",
    year: '2019',
    week: "sun1",
    month: "Dec1",
    day: "151",
    anniversaryCount: "0",
    checkinCount: "0",
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    placeholder: '开始输入..',
    date: {},
    nodes: [{
      name: 'div',
      attrs: {
        class: 'div_class',
        style: 'line-height: 60px; color: black;'
      },
      children: [{
        type: 'text',
        text: ''
      }]
    }],
    checkinList: [],
    anniversaryList: [],
    files: ['../../images/clock/1.png', '../../images/clock/2.png']
  },
  //事件处理函数
  bindViewTap: function() {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },
  onLoad: function(option) {
    var that = this;
    if (app.globalData.userInfo) {
      var arr_month = new Array('Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec');
      var month = arr_month.indexOf(option.month) + 1;
      if (arr_month.indexOf(option.month) == "-1") {
        month = option.month
      }
      console.log(month + "->" + option.month);
      var dateObject = app.getFormatDate(option.year + '-' + option.month + '-' + option.day);
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true,
        year: option.year,
        month: month,
        day: option.day,
        date: dateObject,
        // nodes:wx.getStorageSync("content_html"),
        // content:wx.getStorageSync("content_html")
      })
      //获取当日日记内容
      wx.request({
        url: "https://172.19.241.77:443/project/diary/getDiaryByUserIDandDate",
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        method: 'POST',
        dataType: 'JSON',
        data: {
          user_id: app.globalData.openid,
          this_date: option.year + '-' + month + '-' + option.day,
        },

        //responseType: 'text',
        success: function(res) {
          console.log("this_date:" + option.year + '-' + month + '-' + option.day)
          console.log(res.data)
          var item = JSON.parse(res.data);
          if (item.diarystate == "1") {
            that.setData({
              content: item.content,
              nodes: item.content,
              picture: item.picture
            })

          }
          //console.log(item.diarystate)
        },
        fail: function(res) {},
        complete: function(res) {},
      })
      //获取当日纪念数和打卡数
      wx.request({
        url: "https://172.19.241.77:443/project/user/getOverviewOfToday",
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        method: 'POST',
        dataType: 'JSON',
        data: {
          user_id: app.globalData.openid,
          this_date: option.year + '-' + month + '-' + option.day,
        },

        //responseType: 'text',
        success: function(res) {
          console.log("this_date:" + option.year + '-' + month + '-' + option.day)
          console.log(res.data)
          var item = JSON.parse(res.data);
          that.setData({
            anniversaryCount: item.anniversarycount,
            checkinCount: item.checkincount
          })
        },
        fail: function(res) {},
        complete: function(res) {},
      })
      //获取当日打卡列表
      var checkinArr = new Array();
      wx.request({
        url: "https://172.19.241.77:443/project/checkin/getCheckinsByUser",
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        method: 'POST',
        dataType: 'JSON',
        data: {
          user_id: app.globalData.openid,
          this_date: option.year + '-' + month + '-' + option.day
        },
        //responseType: 'text',
        success: function(res) {
          console.log("this_date:" + option.year + '-' + month + '-' + option.day)
          console.log(res.data)
          var item = JSON.parse(res.data);
          var i = 0;
          for (i = 0; i < item.length; i++) {
            var tmp = item[i];
            //console.log(tmp);
            var obj = {
              id: tmp.id,
              name: tmp.checkin_name, //打卡名称
              iconURL: tmp.icon_url, //指定图标
              background: tmp.background, // 背景颜色
              stickDays: 0, //坚持日期
              details: tmp.checkin_description, //打卡详细内容
              status: true,
            };
            console.log(obj);
            checkinArr.push(obj);
          }

          // 重新刷新数组
          that.setData({
            checkinList: checkinArr
          });
        },
        fail: function(res) {},
        complete: function(res) {},
      })

      //获取当日纪念日列表
      var anniversaryArr = new Array();
      wx.request({
        url: "https://172.19.241.77:443/project/anniversary/getAnniversariesByUser",
        header: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        method: 'POST',
        dataType: 'JSON',
        data: {
          user_id: app.globalData.openid,
          this_date: option.year + '-' + month + '-' + option.day
        },
        //responseType: 'text',
        success: function(res) {
          console.log("this_date:" + option.year + '-' + month + '-' + option.day)
          console.log(res.data)
          var item = JSON.parse(res.data);
          var i = 0;
          for (i = 0; i < item.length; i++) {
            var tmp = item[i];
            //console.log(tmp);
            var obj = {
              id: tmp.id,
              name: tmp.anniversary_name, //纪念日名称
              iconURL: tmp.icon_url, //指定图标
              passDays: tmp.distday, //已过时间
              createDate: tmp.anniversary, //纪念日创建日期
              background: tmp.background,
              description: tmp.anniversary_description,
              anniversary_type: tmp.anniversary_type //纪念方式
            };
            console.log(obj);
            anniversaryArr.push(obj);
          }

          // 重新刷新数组
          that.setData({
            anniversaryList: anniversaryArr
          });
        },
        fail: function(res) {},
        complete: function(res) {},
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
  onshow: function() {
    var that = this;
    that.getMsg();
  },
  //获取日记
  getMsg() {
    let that = this;
    wx.request({
      url: '服务器请求',
      success: function(res) {
        console.log(res.data.content)
        res.data.content = res.data.content.replace(/width\s*:\s*[0-9]+px/g, 'width:100%');
        res.data.content = res.data.content.replace(/<([\/]?)(center)((:?\s*)(:?[^>]*)(:?\s*))>/g, '<$1div$3>'); //替换center标签
        res.data.content = res.data.content.replace(/\<img/gi, '<img class="rich-img" '); //正则给img标签增加class
        //或者这样直接添加修改style
        res.data.content = res.data.content.replace(/style\s*?=\s*?([‘"])[\s\S]*?\1/ig, 'style="width:100%;height:auto;display: block;margin:auto"');
        res.data.content = res.data.content.replace(/\<p/gi, '<P class="rich-p" '); //正则给p标签增加class

        //不支持section标签情况
        //   article_content = article_content.replace(/<img/gi, '<img style="max-width:100%;height:auto;float:left;display:block" ')
        //   .replace(/<section/g, '<div')
        //  .replace(/\/section>/g, '\div>');
        that.setData({
          content: res.data.content
        })
      }
    })
  },
  getUserInfo: function(e) {
      console.log(e)
      app.globalData.userInfo = e.detail.userInfo
      this.setData({
        userInfo: e.detail.userInfo,
        hasUserInfo: true
      })
    }

    ,
  onEditorReady() {
    const that = this
    wx.createSelectorQuery().select('#editor').context(function(res) {
      that.editorCtx = res.context
    }).exec()
  },

  getArticleInfo: function(id) {


  },
  previewImage: function(e) {
    wx.previewImage({
      current: e.currentTarget.id, // 当前显示图片的http链接
      urls: this.data.files // 需要预览的图片http链接列表
    })
  }

})