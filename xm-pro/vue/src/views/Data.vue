<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input style="width: 240px" v-model="data.name" placeholder="请输入名称查询" prefix-icon="Search"></el-input>
      <el-button type="primary">查询</el-button>
      <el-button type="warning">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary">新增</el-button>
      <el-button type="warning">批量删除</el-button>
      <el-button type="danger">导入</el-button>
      <el-button type="info">导出</el-button>
    </div>
    <div class="card" style="margin-bottom: 15px">
      <el-table :data="data.tableData" stripe>
        <el-table-column label="日期" prop="data"/>
        <el-table-column label="名称" prop="name"/>
        <el-table-column label="地址" prop="address"/>
      </el-table>
    </div>
    <div style="margin-bottom: 15px">
      <el-pagination
          v-model:current-page="data.currentPage"
          v-model:page-size="data.pageSize"
          :page-sizes="[5, 10, 15, 20]"
          :background="background"
          layout="total, sizes, prev, pager, next, jumper"
          :total="data.total"
      />
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import {Search} from "@element-plus/icons-vue";

const  data = reactive({
  name:null,
  currentPage: 1,
  pageSize: 10,
  total: 36,
  tableData:[
    {id: 1,data:'2024-04-14',name:' 用户1',address:'广东汕尾'},
    {id: 2,data:'2024-04-15',name:' 用户2',address:'广东深圳'},
    {id: 3,data:'2024-04-17',name:' 用户3',address:'广东广州'},
  ],
})
</script>