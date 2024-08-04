<template>
  <main class="main-content position-relative max-height-vh-100 h-100 border-radius-lg ps ps--active-y">
    <div class="container-fluid py-4">
      <div class="d-sm-flex justify-content-between">
        <div>
          <a href="javascript:;" class="btn btn-icon bg-gradient-primary">
            New Notice
          </a>
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
                  <table class="table align-items-center justify-content-center mt-0 mb-0">
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
                          <div class="text-center px-2">
                            <div class="my-auto">
                              <h6 class="mb-0 text-sm">{{ noticeBoard.title }}</h6>
                            </div>
                          </div>
                        </td>
                        <td>
                          <p class="text-sm text-center font-weight-bold mb-0">{{ noticeBoard.user.userId }}</p>
                        </td>
                        <td>
                          <span class="text-xs font-weight-bold">{{ formatDate(noticeBoard.createdAt) }}</span>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
              <!-- 페이징 -->
              <nav class="mt-4 nav nav-bar d-flex justify-content-end p-3" aria-label="Page navigation">
                <ul class="pagination">
                  <li class="page-item" :class="{ disabled: page === 0}">
                    <a class="page-link" @click.prevent="prevPage">‹</a>
                  </li>
                  <li 
                    v-for="pageNumber in paginatedPageNumbers"
                    :key="pageNumber"
                    class="page-item"
                    :class="{ active: pageNumber - 1 === page }"
                  >
                    <a class="page-link " @click.prevent="goToPage(pageNumber - 1)">{{ pageNumber }}</a>
                  </li>
                  <li class="page-item" :class="{ disabled: page === totalPages - 1 }">
                    <a class="page-link" @click.prevent="nextPage">›</a>
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
  import moment from 'moment';

  const noticeBoardList = ref([]);
  const page = ref(0);
  const totalPages = ref(0);

  const getData = async () => {
    try {
      const response = await apiClient.get('/notice_board', {
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

  const nextPage = () => {
    if (page.value < totalPages.value - 1) {
      page.value += 1;
      getData();
    }
  };

  const prevPage = () => {
    if (page.value > 0) {
      page.value -= 1;
      getData();
    }
  };

  const goToPage = (pageNumber) => {
    page.value = pageNumber;
    getData();
  };

  const formatDate = (date) => {
    return moment(date).format('YYYY/MM/DD');
  };

  const paginatedPageNumbers = computed(() => {
    const totalPageNumbers = 5;
    let startPage = Math.max(1, page.value + 1 - Math.floor(totalPageNumbers / 2));
    let endPage = Math.min(totalPages.value, startPage + totalPageNumbers - 1);

    if (endPage - startPage < totalPageNumbers - 1) {
      startPage = Math.max(1, endPage - totalPageNumbers + 1);
    }

    const pages = [];
    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    return pages;
  });

  onMounted(() => {
    getData();
  });

</script>
<style scoped>
  .active a {
    color: white;
  }
</style>