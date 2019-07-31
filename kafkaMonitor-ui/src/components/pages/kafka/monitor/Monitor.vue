<template>
  <router-view></router-view>
</template>

<script>
  export default {
    name: "",
    mounted() {
      //初始化获取所有邮箱列表
      this.$store.dispatch('getEmailAddressList');
      //初始化所有服务列表
      // this.$store.dispatch('getKafkaMonitorManagerList');
      // this.$store.dispatch('getKafkaMonitorList');
    }
  }
</script>

<style scoped>

</style>
