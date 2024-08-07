<template>
  <main class="main-content  mt-0">
    <div class="page-header align-items-start min-vh-100" style="background-image: url('https://images.unsplash.com/photo-1497294815431-9365093b7331?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1950&q=80');">
      <span class="mask bg-gradient-dark opacity-6"></span>
      <div class="container my-auto">
        <div class="row">
          <div class="col-lg-4 col-md-8 col-12 mx-auto">
            <div class="card z-index-0 fadeIn3 fadeInBottom">
              <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
                <div class="bg-gradient-primary shadow-primary border-radius-lg py-3 pe-1">
                  <h4 class="text-white font-weight-bolder text-center mt-2 mb-2">Login</h4>
                  <!-- <div class="row mt-3">
                    <div class="col-2 text-center ms-auto">
                      <a class="btn btn-link px-3" href="javascript:;">
                        <i class="fa fa-facebook text-white text-lg"></i>
                      </a>
                    </div>
                    <div class="col-2 text-center px-1">
                      <a class="btn btn-link px-3" href="javascript:;">
                        <i class="fa fa-github text-white text-lg"></i>
                      </a>
                    </div>
                    <div class="col-2 text-center me-auto">
                      <a class="btn btn-link px-3" href="javascript:;">
                        <i class="fa fa-google text-white text-lg"></i>
                      </a>
                    </div>
                  </div> -->
                </div>
              </div>
              <div class="card-body">
                <form @submit.prevent="loginSubmit" role="form" class="text-start">
                  <div class="input-group input-group-outline my-3">
                    <label class="form-label">ID</label>
                    <input id="userId" v-model="userId" type="text" class="form-control">
                  </div>
                  <div class="input-group input-group-outline mb-3">
                    <label class="form-label">Password</label>
                    <input id="password" v-model="password" type="password" class="form-control">
                  </div>
                  <!-- <div class="form-check form-switch d-flex align-items-center mb-3">
                    <input class="form-check-input" type="checkbox" id="rememberMe" checked>
                    <label class="form-check-label mb-0 ms-3" for="rememberMe">Remember me</label>
                  </div> -->
                  <div class="text-center">
                    <button type="submit" class="btn bg-gradient-primary w-100 my-4 mb-2">login</button>
                  </div>
                  <p class="mt-4 text-sm text-center">
                    회원가입이 필요한가요?
                    <router-link to="/regist" class="text-primary text-gradient font-weight-bold">Sign up</router-link>
                  </p>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
      <footer class="footer position-absolute bottom-2 py-2 w-100">
        <div class="container">
          <div class="row align-items-center justify-content-lg-between">
            <div class="col-12 col-md-6 my-auto">
              <div class="copyright text-center text-sm text-muted text-lg-start">
                © 2024
                made with by
                <a href="https://github.com/KHyeon9" class="font-weight-bold text-white" target="_blank">Khyeon9</a>
              </div>
            </div>
            <div class="col-12 col-md-6">
              <ul class="nav nav-footer justify-content-center justify-content-lg-end">
                <li class="nav-item">
                  <a href="https://mrk0607.tistory.com/" class="nav-link text-white" target="_blank">Blog</a>
                </li>
                <li class="nav-item">
                  <a href="https://www.creative-tim.com/license" class="nav-link pe-0 text-white" target="_blank">License</a>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </footer>
    </div>
  </main>
</template>
<script setup>
  import { ref } from 'vue';
  import apiClient from '../../../config/authConfig';
  import { useRouter } from 'vue-router';
  import { useAuthStore } from '../../../store/authStore';

  const userId = ref('');
  const password = ref('');
  const router = useRouter();
  const authStore = useAuthStore();

  const validateForm = () => {

    if (!userId.value) {
      alert('ID가 입력되지 않았습니다.');
      return false;
    } else if (userId.value.length < 4) {
      alert('ID는 최소 4자리 이상이어야 합니다.');
      return false;
    }

    if (!password.value) {
      alert('비밀번호가 입력되지 않았습니다.');
      return false;
    } else if (password.value.length < 6) {
      alert('비밀번호는 최소 6자리 이상이어야 합니다.');
      return false;
    }

    return true;
  };

  const loginSubmit = async () => {
    if (!validateForm()) {
      return;
    }

    try {
      const tokenResponse = await apiClient.post('/login', {
        userId: userId.value,
        password: password.value
      });

      const token = tokenResponse.data.result.token;
      authStore.setToken(token);


      const userInfoResponse = await apiClient.post('/user_info', {
        userId: userId.value,
        password: password.value
      });

      const userInfo = userInfoResponse.data.result;
      authStore.setUserInfo(userInfo);
      console.log(userInfo);

      router.push('/main');

    } catch (error) {
      console.error('로그인 실패', error.response.data.resultCode);

      if (error.response && error.response.data.resultCode == 'USER_NOT_FOUND') {
        alert('아이디가 존재하지 않습니다.');
      } else if (error.response && error.response.data.resultCode == 'INVALID_PASSWORD') {
        alert('비밀번호가 틀렸습니다.');
      } else {
        alert('로그인이 실패했습니다.');
      }
    }
  }
</script>
<style scoped>
  
</style>