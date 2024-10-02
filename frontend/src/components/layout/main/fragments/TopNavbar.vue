<template>
  <nav class="navbar navbar-main navbar-expand-lg px-0 mx-4 shadow-none border-radius-xl" id="navbarBlur" data-scroll="true">
    <div class="container-fluid py-1 px-3">
      <nav aria-label="breadcrumb">
        <h4 class="font-weight-bolder mb-0">{{ now_page }}</h4>
      </nav>
      <div class="collapse navbar-collapse mt-sm-0 mt-2 me-md-0 me-sm-4" id="navbar">
        <div class="ms-md-auto pe-md-3 d-flex align-items-center">
          <div :class="['input-group input-group-outline', { 'is-focused': isFindIdFocused || findUserId }]">
            <label class="form-label">User ID</label>
            <input 
              v-model="findUserId" 
              type="text" 
              class="form-control"
              @focus="handleFindUserIdFocus"
              @blur="handleFindUserIdBlur"
              @keyup.enter="findUserProfile"
            >
          </div>
        </div>
        <ul class="navbar-nav  justify-content-end">
          <li class="nav-item d-flex align-items-center">
            <a @click="findUserProfile" class="btn btn-outline-primary btn-sm mb-0 me-3">Find User</a>
          </li>
          <li class="nav-item d-xl-none ps-3 d-flex align-items-center">
            <a href="javascript:;" class="nav-link text-body p-0" id="iconNavbarSidenav">
              <div class="sidenav-toggler-inner">
                <i class="sidenav-toggler-line"></i>
                <i class="sidenav-toggler-line"></i>
                <i class="sidenav-toggler-line"></i>
              </div>
            </a>
          </li>
          <li class="nav-item px-3 d-flex align-items-center">
            <a href="javascript:;" class="nav-link text-body p-0">
              <i class="fa fa-cog fixed-plugin-button-nav cursor-pointer"></i>
            </a>
          </li>
          <li class="nav-item dropdown pe-2 d-flex align-items-center">
            <a @click="useNavAlarmStore.getTopNavAlarmList"
              class="nav-link text-body text-center d-flex align-items-center justify-content-center mt-2" 
              id="dropdownMenuButton" data-bs-toggle="dropdown" aria-expanded="false">
              <div class="nav-alarm">
                <i class="material-icons cursor-pointer">notifications</i>
                <span v-if="alarmCount !== 0" class="position-absolute top-15 start-60 translate-middle badge rounded-pill bg-danger border border-white small py-1 px-2">
                  <span class="small">{{ alarmCount }}</span>
                  <span class="visually-hidden">unread notifications</span>
                </span>
              </div>
            </a>
            <ul class="dropdown-menu dropdown-menu-end px-2 py-3 " aria-labelledby="dropdownMenuButton">
              <li v-if="alarmCount === 0" class="mb-2">
                <a class="dropdown-item border-radius-md">
                  <div class="d-flex py-1">
                    <div class="d-flex flex-column justify-content-center">
                      <h6 class="text-sm font-weight-normal mb-1">
                        <span class="font-weight-bold">새로운 알림이 없습니다.</span>
                      </h6>
                    </div>
                  </div>
                </a>
              </li>
              <li v-else v-for="alarm in navAlarmList" class="mb-2 alarm-list">
                <a class="dropdown-item border-radius-md" href="javascript:;">
                  <div v-if="alarm.alarmType === 'NEW_COMMENT_ON_POST' && alarm.alarmCount != 0" class="d-flex py-1">
                    <div class="d-flex flex-column justify-content-center">
                      <router-link to="/main/alarm">
                        <h6  class="text-sm font-weight-normal mb-1">
                          <span class="font-weight-bold">질문 게시글에 새로운 댓글이  {{ alarm.alarmCount }} 개 달렸습니다.</span>
                        </h6>
                      </router-link>
                    </div>
                  </div>
                  <div v-if="alarm.alarmType === 'NEW_LIKE_ON_POST' && alarm.alarmCount != 0" class="d-flex py-1">
                    <div class="d-flex flex-column justify-content-center">
                      <router-link to="/main/alarm">
                        <h6 class="text-sm font-weight-normal mb-1">
                          <span class="font-weight-bold">질문 게시글에 좋아요가 {{ alarm.alarmCount }} 개 달렸습니다.</span>
                        </h6>
                      </router-link>
                    </div>
                  </div>
                  <div v-if="alarm.alarmType === 'NEW_COMMENT_ON_STUDY_GROUP_POST' && alarm.alarmCount != 0" class="d-flex py-1">
                    <div class="d-flex flex-column justify-content-center">
                      <router-link to="/main/alarm">
                        <h6 class="text-sm font-weight-normal mb-1">
                          <span class="font-weight-bold">스터디 그룹 게시글에 댓글이 {{ alarm.alarmCount }} 개 달렸습니다.</span>
                        </h6>
                      </router-link>
                    </div>
                  </div>
                  <div v-if="alarm.alarmType === 'INVITE_STUDY_GROUP' && alarm.alarmCount != 0" class="d-flex py-1">
                    <div class="d-flex flex-column justify-content-center">
                      <router-link to="/main/alarm">
                        <h6 lass="text-sm font-weight-normal mb-1">
                          <span class="font-weight-bold">스터디 그룹 초대가 왔습니다.</span>
                        </h6>
                      </router-link>
                    </div>
                  </div>
                  <div v-if="alarm.alarmType === 'NEW_CHAT_MESSAGE' && alarm.alarmCount != 0" class="d-flex py-1">
                    <div class="d-flex flex-column justify-content-center">
                      <router-link to="/main/alarm">
                        <h6 class="text-sm font-weight-normal mb-1">
                          <span class="font-weight-bold">새로운 채팅이 {{ alarm.alarmCount }} 개 있습니다.</span>  
                        </h6>
                      </router-link>
                    </div>
                  </div>
                  <div v-if="alarm.alarmType === 'CHANGE_LEADER' && alarm.alarmCount != 0" class="d-flex py-1">
                    <div class="d-flex flex-column justify-content-center">
                      <router-link to="/main/alarm">
                        <h6 v-if="alarm.alarmType === 'CHANGE_LEADER' && alarm.alarmCount != 0" class="text-sm font-weight-normal mb-1">
                          <span class="font-weight-bold">리더가 당신으로 변경되었습니다.</span>
                        </h6>
                      </router-link>
                    </div>
                  </div>
                </a>
              </li>
            </ul>
          </li>
          <li class="nav-item d-flex align-items-center">
            <router-link :to="`/main/profile/${userId}`" class="nav-link text-body font-weight-bold px-0">
              <div class="text-center me-2 d-flex align-items-center justify-content-center">
                <i class="material-icons opacity-10 me-2">account_circle</i>
                <span class="d-sm-inline d-none">{{ nickname }}</span>
              </div>
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>
<script setup>
  import apiClient from '../../../../config/authConfig.js';
  import { useRouter } from 'vue-router';
  import { useIndexStore } from '../../../../store/IndexStore.js';
  import { useAuthStore } from '../../../../store/authStore.js';
  import { useNavAlarmStore } from '../../../../store/navAlarmStore.js';
  import { computed, ref, onMounted } from 'vue';

  const router = useRouter();

  const indexStore = useIndexStore();
  const now_page = computed(() => indexStore.now_page);

  const authStore = useAuthStore();
  const nickname = computed(() => authStore.nickname);
  const userId = computed(() => authStore.userId);

  const navAlarmStore = useNavAlarmStore();
  const navAlarmList = computed(() => navAlarmStore.navAlarmList);
  const alarmCount = computed(() => navAlarmStore.alarmCount);

  const findUserId = ref('');
  const isFindIdFocused = ref(false);

  const findUserProfile = async () => {
    if (!validateFindUser()) {
      return;
    }

    try {
      const response = await apiClient.get(`/main/check/${findUserId.value}`);
      
      console.log(response.data.result);
      console.log(userId.value);
      
      if (findUserId.value !== userId.value) {
        router.push(`/main/profile/user/${findUserId.value}`);
      } else {
        router.push(`/main/profile/${findUserId.value}`);
      }

      findUserId.value = '';
    } catch (error) {
      console.log('에러가 발생했습니다.', error);
      if (error.response.data.resultCode == 'USER_NOT_FOUND') {
        alert('유저를 찾지 못했습니다.');
      } else {
        alert('유저를 찾는데 에러가 발생했습니다.');
      }
    }
  };

  const validateFindUser = () => {
    if (!findUserId.value) {
      alert('찾는 유저의 아이디가 입력되지 않았습니다.');
      return false;
    }

    return true;
  };

  const handleFindUserIdFocus = () => {
    isFindIdFocused.value = true;
  };

  const handleFindUserIdBlur = () => {
    isFindIdFocused.value = false;
  };

  onMounted(() => {
    navAlarmStore.getTopNavAlarmList();
    console.log('component : ', navAlarmList.value);
    console.log('component : ', alarmCount.value);
  });
</script>
<style scoped>
  .dropdown-menu::before {
    content: none;
  }
  .alarm-list {
    width: 320px;
  }
</style>