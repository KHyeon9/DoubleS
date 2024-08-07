<template>
  <div class="container-fluid py-4">
    <div class="row">
      <div class="col-md-12">
        <div class="row">
          <div class="col-12">
            <div class="card bg-transparent shadow-xl">              
              <div class="overflow-hidden position-relative border-radius-xl">
                <img src="/assets/img/welcome.png" class="welcome" alt="Welcome Image">
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-md-6 mt-4">
        <div class="card my-4">
          <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
            <div class="bg-gradient-primary shadow-primary border-radius-lg pt-4 pb-3">
              <h6 class="text-white text-capitalize ps-3">최근 게시물</h6>
            </div>
          </div>
          <div class="card-body pt-4 p-3">
            <ul class="list-group">
              <li v-for="questionBoard in questionBoardList" :key="questionBoard.id" 
                  class="list-group-item border-0 d-flex p-4 mb-2 bg-gray-100 border-radius-lg">
                <div class="d-flex flex-column">
                  <h6 class="mb-3 text-sm">{{ questionBoard.title }}</h6>
                  <span class="mb-2 text-xs">{{ truncateText(questionBoard.content) }}</span>
                </div>
                <div class="ms-auto text-end">
                  <router-link 
                        :to= "`/main/question_board/${questionBoard.id}`"
                        class="btn btn-link text-dark px-3 mb-0" 
                      >
                        <i class="material-icons text-sm me-2">play_arrow</i>Go
                  </router-link>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div class="col-md-6 mt-4">
        <div class="col-12">
          <div class="card my-4">
            <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
              <div class="bg-gradient-primary shadow-primary border-radius-lg pt-4 pb-3">
                <h6 class="text-white text-capitalize ps-3">공지사항</h6>
              </div>
            </div>
            <div class="card-body px-0 pb-2">
              <div class="table-responsive p-0">
                <table class="table align-items-center mb-0">
                  <thead>
                    <tr class="table-header">
                      <th class="text-xs text-uppercase text-secondary font-weight-bolder opacity-7 col-9">Title</th>
                      <th class="text-xs text-center text-uppercase text-secondary font-weight-bolder opacity-7">Date</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="noticeBoard in noticeBoardList" :key="noticeBoard.id">
                      <td>
                        <div class="d-flex px-2 py-1">
                          <div class="d-flex flex-column justify-content-center">
                            <h6 class="mb-0 text-sm">{{ noticeBoard.title }}</h6>
                          </div>
                        </div>
                      </td>
                      <td class="align-middle text-center">
                        <span class="text-secondary text-xs font-weight-bold">{{ formatDate(noticeBoard.createdAt) }}</span>
                      </td>
                    </tr>
                  </tbody>
                </table>
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
  import apiClient from '../../../config/authConfig';
  import moment from 'moment';

  const noticeBoardList = ref([]);
  const questionBoardList = ref([]);

  const getData = async () => {
    try {
      const noticeBoardResponse = await apiClient.get('/main/notice_board_list');
      const qeustionBoardResponse = await apiClient.get('/main/question_board_list');
      
      noticeBoardList.value = noticeBoardResponse.data.result;
      questionBoardList.value = qeustionBoardResponse.data.result;
      console.log(noticeBoardList.value);
      console.log(questionBoardList.value);
    } catch (error) {
      console.log('데이터 가져오기 실패: ', error);
      alert('데이터 가져오기를 실패했습니다.');
    }
  };

  const formatDate = (date) => {
    return moment(date).format('YYYY/MM/DD');
  };

  const truncateText = (text) => {
    // 전체 길이가 maxLength보다 작으면 그대로 반환
    if (text.length <= 20) {
      return text;
    }

    return text.slice(0, 20) + '...';
  };


  onMounted(() => {
    getData();
  });
</script>
<style scoped>
  .table-header {
    font-size: 18px;
  }

  .img-container {
    height: 500px;
  }
  .welcome {
    width: 100%;
    height: 80%;
  }
</style>