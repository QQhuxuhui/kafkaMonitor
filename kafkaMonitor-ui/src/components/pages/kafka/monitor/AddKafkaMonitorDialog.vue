<template xmlns="">
  <div>
    <el-dialog title="添加监控"
               :visible="$store.state.AddKafkaMonitorDialogShow"
               width="45%" @open="handleDialogOpen" @close="handleDialogClose">
      <el-form ref="form" :model="form" status-icon label-width="100px" :rules="rules" class="demo-ruleForm"
               v-if="KafkaMonitorManagerList.length>0">
        <el-form-item label="服务名称" prop="server">
          <el-col :span="20">
            <el-select v-model="form.server" placeholder="请选择服务器" style="width: 100%;position: relative"
                       @change="serverChange">
              <el-option v-for="(item,index) in KafkaMonitorManagerList"
                         :key="index"
                         :label="item.name"
                         :value="item.address">
                <span style="float: left">{{ item.name }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">{{ item.address }}</span>
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="Topic" prop="topic">
          <el-col :span="20">
            <el-autocomplete
              v-model="form.topic"
              :fetch-suggestions="queryTopicAsync"
              placeholder="请输入topic"
              @select="handleTopicSelect"
              style="width: 100%;position: relative">
            </el-autocomplete>
          </el-col>
        </el-form-item>
        <el-form-item label="Group" prop="group">
          <el-col :span="20">
            <el-autocomplete
              v-model="form.group"
              :fetch-suggestions="queryGroupAsync"
              placeholder="请输入group"
              @select="handleGroupSelect"
              style="width: 100%;position: relative">
            </el-autocomplete>
          </el-col>
        </el-form-item>
        <el-form-item label="监控阈值" prop="lag">
          <el-col :span="20">
            <el-input v-model="form.lag"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="邮件提醒">
          <el-switch v-model="form.isEmail"></el-switch>
        </el-form-item>
<!--        <transition name="el-fade-in">-->
          <el-form-item label="收件人" v-show="form.isEmail">
            <el-col :span="20">
              <el-checkbox-group v-model="form.emails">
                <el-checkbox v-for="(item,index) in emailsData" :key="index" :label="item.email">
                  {{item.name}}
                </el-checkbox>
                <el-button type="primary" @click="showEmailDialog">添加收件人</el-button>
              </el-checkbox-group>
            </el-col>
          </el-form-item>
<!--        </transition>-->
        <el-form-item label="备注">
          <el-col :span="20">
            <el-input type="textarea" v-model="form.desc"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="KafkaMonitorManagerList.length>0">
        <el-button @click="$store.state.AddKafkaMonitorDialogShow = false">取 消</el-button>
        <el-button type="primary" @click="submitForm('form')">添 加</el-button>
      </div>
      <!--<router-link to='/monitor/kafkaMonitorManager'>-->
      <el-button type="primary" v-if="KafkaMonitorManagerList.length<=0 || !KafkaMonitorManagerList"
                 @click="showAddKafkaMonitorManagerDialog">
        无服务器，点击添加
      </el-button>
      <!--</router-link>-->
    </el-dialog>
    <AddKafkaMonitorManagerDialog></AddKafkaMonitorManagerDialog>
  </div>
</template>

<script>
  import {getTopicList} from '../../../../api/kafkaMonitorApi'
  import {getTopicDetails} from '../../../../api/kafkaMonitorApi'
  import {saveKafkaMonitor} from '../../../../api/kafkaMonitorApi'
  import AddKafkaMonitorManagerDialog from './AddKafkaMonitorManagerDialog.vue'

  export default {
    name: "",
    components: {
      AddKafkaMonitorManagerDialog
    },
    data() {
      let checkServer = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('请选择服务'));
        }
        callback();
      };
      let checkTopic = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('请输入Topic'));
        } else {
          if (this.topicList.indexOf(value) !== -1) {
            callback();
          } else {
            return callback(new Error('Topic不存在'));
          }
        }
      };
      let checkGroup = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('请输入Group'));
        } else {
          if (this.groupList.indexOf(value) !== -1) {
            callback();
          } else {
            return callback(new Error('group不存在'));
          }
        }
      };
      return {
        form: {
          name: '',
          server: '',
          topic: '',
          group: '',
          lag: 100000,
          isEmail: true,
          emails: [],
          desc: ''
        },
        topicList: [],
        groupList: [],
        rules: {
          topic: [
            {validator: checkTopic, trigger: 'blur'}
          ],
          server: [
            {validator: checkServer, trigger: 'blur'}
          ],
          group: [
            {validator: checkGroup, trigger: 'blur'}
          ]
        },
      }
    },
    computed: {
      emailsData() {
        return this.$store.state.emails;
      },
      KafkaMonitorManagerList() {
        return this.$store.state.KafkaMonitorManagerList;
      },
      //用来监听
      // server() {
      //   return this.form.server;
      // },
      // topic() {
      //   return this.form.topic;
      // }
    },
    methods: {
      handleDialogOpen(){
        //刷新邮箱列表
        this.$store.dispatch('getEmailAddressList');
        this.$store.state.AddKafkaMonitorDialogShow = true;
      },
      handleDialogClose(){
        this.resetForm('form');
        //清空数据
        this.topicList = [];
        this.groupList = [];
        this.$store.state.AddKafkaMonitorDialogShow = false;
      },
      getTopicList() {
        let address = this.form.server;
        if (!address) {
          return;
        }
        getTopicList(address).then(response => {
          if (response) {
            let data = response.data;
            let code = data.code;
            data = data.data;
            if (code === 200) {
              let dataLnegth = data.topicList.length;
              let i = 0;
              this.topicList = [];
              for (i; i < dataLnegth; i++) {
                let reg = new RegExp("\"", "g");
                this.topicList.push(data.topicList[i].replace(reg, ""));
              }
            }
          }
        })
      },
      getTopicDetails() {
        let address = this.form.server;
        // let group = this.form.group;
        let topic = this.form.topic;
        if (!topic) {
          return;
        }
        if (this.topicList.indexOf(topic) === -1) {
          return;
        }
        getTopicDetails(address, topic).then(response => {
          if (response) {
            let data = response.data;
            let code = data.code;
            data = data.data;
            if (code === 200) {
              let dataLnegth = data.consumers.length;
              let i = 0;
              this.groupList = [];
              for (i; i < dataLnegth; i++) {
                this.groupList.push(data.consumers[i].name);
              }
            }
          }
        })
      },
      showEmailDialog() {
        this.$store.state.AddEmailDialogShow = true;
      },
      showAddKafkaMonitorManagerDialog() {
        this.$store.state.AddKafkaMonitorManagerDialogShow = true;
      },
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let address = this.form.server;
            let topic = this.form.topic;
            let group = this.form.group;
            let lag = this.form.lag;
            let emails = this.form.emails;
            let isEmail = this.form.isEmail;
            let desc = this.form.desc;
            let isDeleted = false;
            saveKafkaMonitor(null, address, topic, group, lag, emails, isEmail, isDeleted, desc).then(response => {
              if (response) {
                let data = response.data;
                let code = data.code;
                data = data.data;
                if (code === 200) {
                  this.$message({
                    //消息弹窗组件,类似toast
                    showClose: true,
                    message: '监控添加成功',
                    type: 'success'
                  });
                  //刷新数据
                  this.$store.dispatch('getKafkaMonitorList');
                  //关闭窗口
                  this.handleDialogClose();
                } else {
                  this.$message({
                    //消息弹窗组件,类似toast
                    showClose: true,
                    message: '监控添加失败：' + JSON.stringify(response),
                    type: 'error'
                  });
                }
              }
            })
          }
        })
      },
      resetForm(formName) {
        this.$nextTick(() => {
          this.$refs[formName].resetFields();
        })
      },
      queryTopicAsync(queryString, callback) {
        //封装topic
        let topicListLength = this.topicList.length;
        let topicArrMap = [];
        for (let i = 0; i < topicListLength; i++) {
          let topicMap = {"value": this.topicList[i]}
          topicArrMap.push(topicMap);
        }
        let restaurants = topicArrMap;
        let results = queryString ? restaurants.filter(this.createStateFilter(queryString)) : restaurants;
        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => {
          callback(results);
        }, 0);
      },
      queryGroupAsync(queryString, callback) {
        //封装group
        let groupListLength = this.groupList.length;
        let groupArrMap = [];
        for (let i = 0; i < groupListLength; i++) {
          let groupMap = {"value": this.groupList[i]};
          groupArrMap.push(groupMap);
        }
        let restaurants = groupArrMap;
        let results = queryString ? restaurants.filter(this.createStateFilter(queryString)) : restaurants;
        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => {
          callback(results);
        }, 0);
      },
      createStateFilter(queryString) {
        return (state) => {
          return (state.value.toLowerCase().indexOf(queryString.toLowerCase()) !== -1);
        };
      },
      serverChange() {
        this.$refs['form'].validateField('server');
        this.getTopicList();
        this.form.topic = '';
        this.form.group = '';
      },
      handleTopicSelect(item) {
        //选中校验
        this.$refs['form'].validateField('topic');
        this.getTopicDetails();
        this.form.group = '';
      },
      handleGroupSelect(item) {
        this.$refs['form'].validateField('group');
      }
    },
    mounted() {
    }
  }
</script>

<style scoped>

</style>
