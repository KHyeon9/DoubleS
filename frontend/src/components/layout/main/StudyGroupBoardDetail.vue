<template>
  <div class="container-fluid px-2 px-md-4">
    <div class="card card-body mx-3 mx-md-4">
      <div class="row gx-4 mb-2">
        <div class="col-auto my-auto">
          <div class="h-100 ms-4">
            <h3 class="mb-2">
              {{ studyGroupBoard.title }}
            </h3>
            <p class="mb-0 font-weight-normal text-s">
              {{ studyGroupBoard.user?.nickname }} ( {{ studyGroupBoard.user?.userId }} ) 
            </p>
            <p class="mb-0 font-weight-normal text-sm">
              {{ formatDate(studyGroupBoard.createdAt) }}
            </p>
          </div>
        </div>
        <div class="col-lg-3 col-md-4 my-sm-auto ms-sm-auto me-sm-0 mx-auto mt-3">
          <div class="nav-wrapper position-relative end-0">
            <ul class="nav nav-pills nav-fill p-1" >
              <li v-if="userId == studyGroupBoard.user?.userId" class="nav-item" role="presentation">
                <router-link :to="`/main/question_board/modify/${studyGroupBoard.id}`" class="nav-link mb-0 px-0 py-1" role="tab">
                  <i class="material-icons text-lg position-relative">edit</i>
                  <span class="ms-1">수정</span>
                </router-link>
              </li>
              <li v-if="userId == studyGroupBoard.user?.userId" class="nav-item" role="presentation">
                <button class="nav-link mb-0 px-0 py-1" role="tab">
                  <i class="material-icons text-lg position-relative">delete</i>
                  <span class="ms-1">삭제</span>
                </button>
              </li>
              <li v-if="userId != studyGroupBoard.user?.userId" class="nav-item" role="presentation">
                <a class="nav-link mb-0 px-0 py-1" role="tab">
                  <i class="material-icons text-lg position-relative">email</i>
                  <span class="ms-1">메세지</span>
                </a>
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
                <p class="postContent mb-4" v-html="formattedContent(studyGroupBoard.content)"></p>
                <div class="row align-items-center px-2 mt-4 mb-2">
                  <div class="col-sm-12 px-0">
                    <div class="d-flex">
                      <div class="d-flex align-items-center me-3">
                        <i class="material-icons text-md me-2">mode_comment</i>
                        <span class="text-md">{{ studyGroupBoard.comments }}</span>
                      </div>
                      <div class="d-flex ms-auto align-items-center">
                        <router-link to="/main/study_group"  class="btn bg-gradient-dark btn-sm">스터디 그룹 페이지로</router-link >
                      </div>
                    </div>
                  </div>
                  <hr ref="commentsStart" class="horizontal dark my-3">
                </div>
                <div class="mb-1">
                  <div v-for="studyGroupBoardComment in studyGroupBoardComments" class="d-flex mt-3">
                    <div class="flex-shrink-0">
                      <i class="material-icons cursor-pointer">
                        person
                      </i>
                    </div>
                    <div class="flex-grow-1 ms-3">
                      <h6 class="h5 mt-0">{{ studyGroupBoardComment.user?.nickname }}
                        <span class="text-sm text font-weight-normal">
                          {{ studyGroupBoardComment.user?.userId }}
                        </span>
                      </h6>
                      <!-- 댓글 내용이 수정 중이면 textarea로 변경 -->
                      <div v-if="editingCommentId === studyGroupBoardComment.id">
                        <div class="flex-grow-1 my-auto">
                          <div class="input-group input-group-static">
                            <textarea  v-model="editingCommentText" class="form-control" placeholder="Write your comment" rows="3" spellcheck="false"></textarea>
                          </div>
                        </div>
                        <div class="ms-auto text-end">
                          <button class="btn btn-link text-dark px-3 mb-0">
                            <i class="material-icons text-sm me-2">edit</i>
                            수정
                          </button>
                          <button @click="cancelEdit" class="btn btn-link text-danger text-gradient px-3 mb-0">
                            <i class="material-icons text-sm me-2">cancel</i>
                            취소
                          </button>
                        </div>
                      </div>
                      <div v-else>
                        <p class="text-s">{{ studyGroupBoardComment.comment }}</p>
                        <p class="text-xs text-end me-4">{{ formatDate(studyGroupBoardComment.createdAt) }}</p>
                        <p v-if="studyGroupBoardComment.createdAt != studyGroupBoardComment.modifiedAt" 
                            class="text-xs text-end me-4">수정됨</p>
                        <div v-if="studyGroupBoardComment.user?.userId == userId" class="ms-auto text-end">
                          <button @click="editComment(studyGroupBoardComment.id, studyGroupBoardComment.comment)" 
                                  class="btn btn-link text-dark px-3 mb-0">
                            <i class="material-icons text-sm me-2">edit</i>
                            수정
                          </button>
                          <button @click="deleteComment(studyGroupBoardComment.id)"
                                  class="btn btn-link text-danger text-gradient px-3 mb-0"
                          >
                            <i class="material-icons text-sm me-2" >delete</i>
                            삭제
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                  <!-- 페이징 -->
                  <nav v-if="totalPages > 1" class="mt-4 nav nav-bar d-flex justify-content-end" aria-label="Page navigation">
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
                  <div class="d-flex mt-4">
                    <div class="flex-grow-1 my-auto">
                      <div class="input-group input-group-static">
                        <textarea v-model="studyGroupBoardCommentText" class="form-control" placeholder="Write your comment" rows="4" spellcheck="false"></textarea>
                      </div>
                    </div>
                    <button 
                            class="btn bg-gradient-primary btn-sm mt-auto mb-0 ms-2" 
                            type="button" name="button"
                    >
                      <i class="material-icons text-sm">send</i>
                    </button>
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
  import { ref, onMounted ,nextTick, computed } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { useAuthStore } from '../../../store/authStore';
  import { usePagination } from '../../../utils/pagination';
  import { useFormat } from '../../../utils/format';
  import apiClient from '../../../config/authConfig';

  const route = useRoute();
  const router = useRouter();
  const authStore = useAuthStore();
  const studyGroupBoard = ref({});
  const userId = computed(() => authStore.userId);
  const studyGroupBoardComments = ref([]);
  const studyGroupBoardCommentText = ref('');
  const editingCommentId = ref(null);
  const editingCommentText = ref('');
  const totalElememts = ref(0);
  const commentsStart = ref(null);

  const {
    formattedContent,
    formatDate
  } = useFormat();

  const { 
    page, 
    totalPages, 
    nextPage, 
    prevPage, 
    goToPage, 
    paginatedPageNumbers 
  } = usePagination();

  const getStudyGroupBoard = async (id) => {
    try {
      const response = await apiClient.get(`/main/study_group/board/${id}`);
      studyGroupBoard.value = response.data.result;

      console.log(response.data.result);
    } catch (error) {
      console.log('에러가 발생했습니다.', error);
      alert('스터디 그룹 게시글을 가져오지 못했습니다.');
    }
  };

  const getStudyGroupBoardComment = async (id) => {
    try {
      const response = await apiClient.get(`/main/study_group/board/${id}/comment`);

      studyGroupBoardComments.value = response.data.result.content;
      totalPages.value = response.data.result.totalPages;
      totalElememts.value = response.data.result.totalElements;

      console.log(studyGroupBoardComments.value);
    } catch (error) {
      console.log('에러가 발생했습니다.', error);
      alert('스터디 그룹 게시글의 댓글을 가져오지 못했습니다.');
    }
  };

  const scrollToFirstComment = async () => {
    await nextTick(); // DOM이 업데이트된 후에 실행되도록 nextTick 사용
    if (commentsStart.value) {
      commentsStart.value.scrollIntoView();
    }
  };

  const editComment = (commentId, commentText) => {
    editingCommentId.value = commentId;
    editingCommentText.value = commentText;
  };

  const cancelEdit = () => {
    editingCommentId.value = null;
    editingCommentText.value = '';
  };

  const nextPageAndFetch = () => {
    nextPage();
    getStudyGroupBoardComment(studyGroupBoard.value.id);
    scrollToFirstComment();
  };

  const prevPageAndFetch = () => {
    prevPage();
    getStudyGroupBoardComment(studyGroupBoard.value.id);
    scrollToFirstComment();
  };

  const goToPageAndFetch = (pageNumber) => {
    goToPage(pageNumber);
    getStudyGroupBoardComment(studyGroupBoard.value.id);
    scrollToFirstComment();
  };

  onMounted(() => {
    const studyGroupBoardId = route.params.id;
    getStudyGroupBoard(studyGroupBoardId);
    getStudyGroupBoardComment(studyGroupBoardId);
  });
</script>
<style scoped>
  .postContent {
    font-size: 19px;
  }

  .active a {
    color: white;
  }
</style>