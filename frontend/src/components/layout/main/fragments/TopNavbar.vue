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
            <a href="javascript:;" 
              class="nav-link text-body text-center d-flex align-items-center justify-content-center" 
              id="dropdownMenuButton" data-bs-toggle="dropdown" aria-expanded="false">
              <i class="material-icons cursor-pointer">notifications</i>
            </a>
            <ul class="dropdown-menu dropdown-menu-end px-2 py-3 " aria-labelledby="dropdownMenuButton">
              <li class="mb-2">
                <a class="dropdown-item border-radius-md" href="javascript:;">
                  <div class="d-flex py-1">
                    <div class="my-auto">
                      <img src="/assets/img/team-2.jpg" class="avatar avatar-sm me-3 ">
                    </div>
                    <div class="d-flex flex-column justify-content-center">
                      <h6 class="text-sm font-weight-normal mb-1">
                        <span class="font-weight-bold">New message</span> from Laur
                      </h6>
                      <p class="text-xs text-secondary mb-0">
                        13 minutes ago
                      </p>
                    </div>
                  </div>
                </a>
              </li>
              <li class="mb-2">
                <a class="dropdown-item border-radius-md" href="javascript:;">
                  <div class="d-flex py-1">
                    <div class="my-auto">
                      <img src="/assets/img/small-logos/logo-spotify.svg" class="avatar avatar-sm bg-gradient-dark  me-3 ">
                    </div>
                    <div class="d-flex flex-column justify-content-center">
                      <h6 class="text-sm font-weight-normal mb-1">
                        <span class="font-weight-bold">New album</span> by Travis Scott
                      </h6>
                      <p class="text-xs text-secondary mb-0">
                        <i class="fa fa-clock me-1"></i>
                        1 day
                      </p>
                    </div>
                  </div>
                </a>
              </li>
              <li>
                <a class="dropdown-item border-radius-md" href="javascript:;">
                  <div class="d-flex py-1">
                    <div class="avatar avatar-sm bg-gradient-secondary  me-3  my-auto">
                      <svg width="12px" height="12px" viewBox="0 0 43 36" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
                        <title>credit-card</title>
                        <g stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
                          <g transform="translate(-2169.000000, -745.000000)" fill="#FFFFFF" fill-rule="nonzero">
                            <g transform="translate(1716.000000, 291.000000)">
                              <g transform="translate(453.000000, 454.000000)">
                                <path class="color-background" d="M43,10.7482083 L43,3.58333333 C43,1.60354167 41.3964583,0 39.4166667,0 L3.58333333,0 C1.60354167,0 0,1.60354167 0,3.58333333 L0,10.7482083 L43,10.7482083 Z" opacity="0.593633743"></path>
                                <path class="color-background" d="M0,16.125 L0,32.25 C0,34.2297917 1.60354167,35.8333333 3.58333333,35.8333333 L39.4166667,35.8333333 C41.3964583,35.8333333 43,34.2297917 43,32.25 L43,16.125 L0,16.125 Z M19.7083333,26.875 L7.16666667,26.875 L7.16666667,23.2916667 L19.7083333,23.2916667 L19.7083333,26.875 Z M35.8333333,26.875 L28.6666667,26.875 L28.6666667,23.2916667 L35.8333333,23.2916667 L35.8333333,26.875 Z"></path>
                              </g>
                            </g>
                          </g>
                        </g>
                      </svg>
                    </div>
                    <div class="d-flex flex-column justify-content-center">
                      <h6 class="text-sm font-weight-normal mb-1">
                        Payment successfully completed
                      </h6>
                      <p class="text-xs text-secondary mb-0">
                        <i class="fa fa-clock me-1"></i>
                        2 days
                      </p>
                    </div>
                  </div>
                </a>
              </li>
            </ul>
          </li>
          <li class="nav-item d-flex align-items-center">
            <a href="#" class="nav-link text-body font-weight-bold px-0">
              <div class="text-center me-2 d-flex align-items-center justify-content-center">
                <i class="material-icons opacity-10 me-2">account_circle</i>
                <span class="d-sm-inline d-none">{{ nickname }}</span>
              </div>
            </a>
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
  import { computed, ref } from 'vue';

  const router = useRouter();
  const indexStore = useIndexStore();
  const now_page = computed(() => indexStore.now_page);

  const authStore = useAuthStore();
  const nickname = computed(() => authStore.nickname);

  const findUserId = ref('');
  const isFindIdFocused = ref(false);

  const findUserProfile = async () => {
    if (!validateFindUser()) {
      return;
    }

    try {
      const response = await apiClient.get(`/main/check/${findUserId.value}`);
      
      console.log(response.data.result);
      
      if (response.data.result) {
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
</script>
<style scoped>
  .dropdown-menu::before {
    content: none;
  }
</style>