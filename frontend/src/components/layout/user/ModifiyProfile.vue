<template>
  <div class="container-fluid my-3 py-3">
    <div class="row mb-5">
      <div class="col-lg-12 mt-lg-0 mt-4">
        <div class="card card-body" id="profile">
          <div class="row align-items-center">
            <div class="col-sm-auto col-8 my-auto">
              <div class="h-100">
                <h5 class="mb-1 font-weight-bolder">
                  {{ nickname }}
                </h5>
                <p class="mb-0 font-weight-normal text-sm">
                  {{ userId }}
                </p>
              </div>
            </div>
          </div>
        </div>
        <div class="card mt-4" id="basic-info">
          <div class="card-header">
            <h5>내 정보</h5>
          </div>
          <div class="card-body pt-0">
            <div class="row">
              <div class="col-6">
                <div class="input-group input-group-static">
                  <label class="font-weight-bolder">닉네임</label>
                  <input v-model="nickname" type="text" class="form-control" placeholder="닉네임" onfocus="focused(this)" onfocusout="defocused(this)">
                </div>
              </div>
            </div>
            <div class="row mt-4">
              <div class="col-6">
                <div class="input-group input-group-static">
                  <label class="font-weight-bolder">Email</label>
                  <input v-model="email" type="email" class="form-control" placeholder="example@email.com" onfocus="focused(this)" onfocusout="defocused(this)">
                </div>
              </div>
            </div>
            <div class="row mt-4">
              <div class="col-6">
                <div class="input-group input-group-static">
                  <label class="font-weight-bolder">자기소개</label>
                  <textarea v-model="memo" type="text" 
                            class="form-control" 
                            placeholder="자기소개를 입력해주세요." 
                            rows="4"
                            onfocus="focused(this)" onfocusout="defocused(this)"
                  >
                  </textarea>
                </div>
              </div>
            </div>
            <button @click="modifyUserInfo" class="btn bg-gradient-dark btn-md float-end mt-6 mb-0">내 정보 수정</button>
          </div>
        </div>
        <div class="card mt-4" id="password">
          <div class="card-header">
            <h5>비밀번호 변경</h5>
          </div>
          <div class="card-body pt-0">
            <div class="col-6">
              <div :class="['input-group input-group-outline', { 'is-focused': isNowPasswordFocused || nowPassword }]">
                <label class="form-label">현재 비밀번호</label>
                <input 
                  v-model="nowPassword" 
                  type="password" 
                  class="form-control" 
                  @focus="handleNowPasswordFocus"
                  @blur="handleNowPasswordBlur"
                >
              </div>
              <div :class="['input-group input-group-outline my-4', { 'is-focused': isNewPasswordFocused || newPassword }]">
                <label class="form-label">새 비밀번호</label>
                <input 
                  v-model="newPassword" 
                  type="password" 
                  class="form-control" 
                  @focus="handleNewPasswordFocus"
                  @blur="handleNewPasswordBlur"
                >
              </div>
              <div :class="['input-group input-group-outline', { 'is-focused': isConfirmPasswordFocused || confirmPassword }]">
                <label class="form-label">새 비밀번호 확인</label>
                <input 
                  v-model="confirmPassword" 
                  type="password" 
                  class="form-control" 
                  @focus="handleConfirmPasswordFocus"
                  @blur="handleConfirmPasswordBlur"
                >
              </div>
            </div>
            <button @click="modifyPassword" class="btn bg-gradient-dark btn-md float-end mt-6 mb-0">비밀번호 수정</button>
          </div>
        </div>
        <div class="card mt-4" id="delete">
          <div class="card-body">
            <div class="d-flex align-items-center mb-sm-0 mb-4">
              <div class="w-50">
                <h5>계정 삭제 또는 되돌아가기</h5>
                <p class="text-sm mb-0">계정을 삭제하거나 되돌아갑니다.</p>
              </div>
              <div class="w-50 text-end">
                <router-link to="/main/profile" class="btn btn-outline-secondary mb-3 mb-md-0 ms-auto" type="button" name="button">내 프로필로</router-link>
                <button @click="deleteUser" class="btn bg-gradient-danger mb-0 ms-2" type="button" name="button">계정 삭제</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
  import { ref, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import apiClient from '../../../config/authConfig';
  import {useAuthStore} from "../../../store/authStore.js";

  const authStore = useAuthStore();

  const router = useRouter();
  const userId = ref('');
  const nickname = ref('');
  const email = ref('');
  const memo = ref('');
  const nowPassword = ref('');
  const newPassword = ref('');
  const confirmPassword = ref('');

  const isNowPasswordFocused = ref(false);
  const isNewPasswordFocused = ref(false);
  const isConfirmPasswordFocused = ref(false);

  const getUserInfo = async () => {
    try {
      const response = await apiClient.get('/main/user_info');

      console.log(response.data.result);

      userId.value = response.data.result.userId;
      nickname.value = response.data.result.nickname;
      email.value = response.data.result.email;
      memo.value = response.data.result.memo;
    } catch (error) {
      console.log('에러가 발생했습니다.', error);
      alert('유저 정보 데이터를 가져오지 못했습니다.');
    }
  };

  const modifyUserInfo = async () => {
    if (!validateUserInfoForm()) {
      return;
    }

    try{
      const response = await apiClient.put("/main/profile/user_info", {
        nickname: nickname.value,
        email: email.value,
        memo: memo.value
      });

      console.log("수정 성공:", response.data.result);

      // 알림 메시지 (사용자 경험 향상)
      alert('회원 정보가 수정되었습니다.');

      router.push(`/main/profile/modify/${userId}`);
    } catch (error) {
      console.log('에러가 발생했습니다.', error);
      alert('유저 정보 수정을 실패했습니다.');
    }
  };

  const modifyPassword = async () => {
    if (!validatePasswordForm()) {
      return;
    }

    if (newPassword.value !== confirmPassword.value) {
      alert('새로운 비밀번호와 비밀번호 확인이 맞지 않습니다.');
      return;
    }

    try{
      const response = await apiClient.put("/main/profile/user_password", {
        nowPassword: nowPassword.value,
        changePassword: newPassword.value,
      });
      if (response.status === 200) {
        // 서버 로그아웃 호출 (토큰/세션 무효화)
        try {
          await apiClient.post('/logout');
          console.log('서버 로그아웃 성공');
        } catch (logoutError) {
          // 로그아웃 API가 실패하더라도 클라이언트 상태는 지워야 하므로 에러만 기록
          console.error('서버 로그아웃 호출 중 오류 발생:', logoutError);
        }
        // 클라이언트 상태 초기화 및 리다이렉트
        authStore.logout();
        // 사용자에게 알림 후 이동
        console.log('비밀번호가 성공적으로 변경되었습니다. 다시 로그인 해주세요.');
        router.push('/login');
      }
    } catch (error) {
      console.log('에러가 발생했습니다.', error);
      if(error.response.data.resultCode === "INVALID_PASSWORD") {
        alert('현재 비밀번호가 틀렸습니다.')
      } else {
        alert('비밀번호 수정을 실패했습니다.');
      }
    }
  };

  const deleteUser = async () => {
    try {
      const response = await apiClient.delete("/main/profile");

      console.log(response.data.result);

      alert('계정 삭제가 완료되었습니다.');

      router.push('/login');

    } catch (error) {
      console.log('에러가 발생했습니다.', error);
      if(error.response.data.resultCode === "LEADER_NOT_EXIT") {
        alert('스터디 그룹 리더는 그룹을 탈퇴하거나 삭제하야 합니다.')
      } else {
        alert('계정 삭제에 실패했습니다.');
      }
    }
  }

  const validateUserInfoForm = () => {
    if (!nickname.value) {
      alert('닉네임이 입력되지 않았습니다.');
      return false;
    }

    if (!email.value) {
      alert('이메일이 입력되지 않았습니다.');
      return false;
    }

    return true;
  };

  const validatePasswordForm = () => {
    if (!nowPassword.value) {
      alert('현재 비밀번호가 입력되지 않았습니다.');
      return false;
    } else if (nowPassword.value.length < 6) {
      alert('비밀번호는 최소 6자리 이상이어야 합니다.');
      return false;
    }

    if (!newPassword.value) {
      alert('새로운 비밀번호가 입력되지 않았습니다.');
      return false;
    } else if (newPassword.value.length < 6) {
      alert('새 비밀번호는 최소 6자리 이상이어야 합니다.');
      return false;
    }

    if (!confirmPassword.value) {
      alert('비밀번호 확인이 입력되지 않았습니다.');
      return false;
    }

    return true;
  };

  const handleNowPasswordFocus = () => {
    isNowPasswordFocused.value = true;
  };

  const handleNowPasswordBlur = () => {
    isNowPasswordFocused.value = false;
  };

  const handleNewPasswordFocus = () => {
    isNewPasswordFocused.value = true;
  };

  const handleNewPasswordBlur = () => {
    isNewPasswordFocused.value = false;
  };

  const handleConfirmPasswordFocus = () => {
    isConfirmPasswordFocused.value = true;
  };

  const handleConfirmPasswordBlur = () => {
    isConfirmPasswordFocused.value = false;
  };

  onMounted(() => {
    getUserInfo();
  });
</script>
<style scoped>
  
</style>