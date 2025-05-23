<template>
  <div class="page">
    <div class="page_option">
      <div>
        <label>更换目标图片：</label>
        <input
            type="file"
            accept="image/png, image/jpeg"
            @change="fnChangeTarget($event)"
        />
      </div>
      <div>
        <label>摄像头视频媒体：</label>
        <button @click="fnOpen()" :disabled="state.stream !== null">
          启动
        </button>
        &nbsp;
        <button @click="fnClose()">结束</button>
      </div>
      <div>
        <label>前置or后置切换：</label>
        <select v-model="state.constraints.video.facingMode">
          <option value="user">前置</option>
          <option value="environment">后置</option>
        </select>
      </div>
      <div>
        <label>算法模型：</label>
        <select v-model="state.netsType">
          <option value="ssdMobilenetv1">SSD Mobilenet V1</option>
          <option value="tinyFaceDetector">Tiny Face Detector</option>
        </select>
      </div>
    </div>

    <div class="page_load" v-show="state.netsLoadModel">Load Model...</div>
    <div class="page_draw" v-show="!state.netsLoadModel">
      <h3>识别目标图像：</h3>
      <div class="page_draw-target">
        <img id="page_draw-img-target" src="/images/cp/cp01.jpg" />
        <canvas id="page_draw-canvas-target"></canvas>
      </div>
      <h3>识别匹配视频：</h3>
      <div class="page_draw-discern">
        <video
            id="page_draw-video"
            poster="/images/720x480.png"
            src="/videos/test.mp4"
            muted
            playsinline
        ></video>
        <canvas id="page_draw-video-canvas"></canvas>
      </div>
    </div>
  </div>
</template>

<script setup>
import * as faceapi from "@vladmandic/face-api";
import { onMounted, onUnmounted, reactive, watch } from "vue";

/**属性状态 */
const state = reactive({
  /**初始化模型加载 */
  netsLoadModel: true,
  /**算法模型 */
  netsType: "ssdMobilenetv1",
  /**模型参数 */
  netsOptions: {
    ssdMobilenetv1: undefined,
    tinyFaceDetector: undefined,
  },
  /**目标图片数据匹配对象 */
  faceMatcher: {},
  /**目标图片元素 */
  targetImgEl: null,
  /**目标画布图层元素 */
  targetCanvasEl: null,
  /**识别视频元素 */
  discernVideoEl: null,
  /**识别画布图层元素 */
  discernCanvasEl: null,
  /**绘制定时器 */
  timer: 0,
  /**视频媒体参数配置 */
  constraints: {
    audio: false,
    video: {
      /**ideal（应用最理想的） */
      width: {
        min: 320,
        ideal: 720,
        max: 1280,
      },
      height: {
        min: 200,
        ideal: 480,
        max: 720,
      },
      /**frameRate 受限带宽传输时，低帧率可能更适宜 */
      frameRate: {
        min: 7,
        ideal: 15,
        max: 30,
      },
      /**facingMode 摄像头前后切换 */
      facingMode: "environment",
    },
  },
  /**视频流 */
  stream: null,
});

/**初始化模型加载 */
async function fnLoadModel() {
  // 模型文件访问路径
  const modelsPath = `/models`;
  // 面部轮廓模型
  await faceapi.nets.faceLandmark68Net.load(modelsPath);
  // 面部识别模型
  await faceapi.nets.faceRecognitionNet.load(modelsPath);

  // 模型参数-ssdMobilenetv1
  await faceapi.nets.ssdMobilenetv1.load(modelsPath);
  state.netsOptions.ssdMobilenetv1 = new faceapi.SsdMobilenetv1Options({
    minConfidence: 0.5, // 0 ~ 1
    maxResults: 50, // 0 ~ 100
  });
  // 模型参数-tinyFaceDetector
  await faceapi.nets.tinyFaceDetector.load(modelsPath);
  state.netsOptions.tinyFaceDetector = new faceapi.TinyFaceDetectorOptions({
    inputSize: 416, // 160 224 320 416 512 608
    scoreThreshold: 0.5, // 0 ~ 1
  });

  // 输出库版本
  console.log(
      `FaceAPI Version: ${
          faceapi?.version || "(not loaded)"
      } \nTensorFlow/JS Version: ${
          faceapi.tf?.version_core || "(not loaded)"
      } \nBackend: ${
          faceapi.tf?.getBackend() || "(not loaded)"
      } \nModels loaded: ${faceapi.tf.engine().state.numTensors} tensors`
  );

  // 节点元素
  state.targetImgEl = document.getElementById("page_draw-img-target");
  state.targetCanvasEl = document.getElementById("page_draw-canvas-target");
  state.discernVideoEl = document.getElementById("page_draw-video");
  state.discernCanvasEl = document.getElementById("page_draw-video-canvas");

  // 关闭模型加载
  state.netsLoadModel = false;
}

/**根据模型参数识别绘制--目标图 */
async function fnRedrawTarget() {
  console.log("Run Redraw Target");
  const detect = await faceapi
      .detectAllFaces(state.targetImgEl, state.netsOptions[state.netsType])
      // 需引入面部轮廓模型
      .withFaceLandmarks()
      // 需引入面部识别模型
      .withFaceDescriptors();
  if (!detect.length) {
    state.faceMatcher = null;
    return;
  }

  // 原图人脸矩阵结果
  state.faceMatcher = new faceapi.FaceMatcher(detect);

  // 识别图像绘制
  const dims = faceapi.matchDimensions(state.targetCanvasEl, state.targetImgEl);
  const resizedResults = faceapi.resizeResults(detect, dims);
  resizedResults.forEach(({ detection, descriptor }) => {
    const best = state.faceMatcher.findBestMatch(descriptor);
    // 目标原图绘制框
    new faceapi.draw.DrawBox(detection.box, {
      label: best.label,
      boxColor: "#55b881",
    }).draw(state.targetCanvasEl);
  });
}

/**根据模型参数识别绘制 */
async function fnRedrawDiscern() {
  if (!state.faceMatcher) return;
  console.log("Run Redraw Discern");

  // 暂停视频时清除定时
  if (state.discernVideoEl.paused) {
    clearTimeout(state.timer);
    state.timer = 0;
    return;
  }

  // 识别绘制人脸信息
  const detect = await faceapi
      .detectAllFaces(state.discernVideoEl, state.netsOptions[state.netsType])
      // 需引入面部轮廓模型
      .withFaceLandmarks()
      // 需引入面部识别模型
      .withFaceDescriptors();

  // 无识别数据时，清除定时重新再次识别
  if (!detect) {
    clearTimeout(state.timer);
    state.timer = 0;
    fnRedrawDiscern();
    return;
  }
  // 匹配元素大小
  const dims = faceapi.matchDimensions(
      state.discernCanvasEl,
      state.discernVideoEl,
      true
  );
  const result = faceapi.resizeResults(detect, dims);
  result.forEach(({ detection, descriptor }) => {
    // 最佳匹配 distance越小越匹配
    const best = state.faceMatcher.findBestMatch(descriptor);
    // 识别图绘制框
    const label = best.toString();
    new faceapi.draw.DrawBox(detection.box, { label }).draw(
        state.discernCanvasEl
    );
  });

  // 定时器句柄
  state.timer = setTimeout(() => fnRedrawDiscern(), 0);
}

/**启动摄像头视频媒体 */
async function fnOpen() {
  if (state.stream !== null) return;
  try {
    state.stream = {}; // 置为空对象，避免重复点击
    const stream = await navigator.mediaDevices.getUserMedia(state.constraints);
    state.stream = stream;
    state.discernVideoEl.srcObject = stream;
    state.discernVideoEl.play();
    setTimeout(() => fnRedrawDiscern(), 300);
  } catch (error) {
    state.stream = null; // 置为空，可点击
    console.error(error);
    alert("视频媒体流获取错误: " + error);
  }
}

/**结束摄像头视频媒体 */
function fnClose() {
  if (state.stream === null) return;
  state.discernVideoEl.pause();
  state.discernVideoEl.srcObject = null;
  state.stream.getTracks().forEach((track) => track.stop());
  state.stream = null;
  clearTimeout(state.timer);
  state.timer = 0;

  setTimeout(() => {
    // 清空画布
    state.discernCanvasEl
        .getContext("2d")
        .clearRect(
            0,
            0,
            state.discernCanvasEl.width,
            state.discernCanvasEl.height
        );
  }, 500);
}

/**更换图片 */
async function fnChangeTarget(e) {
  if (!state.targetImgEl || !state.targetCanvasEl) return;
  if (!e.target || !e.target.files.length) return;
  // 将文件显示为图像并识别
  const img = await faceapi.bufferToImage(e.target.files[0]);
  state.targetImgEl.src = img.src;
  fnRedrawTarget();
}

// 摄像头前后切换 启用时，关闭后重开
watch(
    () => state.constraints.video.facingMode,
    () => {
      if (state.stream !== null) {
        fnClose();
        fnOpen();
      } else {
        fnClose();
      }
    }
);

onMounted(() => {
  fnLoadModel().then(() => fnRedrawTarget());
});

onUnmounted(() => {
  fnClose();
});
</script>

<style scoped>
:root {
  font-family: Inter, system-ui, Avenir, Helvetica, Arial, sans-serif;
  line-height: 1.5;
  font-weight: 400;

  color-scheme: light dark;
  color: #242424;
  background-color: rgba(255, 255, 255, 0.87);

  font-synthesis: none;
  text-rendering: optimizeLegibility;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

button,
input[type="file"] {
  border: 2px #55b881 solid;
  border-radius: 6px;
  background: #55b881;
  color: white;
}

/**全局布局 */
.layout {
  margin: 0 auto;
  padding: 2rem;
  display: flex;
  flex-direction: row;
}

.layout .layout_nav {
  min-width: 200px;
  border: 2px #55b881 solid;
  border-radius: 6px;
  padding: 10px;
  margin-right: 20px;
}

.layout .layout_nav ul,
.layout .layout_nav li {
  list-style: none;
  margin: 10px 0;
  padding: 0;
  line-height: 30px;
}

.layout .layout_nav a {
  font-weight: bold;
  color: #2c3e50;
}

.layout .layout_nav a.router-link-exact-active {
  color: #55b881;
}

.layout .layout_content {
  flex: 1;
  border: 2px #55b881 solid;
  border-radius: 6px;
  padding: 20px;
}

/*单页面-通用样式 */
.page .page_option {
  padding-bottom: 20px;
}

.page .page_option div {
  padding: 10px;
  border-bottom: 2px #55b881 solid;
}

.page .page_draw-target,
.page .page_draw-discern,
.page .page_draw {
  position: relative;
}

.page .page_draw #page_draw-img,
.page .page_draw #page_draw-video {
  max-width: 720px;
  max-height: 480px;
}

.page .page_draw #page_draw-canvas-target,
.page .page_draw #page_draw-canvas-discern,
.page .page_draw #page_draw-canvas {
  position: absolute;
  top: 0;
  left: 0;
}

.page .page_draw #page_draw-video-canvas {
  position: absolute;
  top: 0;
  left: 0;
  max-width: 720px;
  max-height: 480px;
}

.page .page_draw #page_draw-canvas-box {
  border: 1px #9a9a9a solid;
}
</style>
