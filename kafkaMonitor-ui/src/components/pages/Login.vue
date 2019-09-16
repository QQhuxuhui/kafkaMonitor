<template>
  <div class="login">
    <div class="login-wrap" v-show="loginShow">
      <div class="login-title">后台管理系统</div>
      <el-form :model="form" :rules="rules" ref="form" label-width="0px" class="login-content">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" @keyup.enter.native="submitForm('form')">
            <template slot="prepend">帐号</template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input show-password v-model="form.password" placeholder="请输入密码" @keyup.enter.native="submitForm('form')">
            <template slot="prepend">密码</template>
          </el-input>
        </el-form-item>
        <el-form-item class="login-btn">
          <el-row>
            <el-button type="primary" @click="submitForm('form')">登录</el-button>
          </el-row>
          <el-row>
            <el-button type="primary" @click="handleRegisterClick()">注册</el-button>
          </el-row>
        </el-form-item>
      </el-form>
    </div>
    <!--注册-->
    <div class="register-wrap" v-show="!loginShow">
      <div class="login-title">用户注册</div>
      <el-form :model="registerForm" :rules="registerRules" ref="registerForm" label-width="0px" class="login-content">
        <el-form-item prop="name">
          <el-input v-model="registerForm.name" placeholder="请输入用户昵称">
            <template slot="prepend">用户昵称</template>
          </el-input>
        </el-form-item>
        <el-form-item prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入登录账号">
            <template slot="prepend">登录帐号</template>
          </el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="registerForm.email" placeholder="请输入登录账号">
            <template slot="prepend">注册邮箱</template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input show-password v-model="registerForm.password" placeholder="请输入密码">
            <template slot="prepend">登录密码</template>
          </el-input>
        </el-form-item>
        <el-form-item prop="rePassword">
          <el-input show-password v-model="registerForm.rePassword" placeholder="请输入密码">
            <template slot="prepend">密码确认</template>
          </el-input>
        </el-form-item>
        <el-form-item class="login-btn">
          <el-row>
            <el-button type="primary" @click="submitForm('registerForm')">注册</el-button>
          </el-row>
          <el-row>
            <el-button type="primary" @click="handleRegisterClick()">返回登录</el-button>
          </el-row>
        </el-form-item>
      </el-form>
    </div>


  </div>
</template>
<script>

  import {login, register, active} from '../../api/UserApi'
  import {getCookie, setCookie, delCookie} from '../../util/cookieUtil'

  export default {
    name: "Login",
    data() {
      let validateEmail = (rule, value, callback) => {
        let reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
        if (value === '') {
          callback(new Error('请输入邮箱地址'));
        } else {
          if (!reg.test(value)) {
            callback(new Error('邮箱地址不合法'));
          } else {
            // this.$refs.ruleForm.validateField('checkName');
            callback();
          }
        }
      };
      return {
        form: {
          username: '',
          password: '',
          tips: ''
        },
        rules: {
          username: [
            {required: true, message: '请输入用户名', trigger: 'blur'}
          ],
          password: [
            {required: true, message: '请输入密码', trigger: 'blur'}
          ]
        },
        registerRules: {
          name: [
            {required: true, message: '请输入昵称', trigger: 'blur'}
          ],
          username: [
            {required: true, message: '请输入账号', trigger: 'blur'}
          ],
          password: [
            {required: true, message: '请输入密码', trigger: 'blur'}
          ],
          rePassword: [
            {required: true, message: '请再次输入密码', trigger: 'blur'}
          ],
          email: [
            {validator: validateEmail, trigger: 'blur'}
          ]
        },
        registerForm: {
          name: '',
          username: '',
          password: '',
          rePassword: '',//密码确认
          email: '',
          tips: ''
        },
        loginShow: true
      }
    },
    methods: {
      submitForm(formName) {
        if ('registerForm' === formName) {
          let that = this;
          this.$refs[formName].validate((valid) => {
            if (valid) {
              //注册
              if (that.registerForm.password !== that.registerForm.rePassword) {
                this.$message({
                  //消息弹窗组件,类似toast
                  showClose: true,
                  message: '密码输入不一致',
                  type: 'error'
                });
                return;
              }
              //注册
              register(this.registerForm.name, this.registerForm.username, this.registerForm.password, this.registerForm.email).then(response => {
                let data = response.data;
                if (data.code === 200) {
                  let userInfo = data.data;
                  console.info(userInfo);
                  //昵称
                  this.$store.state.name = userInfo.name;
                  //账号
                  this.$store.state.username = userInfo.username;
                  // this.$router.push('/');
                  this.$message({
                    //消息弹窗组件,类似toast
                    showClose: true,
                    message: data.msg,
                    type: 'success'
                  });
                  //发送激活邮件
                  active(userInfo.username, userInfo.password).then(response => {
                    let data = response.data;
                    if (data.code === 200) {
                      this.$message({
                        //消息弹窗组件,类似toast
                        showClose: true,
                        message: '激活邮件发送成功，请到邮箱中激活账号',
                        type: 'success'
                      });
                      //this.$router.push('/login');
                      this.loginShow=true
                    } else {
                      this.$message({
                        //消息弹窗组件,类似toast
                        showClose: true,
                        message: data.msg,
                        type: 'error'
                      });
                    }
                  })
                } else {
                  this.$message({
                    //消息弹窗组件,类似toast
                    showClose: true,
                    message: data.msg,
                    type: 'error'
                  });
                }
              });

            } else {
              console.log('error submit!!');
              return false;
            }
          });
        }
        if ('form' === formName) {
          this.$refs[formName].validate((valid) => {
            if (valid) {
              // window.localStorage.setItem('loginUserBaseInfo',this.form.username);
              login(this.form.username, this.form.password).then(response => {
                let data = response.data;
                if (data.code === 200) {
                  let userInfo = data.data;
                  // console.info(userInfo);
                  setCookie('token', userInfo.token);
                  //昵称
                  this.$store.state.name = userInfo.name;
                  //账号
                  this.$store.state.username = userInfo.username;
                  this.$router.push('/');
                } else {
                  this.$message({
                    //消息弹窗组件,类似toast
                    showClose: true,
                    message: data.msg,
                    type: 'error'
                  });
                }
              });
            } else {
              console.log('error submit!!');
              return false;
            }
          });
        }
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      handleRegisterClick() {
        this.loginShow = !this.loginShow;
        this.resetForm('registerForm');
      }
    },
  }
</script>

<style scoped>
  .login {
    position: relative;
    width: 100%;
    height: 100%;
    background-image: url(../../assets/img/login-bg.jpg);
    background-size: 100%;
  }

  .login-wrap {
    position: absolute;
    left: 60%;
    top: 30%;
    width: 25%;
    /*margin:-190px 0 0 -175px;*/
    border-radius: 5px;
    background: white;
    /*background: rgba(255,255,255, 0.3);*/
    overflow: hidden;
  }

  .register-wrap {
    position: absolute;
    left: 60%;
    top: 10%;
    width: 25%;
    /*margin:-190px 0 0 -175px;*/
    border-radius: 5px;
    background: white;
    /*background: rgba(255,255,255, 0.3);*/
    overflow: hidden;
  }

  .login-title {
    width: 100%;
    line-height: 50px;
    text-align: center;
    font-size: 20px;
    /*color: #fff;*/
    color: black;
    border-bottom: 1px solid #ddd;
  }

  .login-content {
    padding: 30px 30px;;
  }

  .login-btn {
    text-align: center;
  }

  .login-btn button {
    width: 100%;
    height: 36px;
    margin-bottom: 10px;
  }

  .login-tips {
    font-size: 12px;
    line-height: 30px;
    color: #fff;
  }
</style>
