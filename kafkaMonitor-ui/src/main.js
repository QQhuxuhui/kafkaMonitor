// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Vuex from 'vuex'
import App from './App'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/icon/iconfont.css'
import {get, post} from './api/api'
import {getEmailAddressList, getKafkaMonitorManagerList, getKafkaMonitorList} from './api/kafkaMonitorApi'
import {getCookie} from './util/cookieUtil'

// vue task node
// import VueTaskNode from 'vue-task-node'
// import "vue-task-node/dist/css/vnode.css"


// Vue.prototype.$http = axios;
Vue.prototype.$get = get;
Vue.prototype.$post = post;

Vue.config.productionTip = false;
Vue.use(ElementUI); //使用ElementUI组件
Vue.use(Vuex);

// Vue.use(VueTaskNode);

//路由狗子函数
router.beforeEach((to, from, next) => {
  if (to.meta.notRequireLogin) {
    next();
  } else {
    // 判断是否需要登录权限
    // if (window.localStorage.getItem("loginUserBaseInfo") && to.path !== '/login') {
    if (getCookie('token') && to.path !== '/login') {
      // 简单的判断IE10及以下不进入富文本编辑器，该组件不兼容
      if (navigator.userAgent.indexOf('MSIE') > -1 && to.path === '/editor') {
        ElementUI.Message({
          showClose: true,
          message: "vue-quill-editor组件不兼容IE10及以下浏览器，请使用更高版本的浏览器查看",
          type: "error"
        });
      } else {
        next()
      }
    } else {
      // 没登录则跳转到登录界面
      next({
        path: "/login"
      });
      ElementUI.Message({
        showClose: true,
        message: "用户身份信息已过期，请重新登录",
        type: "info"
      });
    }
  }
});

//路由钩子函数
router.afterEach((to, from) => {
  window.scrollTo(0, 0);
});


//vuex状态管理器
export const store = new Vuex.Store({
  state: {
    pathData: {
      isShow: false
    },
    collapse: false,
    tagsList: [],
    httpStatus: {
      hasError: false,
      status: '',
      statusText: ''
    },
    //kafkaMonitor
    AddKafkaMonitorDialogShow: false,
    UpdateKafkaMonitorDialogShow: false,
    AddEmailDialogShow: false,
    AddKafkaMonitorManagerDialogShow: false,
    KafkaMonitorManagerList: [],
    kafkaMonitorListData: [],
    kafkaMonitorData:null,
    //Email
    emails: [],
    username: '',//用户账号
    name: ''//用户昵称
  },
  getters: {
    getPathData (state) {
      return state.pathData
    }
  },
  mutations: {
    setPath (state, name) {
      state.pathData = name
    },
    changeCollapse(state) {
      state.collapse = !state.collapse;
    },
    ON_HTTP_ERROR(state, payload) {
      state.httpStatus = payload
    },
    ON_HTTP_SUCCESS(state, payload) {
      state.httpStatus = payload
    },
    tags(state, msg) {
      let arr = [];
      for (let i = 0, len = msg.length; i < len; i++) {
        msg[i].name && arr.push(msg[i].name);
      }
      state.tagsList = arr;
    },
    setEmailAddressList(state) {
      getEmailAddressList().then(response => {
        if (!response) {
          return;
        }
        let data = response.data;
        if (data.code === 200) {
          data = data.data;
          let dataLength = data.length;
          let i = 0;
          state.emails = [];
          for (i; i < dataLength; i += 1) {
            state.emails.push(data[i]);
          }
        } else {
          //ElementUI.Message.error('请求邮箱数据失败')
        }
      })
    },
    setKafkaMonitorManagerList(state) {
      getKafkaMonitorManagerList().then(response => {
        if (!response) {
          return;
        }
        let data = response.data;
        if (data.code === 200) {
          data = data.data;
          let dataLength = data.length;
          let i = 0;
          state.KafkaMonitorManagerList = [];
          for (i; i < dataLength; i += 1) {
            state.KafkaMonitorManagerList.push(data[i]);
          }
        }
      })
    },
    setKafkaMonitorList(state) {
      getKafkaMonitorList().then(response => {
        if (!response) {
          return;
        }
        let data = response.data;
        if (data.code === 200) {
          data = data.data;
          let dataLength = data.length;
          let i = 0;
          state.kafkaMonitorListData = [];
          for (i; i < dataLength; i += 1) {
            state.kafkaMonitorListData.push(data[i]);
          }
        }
      })
    }
  },
  actions: {
    setPathData ({commit, state}, name) {
      commit('setPath', name)
    },
    //请求邮箱列表
    getEmailAddressList(context) {
      setTimeout(() => {
        context.commit('setEmailAddressList');
      }, 0);
    },
    //请求kafkaManager列表
    getKafkaMonitorManagerList(context) {
      setTimeout(() => {
        context.commit('setKafkaMonitorManagerList');
      }, 0);
    },
    //请求监控条目列表
    getKafkaMonitorList(context) {
      setTimeout(() => {
        context.commit('setKafkaMonitorList');
      }, 0);
    }
  }
});

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store: store,
  components: {App},
  template: '<App/>'
});


