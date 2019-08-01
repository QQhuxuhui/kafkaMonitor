<template>
  <div class="container">
    <el-row :gutter="5">
      <el-col :span="3"><span>请选择zookeeper</span></el-col>
      <el-col :span="4">
        <el-select v-model="zookeeper" placeholder="请选择zookeeper"
                   @change="zookeeperChangeMethod" size="mini">
          <el-option v-for="(item,index) in zookeeperList"
                     :key="index"
                     :label="item.name"
                     :value="item.address">
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.address }}</span>
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="5">
        <el-button type="primary" size="mini" @click="$router.push('/zookeeper/zookeeperServer')">zookeeper管理
        </el-button>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin: 40px 0 0 0">
      <el-col :span="4">
        <el-button type="primary" size="mini" @click="addTopicDialogShow = true">添加Topic</el-button>
      </el-col>
    </el-row>
    <el-table
      v-loading="loading"
      :data="topicTableData.filter(data => !topicSearch || data.topic.toLowerCase().includes(topicSearch.toLowerCase()))"
      style="width: 870px">
      <el-table-column
        prop="topic"
        label="Topic名称"
        width="400">
      </el-table-column>
      <el-table-column
        sortable
        prop="createDate"
        label="创建日期"
        :formatter="formatCreateDate"
        width="150">
      </el-table-column>
      <el-table-column
        prop="createBy"
        label="创建人"
        width="120">
      </el-table-column>
      <el-table-column
        align="right" width="200">
        <template slot="header" slot-scope="scope">
          <el-input
            v-model="topicSearch"
            size="mini"
            placeholder="输入关键字搜索"></el-input>
        </template>
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="分区详情" placement="top-start">
            <el-button
              size="mini"
              type="primary"
              plain
              icon="el-icon-view"
              @click="getTopicPartitionDetailMethod(scope.$index, scope.row)">
            </el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="删除" placement="top-start">
            <el-button
              size="mini"
              type="danger"
              plain
              icon="el-icon-delete"
              @click="handleDelete(scope.$index, scope.row)">
            </el-button>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>


    <!--新增topic弹窗-->
    <el-dialog title="添加Topic"
               :visible="addTopicDialogShow"
               width="45%" @open="handleDialogOpen" @close="handleDialogClose">
      <el-form ref="addTopicForm" :model="addTopicForm" status-icon label-width="100px"
               class="demo-ruleForm">
        <el-form-item label="Topic名称" prop="topic">
          <el-col :span="20">
            <el-input v-model="addTopicForm.topic"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="分区数" prop="partitions">
          <el-col :span="20">
            <el-input v-model="addTopicForm.partitions"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="副本数" prop="replication">
          <el-col :span="20">
            <el-input v-model="addTopicForm.replication"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addTopicDialogShow = false">取 消</el-button>
        <el-button type="primary" @click="submitForm('addTopicForm')">添 加</el-button>
      </div>
      <!--</router-link>-->
    </el-dialog>


    <el-dialog title="分区详情"
               :visible.sync="topicPartitionDetailDialogShow" width="55%">
      <el-table
        :data="topicPartitionDetailTableData"
        style="width: 800px">
        <el-table-column
          prop="topic"
          label="Topic"
          width="350">
        </el-table-column>
        <el-table-column
          prop="partition"
          label="分区"
          width="50">
        </el-table-column>
        <el-table-column
          prop="address"
          label="副本分布"
          width="400">
        </el-table-column>
      </el-table>
      <!--</router-link>-->
    </el-dialog>
  </div>

</template>

<script>

  import {
    getTopicList,
    createTopic,
    deleteTopic,
    topicExists,
    getTopicPartitionDetail
  } from '../../../../api/kafkaOperateApi';
  import dateUtil from 'element-ui/src/utils/date';
  import {getZooKeeperServerList, saveZookeeperServer, testConn} from '../../../../api/zookeeperApi'


  export default {
    name: "",
    data() {
      return {
        zookeeper: '',
        zookeeperList: [],
        topicList: [],
        partitions: 1,
        replication: 1,
        topic: 'hxh_test',
        topicTableData: [],
        topicSearch: '',
        addTopicDialogShow: false,
        topicPartitionDetailDialogShow: false,
        addTopicForm: {
          topic: '',
          replication: 2,
          partitions: 4

        },
        rules: {
          // topic: [
          //   {validator: checkTopic, trigger: 'blur'}
          // ],
          // server: [
          //   {validator: checkServer, trigger: 'blur'}
          // ],
          // group: [
          //   {validator: checkGroup, trigger: 'blur'}
          // ]
        },
        topicPartitionDetailTableData: [],
        loading: true
      }
    },
    methods: {
      zookeeperChangeMethod() {
        this.getTopicListMethod();
      },
      getTopicListMethod() {
        if (!this.zookeeper || this.zookeeper === '') {
          this.loading = false;
          return;
        }
        this.loading = true;
        getTopicList(this.zookeeper).then(response => {
          if (response) {
            let data = response.data;
            if (data.code === 200) {
              data = data.data;
              this.topicList = [];
              this.topicTableData = [];
              let date = new Date();
              for (let i = 0; i < data.length; i++) {
                this.topicList.push(data[i].topic);
                this.topicTableData.push(data[i]);
              }
            }
          }
          this.loading = false;
        });
      },
      getZooKeeperServerListMethod() {
        getZooKeeperServerList().then(response => {
          if (response) {
            let data = response.data;
            if (data.code === 200) {
              data = data.data;
              this.zookeeperList = [];
              for (let i = 0; i < data.length; i++) {
                this.zookeeperList.push(data[i]);
              }
            }
          }
        });
      },
      getTopicPartitionDetailMethod(index, row) {
        getTopicPartitionDetail(this.zookeeper, row.topic).then(response => {
          if (response) {
            let data = response.data;
            if (data.code === 200) {
              this.topicPartitionDetailTableData = [];
              data = data.data;
              for (let i = 0; i < data.length; i++) {
                this.topicPartitionDetailTableData.push(data[i]);
              }
              this.topicPartitionDetailDialogShow = true;
            }
          }
        })
      },
      formatCreateDate: function (row, column) {
        let date = new Date(row.createDate);
        return dateUtil.format(date, 'yyyy-MM-dd HH:mm:ss');
      },
      deleteTopicMethod(zookeeper, topic) {
        deleteTopic(zookeeper, topic).then(response => {
          if (response) {
            let data = response.data;
            if (data.code === 200) {
              this.$message({
                //消息弹窗组件,类似toast
                showClose: true,
                message: topic + '删除成功',
                type: 'success'
              });
              //topic 的变化不会这么快，确定删除后，直接改变数组内数据即可
              let topicTableDataCopy = this.topicTableData;
              this.topicTableData = [];
              for (let i = 0; i < topicTableDataCopy.length; i++) {
                if (topicTableDataCopy[i].topic !== topic) {
                  this.topicTableData.push(topicTableDataCopy[i]);
                }
              }
            } else {
              this.$message({
                //消息弹窗组件,类似toast
                showClose: true,
                message: data.msg,
                type: 'error'
              });
            }
          }
        });
      },
      topicExistsMethod() {
        topicExists(this.zookeeper, this.topic).then(response => {
          if (response) {
            let data = response.data;
            if (data.code === 200) {
              this.$message({
                //消息弹窗组件,类似toast
                showClose: true,
                message: this.topic + "存在",
                type: 'success'
              });
            } else if (data.code === 201) {
              this.$message({
                //消息弹窗组件,类似toast
                showClose: true,
                message: this.topic + "不存在",
                type: 'success'
              });
            } else {
              this.$message({
                //消息弹窗组件,类似toast
                showClose: true,
                message: data.msg,
                type: 'error'
              });
            }
          }
        });
      },
      handleEdit(index, row) {
        console.log(index, row);
      },
      handleDelete(index, row) {
        this.$confirm('确定删除?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          //id, address, name, isDeleted, tag
          this.deleteTopicMethod(this.zookeeper, row.topic);
          // this.topicTableData.splice(row,1);
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      handleDialogOpen() {
        //刷新邮箱列表
        this.addTopicDialogShow = true;
      },
      handleDialogClose() {
        this.resetForm('addTopicForm');
        this.addTopicDialogShow = false;
      },
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            createTopic(this.zookeeper, this.addTopicForm.topic, this.addTopicForm.partitions, this.addTopicForm.replication).then(response => {
              if (response) {
                let data = response.data;
                if (data.code === 200) {
                  this.$message({
                    //消息弹窗组件,类似toast
                    showClose: true,
                    message: this.topic + '创建成功',
                    type: 'success'
                  });
                  this.getTopicListMethod();
                  this.handleDialogClose();
                } else {
                  this.$message({
                    //消息弹窗组件,类似toast
                    showClose: true,
                    message: data.msg,
                    type: 'error'
                  });
                }
              }
            });
          }
        })
      },
      resetForm(formName) {
        this.$nextTick(() => {
          if (this.$refs[formName] !== undefined) {
            this.$refs[formName].resetFields();
          }
        })
      },
    },
    mounted() {
      this.getZooKeeperServerListMethod();
      this.getTopicListMethod();
    }
  }
</script>

<style scoped>

</style>
