<template>
  <div class="container-fluid my-3 py-3">
    <div class="row mb-5">
      <div class="col-lg-12 mt-lg-0 mt-4">
        <div class="card card-body" id="profile">
          <div class="row align-items-center">
            <div class="col-sm-auto col-8 my-auto">
              <div class="h-100">
                <h5 class="mb-1 font-weight-bolder">
                  Richard Davis
                </h5>
                <p class="mb-0 font-weight-normal text-sm">
                  CEO / Co-Founder
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
            <button class="btn bg-gradient-dark btn-sm float-end mt-6 mb-0">Update Info</button>
          </div>
        </div>
        <div class="card mt-4" id="password">
          <div class="card-header">
            <h5>비밀번호 변경</h5>
          </div>
          <div class="card-body pt-0">
            <div class="input-group input-group-outline">
              <label class="form-label">현제 비밀번호</label>
              <input v-model="nowPassword" type="password" class="form-control" onfocus="focused(this)" onfocusout="defocused(this)">
            </div>
            <div class="input-group input-group-outline my-4">
              <label class="form-label">새 비밀번호</label>
              <input v-model="newPassword" type="password" class="form-control" onfocus="focused(this)" onfocusout="defocused(this)">
            </div>
            <div class="input-group input-group-outline">
              <label class="form-label">새 비밀번호 확인</label>
              <input v-model="confirmPassword" type="password" class="form-control" onfocus="focused(this)" onfocusout="defocused(this)">
            </div>
            <button class="btn bg-gradient-dark btn-sm float-end mt-6 mb-0">비밀번호 수정</button>
          </div>
        </div>
        <div class="card mt-4" id="delete">
          <div class="card-body">
            <div class="d-flex align-items-center mb-sm-0 mb-4">
              <div class="w-50">
                <h5>Delete Account</h5>
                <p class="text-sm mb-0">Once you delete your account, there is no going back. Please be certain.</p>
              </div>
              <div class="w-50 text-end">
                <button class="btn btn-outline-secondary mb-3 mb-md-0 ms-auto" type="button" name="button">Deactivate</button>
                <button class="btn bg-gradient-danger mb-0 ms-2" type="button" name="button">Delete Account</button>
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
  import apiClient from '../../../config/authConfig';

  const nickname = ref('');
  const email = ref('');
  const memo = ref('');
  const nowPassword = ref('');
  const newPassword = ref('');
  const confirmPassword = ref('');

  const getUserInfo = async () => {
    try {
      const response = await apiClient.get('/main/user_info');

      console.log(response.data.result);

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

    } catch (error) {
      console.log('에러가 발생했습니다.', error);
      alert('유저 정보 수정을 실패했습니다.');
    }
  };

  const modifyPassword = async () => {
    if (!validatePasswordForm()) {
      return;
    }

    try{

    } catch (error) {
      console.log('에러가 발생했습니다.', error);
      alert('비밀번호 수정을 실패했습니다.');
    }
  };

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
    } else if (password.value.length < 6) {
      alert('비밀번호는 최소 6자리 이상이어야 합니다.');
      return false;
    }

    if (!newPassword.value) {
      alert('새로운 비밀번호가 입력되지 않았습니다.');
      return false;
    } else if (password.value.length < 6) {
      alert('새 비밀번호는 최소 6자리 이상이어야 합니다.');
      return false;
    }

    if (!confirmPassword.value) {
      alert('비밀번호 확인이 입력되지 않았습니다.');
      return false;
    }

    return true;
  };

  onMounted(() => {
    getUserInfo();
  });
</script>
<style scoped>
  
</style>