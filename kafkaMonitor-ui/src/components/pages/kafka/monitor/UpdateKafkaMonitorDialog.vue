<template>
  <div>
    <el-dialog title="编辑监控"
               :visible="$store.state.UpdateKafkaMonitorDialogShow"
               width="45%"
               @open="handleOpen" @close="handleClose">
      <el-form :model="form" status-icon ref="form" label-width="100px" class="demo-ruleForm"
               v-if="kafkaMonitor">
        <el-form-item label="服务地址" prop="server">
          <el-col :span="20">
            <span>{{form.server}}</span>
          </el-col>
        </el-form-item>
        <el-form-item label="Topic" prop="topic">
          <el-col :span="20">
            <span>{{form.topic}}</span>
          </el-col>
        </el-form-item>
        <el-form-item label="Group" prop="group">
          <el-col :span="20">
            <span>{{form.group}}</span>
          </el-col>
        </el-form-item>
        <el-form-item label="监控阈值" prop="lag">
          <el-col :span="20">
            <el-input v-model="form.lag"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="邮件提醒" prop="emailAlter">
          <el-switch v-model="form.emailAlter"></el-switch>
        </el-form-item>
        <el-form-item label="收件人" v-show="form.emailAlter">
          <el-col :span="20">
              <el-checkbox-group v-model="form.emails">
                <el-checkbox v-for="(item,index) in emailsData" :key="index" :label="item.email">
                  {{item.name}}
                </el-checkbox>
                <el-button type="primary" @click="showEmailDialog">添加收件人</el-button>
              </el-checkbox-group>
          </el-col>
        </el-form-item>
        <el-form-item label="备注">
          <el-col :span="20">
            <el-input type="textarea" v-model="form.desc"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="kafkaMonitor">
        <el-button @click="$store.state.UpdateKafkaMonitorDialogShow = false">取 消</el-button>
        <el-button type="primary" @click="submitForm('form')">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

  import {getKafkaMonitorEmails, saveKafkaMonitor} from '../../../../api/kafkaMonitorApi'

  export default {
    name: "",
    props: {
      kafkaMonitor: Object,
      showDialog: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        form: {
          id: '',
          name: '',
          server: '',
          topic: '',
          group: '',
          lag: 100000,
          emailAlter: true,
          emails: [],
          desc: ''
        },
      }
    },
    methods: {
      showEmailDialog() {
        this.$store.state.AddEmailDialogShow = true;
      },
      handleOpen() {
        this.form.server = this.kafkaMonitor.address;
        this.form.topic = this.kafkaMonitor.topic;
        this.form.group = this.kafkaMonitor.groupId;
        this.form.lag = this.kafkaMonitor.lag;
        this.form.emailAlter = this.kafkaMonitor.emailAlter;
        this.form.id = this.kafkaMonitor.id;
        this.form.desc = this.kafkaMonitor.description;
        //查询邮件列表
        this.form.emails = [];
        let that = this;
        if (this.form.emailAlter) {
          getKafkaMonitorEmails(this.kafkaMonitor.id).then(response => {
            if (response) {
              let data = response.data;
              if (data.code === 200) {
                data = data.data;
                for (let i = 0; i < data.length; i++) {
                  that.form.emails.push(data[i].email);
                }
              }
            }
          })
        }
      },
      handleClose() {
        this.resetForm('form');
        this.$store.state.UpdateKafkaMonitorDialogShow = false;
      },
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let address = this.form.server;
            let topic = this.form.topic;
            let group = this.form.group;
            let lag = this.form.lag;
            let emails = this.form.emails;
            let isEmail = this.form.emailAlter;
            let desc = this.form.desc;
            let id = this.form.id;
            let isDeleted = false;
            saveKafkaMonitor(id, address, topic, group, lag, emails, isEmail, isDeleted, desc).then(response => {
              if (response) {
                let data = response.data;
                let code = data.code;
                data = data.data;
                if (code === 200) {
                  this.$message({
                    //消息弹窗组件,类似toast
                    showClose: true,
                    message: '监控修改成功',
                    type: 'success'
                  });
                  //刷新数据
                  this.$store.dispatch('getKafkaMonitorList');
                  //关闭窗口
                  this.$store.state.UpdateKafkaMonitorDialogShow = false;
                } else {
                  this.$message({
                    //消息弹窗组件,类似toast
                    showClose: true,
                    message: '监控修改失败：' + JSON.stringify(response),
                    type: 'error'
                  });
                }
              }
            })
          }
        })
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
    },
    computed: {
      emailsData() {
        return this.$store.state.emails;
      }
    }
  }
</script>

<style scoped>

</style>
