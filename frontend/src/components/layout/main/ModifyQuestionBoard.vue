<template>
  <div class="container-fluid py-4">
    <div class="row mt-4">
      <div class="col-lg-9 col-12 mx-auto position-relative">
        <div class="card">
          <div class="card-header p-3 pt-2">
            <div class="icon icon-lg icon-shape bg-gradient-dark shadow text-center border-radius-xl mt-n4 me-3 float-start">
              <i class="material-icons opacity-10">article</i>
            </div>
            <h6 class="mb-0">Modify Question Board</h6>
          </div>
          <div class="card-body pt-2">
            <div class="input-group input-group-dynamic">
              <input v-model="questionBoardTitle" type="text" class="form-control" id="questionBoardTitle" placeholder="Title">
            </div>
            <div class="row mt-4">
              <div class="col-12 col-md-12">
                <label class="mb-2 form-label">Content</label>
                <div class="input-group input-group-static">
                  <textarea v-model="questionBoardContent"
                            class="form-control" 
                            placeholder="Write your Content"
                             rows="14" spellcheck="false">
                  </textarea>
                </div>
              </div>
            </div>
            <label class="mt-4 mb-2 form-label">Question Board Tags</label>
            <div class="d-flex">
              <div class="d-inline">
                <button href="javascript:;" class="btn bg-gradient-dark dropdown-toggle mb-5" data-bs-toggle="dropdown" id="navbarDropdownMenuLink2" aria-expanded="false">
                  {{ questionBoardTagName || 'Tag' }}
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
            <div class="d-flex justify-content-end mt-4">
              <router-link :to="`/main/question_board/${questionBoard.id}`" class="btn btn-light m-0">Cancel</router-link>
              <button type="button"  @click="modifyQuestionBoard" name="button" class="btn bg-gradient-dark m-0 ms-2">Modify Board</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
  import { ref, onMounted } from 'vue';
  import apiClient from '../../../config/authConfig';
  import { useRoute, useRouter } from 'vue-router';

  const route = useRoute();
  const router = useRouter();
  const tags = ref([]);
  const questionBoard = ref({});
  const questionBoardTitle = ref('');
  const questionBoardContent = ref('');
  const questionBoardTag = ref('');
  const questionBoardTagName = ref('');

  const validateForm = () => {
    if (!questionBoardTitle.value) {
      alert('제목이 입력되지 않았습니다.');
      return false;
    }

    if (!questionBoardContent.value) {
      alert('내용이 입력되지 않았습니다.');
      return false;
    }

    return true;
  };

  const modifyQuestionBoard = async () => {
    if (!validateForm()) {
      return;
    }

    try {
      const response = await apiClient.put(`/main/question_board/${questionBoard.value.id}`, {
        title: questionBoardTitle.value,
        content: questionBoardContent.value,
        tag: questionBoardTag.value
      });

      console.log('게시글 수정 성공', response.data);

      router.push(`/main/question_board/${questionBoard.value.id}`);
    } catch (error) {
      console.log('에러가 발생했습니다. ', error);
      alert('질문 게시글을 수정하는 데 실패했습니다.')
    }
  };


  const getQuestionBoard = async (id) => {
    try {
      const response = await apiClient.get(`/main/question_board/${id}`);
      questionBoard.value = response.data.result;

      console.log(response.data.result);

      questionBoardTitle.value = response.data.result.title;
      questionBoardContent.value = response.data.result.content;
      questionBoardTag.value = response.data.result.tag.key;
      questionBoardTagName.value = response.data.result.tag.value;
    } catch (error) {
      console.log('에러가 발생했습니다. ', error);
      alert('질문 게시글을 가져오는 데 실패했습니다.')
    }
  };

  const getTags = async () => {
    try {
      const response = await apiClient.get('/main/question_board/tags');
      tags.value = response.data.result;
      console.log(response.data)
    } catch (error) {
      console.log('태그를 가져오지 못했습니다.', error);
    }
  };

  const selectTag = (tag) => {
    questionBoardTagName.value = tag.value;
    questionBoardTag.value = tag.key
  };

  onMounted(() => {
    const questionBoardId = route.params.id;
    getQuestionBoard(questionBoardId);
    getTags();
  });
</script>
<style scoped>
  
</style>