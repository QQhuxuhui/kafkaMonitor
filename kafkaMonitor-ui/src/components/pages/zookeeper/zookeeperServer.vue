<template>
  <div class="container">
    <el-row :gutter="20" style="margin: 40px 0 0 0">
      <el-col :span="4">
        <el-button type="primary" size="mini" @click="addZooKeeperDialogShow = true">添加zookeeper</el-button>
      </el-col>
    </el-row>


    <el-table
      v-loading="loading"
      :data="zookeeperTableData.filter(data => !addressSearch || data.address.toLowerCase().includes(addressSearch.toLowerCase()))"
      style="width: 920px">
      <el-table-column
        prop="name"
        label="名称"
        width="200">
      </el-table-column>
      <el-table-column
        prop="address"
        label="zookeeper地址"
        width="200">
      </el-table-column>
      <el-table-column
        sortable
        prop="createDate"
        label="创建日期"
        :formatter="formatCreateDate"
        width="200">
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
            v-model="addressSearch"
            size="mini"
            placeholder="输入关键字搜索"></el-input>
        </template>
        <template slot-scope="scope">
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


    <el-dialog title="添加"
               :visible="addZooKeeperDialogShow"
               width="40%" @open="handleDialogOpen" @close="handleDialogClose">
      <el-form :model="form" status-icon ref="form" :rules="rules" label-width="150px" class="demo-ruleForm">
        <el-form-item label="名称" prop="name">
          <el-row :gutter="10">
            <el-col :span="15">
              <el-input v-model="form.name"></el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="服务地址" prop="address">
          <el-row :gutter="10">
            <el-col :span="15">
              <el-input v-model="form.address" autocomplete="off" placeholder="ip:port"></el-input>
            </el-col>
            <el-col :span="5">
              <el-button @click="checkAddress(form.address)" v-show="!testingConn">连接测试</el-button>
              <el-button disabled v-show="testingConn">正在连接 <i class="el-icon-loading"></i></el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit('form')">确定</el-button>
          <el-button @click="handleDialogClose()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
  import {getZooKeeperServerList, saveZookeeperServer, testConn} from '../../../api/zookeeperApi'
  import dateUtil from 'element-ui/src/utils/date';

  export default {
    name: "zookeeperServer",
    data() {
      let checkName = (rule, value, callback) => {
        if (!value || value === '') {
          return callback(new Error('请输入名称'));
        }
        callback();
      };
      let checkAddress = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入服务地址'));
        } else {
          //检测地址是否可用
          let regx = new RegExp("[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
          if (!regx.test(value)) {
            callback(new Error('服务地址非法(示例:10.80.0.55:2181)'));
          }
        }
        callback();
      };
      return {
        addressSearch: '',
        form: {
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
        addZooKeeperDialogShow: false,
        testingConn: false,
        zookeeperTableData: [],
        loading: false
      }
    },
    methods: {
      formatCreateDate: function (row, column) {
        let date = new Date(row.createDate);
        return dateUtil.format(date, 'yyyy-MM-dd HH:mm:ss');
      },
      onSubmit(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            saveZookeeperServer(this.form.address, this.form.name, false).then(response => {
              if (response) {
                let code = response.data.code;
                console.info(response.data);
                if (code === 200) {
                  this.$message({
                    //消息弹窗组件,类似toast
                    showClose: true,
                    message: '添加成功',
                    type: 'success'
                  });
                  this.handleDialogClose();
                  this.getZooKeeperServerListMethod();
                } else {
                  this.$message({
                    //消息弹窗组件,类似toast
                    showClose: true,
                    message: '添加失败:' + response.data.msg,
                    type: 'error'
                  });
                }
              }
              this.testingConn = false;
            })
          }
        })
      },
      deleteZookeeperMethod(address, name) {
        saveZookeeperServer(address, name, true).then(response => {
          if (response) {
            let code = response.data.code;
            console.info(response.data);
            if (code === 200) {
              this.$message({
                //消息弹窗组件,类似toast
                showClose: true,
                message: '删除成功',
                type: 'success'
              });
              this.getZooKeeperServerListMethod();
            } else {
              this.$message({
                //消息弹窗组件,类似toast
                showClose: true,
                message: '删除失败:' + response.data.msg,
                type: 'error'
              });
            }
          }
          this.testingConn = false;
        })
      },
      getZooKeeperServerListMethod() {
        this.loading = true;
        getZooKeeperServerList().then(response => {
          if (response) {
            let data = response.data;
            if (data.code === 200) {
              data = data.data;
              this.zookeeperTableData = [];
              for (let i = 0; i < data.length; i++) {
                this.zookeeperTableData.push(data[i]);
              }
            }
          }
        });
        this.loading = false;
      },
      handleDialogClose() {
        this.resetForm('form');
        this.addZooKeeperDialogShow = false;
      },
      handleDialogOpen() {
        this.addZooKeeperDialogShow = true;
      },
      handleDelete(index, row) {
        this.$confirm('确定删除?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteZookeeperMethod(row.address, row.name);
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      resetForm(formName) {
        this.$nextTick(() => {
          if (this.$refs[formName] !== undefined) {
            this.$refs[formName].resetFields();
          }
        })
      },
      checkAddress(address) {
        if (address === '') {
          this.$message({
            //消息弹窗组件,类似toast
            showClose: true,
            message: '请输入服务地址',
            type: 'error'
          });
          return;
        } else {
          //检测地址是否可用
          let regx = new RegExp("[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
          if (!regx.test(address)) {
            this.$message({
              //消息弹窗组件,类似toast
              showClose: true,
              message: '服务地址非法(示例:10.80.0.55:2181)',
              type: 'error'
            });
            return;
          }
        }
        this.testingConn = true;
        testConn(address).then(response => {
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
          this.testingConn = false;
        })
      }
    },
    mounted() {
      this.getZooKeeperServerListMethod();
    }
  }
</script>

<style scoped>

</style>
