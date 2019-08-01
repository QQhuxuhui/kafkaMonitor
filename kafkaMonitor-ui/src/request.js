import axios from 'axios'
import {Message} from "element-ui";
import {Notification} from 'element-ui';
import router from "./router/index.js";
import QS from 'qs';

import {getCookie} from './util/cookieUtil'


let cancel, promiseArr = {}
const CancelToken = axios.CancelToken;

//设置默认值
// axios.defaults.baseURL = 'http://localhost:8091';
//设置默认请求头
axios.defaults.headers = {
  'X-Requested-With': 'XMLHttpRequest',
  'Content-Type': 'application/json'
};
axios.defaults.timeout = 10000;

//请求拦截
axios.interceptors.request.use(
  config => {
    if (config.method === "get") {
      //get请求序列化参数为&格式
      // console.info(config);
      //.params = QS.stringify(config.params);
      // console.info(config);
    }
    //发起请求时，取消掉当前正在进行的相同请求
    if (promiseArr[config.url]) {
      promiseArr[config.url]('操作取消');
      promiseArr[config.url] = cancel
    } else {
      promiseArr[config.url] = cancel
    }
    // 若是有做鉴权token , 就给头部带上token
    if (getCookie('token')) {
      config.headers.Authorization = getCookie('token');
    }
    return config
  },
  error => {
    Message({
      //消息弹窗组件,类似toast
      showClose: true,
      message: error && error.data.error.message,
      type: 'error'
    });
    return Promise.reject(error)
  });

//返回拦截
axios.interceptors.response.use(
  response => {
    if (!getCookie('token')) {
      // 若是接口访问的时候没有发现有鉴权的基础信息,直接返回登录页
      router.push({
        path: "/login"
      });
    } else {
      let data = response.data;
      // console.info(data);
      switch (data.code) {
        case 403:
          router.push({
            path: "/login"
          });
          Message({
            showClose: true,
            message: data.msg,
            type: "info"
          });
          break;
      }
    }
    return response
  },
  err => {
    // 用户登录的时候会拿到一个基础信息,比如用户名,token,过期时间戳
    if (!getCookie('token')) {
      // 若是接口访问的时候没有发现有鉴权的基础信息,直接返回登录页
      router.push({
        path: "/login"
      });
    } else {
      // 若是有基础信息的情况下,判断时间戳和当前的时间,若是当前的时间大于服务器过期的时间
      // 乖乖的返回去登录页重新登录
      // console.info('err.response', err.response);
      if (err && err.response) {
        switch (err.response.status) {
          case 400:
            err.message = '错误请求';
            break;
          case 401:
            err.message = '未授权，请重新登录';
            break;
          case 403:
            err.message = '用户未登录，请重新登陆';
            break;
          case 404:
            err.message = '未找到该资源:' + err.response.data.path;
            router.push({
              path: "/404"
            });
            break;
          case 405:
            err.message = '请求方法未允许';
            break;
          case 408:
            err.message = '请求超时';
            break;
          case 500:
            err.message = '服务器端出错';
            break;
          case 501:
            err.message = '网络未实现';
            break;
          case 502:
            err.message = '网络错误';
            break;
          case 503:
            err.message = '服务不可用';
            break;
          case 504:
            err.message = '网络超时';
            break;
          case 505:
            err.message = 'http版本不支持该请求';
            break;
          default:
            err.message = `连接错误${err.response.status}`
        }
      } else {
        err.message = "连接到服务器失败，请检查网络连接"
      }
    }
    Message({
      showClose: true,
      message: err.message,
      type: "error"
    });
    return Promise.resolve(err.response)
  });

export default {
  //get请求
  get(url, params = {}) {
    return new Promise((resolve, reject) => {
      axios({
        method: 'get',
        url,
        params,
        cancelToken: new CancelToken(c => {
          cancel = c
        })
      }).then(res => {
        resolve(res)
      })
    })
  },
  //post请求
  post(url, param = {}) {
    return new Promise((resolve, reject) => {
      axios({
        method: 'post',
        url,
        data: param,
        cancelToken: new CancelToken(c => {
          cancel = c
        })
      }).then(res => {
        resolve(res)
      })
    })
  }
}
// export default service
