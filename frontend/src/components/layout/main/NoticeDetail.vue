<template>
  <div class="container-fluid px-2 px-md-4">
    <div class="card card-body mx-3 mx-md-4">
      <div class="row gx-4 mb-2">
        <div class="col-auto my-auto">
          <div class="h-100 ms-4">
            <h3 class="mt-2 mb-2">
              {{ notice.title }}
            </h3>
            <p class="mb-0 font-weight-normal text-s">
              {{ notice.user?.userId }}
            </p>
            <p class="mb-0 font-weight-normal text-sm">
              {{ formatDate(notice.createdAt) }}
            </p>
          </div>
        </div>
        <div v-if="isAdmin" class="col-lg-3 col-md-4 my-sm-auto ms-sm-auto me-sm-0 mx-auto mt-3">
          <div class="nav-wrapper position-relative end-0">
            <ul class="nav nav-pills nav-fill p-1" >
              <li class="nav-item" role="presentation">
                <router-link :to="`/main/notice/modify/${notice.id}`"  class="nav-link mb-0 px-0 py-1">
                  <i class="material-icons text-lg position-relative">edit</i>
                  <span class="ms-1">수정</span>
                </router-link>
              </li>
              <li class="nav-item" role="presentation">
                <button class="nav-link mb-0 px-0 py-1" @click="deleteNotice">
                  <i class="material-icons text-lg position-relative">delete</i>
                  <span class="ms-1">삭제</span>
                </button>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="row mt-4">
          <div class="col-12 col-lg-12">
            <div class="card card-plain">
              <hr class="dark horizontal">
              <div class="card-body pt-3">
                <p class="noticeContent mb-4" v-html="formattedContent(notice.content)"></p>
              </div>
              <div class="row align-items-center px-2 mt-4 mb-2">
                <div class="col-sm-12 px-0">
                  <div class="d-flex">
                    <div class="d-flex ms-auto align-items-center">
                      <router-link to="/main/notice"  class="btn bg-gradient-dark btn-sm">공지사항 목록</router-link >
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
  import { ref, onMounted, computed } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { useAuthStore } from '../../../store/authStore';
  import apiClient from '../../../config/authConfig';
  import { useFormat } from '../../../utils/format';

  const route = useRoute();
  const router = useRouter();
  const notice = ref({});
  const authStore = useAuthStore();
  const isAdmin = computed(() => authStore.isAdmin);
  const {
    formattedContent,
    formatDate
  } = useFormat();

  const getNotice = async (id) => {
    try {
      const response = await apiClient.get(`/main/notice_board/${id}`);
      notice.value = response.data.result;

      console.log(response.data.result);
    } catch (error) {
      console.log('에러가 발생했습니다.', error);
      alert('공지사항 데이터를 가져오지 못했습니다.');
    }
  };

  const deleteNotice = async () => {
    try {
      if (confirm('정말 삭제하시겠습니까?')) {
        const response = await apiClient.delete(`/main/notice_board/${notice.value.id}`);
        console.log(response);

        router.push('/main/notice');
      }
    } catch (error) {
      console.log('에러가 발생했습니다. ', error);
      alert('삭제 실패했습니다.');
    }
  };

  onMounted(() => {
    const noticeId = route.params.id;
    getNotice(noticeId);
  });
</script>
<style scoped>
</style>