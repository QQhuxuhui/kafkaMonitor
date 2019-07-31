<template>
  <div class="sidebar">
    <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" background-color="#324157"
             text-color="#bfcbd9" active-text-color="#20a0ff" unique-opened router>
      <template v-for="item in items">
        <template v-if="item.subs">
          <el-submenu :index="item.index" :key="item.index">
            <template slot="title">
              <i :class="[item.icon,'icon']"></i><span slot="title">{{ item.title }}</span>
            </template>
            <template v-for="subItem in item.subs">
              <el-submenu v-if="subItem.subs" :index="subItem.index" :key="subItem.index">
                <template slot="title">
                  {{ subItem.title }}
                </template>
                <el-menu-item v-for="(threeItem,i) in subItem.subs" :key="i" :index="threeItem.index">
                  {{ threeItem.title }}
                </el-menu-item>
              </el-submenu>
              <el-menu-item v-else :index="subItem.index" :key="subItem.index">
                {{ subItem.title }}
              </el-menu-item>
            </template>
          </el-submenu>
        </template>
        <template v-else>
          <el-menu-item :index="item.index" :key="item.index">
            <i :class="[item.icon,'icon']"></i><span slot="title">{{ item.title }}</span>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        items: [
          {
            icon: 'iconshouye',
            //路由跳转都从根路径开始，否则多级路由跳转会建立在上次路径的基础上
            index: '/dashboard',
            title: '系统首页'
          },
          {
            icon: 'iconKafka',
            index: 'kafka',
            title: 'Kafka',
            subs: [
              {
                title: '数据监控',
                index: '/kafka/kafkaOffsetMonitor'
              },
              {
                title: 'Topic管理',
                index: '/kafka/kafkaTopicOperate'
              },
              {
                title: '服务管理',
                index: '/kafka/kafkaMonitorManager'
              },

            ]
          },
          {
            icon: 'iconzookeeper',
            index: '/zookeeper',
            title: 'ZooKeeper',
            subs: [
              {
                title: '服务管理',
                index: '/zookeeper/zookeeperServer'
              }

            ]
          },
          // {
          //   icon: 'iconhdfs',
          //   index: '/hdfs',
          //   title: 'HDFS',
          // },
          // {
          //   icon: 'iconmapreduce',
          //   index: '/mapreduce',
          //   title: 'MapReduce',
          // },
          // {
          //   icon: 'iconspark',
          //   index: '/spark',
          //   title: 'Spark',
          // },
          // {
          //   icon: 'iconstorm',
          //   index: '/storm',
          //   title: 'Storm',
          // },
          // {
          //   icon: 'icontixingshixin',
          //   index: '7',
          //   title: '错误处理',
          //   subs: [
          //     {
          //       index: '/permission',
          //       title: '权限测试'
          //     },
          //     {
          //       index: '/404',
          //       title: '404页面'
          //     }
          //   ]
          // },
          // {
          //   icon: 'iconliebiao',
          //   index: '/test',
          //   title: '测试板块'
          // },
        ]
      }
    },
    computed: {
      onRoutes() {
        return this.$route.path;
      },
      collapse() {
        return this.$store.state.collapse;
      }
    }
  }
</script>

<style scoped>
  .sidebar {
    display: block;
    position: absolute;
    left: 0;
    top: 50px;
    bottom: 0;
    overflow-y: scroll;
  }

  .sidebar::-webkit-scrollbar {
    width: 0;
  }

  .sidebar-el-menu:not(.el-menu--collapse) {
    width: 200px;
  }

  .sidebar > ul {
    height: 100%;
  }
  .el-submenu .el-menu-item{
    min-width: 180px;
  }
  .icon {
    margin-right: 14px;
    margin-left: 0px;
    width: 24px;
    text-align: center;
    font-size: 25px;
    vertical-align: middle
  }
</style>
