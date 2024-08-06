<template>
  <main class="main-content position-relative h-100 border-radius-lg ps ps--active-y">
    <div class="container-fluid py-4">
      <div class="d-sm-flex justify-content-between">
        <div>
          <router-link to="/main/question_board/new" class="btn btn-icon bg-gradient-primary">
            New Post
          </router-link>
        </div>
        <div class="d-flex">
          <div class="d-inline">
            <button href="javascript:;" class="btn btn-outline-dark dropdown-toggle" data-bs-toggle="dropdown" id="navbarDropdownMenuLink2" aria-expanded="false">
              {{ postTagName || 'Tag Filter' }}
            </button>
            <ul class="dropdown-menu dropdown-menu-lg-start px-2 py-3" aria-labelledby="navbarDropdownMenuLink2" style="">
              <li v-for="tag in tags" :key="tag.key">
                <a class="dropdown-item border-radius-md" @click="selectTag(tag)">
                  {{ tag.value }}
                </a>
              </li>
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
                        <div class="mb-3">
                          <div class="fw-bolder fs-5 mb-1">{{ questionBoard.title }}</div>
                          <div class="boardCreateInfo">
                            <span>{{ questionBoard.user.userId }}</span>  
                            <span>{{ formatDate(questionBoard.createdAt) }}</span>
                          </div>
                        </div>
                        <span class="mb-2 text-s">{{ questionBoard.content }}</span>
                        <div class="boardInfo">
                          <div class="text-dark mb-0 mt-1">
                            <i class="material-icons text-sm me-2">sell</i>
                            {{ questionBoard.tag.value }}
                          </div>
                          <div class="ms-auto boardCount">
                            <div class="text-dark px-3 mb-0">
                              <i class="material-icons text-sm me-2">visibility</i>
                              <span class="font-weight-bold">{{ questionBoard.viewCounts }}</span>
                            </div>
                            <div class="text-dark px-3 mb-0">
                              <i class="material-icons text-sm me-2">thumb_up</i>
                              <span class="font-weight-bold">{{ questionBoard.likes }}</span>
                            </div>
                            <div class="text-dark px-3 mb-0">
                              <i class="material-icons text-sm me-2">mode_comment</i>
                              <span class="font-weight-bold">{{ questionBoard.comments }}</span>
                            </div>
                          </div>
                        </div>
                    </div>
                  </router-link>
                </li>
              </ul>
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
      </div>
    </div>
  </main>
</template>
<script setup>
  import apiClient from '../../../config/authConfig';
  import { ref, onMounted } from 'vue';
  import { usePagination } from '../../../utils/pagination';

  const questionBoardList = ref([]);
  const tags = ref([]);
  const postTag = ref('');
  const postTagName = ref('');
  const { 
    page, 
    totalPages, 
    nextPage, 
    prevPage, 
    goToPage, 
    formatDate, 
    paginatedPageNumbers 
  } = usePagination();

  const selectTag = (tag) => {
    postTagName.value = tag.value;
    postTag.value = tag.key
  };


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

  const getTags = async () => {
    try {
      const response = await apiClient.get('/question_board/tags');
      const tagResponse = response.data.result;
      tags.value = [{ key: 'All', value: '전체' }, ...tagResponse];
      
      console.log(response.data)
    } catch (error) {
      console.log('태그를 가져오지 못했습니다.', error);
    }
  };

  const nextPageAndFetch = () => {
    nextPage();
    getData();
  };

  const prevPageAndFetch = () => {
    prevPage();
    getData();
  };

  const goToPageAndFetch = (pageNumber) => {
    goToPage(pageNumber);
    getData();
  };
  
  onMounted(() => {
    getData();
    getTags();
  });
</script>
<style scoped>
  .active a {
    color: white;
  }

  .boardCreateInfo, .boardInfo {
    display: flex;
    flex-direction: row;
    justify-content: space-between; 
    align-items: center;
  }

  .boardCount {
    display: flex; 
    flex-direction: row; 
    align-items: center;
  }

  .boardCreateInfo {
    font-size: 15px;
    font-weight: 200;
  }

  .dropdown-menu {
    box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.3);
  }
</style>