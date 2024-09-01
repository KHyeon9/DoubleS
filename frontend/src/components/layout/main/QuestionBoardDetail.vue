<template>
  <div class="container-fluid px-2 px-md-4">
    <div class="card card-body mx-3 mx-md-4">
      <div class="row gx-4 mb-2">
        <div class="col-auto my-auto">
          <div class="h-100 ms-4">
            <div class="mt-2">
              <i class="material-icons text-lg me-2">sell</i>
              <span class="text-lg me-3 ">{{ questionBoard.tag?.value }}</span>
            </div>
            <h3 class="mb-2">
              {{ questionBoard.title }}
            </h3>
            <p class="mb-0 font-weight-normal text-s">
              {{ questionBoard.user?.nickname }} ( {{ questionBoard.user?.userId }} ) 
            </p>
            <p class="mb-0 font-weight-normal text-sm">
              {{ formatDate(questionBoard.createdAt) }}
            </p>
          </div>
        </div>
        <div class="col-lg-3 col-md-4 my-sm-auto ms-sm-auto me-sm-0 mx-auto mt-3">
          <div class="nav-wrapper position-relative end-0">
            <ul class="nav nav-pills nav-fill p-1" >
              <li v-if="userId == questionBoard.user?.userId" class="nav-item" role="presentation">
                <router-link :to="`/main/question_board/modify/${questionBoard.id}`"  class="nav-link mb-0 px-0 py-1 " role="tab">
                  <i class="material-icons text-lg position-relative">edit</i>
                  <span class="ms-1">수정</span>
                </router-link>
              </li>
              <li v-if="userId == questionBoard.user?.userId" class="nav-item" role="presentation">
                <button @click="deleteQuestionBoard" class="nav-link mb-0 px-0 py-1" role="tab">
                  <i class="material-icons text-lg position-relative">delete</i>
                  <span class="ms-1">삭제</span>
                </button>
              </li>
              <li v-if="userId != questionBoard.user?.userId" class="nav-item" role="presentation">
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
                <p class="postContent mb-4" v-html="formattedContent(questionBoard.content)"></p>
                <div class="row align-items-center px-2 mt-4 mb-2">
                  <div class="col-sm-12 px-0">
                    <div class="d-flex">
                      <div class="d-flex align-items-center me-3">
                        <i class="material-icons text-md me-2">visibility</i>
                        <span class="text-md">{{ questionBoard.viewCounts }}</span>
                      </div>
                      <div class="d-flex align-items-center me-3">
                        <i class="material-icons text-md me-2">mode_comment</i>
                        <span class="text-md">{{ comments }}</span>
                      </div>
                      <div @click="like" class="d-flex align-items-center me-3 cursor-pointer">
                        <i class="material-icons text-md me-2">thumb_up</i>
                        <span class="text-md">{{ questionBoard.likes }}</span>
                      </div>  
                      <div class="d-flex ms-auto align-items-center">
                        <router-link to="/main/question_board"  class="btn bg-gradient-dark btn-sm">질문 게시글 목록</router-link >
                      </div>
                    </div>
                  </div>
                  <hr ref="commentsStart" class="horizontal dark my-3">
                </div>
                <div class="mb-1">
                  <div v-for="quesionBoardComment in questionBoardComments" class="d-flex mt-3">
                    <div class="flex-shrink-0">
                      <i class="material-icons cursor-pointer">
                        person
                      </i>
                    </div>
                    <div class="flex-grow-1 ms-3">
                      <h6 class="h5 mt-0">{{ quesionBoardComment.user?.nickname }} 
                        <span class="text-sm text font-weight-normal">
                          ( {{ quesionBoardComment.user?.userId }} )
                        </span>
                      </h6>
                      <!-- 댓글 내용이 수정 중이면 textarea로 변경 -->
                      <div v-if="editingCommentId === quesionBoardComment.id">
                        <div class="flex-grow-1 my-auto">
                          <div class="input-group input-group-static">
                            <textarea  v-model="editingCommentText" class="form-control" placeholder="Write your comment" rows="3" spellcheck="false"></textarea>
                          </div>
                        </div>
                        <div class="ms-auto text-end">
                          <button @click="modifyComment(quesionBoardComment.id)" class="btn btn-link text-dark px-3 mb-0">
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
                        <p class="text-s">{{ quesionBoardComment.comment }}</p>
                        <p class="text-xs text-end me-4">{{ formatDate(quesionBoardComment.createdAt) }}</p>
                        <p v-if="quesionBoardComment.createdAt != quesionBoardComment.modifiedAt" 
                            class="text-xs text-end me-4">수정됨</p>
                        <div v-if="quesionBoardComment.user?.userId == userId" class="ms-auto text-end">
                          <button  @click="editComment(quesionBoardComment.id, quesionBoardComment.comment)" 
                                    class="btn btn-link text-dark px-3 mb-0">
                            <i class="material-icons text-sm me-2">edit</i>
                            수정
                          </button>
                          <button  @click="deleteComment(quesionBoardComment.id)"
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
                        <textarea v-model="quetionBoardCommentText" class="form-control" placeholder="Write your comment" rows="4" spellcheck="false"></textarea>
                      </div>
                    </div>
                    <button @click="createQuestionBoardComment" 
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
  import { ref, onMounted ,nextTick } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { useAuthStore } from '../../../store/authStore';
  import { computed } from 'vue';
  import { usePagination } from '../../../utils/pagination';
  import { useFormat } from '../../../utils/format';
  import apiClient from '../../../config/authConfig';

  const route = useRoute();
  const router = useRouter();
  const authStore = useAuthStore();
  const questionBoard = ref({});
  const comments = ref(0);
  const userId = computed(() => authStore.userId)
  const questionBoardComments = ref([]);
  const quetionBoardCommentText = ref('');
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

  const getQuestionBoard = async (id) => {
    try {
      const response = await apiClient.get(`/main/question_board/${id}`);
      questionBoard.value = response.data.result;
      comments.value = questionBoard.value.comments;
      console.log( questionBoard.value);
    } catch (error) {
      console.log('에러가 발생했습니다.', error);
      alert('질문 게시글을 가져오지 못했습니다.');
    }
  };

  const getQuestionBoardComment = async (id) => {
    try {
      const response = await apiClient.get(`/main/question_board/${id}/comment`, {
        params: {
          page: page.value,
        },
      });

      questionBoardComments.value = response.data.result.content;
      totalPages.value = response.data.result.totalPages;
      totalElememts.value = response.data.result.totalElements;

      console.log(response.data);
    } catch (error) {
      console.log('에러가 발생했습니다.', error);
      alert('해당 게시글의 댓글을 가져오지 못했습니다.');
    }
  }

  const deleteQuestionBoard = async () => {
    try {
      if (confirm('정말 삭제하시겠습니까?')) {
        const response = await apiClient.delete(`/main/question_board/${questionBoard.value.id}`);
        console.log(response);

        alert('게시글이 삭제되었습니다.');

        router.push('/main/question_board');
      }
    } catch (error) {
      console.log('에러가 발생했습니다. ', error);
      alert('질문 게시글을 삭제하는데 실패했습니다.');
    }
  };

  const createQuestionBoardComment = async () => {
    if (!quetionBoardCommentText.value) {
      alert('댓글이 입력되지 않았습니다.');
      return;
    }

    try {
      const response = await apiClient.post(`/main/question_board/${questionBoard.value.id}/comment`, {
        comment: quetionBoardCommentText.value,
      });

      console.log(response.data);

      quetionBoardCommentText.value = '';

      alert('댓글이 생성되었습니다.');

      // 최신 페이지로 이동
      page.value = totalPages.value;

      if (totalElememts.value % 10 != 0) {
        page.value -= 1;
      }

      comments.value += 1;

      await getQuestionBoardComment(questionBoard.value.id);

    } catch (error) {
      console.log('에러가 발생했습니다. ', error);
      alert('댓글 생성에 실패했습니다.');
    }
  };

  const modifyComment = async (questiionBoardCommentId) => {
    if (!editingCommentText.value) {
      alert('수정될 댓글이 입력되지 않았습니다.');
      return;
    }

    try {
      const response = await apiClient.put(`/main/question_board/${questionBoard.value.id}/comment/${questiionBoardCommentId}`, {
        comment: editingCommentText.value,
      });

      // 수정 완료 후 댓글 업데이트
      await getQuestionBoardComment(questionBoard.value.id);

      // 수정 상태 초기화
      cancelEdit();

      alert('댓글이 수정되었습니다.');

    } catch (error) {
      console.log('에러가 발생했습니다. ', error);
      alert('댓글 수정에 실패했습니다.');
    }
  };

  const deleteComment = async (questiionBoardCommentId) => {
    try {
      if (confirm('정말 댓글을 삭제하시겠습니까?')) {
        const response = await apiClient.delete(`/main/question_board/${questionBoard.value.id}/comment/${questiionBoardCommentId}`);

        await getQuestionBoardComment(questionBoard.value.id);

        alert('댓글이 삭제되었습니다.');
      }
    } catch (error) {
      console.log('에러가 발생했습니다. ', error);
      alert('댓글 삭제에 실패했습니다.');
    }
  };

  const like = async () => {
    try {
      const response = await apiClient.post(`/main/question_board/${questionBoard.value.id}/like`);

      questionBoard.value.likes += 1;

      alert('좋아요가 반영되었습니다.');

    } catch (error) {
      console.log('에러가 발생했습니다. ', error);
      if (error.response.data.resultCode === 'ALREADY_LIKED') {
        if (confirm("이미 '좋아요'를 누른 글입니다. 취소하시겠습니까?")) {
          await apiClient.delete(`/main/question_board/${questionBoard.value.id}/like`);
          questionBoard.value.likes -= 1;
        }
      } else {
        alert('좋아요 처리에 실패했습니다.');
      }
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
    getQuestionBoardComment(questionBoard.value.id);
    scrollToFirstComment();
  };

  const prevPageAndFetch = () => {
    prevPage();
    getQuestionBoardComment(questionBoard.value.id);
    scrollToFirstComment();
  };

  const goToPageAndFetch = (pageNumber) => {
    goToPage(pageNumber);
    getQuestionBoardComment(questionBoard.value.id);
    scrollToFirstComment();
  };


  onMounted(() => {
    const boardId = route.params.id;
    getQuestionBoard(boardId);
    getQuestionBoardComment(boardId);
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