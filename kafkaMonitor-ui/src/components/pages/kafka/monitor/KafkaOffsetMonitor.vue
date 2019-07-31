<template>
  <div class="container">
    <el-row :gutter="20">
      <div v-show="kafkaMonitorListData && char_groupId!=''">
        <el-col :span="12">
          <div id="spline" style="box-shadow: 0 2px 12px 0 rgba(0,0,0,.1)">
          </div>
        </el-col>
        <el-col :span="12">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>详情</span>
            </div>
            <div class="text item">
              <el-row>
                <el-col :span="5">
                  创建人
                </el-col>
                <el-col :span="4">
                  {{char_createBy}}
                </el-col>
              </el-row>
            </div>
            <div class="text item">
              <el-row>
                <el-col :span="5">
                  服务地址
                </el-col>
                <el-col :span="4">
                  {{char_address}}
                </el-col>
              </el-row>
            </div>
            <div class="text item">
              <el-row>
                <el-col :span="5">
                  topic
                </el-col>
                <el-col :span="4">
                  {{char_topic}}
                </el-col>
              </el-row>
            </div>
            <div class="text item">
              <el-row>
                <el-col :span="5">
                  group
                </el-col>
                <el-col :span="19">
                  {{char_groupId}}
                </el-col>
              </el-row>
            </div>
            <div class="text item">
              <el-row>
                <el-col :span="5">
                  数据总量
                </el-col>
                <el-col :span="4">
                  {{char_logSize}}
                </el-col>
              </el-row>
            </div>
            <div class="text item">
              <el-row>
                <el-col :span="5">
                  已消费量
                </el-col>
                <el-col :span="4">
                  {{char_offset}}
                </el-col>
              </el-row>
            </div>
            <div class="text item">
              <el-row>
                <el-col :span="5">
                  堆积量
                </el-col>
                <el-col :span="4">
                  {{char_lag}}
                </el-col>
              </el-row>
            </div>
            <div class="text item">
              <el-row>
                <el-col :span="5">
                  备注
                </el-col>
                <el-col :span="19">
                  {{char_des}}
                </el-col>
              </el-row>
            </div>
          </el-card>
        </el-col>
      </div>
    </el-row>

    <AddKafkaMonitorDialog></AddKafkaMonitorDialog>

    <UpdateKafkaMonitorDialog :kafkaMonitor="kafkaMonitorComputed"></UpdateKafkaMonitorDialog>

    <!--    <div v-if="this.$store.state.UpdateKafkaMonitorDialogShow">-->
    <!--      asd-->
    <!--    </div>-->
    <!--<el-button type="primary" @click="showEmailDialog">添加邮箱</el-button>-->
    <!--这里需要引入才能展示-->
    <AddEmailDialog></AddEmailDialog>

    <!--列表展示-->
    <el-row :gutter="20" style="margin: 40px 0 0 0">
      <el-col :span="4">
        <el-button type="primary" size="mini" @click="showAddKafkaMonitorDialog">添加监控</el-button>
      </el-col>
      <el-col :span="4" :offset="8">
        <el-input
          v-model="searchGroupId"
          suffix-icon="el-icon-search"
          size="mini"
          placeholder="输入groupId搜索"></el-input>
      </el-col>
      <el-col :span="4">
        <el-input
          v-model="searchCreateBy"
          suffix-icon="el-icon-search"
          size="mini"
          placeholder="输入创建人搜索"></el-input>
      </el-col>
      <el-col :span="4">
        <el-button
          size="mini"
          type="primary"
          plain
          @click="resetSearchTable">重置搜索
        </el-button>
      </el-col>
    </el-row>

    <el-table
      :data="kafkaMonitorListPagedData"
      style="width: 100%"
      :row-class-name="tableRowClassName">
      <el-table-column
        prop="topic"
        label="Topic">
      </el-table-column>
      <el-table-column
        prop="groupId"
        label="Group">
      </el-table-column>
      <el-table-column
        width="100"
        prop="lag"
        label="阈值">
      </el-table-column>
      <el-table-column
        prop="address"
        label="服务地址"
        width="180">
      </el-table-column>
      <el-table-column
        prop="createBy"
        label="创建人"
        width="100">
      </el-table-column>
      <el-table-column
        label="操作"
        width="180">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" content="实时数据" placement="top-start">
            <el-button
              size="mini"
              type="primary"
              plain
              icon="el-icon-view"
              @click="getGroupAndKafkaMonitorMethod(scope.$index, scope.row)">
            </el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="编辑" placement="top-start">
            <el-button
              size="mini"
              type="primary"
              plain
              icon="el-icon-edit"
              @click="handleEdit(scope.$index, scope.row)">
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
    <div class="block" v-show="kafkaMonitorListPagedData && kafkaMonitorListPagedData.length>0">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[5, 10, 20, 50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
  import Highcharts from "highcharts";
  import AddEmailDialog from './AddEmailDialog.vue'
  import AddKafkaMonitorDialog from './AddKafkaMonitorDialog.vue'
  import UpdateKafkaMonitorDialog from './UpdateKafkaMonitorDialog.vue'
  import {
    saveKafkaMonitor,
    getGroupList,
    getGroupOffsetAndLagList,
    getGroupAndKafkaMonitor
  } from '../../../../api/kafkaMonitorApi'

  export default {
    name: "KafkaOffsetMonitor",
    components: {
      AddEmailDialog,
      AddKafkaMonitorDialog,
      UpdateKafkaMonitorDialog
    },
    data() {
      return {
        //存放kafka监控列表，包括过滤后的数据，分页后的数据存放在kafkaMonitorListPagedData
        kafkaMonitorListData: this.$store.state.kafkaMonitorListData,
        kafkaMonitorListPagedData: this.$store.state.kafkaMonitorListData,
        charts: '',
        groupListData: [],
        char_address: '',
        char_topic: '',
        char_groupId: '',
        char_logSize: '',
        char_offset: '',
        char_lag: '',
        char_interval: '',
        char_createBy: '',
        char_des: '',
        searchGroupId: '',
        searchCreateBy: '',
        //分页
        currentPage: 1,
        total: 0,
        pageSize: 20
      }
    },
    mounted() {
      // this.charts = this.char();
      let that = this;
      this.$store.dispatch('getKafkaMonitorList');
      Highcharts.setOptions({
        global: {
          useUTC: false
        },
        lang: {
          months: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
          weekdays: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
        }
      });
      this.charts = Highcharts.chart('spline', {
        chart: {
          type: 'spline',
          marginRight: 10
        },
        title: {
          text: '实时偏移量'
        },
        xAxis: {
          type: 'datetime',
          tickPixelInterval: 150
        },
        yAxis: {
          title: {
            text: '数据堆积量'
          }
        },
        tooltip: {
          /*启用十字准星线*/
          crosshairs: [true, true],
          formatter: function () {
            return '<b>' + this.series.name + '</b><br/>' +
              '<b>' + 'topic:' + that.char_topic + '</b><br/>' +
              '<b>' + 'group:' + that.char_groupId + '</b><br/>' +
              Highcharts.numberFormat(this.y, 0) + '<br/>' +
              Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>';
          }
        },
        legend: {
          enabled: false
        },
        series: [{
          name: '偏移量数据',
          data: (function () {
            // 生成随机值
            let data = [],
              time = (new Date()).getTime(),
              i;
            for (i = -19; i <= 0; i += 1) {
              data.push({
                x: time + i * 3000,
                //y: Math.random()
              });
            }
            return data;
          }()),
          marker: {
            enabled: false
          },
        }]
      });
    },
    methods: {
      tableRowClassName({row, rowIndex}) {
        if (rowIndex === 1) {
          console.info("warning-row");
          return 'warning-row';
        } else if (rowIndex === 3) {
          console.info("success-row");
          return 'success-row';
        }
        return '';
      },
      char(address, topic, groupId) {
        if (!address || !topic || !groupId) {
          return;
        }
        this.char_address = address;
        this.char_topic = topic;
        this.char_groupId = groupId;
        let that = this;
        //初始化定时器、图表数据
        clearInterval(this.char_interval);
        this.clearChartsData();
        this.char_interval = setInterval(function () {
          getGroupAndKafkaMonitor(address, topic, groupId).then(response => {
            if (response) {
              let data = response.data;
              if (data.code === 200) {
                data = data.data;
                let offsets = data.group.offsets[0];
                let modified = offsets.modified;
                let offset = offsets.offset;
                let logSize = offsets.logSize;
                let lag = logSize - offset;
                that.char_logSize = logSize;
                that.char_offset = offset;
                that.char_lag = lag;
                that.char_des = data.kafkaOffsetMonitor.description;
                let x = (new Date()).getTime(), // 当前时间
                  y = lag;          // 随机值
                that.charts.series[0].addPoint([x, y], true, true);
              }
            }
          });
          that.activeLastPointToolip(this.charts);
        }, 3000);
        this.$once('hook:beforeDestroy', () => {
          clearInterval(this.char_interval);
        })
      },
      clearChartsData() {
        this.charts.update({
          series: [{
            data: (function () {
              // 生成随机值
              let data = [],
                time = (new Date()).getTime(),
                i;
              for (i = -19; i <= 0; i += 1) {
                data.push({
                  x: time + i * 1000,
                  //y: Math.random()
                });
              }
              return data;
            }())
          }]
        });
      },
      showAddKafkaMonitorDialog() {
        //刷新数据
        this.$store.dispatch('getKafkaMonitorManagerList');
        this.$store.state.AddKafkaMonitorDialogShow = true;
      },
      handleEdit(index, row) {
        this.$store.state.kafkaMonitorData = row;
        this.$store.state.UpdateKafkaMonitorDialogShow = true;
      },
      handleDelete(index, row) {
        this.$confirm('确定删除?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          //id, address, topic, group, lag, emails, isEmail, isDeleted, desc
          //逻辑删除
          saveKafkaMonitor(row.id, row.address, row.topic, row.groupId, row.lag, null, row.isEmail, true, null).then(response => {
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
                this.$store.dispatch('getKafkaMonitorList');
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
      },
      getGroupAndKafkaMonitorMethod(index, row) {
        this.char_createBy = row.createBy;
        this.char_des = row.description;
        this.char(row.address, row.topic, row.groupId);
      },
      activeLastPointToolip(chart) {
        if (chart) {
          let points = chart.series[0].points;
          chart.tooltip.refresh(points[points.length - 1]);
        }
      },
      //分页
      handleSizeChange(val) {
        this.pageSize = val;
        this.currentPage = 1;
        this.fetchData(1, val);
        // console.log(`每页 ${val} 条`);
      },
      handleCurrentChange(val) {
        this.currentPage = val;
        this.fetchData(val, this.pageSize);
        // console.log(`当前页: ${val}`);
      },
      fetchData(currentPage, pageSize) { //获取数据
        //对kafkaMonitorListData数据进行分页操作
        //开始分页
        let result = [];
        for (let i = (currentPage - 1) * pageSize; i < currentPage * pageSize; i++) {
          //防止越界
          if (i === this.kafkaMonitorListData.length) {
            break;
          }
          result.push(this.kafkaMonitorListData[i]);
        }
        this.kafkaMonitorListPagedData = result;
      },
      //过滤kafkaMonitorListComputed数据
      kafkaMonitorTableGroupIdFilter() {
        return (state) => {
          return (state.groupId.toLowerCase().includes(this.searchGroupId.toLowerCase()));
        };
      },
      //创建人过滤
      kafkaMonitorTableCreateByFilter() {
        return (state) => {
          return (state.createBy.toLowerCase().includes(this.searchCreateBy.toLowerCase()));
        };
      },
      //清空查询条件
      resetSearchTable() {
        this.searchGroupId = '';
        this.searchCreateBy = '';
      }
    },
    computed: {
      kafkaMonitorListComputed() {
        return this.$store.state.kafkaMonitorListData;
      },
      kafkaMonitorComputed: {
        get() {
          return this.$store.state.kafkaMonitorData
        },
        set(val) {
          this.$store.state.kafkaMonitorData = val;
        }
      }
    },
    watch: {
      searchGroupId() {
        let result = this.searchGroupId ? this.kafkaMonitorListComputed.filter(this.kafkaMonitorTableGroupIdFilter()) : this.kafkaMonitorListComputed;
        this.kafkaMonitorListData = result;
      },
      searchCreateBy() {
        let result = this.searchCreateBy ? this.kafkaMonitorListComputed.filter(this.kafkaMonitorTableCreateByFilter()) : this.kafkaMonitorListComputed;
        this.kafkaMonitorListData = result;
      },
      kafkaMonitorListData() {
        if (this.kafkaMonitorListData) {
          this.currentPage = 1;
          this.total = this.kafkaMonitorListData.length;
          this.kafkaMonitorListPagedData = this.kafkaMonitorListData;
          //过滤后的数据变化，重新分页
          this.fetchData(this.currentPage, this.pageSize);
        }
      },
      //data数据动态变化
      kafkaMonitorListComputed() {
        this.kafkaMonitorListData = this.$store.state.kafkaMonitorListData;
        this.resetSearchTable();
      }
    }
  }
</script>

<style scoped>
  .el-table .warning-row {
    background: #78fd77;
  }

  .el-table .success-row {
    background: #f0f9eb;
  }

  .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }

  .box-card {
    width: 100%;
    height: 400px;
  }
</style>
