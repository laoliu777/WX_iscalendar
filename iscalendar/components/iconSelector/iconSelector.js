// components/iconSelector/iconSelector.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {

  },

  /**
   * 组件的初始数据
   */
  data: {
    backgroundColor: '#DDD7F7',
    imageUrl: '1.png',
    icons: [],
    displays: [],
    backgrounds: []
  },

  /**
   * 组件的方法列表
   */
  methods: {
    getBackgrounds: function() {
      let that = this
      wx.getFileSystemManager().readFile({
        filePath: 'utils/colors.txt',
        encoding: 'utf-8',
        success: res => {
          that.setData({
            colors: res.data.split('\n')
          })
        },
        fail: console.error
      })
    },
    getIconImages: function() {
      let that = this
      wx.getFileSystemManager().readdir({
        dirPath: 'images/clock/',
        success: res => {
          that.setData({
            icons: res.files
          })
          that.setData({
            displays: that.data.icons.map((item) => {
              return item.indexOf('.png') > 0
            })
          })
        },
        fail: console.error
      })
    },
    clickClock: function (event) {
      this.setData({
        imageUrl: event.currentTarget.dataset.item
      })
    },
    clickColor: function (event) {
      this.setData({
        backgroundColor: event.currentTarget.dataset.item
      })
    }
  },

  attached: function() {
    this.getIconImages();
    this.getBackgrounds();
  }
})