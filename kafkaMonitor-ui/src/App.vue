<template>
  <div id="GG-web">
    <router-view></router-view>
  </div>
</template>

<script>

  export default {
  name: 'App',
  mounted(){
    // 关闭浏览器窗口的时候清空浏览器缓存在cookie的用户数据
    // window.onbeforeunload = function (e) {
    //   delCookie('token');
    // }
  },
    created () {
      //在页面加载时读取sessionStorage里的状态信息
      if (sessionStorage.getItem("store") ) {
        this.$store.replaceState(Object.assign({}, this.$store.state,JSON.parse(sessionStorage.getItem("store"))))
      }

      //在页面刷新时将vuex里的信息保存到sessionStorage里
      window.addEventListener("beforeunload",()=>{
        sessionStorage.setItem("store",JSON.stringify(this.$store.state))
      })
    }
}
</script>

<style>
  @import "./assets/css/main.css";
  @import "./assets/css/color-dark.css";     /*深色主题*/
  /*@import "./assets/css/theme-green/color-green.css";   !*浅绿色主题*!*/
</style>
