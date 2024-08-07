<template>
  <div class="container-fluid px-2 px-md-4">
    
    <div class="card card-body mx-3 mx-md-4">
      <div class="row gx-4 mb-2">
        <div class="col-auto my-auto">
          <div class="h-100 ms-4">
            <h3 class="mt-2 mb-2">
              {{ board.title }}
            </h3>
            <p class="mb-0 font-weight-normal text-s">
              {{ board.user.userId }}
            </p>
            <p class="mb-0 font-weight-normal text-sm">
              {{ formatDate(board.createdAt) }}
            </p>
          </div>
        </div>
        <div class="col-lg-3 col-md-4 my-sm-auto ms-sm-auto me-sm-0 mx-auto mt-3">
          <div class="nav-wrapper position-relative end-0">
            <ul class="nav nav-pills nav-fill p-1" >
              <li class="nav-item" role="presentation">
                <a class="nav-link mb-0 px-0 py-1 " data-bs-toggle="tab" href="javascript:;" role="tab" aria-selected="false" tabindex="-1">
                  <i class="material-icons text-lg position-relative">edit</i>
                  <span class="ms-1">Edit</span>
                </a>
              </li>
              <li class="nav-item" role="presentation">
                <a class="nav-link mb-0 px-0 py-1 " href="javascript:;">
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
                <p class="post_content mb-4">
                  {{ board.content }}
                </p>
                <div class="row align-items-center px-2 mt-4 mb-2">
                  <div class="col-sm-6">
                    <div class="d-flex">
                      <div class="d-flex align-items-center">
                        <i class="material-icons text-sm me-1 cursor-pointer">thumb_up</i>
                        <span class="text-sm me-3 ">{{ board.likes }}</span>
                      </div>
                      <div class="d-flex align-items-center">
                        <i class="material-icons text-sm me-1 cursor-pointer">mode_comment</i>
                        <span class="text-sm me-3">{{ board.comments }}</span>
                      </div>
                    </div>
                  </div>
                  <hr class="horizontal dark my-3">
                </div>
                <div class="mb-1">
                  <div class="d-flex">
                    <div class="flex-shrink-0">
                      <i class="material-icons cursor-pointer">
                        person
                      </i>
                    </div>
                    <div class="flex-grow-1 ms-3">
                      <h6 class="h5 mt-0">Michael Lewis</h6>
                      <p class="text-s">I always felt like I could do anything. That’s the main thing people are controlled by! Thoughts- their perception of themselves!</p>
                      <p class="text-xs text-end me-4">DateTime</p>
                      <div class="ms-auto text-end" data-v-4fa5880f="">
                        <a class="btn btn-link text-danger text-gradient px-3 mb-0" href="javascript:;" data-v-4fa5880f="">
                          <i class="material-icons text-sm me-2" data-v-4fa5880f="">delete</i>
                          Delete
                        </a>
                        <a class="btn btn-link text-dark px-3 mb-0" href="javascript:;" data-v-4fa5880f="">
                          <i class="material-icons text-sm me-2" data-v-4fa5880f="">edit</i>
                          Edit
                        </a>
                      </div>
                    </div>
                  </div>
                  <div class="d-flex mt-3">
                    <div class="flex-shrink-0">
                      <i class="material-icons cursor-pointer">
                        person
                      </i>
                    </div>
                    <div class="flex-grow-1 ms-3">
                      <h6 class="h5 mt-0">Jessica Stones</h6>
                      <p class="text-s">Society has put up so many boundaries, so many limitations on what’s right and wrong that it’s almost impossible to get a pure thought out. It’s like a little kid, a little boy.</p>
                      <p class="text-xs text-end me-4">DateTime</p>
                      <div class="ms-auto text-end" data-v-4fa5880f="">
                        <a class="btn btn-link text-danger text-gradient px-3 mb-0" href="javascript:;" data-v-4fa5880f="">
                          <i class="material-icons text-sm me-2" data-v-4fa5880f="">delete</i>
                          Delete
                        </a>
                        <a class="btn btn-link text-dark px-3 mb-0" href="javascript:;" data-v-4fa5880f="">
                          <i class="material-icons text-sm me-2" data-v-4fa5880f="">edit</i>
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
  import { useRoute } from 'vue-router';
  import apiClient from '../../../config/authConfig';
  import moment from 'moment';

  const route = useRoute();
  const board = ref({});

  const getBoardData = async (id) => {
    try {
      const response = await apiClient.get(`/main/question_board/${id}`);
      board.value = response.data.result

      console.log( board.value);
    } catch (error) {
      console.log('에러가 발생했습니다.', error);
    }
  };

  const formatDate = (date) => {
    return moment(date).format('YYYY/MM/DD');
  };


  onMounted(() => {
    const boardId = route.params.id;
    console.log(boardId);
    getBoardData(boardId);
  });
</script>
<style scoped>
  .post_content {
    font-size: 19px;
  }
</style>