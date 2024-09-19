<template>
  <div class="row">
        <div class="col-lg-10 col-md-10 mx-auto">
          <div class="card mt-4">
            <div class="card-header p-3">
              <h5 class="mb-0">Alarm List</h5>
            </div>
            <div class="alarm-body card-body p-3 pb-0">
              <div class="alert alert-info alert-dismissible text-white" role="alert">
                <span class="text-sm">질문 게시글 좋아요</span>
                <button type="button" class="btn-close text-lg py-3 opacity-10" data-bs-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="alert alert-info alert-dismissible text-white" role="alert">
                <span class="text-sm">질문 게시글 댓글</span>
                <button type="button" class="btn-close text-lg py-3 opacity-10" data-bs-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="alert alert-info alert-dismissible text-white" role="alert">
                <span class="text-sm">스터디 그룹 초대</span>
                <button type="button" class="btn-close text-lg py-3 opacity-10" data-bs-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="alert alert-info alert-dismissible text-white" role="alert">
                <span class="text-sm">스터디 그룹 게시글 댓글</span>
                <button type="button" class="btn-close text-lg py-3 opacity-10" data-bs-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="alert alert-info alert-dismissible text-white" role="alert">
                <span class="text-sm">새로운 채팅</span>
                <button type="button" class="btn-close text-lg py-3 opacity-10" data-bs-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
            </div>
            <!-- 페이징 -->
            <nav class="mt-4 nav nav-bar d-flex justify-content-end" aria-label="Page navigation">
              <ul class="pagination">
                <li class="page-item" :class="{ disabled: page === 0}">
                  <a class="page-link" @click.prevent="prevPageAndFetch">‹</a>
                </li>
                <li 
                  v-for="pageNumber in paginatedPageNumbers"
                  :key="pageNumber"
                  class="page-item"
                  :class="{ active: pageNumber - 1 === page }"
                >
                  <a class="page-link " @click.prevent="goToPageAndFetch(pageNumber - 1)">{{ pageNumber }}</a>
                </li>
                <li class="page-item" :class="{ disabled: page === totalPages - 1 }">
                  <a class="page-link" @click.prevent="nextPageAndFetch">›</a>
                </li>
              </ul>
            </nav>
          </div>
        </div>
      </div>
</template>
<script setup>
   import apiClient from '../../../config/authConfig';
   import { ref, onMounted } from 'vue';
   import { usePagination } from '../../../utils/pagination';

   const { 
    page, 
    totalPages, 
    nextPage, 
    prevPage, 
    goToPage, 
    paginatedPageNumbers,
    pageScrollTop
  } = usePagination();

   const alarmList = ref([]);

   const getAlarmList = async () => {
    try {
      const response = await apiClient.get('/main/alarm', {
        params: {
          page: page.value,
        },
      });

      console.log(response.data.result);

    } catch (error) {
      console.log('알람 리스트를 가져오는데 에러 발생', error);
      alert('알람 리스트를 가져오지 못했습니다.');
    }
   };

   onMounted(() => {
    getAlarmList();
  });
</script>
<style scoped>
  .active a {
    color: white;
  }

  .alarm-body {
    height: 755px;
  }
</style>