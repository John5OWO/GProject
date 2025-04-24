<template>
  <div style="background: aqua">
    欢迎123
  </div>

  <div style="font-size: 30px;margin-bottom: 20px">
    {{a}}
    {{b}}
  </div>

  <div style="margin-bottom: 20px">
    {{data.a}}
    {{data.b}}
  </div>

  <div style="margin-bottom: 20px">
    <input v-model = "data.a" />
  </div>

  <div style="margin-bottom: 20px">
    <span style="color: red" v-if ="data.name === '佩奇'">小猪佩奇</span>
    <span style="color: blue" v-if="data.name === '乔治'">小猪乔治</span>
    <span style="color: hotpink" v-else>dddddddddddddd</span>
  </div>

  <div style="display: flex">
    <div style="width:300px;height:300px;text-align:center;line-height:300px;font-size:30px;margin-right:10px;background-color:pink"
         v-for="item in data.arr">{{item}}</div>
  </div>

  <div>
    <select style="width: 200px">
      <option v-for="item in data.fruits">{{item}}</option>
    </select>
  </div>

  <div style="margin-top: 20px;width: 300px;height: 450px;background: pink;display: flex;margin-bottom: 20px">
    <div style="width: 300px;height:50px;background: aqua" v-for="item in data.tag">{{item}}</div>
  </div>

  <div style="margin-bottom: 20px">
    <button @click="click">点击测试</button>
  </div>

  <div style="width:300px;height:300px;background:pink;margin-bottom: 20px">
    <div>
      <img :src="data.img" alt="">
    </div>
  </div>

  <div style="margin:20px">
    <el-button @click="clickData">Default</el-button>
    <el-button type="primary">Primary</el-button>
    <el-button type="success">Success</el-button>
    <el-button type='info'>Info</el-button>
    <el-button type="warning">Warning</el-button>
    <el-button type="danger">Danger</el-button>
  </div>

  <div>
    <el-icon :size="40" :color="color">
      <Edit />
    </el-icon>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import router from "@/router/index.js";

const a = ref(1)
const b = ref(3)

const data = reactive({
  id:router.currentRoute.value.query.id,
  a: "a",
  b: "nba",
  name:"佩奇",
  arr:[7,2,3],
  fruits:['apple','banana','peach'],
  tag:['女装','男装','美妆','球鞋'],
  img:'https://gw.alicdn.com/tfs/TB176rg4VP7gK0jSZFjXXc5aXXa-286-118.png'
})

console.log('获取到的id='+ data.id)

const click =()=>{
  alert("弹窗测试")
}

const clickData=()=>{
  alert("触发了按钮的点击事件")
}

</script>