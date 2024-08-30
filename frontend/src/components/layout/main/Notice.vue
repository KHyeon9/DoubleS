<template>
  <main class="main-content position-relative max-height-vh-100 h-100 border-radius-lg ps ps--active-y">
    <div class="container-fluid py-4">
      <div v-if="isAdmin" class="d-sm-flex justify-content-between">
        <div>
          <router-link to="/main/notice/new" class="btn btn-icon bg-gradient-primary">
            New Notice
          </router-link>
        </div>
      </div>
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-header pb-0">
              <h5 class="mb-3">공지사항</h5>
              <p class="text-sm mb-0">
                새로 올라온 공지사항을 확인해보세요.
              </p>
            </div>
            <div class="p-1">
              <div class="card-body">
                <div class="col-12">
                  <table v-if="noticeBoardList.length > 0" class="table align-items-center justify-content-center mt-0 mb-0">
                    <thead>
                      <tr>
                        <th class="col-1 text-center text-uppercase text-secondary text-xs font-weight-bolder opacity-12">번호</th>
                        <th class="col-6 text-center text-uppercase text-secondary text-xs font-weight-bolder opacity-12">제목</th>
                        <th class="col-3 text-center text-uppercase text-secondary text-xs font-weight-bolder opacity-7 ps-2">작성자</th>
                        <th class="col-2 text-center text-uppercase text-secondary text-xs font-weight-bolder opacity-7 ps-2">작성일</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="noticeBoard in noticeBoardList" :key="noticeBoard.id">
                        <td>
                          <div class="text-center px-2">
                            <div class="my-auto">
                              <h6 class="mb-0 text-sm text-center">{{ noticeBoard.id }}</h6>
                            </div>
                          </div>
                        </td>
                        <td>
                          <router-link :to="`/main/notice/${noticeBoard.id}`">
                            <div class="text-center px-2">
                              <div class="my-auto">
                                <h6 class="mb-0 text-sm">{{ noticeBoard.title }}</h6>
                              </div>
                            </div>
                          </router-link>
                        </td>
                        <td>
                          <p class="text-sm text-center font-weight-bold mb-0">{{ noticeBoard.user.userId }}</p>
                        </td>
                        <td>
                          <p class="text-xs text-center font-weight-bold">{{ formatDate(noticeBoard.createdAt) }}</p>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                   <!-- 공지사항이 없을 때 -->
                   <p v-else class="text-center text-sm">공지사항이 없습니다.</p>
                </div>
              </div>
              <!-- 페이징 -->
              <nav class="mt-4 me-3 nav nav-bar d-flex justify-content-end" aria-label="Page navigation">
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
      </div>
    </div>
  </main>
</template>
<script setup>
  import apiClient from '../../../config/authConfig';
  import { ref, onMounted, computed } from 'vue';
  import { usePagination } from '../../../utils/pagination';
  import { useAuthStore } from '../../../store/authStore';
  import { useFormat } from '../../../utils/format';

  const noticeBoardList = ref([]);
  const authStore = useAuthStore();
  const isAdmin = computed(() => authStore.isAdmin);
  const { formatDate } = useFormat();
  
  const { 
    page, 
    totalPages, 
    nextPage, 
    prevPage, 
    goToPage, 
    pageScrollTop,
    paginatedPageNumbers 
  } = usePagination();

  const getData = async () => {
    try {
      const response = await apiClient.get('/main/notice_board', {
        params: {
          page: page.value,
        },
      });

      noticeBoardList.value = response.data.result.content;
      totalPages.value = response.data.result.totalPages;

      console.log(response.data);
    } catch (error) {
      console.log('오류 발생: ', error);
    }
  };

  const nextPageAndFetch = () => {
    nextPage();
    getData();
    pageScrollTop();
  };

  const prevPageAndFetch = () => {
    prevPage();
    getData();
    pageScrollTop();
  };

  const goToPageAndFetch = (pageNumber) => {
    goToPage(pageNumber);
    getData();
    pageScrollTop();
  };

  onMounted(() => {
    getData();
  });

</script>
<style scoped>
  .active a {
    color: white;
  }
</style>