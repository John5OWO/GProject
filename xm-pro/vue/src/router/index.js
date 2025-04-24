import { createRouter, createWebHistory } from 'vue-router'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {path: '/', redirect: '/manager'},
    {path: '/manager', component: () => import('../views/Manager.vue'),children: [
        {path: 'home',meta:{title:'主页'},component: () => import('../views/Home.vue')},
        {path: 'test',meta:{title:'测试页面'},component: () => import('../views/Test.vue')},
        {path: 'data',meta:{title:'数据展示页面'},component: () => import('../views/Data.vue')},
        {path: 'employee',meta:{title:'员工信息'},component: () => import('../views/Employee.vue')},
        {path: 'recognition',meta:{title:'监控识别'},component: () => import('../views/Recognition.vue')},
        ]},
  ],
})

export default router
