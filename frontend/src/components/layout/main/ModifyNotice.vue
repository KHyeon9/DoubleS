<template>
  <div class="container-fluid py-4">
    <div class="row mt-4">
      <div class="col-lg-9 col-12 mx-auto position-relative">
        <div class="card">
          <div class="card-header p-3 pt-2">
            <div class="icon icon-lg icon-shape bg-gradient-dark shadow text-center border-radius-xl mt-n4 me-3 float-start">
              <i class="material-icons opacity-10">priority_high</i>
            </div>
            <h6 class="mb-0">Modify Notice</h6>
          </div>
          <div class="card-body pt-2">
            <div class="input-group input-group-dynamic">
              <input v-model="noticeTitle" type="text" class="form-control" id="noticeTitle" placeholder="Title">
            </div>
            <div class="row mt-4">
              <div class="col-12 col-md-12">
                <label class="mb-2 form-label">Content</label>
                <div class="input-group input-group-static">
                  <textarea v-model="noticeContent"
                            class="form-control" 
                            placeholder="Write your Content"
                            rows="14" spellcheck="false">
                  </textarea>
                </div>
              </div>
            </div>
            <div class="d-flex justify-content-end mt-4">
              <router-link to="/main/notice" class="btn btn-light m-0">Cancel</router-link>
              <button type="button"  @click="modifyNotice" name="button" class="btn bg-gradient-dark m-0 ms-2">Modify Notice</button>
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
  const notice = ref({});
  const noticeTitle = ref('');
  const noticeContent = ref('');

  const validateForm = () => {
    if (!noticeTitle.value) {
      alert('제목이 입력되지 않았습니다.');
      return false;
    }

    if (!noticeContent.value) {
      alert('내용이 입력되지 않았습니다.');
      return false;
    }

    return true;
  };

  const modifyNotice = async () => {
    if (!validateForm()) {
        return;
    }
    
    try {
      console.log('수정 버튼 클릭');

      router.push(`/main/notice/${notice.id}`);
    } catch (error) {
      console.log('에러가 발생했습니다. ', error);
      alert('공지사항 수정을 실패했습니다.');
    }
  };

  const getNotice = async (id) => {
    try {
      const response = await apiClient.get(`/main/notice_board/${id}`);
      notice.value = response.data.result;

      console.log(response.data.result);

      noticeTitle.value = response.data.result.title;
      noticeContent.value = response.data.result.content;
    } catch (error) {
      console.log('에러가 발생했습니다.', error);
      alert('공지사항 데이터를 가져오지 못했습니다.');
    }
  };

  onMounted(() => {
    const noticeId = route.params.id;
    getNotice(noticeId);
  });
</script>
<style scoped>

</style>