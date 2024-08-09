<template>
  <div class="container-fluid px-2 px-md-4">
    <div class="card card-body mx-3 mx-md-4">
      <div class="row gx-4 mb-2">
        <div class="col-auto my-auto">
          <div class="h-100 ms-4">
            <h3 class="mt-2 mb-2">
              {{ questionBoard.title }}
            </h3>
            <p class="mb-0 font-weight-normal text-s">
              {{ questionBoard.user.userId }}
            </p>
            <p class="mb-0 font-weight-normal text-sm">
              {{ formatDate(questionBoard.createdAt) }}
            </p>
          </div>
        </div>
        <div class="col-lg-3 col-md-4 my-sm-auto ms-sm-auto me-sm-0 mx-auto mt-3">
          <div class="nav-wrapper position-relative end-0">
            <ul class="nav nav-pills nav-fill p-1" >
              <li v-if="userId == questionBoard.user.userId" class="nav-item" role="presentation">
                <router-link :to="`/main/question_board/modify/${questionBoard.id}`"  class="nav-link mb-0 px-0 py-1 " role="tab">
                  <i class="material-icons text-lg position-relative">edit</i>
                  <span class="ms-1">Edit</span>
                </router-link>
              </li>
              <li v-if="userId == questionBoard.user.userId" class="nav-item" role="presentation">
                <button @click="deleteQuestionBoard" class="nav-link mb-0 px-0 py-1" role="tab">
                  <i class="material-icons text-lg position-relative">delete</i>
                  <span class="ms-1">Delete</span>
                </button>
              </li>
              <li v-if="userId != questionBoard.user.userId" class="nav-item" role="presentation">
                <a class="nav-link mb-0 px-0 py-1" role="tab">
                  <i class="material-icons text-lg position-relative">email</i>
                  <span class="ms-1">Messages</span>
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
                <p class="postContent mb-4">
                  {{ questionBoard.content }}
                </p>
                <div class="row align-items-center px-2 mt-4 mb-2">
                  <div class="col-sm-6">
                    <div class="d-flex">
                      <div class="d-flex align-items-center">
                        <i class="material-icons text-sm me-1 cursor-pointer">visibility</i>
                        <span class="text-sm me-3 ">{{ questionBoard.viewCounts }}</span>
                      </div>
                      <div class="d-flex align-items-center">
                        <i class="material-icons text-sm me-1 cursor-pointer">thumb_up</i>
                        <span class="text-sm me-3 ">{{ questionBoard.likes }}</span>
                      </div>
                      <div class="d-flex align-items-center">
                        <i class="material-icons text-sm me-1 cursor-pointer">mode_comment</i>
                        <span class="text-sm me-3">{{ questionBoard.comments }}</span>
                      </div>
                    </div>
                  </div>
                  <hr class="horizontal dark my-3">
                </div>
                <div class="mb-1">
                  <div class="d-flex mt-3">
                    <div class="flex-shrink-0">
                      <i class="material-icons cursor-pointer">
                        person
                      </i>
                    </div>
                    <div class="flex-grow-1 ms-3">
                      <h6 class="h5 mt-0">Michael Lewis</h6>
                      <p class="text-s">I always felt like I could do anything. That’s the main thing people are controlled by! Thoughts- their perception of themselves!</p>
                      <p class="text-xs text-end me-4">DateTime</p>
                      <div class="ms-auto text-end">
                        <a  class="btn btn-link text-danger text-gradient px-3 mb-0">
                          <i class="material-icons text-sm me-2" >delete</i>
                          Delete
                        </a>
                        <a class="btn btn-link text-dark px-3 mb-0" href="javascript:;" >
                          <i class="material-icons text-sm me-2">edit</i>
                          Edit
                        </a>
                      </div>
                    </div>
                  </div>
                  <div class="d-flex mt-4">
                    <div class="flex-grow-1 my-auto">
                      <div class="input-group input-group-static">
                        <textarea class="form-control" placeholder="Write your comment" rows="4" spellcheck="false"></textarea>
                      </div>
                    </div>
                    <button class="btn bg-gradient-primary btn-sm mt-auto mb-0 ms-2" type="button" name="button"><i class="material-icons text-sm">send</i></button>
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
  import { ref, onMounted } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { useAuthStore } from '../../../store/authStore';
  import { computed } from 'vue';
  import apiClient from '../../../config/authConfig';
  import moment from 'moment';

  const route = useRoute();
  const router = useRouter();
  const authStore = useAuthStore();
  const questionBoard = ref({});
  const userId = computed(() => authStore.userId)

  const getBoardData = async (id) => {
    try {
      const response = await apiClient.get(`/main/question_board/${id}`);
      questionBoard.value = response.data.result

      console.log( questionBoard.value);
    } catch (error) {
      console.log('에러가 발생했습니다.', error);
      alert('질문 게시글을 가져오지 못했습니다.');
    }
  };

  const deleteQuestionBoard = async () => {
    try {
      if (confirm('정말 삭제하시겠습니까?')) {
        const response = await apiClient.delete(`/main/question_board/${questionBoard.value.id}`);
        console.log(response);

        router.push('/main/question_board');
      }
    } catch (error) {
      console.log('에러가 발생했습니다. ', error);
      alert('질문 게시글을 삭제하는데 실패했습니다.');
    }
  };

  const formatDate = (date) => {
    return moment(date).format('YYYY/MM/DD');
  };


  onMounted(() => {
    const boardId = route.params.id;
    getBoardData(boardId);
  });
</script>
<style scoped>
  .postContent {
    font-size: 19px;
  }
</style>