<template>
  <div class="container-fluid py-4">
    <div class="row mt-4">
      <div class="col-lg-9 col-12 mx-auto position-relative">
        <div class="card">
          <div class="card-header p-3 pt-2">
            <div class="icon icon-lg icon-shape bg-gradient-dark shadow text-center border-radius-xl mt-n4 me-3 float-start">
              <i class="material-icons opacity-10">article</i>
            </div>
            <h6 class="mb-0">New Post</h6>
          </div>
          <div class="card-body pt-2">
            <div class="input-group input-group-dynamic">
              <input v-model="postTitle" type="text" class="form-control" id="postTitle" placeholder="Title">
            </div>
            <div class="row mt-4">
              <div class="col-12 col-md-12">
                <label class="mb-2 form-label">Content</label>
                <div class="input-group input-group-static">
                  <textarea v-model="postContent"
                            class="form-control" 
                            placeholder="Write your Content"
                             rows="14" spellcheck="false"></textarea>
                </div>
              </div>
            </div>
            <label class="mt-4 mb-2 form-label">Post Tags</label>
            <div class="d-flex">
              <div class="d-inline">
                <button href="javascript:;" class="btn bg-gradient-dark dropdown-toggle mb-5" data-bs-toggle="dropdown" id="navbarDropdownMenuLink2" aria-expanded="false">
                  {{ postTagName || 'Tag' }}
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
              <router-link to="/main/question_board" class="btn btn-light m-0">Cancel</router-link>
              <button type="button"  @click="createPost" name="button" class="btn bg-gradient-dark m-0 ms-2">Create Post</button>
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
  const tags = ref([]);
  const postTitle = ref('');
  const postContent = ref('');
  const postTag = ref('Free');
  const postTagName = ref('자유');

  const selectTag = (tag) => {
    postTagName.value = tag.value;
    postTag.value = tag.key
  };

  const validateForm = () => {
    if (!postTitle.value) {
      alert('제목이 입력되지 않았습니다.');
      return false;
    }

    if (!postContent.value) {
      alert('내용이 입력되지 않았습니다.');
      return false;
    }

    return true;
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

  const createPost = async () => {
    if (!validateForm()) {
      return;
    }

    try {
      const response = await apiClient.post('/main/question_board', {
        title: postTitle.value,
        content: postContent.value,
        tag: postTag.value
      });

      console.log('게시글 생성 성공', response.data);

      router.push('/main/question_board');
    } catch (error) {
      console.log('에러 발생', error);
    }
  };

  onMounted(() => {
    getTags();
  });
  
</script>
<style scoped>
  .dropdown-menu {
    box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.3);
  }
</style>