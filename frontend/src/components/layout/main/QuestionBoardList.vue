<template>
  <main class="main-content position-relative h-100 border-radius-lg ps ps--active-y">
    <div class="container-fluid py-4">
      <div class="d-sm-flex justify-content-between">
        <div>
          <a href="javascript:;" class="btn btn-icon bg-gradient-primary">
            New Post
          </a>
        </div>
        <div class="d-flex">
          <div class="d-inline">
            <button href="javascript:;" class="btn btn-outline-dark dropdown-toggle" data-bs-toggle="dropdown" id="navbarDropdownMenuLink2" aria-expanded="false">
              Filters
            </button>
            <ul class="dropdown-menu dropdown-menu-lg-start px-2 py-3" aria-labelledby="navbarDropdownMenuLink2" style="">
              <li><a class="dropdown-item border-radius-md" href="javascript:;">Status: Paid</a></li>
              <li><a class="dropdown-item border-radius-md" href="javascript:;">Status: Refunded</a></li>
              <li><a class="dropdown-item border-radius-md" href="javascript:;">Status: Canceled</a></li>
            </ul>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-header">
              <h5 class="mb-0">질문 게시판</h5>
              <p class="text-sm mb-0">
                궁금한 것을 공유해보세요.
              </p>
            </div>
            <div class="p-3">
              <ul class="list-group">
                <li v-for="questionBoard in questionBoardList" :key="questionBoard.id" 
                    class="list-group-item border-0 d-flex p-4 mb-2 bg-gray-100 border-radius-lg">
                  <router-link class="col-12" :to="`/main/question_board/${questionBoard.id}`">
                    <div class="d-flex flex-column">
                        <div class="fw-bolder mb-3 fs-5">{{ questionBoard.title }}</div>
                        <span class="mb-2 text-s">{{ questionBoard.content }}</span>
                        <div class="text-dark mb-0 mt-1"><i class="material-icons text-sm me-2">sell</i>Tag</div>
                        <div class="ms-auto board_info">
                          <div class="text-dark px-3 mb-0"><i class="material-icons text-sm me-2">thumb_up</i><span class="font-weight-bold">13</span></div>
                          <div class="text-dark px-3 mb-0"><i class="material-icons text-sm me-2">mode_comment</i><span class="font-weight-bold">3</span></div>
                        </div>
                    </div>
                  </router-link>
                </li>
              </ul>
              <!-- 페이징 -->
              <nav class="mt-4 nav nav-bar d-flex justify-content-end" aria-label="Page navigation">
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

  const questionBoardList = ref([]);
  const page = ref(0);
  const totalPages = ref(0);

  const getData = async () => {
    try {
      const response = await apiClient.get('/question_board', {
        params: {
          page: page.value,
        },
      });

      questionBoardList.value = response.data.result.content;
      totalPages.value = response.data.result.totalPages;

      console.log(response.data);
    } catch (error) {
      console.log('에러 발생', error);
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