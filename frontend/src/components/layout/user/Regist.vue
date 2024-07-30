<template>
  <section>
    <div class="page-header min-vh-100">
      <div class="container">
        <div class="row">
          <div class="col-6 d-lg-flex d-none h-100 my-auto pe-0 position-absolute top-0 start-0 text-center justify-content-center flex-column">
            <div class="position-relative bg-gradient-primary h-100 m-3 px-7 border-radius-lg d-flex flex-column justify-content-center" style="background-image: url('../assets/img/illustrations/illustration-signup.jpg'); background-size: cover;">
            </div>
          </div>
          <div class="col-xl-4 col-lg-5 col-md-7 d-flex flex-column ms-auto me-auto ms-lg-auto me-lg-5">
            <div class="card card-plain">
              <div class="card-header">
                <h4 class="font-weight-bolder">Sign Up</h4>
                <p class="mb-0">Enter your info to register</p>
              </div>
              <div class="card-body">
                <form @submit.prevent="registSubmit" class="registForm" role="form">
                  <div class="input-group input-group-outline mb-3">
                    <label class="form-label"></label>
                    <input id="userId" v-model="userId" type="text" class="form-control" placeholder="ID">
                  </div>
                  <div class="input-group input-group-outline mb-3">
                    <label class="form-label"></label>
                    <input id="nickname" v-model="nickname" type="text" class="form-control" placeholder="Nickname">
                  </div>
                  <div class="input-group input-group-outline mb-3">
                    <label class="form-label"></label>
                    <input id="email" v-model="email" type="email" class="form-control" placeholder="Email">
                  </div>
                  <div class="input-group input-group-outline mb-3">
                    <label class="form-label"></label>
                    <input id="password" v-model="password" type="password" class="form-control" placeholder="Password">
                  </div>
                  <div class="form-check form-check-info text-start ps-0">
                    <input class="form-check-input" type="checkbox" value="" id="flexCheckDefault" v-model="agreed">
                    <label class="form-check-label" for="flexCheckDefault">
                      <span class="text-dark font-weight-bolder">DoubleS</span>에 회원가입하는 것에 동의합니다.
                    </label>
                  </div>
                  <div class="text-center">
                    <button type="submit" class="btn btn-lg bg-gradient-primary btn-lg w-100 mt-4 mb-0">Sign Up</button>
                  </div>
                </form>
              </div>
              <div class="card-footer text-center pt-0 px-lg-2 px-1">
                <p class="mb-2 text-sm mx-auto">
                  이미 회원가입을 하셨나요? 
                  <router-link to="/login" class="text-primary text-gradient font-weight-bold">Login</router-link>
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
<script setup>
  import { ref } from 'vue';
  import axios from 'axios';
  import { useRouter } from 'vue-router';

  const userId = ref('');
  const nickname = ref('');
  const email = ref('');
  const password = ref('');
  const agreed = ref(false);
  const router = useRouter();

  const validateForm = () => {

    if (!userId.value) {
      alert('ID가 입력되지 않았습니다.');
      return false;
    } else if (userId.value.length < 4) {
      alert('ID는 최소 4자리 이상이어야 합니다.');
      return false;
    }

    if (!nickname.value) {
      alert('닉네임이 입력되지 않았습니다.');
      return false;
    }

    if (!email.value) {
      alert('Email이 입력되지 않았습니다.');
      return false;
    } 

    if (!password.value) {
      alert('비밀번호가 입력되지 않았습니다.');
      return false;
    } else if (password.value.length < 6) {
      alert('비밀번호는 최소 6자리 이상이어야 합니다.');
      return false;
    }

    if (!agreed.value) {
      alert('회원 가입에 동의 체크를 하셔야합니다.')
      return false;
    }

    return true;
  };

  const registSubmit = async () => {
    if (!validateForm()) {
      console.log(errors);
      return;
    }

    try {
      const response = await axios.post('/api/regist', {
          userId: userId.value,
          nickname: nickname.value,
          email: email.value,
          password: password.value,
        }
      );

      // 성공적으로 회원가입한 경우
      console.log('회원가입 성공', response.data);

      alert('회원가입 되었습니다. 로그인 페이지로 이동합니다.')

      router.push('/login');
    } catch (error) {
      console.error('회원가입 실패', error.response.data.resultCode);
      if (error.response && error.response.data.resultCode == 'DUPLICATED_USER_ID') {
        // 서버로부터 반환된 에러 메시지를 설정
        alert('이미 있는 아이디입니다. 다시 한번 시도해 주세요.');
      } else {
        alert('회원가입 실패. 다시 한번 시도해 주세요.');
      }
    }
  };

</script>
<style scoped>
  
</style>