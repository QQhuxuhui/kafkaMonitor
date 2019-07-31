<template>
  <div class="container">

    <AddKafkaMonitorManagerDialog></AddKafkaMonitorManagerDialog>
    <div style="margin: 20px 20px 20px">
      <el-button type="primary" size="mini" @click="showAddKafkaMonitorManagerDialog">添加服务</el-button>
    </div>
    <el-table
      :data="KafkaMonitorManagerList"
      style="width: 100%">
      <el-table-column
        prop="name"
        label="服务名称">
      </el-table-column>
      <el-table-column
        prop="address"
        label="服务地址">
      </el-table-column>
      <el-table-column
        prop="updateDate"
        :formatter="formatUpdateDate"
        label="更新日期">
      </el-table-column>
      <el-table-column
        label="操作">
        <template slot-scope="scope">
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="primary"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleEdit(scope.$index, scope.row)">编辑-->
<!--          </el-button>-->
          <el-button
            size="mini"
            type="danger"
            icon="el-icon-delete"
            @click="handleDelete(scope.$index, scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import AddKafkaMonitorManagerDialog from './AddKafkaMonitorManagerDialog.vue'
  import dateUtil from 'element-ui/src/utils/date';
  import {saveKafkaMonitorManager} from '../../../../api/kafkaMonitorApi'

  export default {
    name: "",
    components: {
      AddKafkaMonitorManagerDialog
    },
    methods: {
      showAddKafkaMonitorManagerDialog() {
        this.$store.state.AddKafkaMonitorManagerDialogShow = true;
      },
      formatUpdateDate: function (row, column) {
        let date = new Date(row.updateDate);
        return dateUtil.format(date, 'yyyy-MM-dd HH:mm:ss');
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
          saveKafkaMonitorManager(row.id, row.address, row.name, true, null).then(response => {
            if (response) {
              let data = response.data;
              if (data.code === 200) {
                this.$message({
                  //消息弹窗组件,类似toast
                  showClose: true,
                  message: '删除成功',
                  type: 'success'
                });
                //刷新列表
                this.$store.dispatch('getKafkaMonitorManagerList');
              } else {
                this.$message({
                  //消息弹窗组件,类似toast
                  showClose: true,
                  message: '删除失败:' + response.data,
                  type: 'error'
                });
              }
            }
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      }
    },
    computed: {
      KafkaMonitorManagerList() {
        return this.$store.state.KafkaMonitorManagerList;
      }
    },
    mounted() {
      this.$store.dispatch('getKafkaMonitorManagerList');
    }
  }
</script>

<style scoped>

</style>
