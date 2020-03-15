const app = getApp()

Page({ //页面的生命周期钩子、事件处理函数、页面的默认数据
  data: {
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),

    formats: {},
    readOnly: false,
    editorHeight: 300,
    keyboardHeight: 0,
    isIOS: false,

    content: '',
    content_html: '',
    placeholder: '开始输入...',
    isReadOnly: false,
    nodes: [{
      name: 'div',
      attrs: {
        class: 'div_class',
        style: 'line-height: 60px; color: red;'
      },
      children: [{
        type: 'text',
        text: 'RichText组件'
      }]
    }],
    files: []
  },
  //事件处理函数
  readOnlyChange() {
    this.setData({
      readOnly: !this.data.readOnly
    })
  },
  onLoad() {
    const platform = wx.getSystemInfoSync().platform
    const isIOS = platform === 'ios'
    this.setData({
      isIOS
    })
    const that = this
    this.updatePosition(0)
    let keyboardHeight = 0
    wx.onKeyboardHeightChange(res => {
      if (res.height === keyboardHeight) return
      const duration = res.height > 0 ? res.duration * 1000 : 0
      keyboardHeight = res.height
      setTimeout(() => {
        wx.pageScrollTo({
          scrollTop: 0,
          success() {
            that.updatePosition(keyboardHeight)
            that.editorCtx.scrollIntoView()
          }
        })
      }, duration)

    })
  },
  onShow: function () {

  },
  updatePosition(keyboardHeight) {
    const toolbarHeight = 50
    const {
      windowHeight,
      platform
    } = wx.getSystemInfoSync()
    let editorHeight = keyboardHeight > 0 ? (windowHeight - keyboardHeight - toolbarHeight) : windowHeight
    this.setData({
      editorHeight,
      keyboardHeight
    })
  },
  calNavigationBarAndStatusBar() {
    const systemInfo = wx.getSystemInfoSync()
    const {
      statusBarHeight,
      platform
    } = systemInfo
    const isIOS = platform === 'ios'
    const navigationBarHeight = isIOS ? 44 : 48
    return statusBarHeight + navigationBarHeight
  },
  onEditorReady() {
    //输入-编辑框
    const that = this
    wx.createSelectorQuery().select('#editor').context(function(res) {
      that.editorCtx = res.context;
      if (wx.getStorageSync("content")) { // 设置~历史值
        // console.log(wx.getStorageSync("content"))
        // that.editorCtx.insertText(wx.getStorageSync("content")) // 注意：插入的是对象
      }
    }).exec()

    var date = new Date()
    wx.request({
      url: "https://172.19.241.77:443/project/diary/getDiaryByUserIDandDate",
      header: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      method: 'POST',
      dataType: 'JSON',
      data: {
        user_id: app.globalData.openid,
        this_date: date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate(),
      },

      //responseType: 'text',
      success: function (res) {
        console.log(res.data)
        var item = JSON.parse(res.data);
        if (item.diarystate == "1") {
          that.setData({
            content: item.content,
            nodes: item.content,
            picture: item.picture
          })

          let nodes = item.content.split("<")
          console.log(nodes)

          for(let i = 0; i < nodes.length; i++) {
            if (nodes[i].startsWith("p") && nodes[i].indexOf(">") != nodes[i].length - 1){
              that.editorCtx.insertText({
                html: '<' + nodes[i] + '</p>',
                text: nodes[i].split('>')[1]
              })
            }
            if (nodes[i].startsWith("img")) {
              that.editorCtx.insertImage({
                src: nodes[i].split('"')[1],
                data: {
                  id: 'abcd' + i,
                  role: 'god'
                },
                width: '90%',
                success: function () {
                  console.log('insert image success')
                }
              })
            }
          }
        }
        // console.log(that.data.content)
      },
      fail: function (res) { },
      complete: function (res) { },
    })
  },
  // 获取内容
  onContentChange(e) {
    this.setData({
      content: e.detail,
    })
    wx.setStorageSync("content", e.detail)
  },
  // 保存并上传日记
  clickSaveText(e) {
    this.setData({
      nodes: this.data.content.html,
      content_html: this.data.content.html
    })
    //wx.setStorageSync("content_html",this.data.content.html)
    wx.navigateBack()
    wx.request({
      url: "https://172.19.241.77:443/project/diary/createDiary",
      header: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      method: 'POST',
      dataType: 'JSON',
      data: {
        user_id: app.globalData.openid,
        picture: "",
        content: this.data.content.html
      },

      //responseType: 'text',
      success: function(res) {
        // console.log(res.data)
        wx.showToast({
          icon: 'success',
          title: '日记保存成功！',
          duration: 2000
        })
      },
      fail: function(res) {},
      complete: function(res) {},
    })
  },
  blur() {
    this.editorCtx.blur()
  },
  format(e) {
    let {
      name,
      value
    } = e.target.dataset
    if (!name) return
    // console.log('format', name, value)
    this.editorCtx.format(name, value)

  },
  onStatusChange(e) {
    const formats = e.detail
    this.setData({
      formats
    })
  },
  insertDivider() {
    this.editorCtx.insertDivider({
      success: function() {
        console.log('insert divider success')
      }
    })
  },
  clear() {
    this.editorCtx.clear({
      success: function(res) {
        console.log("clear success")
      }
    })
  },
  removeFormat() {
    this.editorCtx.removeFormat()
  },
  insertDate() {
    const date = new Date()
    const formatDate = `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}`
    this.editorCtx.insertText({
      text: formatDate
    })
  },
  insertImage() {
    const that = this
    wx.chooseImage({
      count: 1,
      success: function(res) {
        that.editorCtx.insertImage({
          src: res.tempFilePaths[0],
          data: {
            id: 'abcd',
            role: 'god'
          },
          width: '80%',
          success: function() {
            console.log('insert image success')
          }
        })
      }
    })
  },

  returnLastPage: function() {
    wx.navigateBack()
  },
  chooseImage: function(e) {
    var that = this;
    wx.chooseImage({
      sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success: function(res) {
        // 返回选定照片的本地文件路径列表，tempFilePath可以作为img标签的src属性显示图片
        // console.log(res)
        that.setData({
          files: that.data.files.concat(res.tempFilePaths)
        });
        var i = 0;
        for (; i < res.tempFilePaths.length; i++) {
          that.editorCtx.insertImage({
            src: res.tempFilePaths[i],
            data: {
              id: 'abcd' + i,
              role: 'god'
            },
            width: '90%',
            success: function() {
              console.log('insert image success')
            }
          })
        }
      }
    })
  },
  previewImage: function(e) {
    wx.previewImage({
      current: e.currentTarget.id, // 当前显示图片的http链接
      urls: this.data.files // 需要预览的图片http链接列表
    })
  }

})