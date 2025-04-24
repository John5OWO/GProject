<template>

  <div style="margin-bottom: 20px">
    <RouterLink to="test">通过router跳转到Test.vue</RouterLink>
  </div>

  <div>
    <el-button type="primary" @click="router.push('test')">跳转到新页面</el-button>
  </div>

  <div style="margin-bottom: 20px">
    <el-input v-model="data.input" style="width: 240px" placeholder="Please input" :prefix-icon="Search"/>
  </div>

  <div style="margin-bottom: 20px">
    <el-button type="primary" @click="router.push('test?id=1')">传递id=1</el-button>
  </div>

</template>

<script setup>
import {reactive} from "vue";
import { Search } from "@element-plus/icons-vue";
import router from "@/router/index.js";
import request from "@/utils/request.js";

const data = reactive({
  input:null,
  employeeList: []
  })

request.get('/employee/selectAll').then(res => {
  console.log(res)
  data.employeeList = res.data
})

</script>