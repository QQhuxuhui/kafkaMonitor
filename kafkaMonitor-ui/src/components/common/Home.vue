<template>
  <div class="wrapper">
    <v-head></v-head>
    <v-sidebar></v-sidebar>
    <div class="content-box" :class="{'content-collapse':collapse}">
      <v-tags></v-tags>
      <div class="content">
        <transition name="move" mode="out-in">
          <keep-alive :include="this.$store.state.tagsList">
            <router-view></router-view>
          </keep-alive>
        </transition>
      </div>
    </div>
  </div>
</template>

<script>
  import vHead from './Header.vue';
  import vSidebar from './Sidebar.vue';
  import vTags from './Tags.vue';

  export default {
    name: "",
    data() {
      return {
      }
    },
    components: {
      vHead, vSidebar,vTags
    },
    computed: {
      //传给子主键，控制窗口伸缩
      collapse() {
        return this.$store.state.collapse
      }
    }
  }
</script>

<style scoped>

</style>
