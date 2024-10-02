<template>
  <div class="container-fluid py-4">
    <div class="row mt-4">
      <div class="col-lg-9 col-12 mx-auto position-relative">
        <div class="card">
          <div class="card-header p-3 pt-2">
            <div class="icon icon-lg icon-shape bg-gradient-dark shadow text-center border-radius-xl mt-n4 me-3 float-start">
              <i class="material-icons opacity-10">group</i>
            </div>
            <h6 class="mb-0">New Study Group</h6>
          </div>
          <div class="card-body pt-2">
            <label class="mb-2 form-label">Study Group Name</label>
            <div class="input-group input-group-dynamic">
              <input v-model="studyGroupName" type="text" class="form-control" id="studyGroupName" placeholder="스터디 그룹 이름">
            </div>
            <div class="row mt-4">
              <div class="col-12 col-md-12">
                <label class="mb-2 form-label">Description</label>
                <div class="input-group input-group-static">
                  <textarea v-model="description"
                            class="form-control" 
                            placeholder="스터디 그룹의 설명을 입력해 주세요."
                             rows="8" spellcheck="false">
                  </textarea>
                </div>
              </div>
            </div>
            <div class="d-flex justify-content-end mt-4">
              <router-link to="/main/study_group" class="btn btn-light m-0">Cancel</router-link>
              <button type="button"  @click="createStudyGroup" name="button" class="btn bg-gradient-dark m-0 ms-2">Create Study Group</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
  import { ref } from 'vue';
  import apiClient from '../../../config/authConfig';
  import { useRouter } from 'vue-router';
  
  const router = useRouter();
  const studyGroupName = ref('');
  const description = ref('');

  const createStudyGroup = async () => {
    if (!validateForm()) {
      return;
    }

    try {
      const response = await apiClient.post('/main/study_group', {
        studyGroupName: studyGroupName.value,
        description: description.value,
      });

      console.log('스터디 그룹 생성 성공', response.data);

      alert('스터디 그룹이 생성되었습니다.');

      router.push('/main/study_group');
    } catch (error) {
      console.log('에러 발생', error);
      alert('스터디 그룹 생성에 실패했습니다.');
    }
  };


  const validateForm = () => {
    if (!studyGroupName.value) {
      alert('스터디 그룹명이 입력되지 않았습니다.');
      return false;
    }

    if (!description.value) {
      alert('스터디 그룹 설명이 입력되지 않았습니다.');
      return false;
    }

    return true;
  };


</script>
<style scoped>

</style>