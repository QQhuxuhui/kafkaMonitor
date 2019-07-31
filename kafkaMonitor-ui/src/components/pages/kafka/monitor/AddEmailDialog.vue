<template>
  <div>
    <el-dialog title="添加邮箱"
               :visible.sync="AddEmailDialogShow"
               width="28%">
      <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="Email" prop="email">
          <el-col :span="18">
            <el-input v-model="ruleForm.email" autocomplete="off"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-col :span="18">
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
  import {saveEmailAddress} from '../../../../api/kafkaMonitorApi'

  export default {
    data() {
      let checkName = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('名称不能为空'));
        }
        callback();
      };
      let validateEmail = (rule, value, callback) => {
        let reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
        if (value === '') {
          callback(new Error('请输入邮箱地址'));
        } else {
          if (!reg.test(value)) {
            callback(new Error('邮箱地址不合法'));
          } else {
            this.$refs.ruleForm.validateField('checkName');
            callback();
          }
        }
      };
      return {
        ruleForm: {
          email: '',
          name: ''
        },
        rules: {
          email: [
            {validator: validateEmail, trigger: 'blur'}
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
              saveEmailAddress(this.ruleForm.email, this.ruleForm.name).then(response => {
                let data = response.data;
                if (data.code === 200) {
                  this.$message({
                    //消息弹窗组件,类似toast
                    showClose: true,
                    message: '邮箱添加成功',
                    type: 'success'
                  });
                  this.$store.dispatch('getEmailAddressList');
                  //关闭窗口
                  this.AddEmailDialogShow = false;
                } else {
                  this.$message({
                    //消息弹窗组件,类似toast
                    showClose: true,
                    message: '邮箱添加失败:' + JSON.stringify(response),
                    type: 'error'
                  });
                }
              });
            } else {
              console.log('error submit!!');
              return false;
            }
          }
        );
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    },
    computed: {
      AddEmailDialogShow: {
        get() {
          return this.$store.state.AddEmailDialogShow;
        },
        set(val) {
          this.$store.state.AddEmailDialogShow = val;
        }
      }
    },
    watch: {
      AddEmailDialogShow() {
        if (!this.AddEmailDialogShow) {
          this.resetForm('ruleForm');
        }
      }
    }
  }
</script>
