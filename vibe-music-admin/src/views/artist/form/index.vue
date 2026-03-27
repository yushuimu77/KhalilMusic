<script setup lang="ts">
import { ref, onBeforeUnmount, h } from "vue";
import ReCol from "@/components/ReCol";
import { formRules } from "../utils/rule";
import { FormProps } from "../utils/types";
import { message } from "@/utils/message";
import userAvatar from "@/assets/user.jpg";
import { addDialog } from "@/components/ReDialog";
import ReCropperPreview from "@/components/ReCropperPreview";
import { deviceDetection } from "@pureadmin/utils";

const props = withDefaults(defineProps<FormProps>(), {
  formInline: () => ({
    title: "新增",
    artistId: 0,
    artistName: "",
    gender: 0,
    birth: null,
    area: "",
    introduction: "",
    avatar: ""
  })
});

const genderOptions = [
  {
    value: 0,
    label: "男歌手"
  },
  {
    value: 1,
    label: "女歌手"
  },
  {
    value: 2,
    label: "组合/乐队"
  }
];

const ruleFormRef = ref();
const newFormInline = ref(props.formInline);

const avatarFile = ref<File | null>(null);
const avatarPreviewUrl = ref("");
const avatarCropInfo = ref();

function clearAvatarPreview() {
  if (avatarPreviewUrl.value) {
    URL.revokeObjectURL(avatarPreviewUrl.value);
  }
  avatarPreviewUrl.value = "";
}

function validateAvatarFile(file: File) {
  const isPngOrJpg =
    file.type === "image/png" ||
    file.type === "image/jpeg" ||
    /\.(png|jpe?g)$/i.test(file.name);
  if (!isPngOrJpg) {
    message("仅支持上传 jpg/png 格式的头像图片", { type: "warning" });
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

function handleAvatarChange(uploadFile: any) {
  const raw = uploadFile?.raw as File | undefined;
  if (!raw) return;
  if (!validateAvatarFile(raw)) {
    avatarFile.value = null;
    clearAvatarPreview();
    return;
  }

  const reader = new FileReader();
  reader.readAsDataURL(raw);
  reader.onload = e => {
    const imgSrc = e.target?.result as string;
    addDialog({
      title: "裁剪头像",
      width: "40%",
      closeOnClickModal: false,
      fullscreen: deviceDetection(),
      contentRenderer: () =>
        h(ReCropperPreview, {
          ref: cropRef,
          imgSrc: imgSrc,
          onCropper: info => (avatarCropInfo.value = info)
        }),
      beforeSure: async done => {
        if (!avatarCropInfo.value?.blob) {
          message("图片裁剪失败，请重试", { type: "error" });
          return;
        }
        if (avatarCropInfo.value.blob.size > 2 * 1024 * 1024) {
          message("裁剪后的头像图片大小不能超过 2MB", { type: "warning" });
          return;
        }

        avatarFile.value = avatarCropInfo.value.blob;
        clearAvatarPreview();
        avatarPreviewUrl.value = avatarCropInfo.value.base64;
        done();
      },
      closeCallBack: () => cropRef.value.hidePopover()
    });
  };
}

function handleAvatarRemove() {
  avatarFile.value = null;
  clearAvatarPreview();
}

function getRef() {
  return ruleFormRef.value;
}

function getAvatarFile() {
  return avatarFile.value;
}

function resetAvatar() {
  avatarFile.value = null;
  clearAvatarPreview();
}

onBeforeUnmount(() => {
  clearAvatarPreview();
});

defineExpose({ getRef, getAvatarFile, resetAvatar });
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
        v-if="newFormInline.title === '修改'"
        :value="12"
        :xs="24"
        :sm="24"
      >
        <el-form-item label="歌手编号" prop="artistId">
          <el-input
            v-model="newFormInline.artistId"
            disabled
            placeholder="newFormInline.artistId"
          />
        </el-form-item>
      </re-col>

      <re-col :value="12" :xs="24" :sm="24">
        <el-form-item label="歌手" prop="artistName" required>
          <el-input
            v-model="newFormInline.artistName"
            clearable
            placeholder="请输入歌手"
          />
        </el-form-item>
      </re-col>

      <re-col :value="12" :xs="24" :sm="24">
        <el-form-item label="类型">
          <el-select
            v-model="newFormInline.gender"
            placeholder="请选择类型"
            class="w-full"
            clearable
          >
            <el-option
              v-for="(item, index) in genderOptions"
              :key="index"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
      </re-col>

      <re-col :value="12" :xs="24" :sm="24">
        <el-form-item label="生日" prop="birth">
          <el-date-picker
            v-model="newFormInline.birth"
            type="date"
            placeholder="请选择生日"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
      </re-col>

      <re-col :value="12" :xs="24" :sm="24">
        <el-form-item label="国籍" prop="area">
          <el-input
            v-model="newFormInline.area"
            clearable
            placeholder="请输入国籍"
          />
        </el-form-item>
      </re-col>

      <re-col :value="12" :xs="24" :sm="24">
        <el-form-item label="头像">
          <div class="flex items-start gap-3">
            <div class="flex flex-col gap-2">
              <el-upload
                :auto-upload="false"
                :show-file-list="false"
                accept="image/png,image/jpeg"
                :on-change="handleAvatarChange"
                :on-remove="handleAvatarRemove"
              >
                <el-button type="primary">选择头像</el-button>
              </el-upload>
              <el-button v-if="avatarFile" @click="resetAvatar">移除头像</el-button>
              <span class="text-xs text-[var(--el-text-color-secondary)]">
                支持 jpg/png，2MB 以内
              </span>
            </div>
            <el-image
              fit="cover"
              preview-teleported
              :src="avatarPreviewUrl || newFormInline.avatar || userAvatar"
              :preview-src-list="
                Array.of(avatarPreviewUrl || newFormInline.avatar || userAvatar)
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
