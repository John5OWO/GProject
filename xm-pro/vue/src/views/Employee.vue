<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input style="width: 240px" v-model="data.name" placeholder="请输入名称查询" prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary">新增</el-button>
      <el-button type="warning">批量删除</el-button>
<!--      <el-button type="danger">导入</el-button>-->
<!--      <el-button type="info">导出</el-button>-->
    </div>
    <div class="card" style="margin-bottom: 15px">
      <el-table :data="data.tableData" stripe>
        <el-table-column label="名称" prop="name"/>
        <el-table-column label="性别" prop="sex"/>
        <el-table-column label="年龄" prop="age"/>
        <el-table-column label="个人介绍" prop="description" show-overflow-tooltip/>
        <el-table-column label="部门" prop="departmentName"/>
      </el-table>
    </div>
    <div style="margin-bottom: 15px">
      <el-pagination
          @size-change = "load"
          @current-change = "load"
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
import request from "@/utils/request.js";

const  data = reactive({
  name:null,
  pageNum: 1,
  pageSize: 10,
  total: 0,
  tableData:[],
})

const load = ()=>{
  request.get('/employee/selectPage',{
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      name:data.name
    }
  }).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
  })
}
load()

const reset = () => {
  data.name = null
  load()
}
</script>