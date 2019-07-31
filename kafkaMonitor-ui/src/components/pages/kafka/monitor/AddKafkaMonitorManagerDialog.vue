<template>
  <div>
    <el-dialog title="添加服务"
               :visible.sync="AddKafkaMonitorManagerDialogShow"
               width="40%">
      <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="服务地址" prop="address">
          <el-row :gutter="10">
            <el-col :span="15">
              <el-input v-model="ruleForm.address" autocomplete="off" placeholder="http://ip:port"></el-input>
            </el-col>
            <el-col :span="5">
              <el-button @click="checkAddress(ruleForm.address)">连接测试</el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="服务名称" prop="name">
          <el-col :span="15">
            <el-input v-model="ruleForm.name" autocomplete="off"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
          <el-button @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
  import {getTopicList} from '../../../../api/kafkaMonitorApi'
  import {saveKafkaMonitorManager} from '../../../../api/kafkaMonitorApi'

  export default {
    data() {
      let checkName = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('请输入名称'));
        }
        callback();
      };
      let checkAddress = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入服务地址'));
        } else {
          //检测地址是否可用
          let regx = new RegExp("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
          if (!regx.test(value)) {
            callback(new Error('服务地址非法(示例:http://10.80.0.73:9002)'));
          }
        }
        callback();
      };
      return {
        ruleForm: {
          address: '',
          name: ''
        },
        rules: {
          address: [
            {validator: checkAddress, trigger: 'blur'}
          ],
          name: [
            {validator: checkName, trigger: 'blur'}
          ]
        },

      };
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
            if (valid) {
              saveKafkaMonitorManager(null, this.ruleForm.address, this.ruleForm.name, null).then(response => {
                if (response) {
                  let data = response.data;
                  if (data.code === 200) {
                    this.$message({
                      //消息弹窗组件,类似toast
                      showClose: true,
                      message: '服务添加成功',
                      type: 'success'
                    });
                    //刷新列表
                    this.$store.dispatch('getKafkaMonitorManagerList');
                    //关闭窗口
                    this.AddKafkaMonitorManagerDialogShow = false;
                  } else {
                    this.$message({
                      //消息弹窗组件,类似toast
                      showClose: true,
                      message: '服务添加失败:' + response.data,
                      type: 'error'
                    });
                  }
                }
              })
            } else {
              console.log('error submit!!');
              return false;
            }
          }
        );
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      getTopicList(address) {
        getTopicList(address).then(response => {
          if (response) {
            let code = response.data.code;
            if (code !== 200) {
              this.$notify.error({
                title: '服务不可用',
                message: response.data.msg
              });
            } else {
              this.$notify({
                title: '连接成功',
                type: 'success'
              });
            }
          }
        })
      },
      checkAddress(address) {
        this.$refs['ruleForm'].validate((valid) => {
          if (valid) {
            this.getTopicList(address);
          }
        })
      }
    },
    computed: {
      AddKafkaMonitorManagerDialogShow: {
        get() {
          return this.$store.state.AddKafkaMonitorManagerDialogShow;
        },
        set(val) {
          this.$store.state.AddKafkaMonitorManagerDialogShow = val;
        }
      }
    },
    watch: {
      AddKafkaMonitorManagerDialogShow() {
        if (!this.AddKafkaMonitorManagerDialogShow) {
          this.resetForm('ruleForm');
        }
      }
    }
  }
</script>
