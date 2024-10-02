<template>
  <div class="container-fluid py-4">
    <div class="row mt-4">
      <div class="col-lg-9 col-12 mx-auto position-relative">
        <div class="card">
          <div class="card-header p-3 pt-2">
            <div class="icon icon-lg icon-shape bg-gradient-dark shadow text-center border-radius-xl mt-n4 me-3 float-start">
              <i class="material-icons opacity-10">article</i>
            </div>
            <h6 class="mb-0">New Study Group Board</h6>
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
              <router-link to="/main/study_group" class="btn btn-light m-0">Cancel</router-link>
              <button type="button"  @click="createStudyGroupBoard" name="button" class="btn bg-gradient-dark m-0 ms-2">Create Board</button>
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
  import { useRouter } from 'vue-router';
  
  const router = useRouter();
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

  const createStudyGroupBoard = async () => {
    if (!validateForm()) {
      return;
    }

    try {
      const response = await apiClient.post('/main/study_group/board', {
        title: studyGroupBoardTitle.value,
        content: studyGroupBoardContent.value,
      });

      console.log(response.data.result);

      alert('게시글이 작성되었습니다.');

      router.push("/main/study_group");
    } catch (error) {
      console.log('에러 발생', error);
      alert('스터디 그룹 게시글 생성에 실패했습니다.');
    }
  };

  onMounted(() => {
    getTags();
  });
  
</script>
<style scoped>
</style>