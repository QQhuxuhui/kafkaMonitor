import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: '/dashboard',
    },
    {
      path: '/login',
      name: 'login',
      component: resolve => require(['../components/pages/Login.vue'], resolve),
      meta: {
        title: '用户登录',
        notRequireLogin: true
      }
    },
    {
      path: '/',
      name: '首页',
      component: resolve => require(['../components/common/Home.vue'], resolve),
      meta: {title: '自述文件'},
      children: [
        {
          path: 'dashboard',
          name :'dashboard',
          component: resolve => require(['../components/pages/Dashboard.vue'], resolve),
          meta: {title: '系统首页'}
        },
        {
          path: 'monitor',
          name :'monitor',
          meta: {title: '监控中心'},
          component: resolve => require(['../components/pages/monitor/Monitor.vue'], resolve),
        },
        {
          path: 'kafka',
          name :'Kafka',
          meta: {title: 'Kafka'},
          component: resolve => require(['../components/pages/kafka/kafka.vue'], resolve),
          children: [
            {
              path: 'kafkaOffsetMonitor',
              name :'kafkaOffsetMonitor',
              meta: {title: '偏移量监控'},
              component: resolve => require(['../components/pages/kafka/monitor/KafkaOffsetMonitor.vue'], resolve)
            },
            {
              path: 'kafkaMonitorManager',
              name :'kafkaMonitorManager',
              meta: {title: '服务管理'},
              component: resolve => require(['../components/pages/kafka/monitor/KafkaMonitorManager.vue'], resolve)
            },
            {
              path: 'kafkaTopicOperate',
              name :'kafkaTopicOperate',
              meta: {title: 'Topic管理'},
              component: resolve => require(['../components/pages/kafka/operate/Topic.vue'], resolve)
            }
          ]
        },
        {
          path: 'mapreduce',
          name :'MapReduce',
          meta: {title: 'MapReduce'},
          component: resolve => require(['../components/pages/mapreduce/mapreduce.vue'], resolve)
        },
        {
          path: 'hdfs',
          name :'HDFS',
          meta: {title: 'HDFS'},
          component: resolve => require(['../components/pages/hdfs/hdfs.vue'], resolve)
        },
        {
          path: 'spark',
          name :'Spark',
          meta: {title: 'Spark'},
          component: resolve => require(['../components/pages/spark/spark.vue'], resolve)
        },
        {
          path: 'storm',
          name :'Storm',
          meta: {title: 'Storm'},
          component: resolve => require(['../components/pages/storm/storm.vue'], resolve)
        },
        {
          path: 'zookeeper',
          name :'ZooKeeper',
          meta: {title: 'ZooKeeper'},
          component: resolve => require(['../components/pages/zookeeper/zookeeper.vue'], resolve),
          children: [
            {
              path: 'zookeeperServer',
              name :'zookeeperServer',
              meta: {title: 'zk管理'},
              component: resolve => require(['../components/pages/zookeeper/zookeeperServer.vue'], resolve)
            }
          ]
        },
        {
          path: 'test',
          name :'test',
          component: resolve => require(['../components/pages/test.vue'], resolve),
          meta: {title: '测试板块'}
        },
        {
          path: 'permission',
          name :'permission',
          component: resolve => require(['../components/pages/permission.vue'], resolve),
          meta: {title: '权限测试'}
        },
        {
          path: 'user',
          name :'User',
          meta: {title: '用户中心'},
          component: resolve => require(['../components/pages/user/user.vue'], resolve)
        },

      ]
    },
    {
      path: '/404',
      component: resolve => require(['../components/pages/404.vue'], resolve),
      meta: { title: '404' }
    }
  ]
})
