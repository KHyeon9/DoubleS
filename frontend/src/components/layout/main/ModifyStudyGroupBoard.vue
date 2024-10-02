<template>
  <div class="container-fluid py-4">
    <div class="row mt-4">
      <div class="col-lg-9 col-12 mx-auto position-relative">
        <div class="card">
          <div class="card-header p-3 pt-2">
            <div class="icon icon-lg icon-shape bg-gradient-dark shadow text-center border-radius-xl mt-n4 me-3 float-start">
              <i class="material-icons opacity-10">article</i>
            </div>
            <h6 class="mb-0">Modify Study Group Board</h6>
          </div>
          <div class="card-body pt-2">
            <label class="mb-2 form-label">Title</label>
            <div class="input-group input-group-dynamic">
              <input v-model="studyGroupBoardTitle" type="text" class="form-control" id="studyGroupBoardTitle" placeholder="제목을 입력해주세요.">
            </div>
            <div class="row mt-4">
              <div class="col-12 col-md-12">
                <label class="mb-2 form-label">Content</label>
                <div class="input-group input-group-static">
                  <textarea v-model="studyGroupBoardContent"
                            class="form-control" 
                            placeholder="내용을 입력해주세요."
                            rows="14" spellcheck="false">
                  </textarea>
                </div>
              </div>
            </div>
            <div class="d-flex justify-content-end mt-4">
              <router-link :to="`/main/study_group/board/${studyGroupBoard.id}`" class="btn btn-light m-0">Cancel</router-link>
              <button type="button"  @click="modifyStudyGroupBoard" name="button" class="btn bg-gradient-dark m-0 ms-2">Modify Board</button>
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
  const studyGroupBoard = ref({});
  const studyGroupBoardTitle = ref('');
  const studyGroupBoardContent = ref('');

  const validateForm = () => {
    if (!studyGroupBoardTitle.value) {
      alert('제목이 입력되지 않았습니다.');
      return false;
    }

    if (!studyGroupBoardContent.value) {
      alert('내용이 입력되지 않았습니다.');
      return false;
    }

    return true;
  };

  const modifyStudyGroupBoard = async () => {
    if (!validateForm()) {
      return;
    }

    try {
      const response = await apiClient.put(`/main/study_group/board/${studyGroupBoard.value.id}`, {
        title: studyGroupBoardTitle.value,
        content: studyGroupBoardContent.value,
      });

      console.log('스터디 그룹 게시글 수정 성공', response.data);

      alert('게시글이 수정이 되었습니다.');

      router.push(`/main/study_group/board/${studyGroupBoard.value.id}`);
    } catch (error) {
      console.log('에러가 발생했습니다. ', error);
      alert('질문 게시글을 수정하는 데 실패했습니다.')
    }
  };


  const getStudyGroupBoard = async (studyGroupBoardId) => {
    try {
      const response = await apiClient.get(`/main/study_group/board/${studyGroupBoardId}`);

      studyGroupBoard.value = response.data.result

      console.log(studyGroupBoard.value);

      studyGroupBoardTitle.value = studyGroupBoard.value.title;
      studyGroupBoardContent.value = studyGroupBoard.value.content;

    } catch (error) {
      console.log('에러가 발생했습니다. ', error);
      alert('스터디 그룹 게시글을 가져오는 데 실패했습니다.')
    }
  };

  onMounted(() => {
    const studyGroupBoardId = route.params.id;
    getStudyGroupBoard(studyGroupBoardId);
  });
</script>
<style scoped>
  
</style>