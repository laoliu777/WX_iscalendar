// components/clock/clock.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    clockConfig: {
      type: Object,
      value: ''
    }
  },

  /**
   * 组件的初始数据
   */
  data: {
    name: '',
    backgroundColor: '#FFFFFF',
    checked: false,
    stick_days: 0
  },

  /**
   * 组件的方法列表
   */
  methods: {
    clickClock: function() {
      if (!this.data.checked) {
        wx.request({
          url: "https://172.19.241.77:443/project/checkin/checkin",
          method: "POST",
          dataType: 'JSON',
          header: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          data: {
            checkin_id: this.properties.clockConfig.id,
          },
          success: (res) => {
            var item = JSON.parse(res.data);
            console.log(item)
            this.setData({
              checked: true,
              stick_days: item.stick_days
            })
            this.setBackground();
          },
          fail: (res) => {
            console.log(res);
          }
        })
      } else {
        wx.request({
          url: "https://172.19.241.77:443/project/checkin/cancelCheckin",
          method: "POST",
          dataType: 'JSON',
          header: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          data: {
            checkin_id: this.properties.clockConfig.id,
          },
          success: (res) => {
            var item = JSON.parse(res.data);
            this.setData({
              checked: false,
              stick_days: item.stick_days
            })
            this.setBackground();
          },
          fail: (res) => {
            console.log(res);
          }
        })
      }
    },
    setBackground: function() {
      if (this.data.checked) {
        this.setData({
          backgroundColor: this.properties.clockConfig.background
        })
      } else {
        this.setData({
          backgroundColor: '#FFFFFF'
        })
      }
    }
  },

  attached: function() {
    this.setData({
      checked: this.properties.clockConfig.checked,
      stick_days: this.properties.clockConfig.stickDays,
    })

    this.setBackground();
  }, // 此处attached的声明会被lifetimes字段中的声明覆盖
})