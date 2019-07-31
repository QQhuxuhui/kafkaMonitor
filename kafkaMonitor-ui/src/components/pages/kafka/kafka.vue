<template>
  <router-view></router-view>
</template>

<script>
  export default {
    name: "",
    mounted() {
      //初始化获取所有邮箱列表
      this.$store.dispatch('getEmailAddressList');
    }
  }
</script>

<style scoped>

</style>
