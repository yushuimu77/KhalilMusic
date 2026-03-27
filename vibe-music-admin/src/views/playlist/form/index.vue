<script setup lang="ts">
import { ref, onBeforeUnmount, h } from "vue";
import ReCol from "@/components/ReCol";
import { formRules } from "../utils/rule";
import { FormProps } from "../utils/types";
import { message } from "@/utils/message";
import playlistCover from "@/assets/song.jpg";
import { addDialog } from "@/components/ReDialog";
import ReCropperPreview from "@/components/ReCropperPreview";
import { deviceDetection } from "@pureadmin/utils";

const props = withDefaults(defineProps<FormProps>(), {
  formInline: () => ({
    formTitle: "新增",
    playlistId: 0,
    title: "",
    introduction: "",
    style: "",
    coverUrl: ""
  })
});

const styleOptions = [
  "节奏布鲁斯",
  "欧美流行",
  "华语流行",
  "粤语流行",
  "国风流行",
  "韩国流行",
  "日本流行",
  "嘻哈说唱",
  "非洲节拍",
  "原声带",
  "轻音乐",
  "摇滚",
  "朋克",
  "电子",
  "国风",
  "乡村",
  "古典"
];

const ruleFormRef = ref();
const newFormInline = ref(props.formInline);

const coverFile = ref<File | null>(null);
const coverPreviewUrl = ref("");
const coverCropInfo = ref();

function clearCoverPreview() {
  if (coverPreviewUrl.value) {
    URL.revokeObjectURL(coverPreviewUrl.value);
  }
  coverPreviewUrl.value = "";
}

function validateCoverFile(file: File) {
  const isPngOrJpg =
    file.type === "image/png" ||
    file.type === "image/jpeg" ||
    /\.(png|jpe?g)$/i.test(file.name);
  if (!isPngOrJpg) {
    message("仅支持上传 jpg/png 格式的封面图片", { type: "warning" });
    return false;
  }
  const isLt5M = file.size <= 5 * 1024 * 1024;
  if (!isLt5M) {
    message("原始图片大小不能超过 5MB", { type: "warning" });
    return false;
  }
  return true;
}

const cropRef = ref();

function handleCoverChange(uploadFile: any) {
  const raw = uploadFile?.raw as File | undefined;
  if (!raw) return;
  if (!validateCoverFile(raw)) {
    coverFile.value = null;
    clearCoverPreview();
    return;
  }

  const reader = new FileReader();
  reader.readAsDataURL(raw);
  reader.onload = e => {
    const imgSrc = e.target?.result as string;
    addDialog({
      title: "裁剪封面",
      width: "40%",
      closeOnClickModal: false,
      fullscreen: deviceDetection(),
      contentRenderer: () =>
        h(ReCropperPreview, {
          ref: cropRef,
          imgSrc: imgSrc,
          onCropper: info => (coverCropInfo.value = info)
        }),
      beforeSure: async done => {
        if (!coverCropInfo.value?.blob) {
          message("图片裁剪失败，请重试", { type: "error" });
          return;
        }
        if (coverCropInfo.value.blob.size > 2 * 1024 * 1024) {
          message("裁剪后的封面图片大小不能超过 2MB", { type: "warning" });
          return;
        }

        coverFile.value = coverCropInfo.value.blob;
        clearCoverPreview();
        coverPreviewUrl.value = coverCropInfo.value.base64;
        done();
      },
      closeCallBack: () => cropRef.value.hidePopover()
    });
  };
}

function handleCoverRemove() {
  coverFile.value = null;
  clearCoverPreview();
}

function getRef() {
  return ruleFormRef.value;
}

function getCoverFile() {
  return coverFile.value;
}

function resetCover() {
  coverFile.value = null;
  clearCoverPreview();
}

onBeforeUnmount(() => {
  clearCoverPreview();
});

defineExpose({ getRef, getCoverFile, resetCover });
</script>

<template>
  <el-form
    ref="ruleFormRef"
    :model="newFormInline"
    :rules="formRules"
    label-width="82px"
  >
    <el-row :gutter="30">
      <re-col
        v-if="newFormInline.formTitle === '修改'"
        :value="12"
        :xs="24"
        :sm="24"
      >
        <el-form-item label="歌单编号" prop="playlistId">
          <el-input
            v-model="newFormInline.playlistId"
            disabled
            placeholder="newFormInline.playlistId"
          />
        </el-form-item>
      </re-col>

      <re-col :value="12" :xs="24" :sm="24">
        <el-form-item label="标题" prop="title" required>
          <el-input
            v-model="newFormInline.title"
            clearable
            placeholder="请输入标题"
          />
        </el-form-item>
      </re-col>

      <re-col :value="12" :xs="24" :sm="24">
        <el-form-item label="风格">
          <el-select
            v-model="newFormInline.style"
            placeholder="请选择风格"
            class="w-full"
            clearable
          >
            <el-option
              v-for="(item, index) in styleOptions"
              :key="index"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
      </re-col>

      <re-col :value="12" :xs="24" :sm="24">
        <el-form-item label="封面">
          <div class="flex items-start gap-3">
            <div class="flex flex-col gap-2">
              <el-upload
                :auto-upload="false"
                :show-file-list="false"
                accept="image/png,image/jpeg"
                :on-change="handleCoverChange"
                :on-remove="handleCoverRemove"
              >
                <el-button type="primary">选择封面</el-button>
              </el-upload>
              <el-button v-if="coverFile" @click="resetCover">移除封面</el-button>
              <span class="text-xs text-[var(--el-text-color-secondary)]">
                支持 jpg/png，2MB 以内
              </span>
            </div>
            <el-image
              fit="cover"
              preview-teleported
              :src="coverPreviewUrl || newFormInline.coverUrl || playlistCover"
              :preview-src-list="
                Array.of(coverPreviewUrl || newFormInline.coverUrl || playlistCover)
              "
              class="w-[96px] h-[96px] rounded-full border border-[var(--el-border-color)]"
            />
          </div>
        </el-form-item>
      </re-col>

      <re-col>
        <el-form-item label="简介">
          <el-input
            v-model="newFormInline.introduction"
            placeholder="请输入简介"
            type="textarea"
            line-number
            :autosize="{ minRows: 4, maxRows: 10 }"
          />
        </el-form-item>
      </re-col>
    </el-row>
  </el-form>
</template>
