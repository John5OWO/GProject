<template>
  <div>
    <!--  头部开始  -->
    <div style="height:60px;background-color:#242f42; display: flex; align-items: center;">
      <div style="width: 200px;display: flex;align-items: center;padding-left: 15px">
        <img style="width: 40px;margin-right: 7px" src="@/assets/skull.png" alt="">
        <span style="font-size: 24px;color: white">入侵监测系统</span>
      </div>
      <div style="flex: 1"></div>
      <div style="width: fit-content;display: flex;align-items: center;padding-right: 25px">
        <img src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" alt="" style="width: 40px;height: 40px;">
        <span style="color: white;margin-left: 5px">辣椒油</span>
      </div>
    </div>
    <!--  头部结束  -->

    <!--  下面部分开始  -->
    <div style="display: flex">
      <!--  左侧导航菜单开始    -->
      <div style="width: 200px;border-right: 1px solid #ddd;min-height:calc(100vh - 60px)">
        <el-menu router :default-active="router.currentRoute.value.path" style="border:0">
          <el-menu-item index="/manager/home">
            <el-icon><House /></el-icon>
            系统首页
          </el-menu-item>
          <el-menu-item index="/manager/data">
            <el-icon><DataAnalysis /></el-icon>
            数据统计
          </el-menu-item>
          <el-menu-item index="/manager/recognition">
            <el-icon><DataAnalysis /></el-icon>
            视频监控
          </el-menu-item>
            <el-sub-menu index="1">
              <template #title>
                <el-icon><User /></el-icon>
                <span>用户管理</span>
              </template>
              <el-menu-item>管理员信息</el-menu-item>
              <el-menu-item index="/manager/employee">员工信息</el-menu-item>
            </el-sub-menu>
          <el-menu-item index="/manager/person">
            <el-icon><UserFilled /></el-icon>
            个人信息
          </el-menu-item>
          <el-menu-item>
            <el-icon><SwitchButton /></el-icon>
            退出登录
          </el-menu-item>
        </el-menu>
      </div>
      <!--  左侧导航菜单结束    -->

      <!--  右侧主体区域开始    -->
      <div style="flex:1;width:0;background-color:#ffffff;padding:10px">
        <RouterView />
      </div>
      <!--  右侧主体区域结束    -->

    </div>
    <!--  下面部分结束  -->
  </div>
</template>

<script setup>
import router from "@/router/index.js";
</script>

<style>
.el-menu .is-active{
  background: #e6ecf7 !important;
}
</style>