<template>
  <div class="row">
        <div class="col-lg-10 col-md-10 mx-auto">
          <div class="card mt-4">
            <div class="card-header p-3">
              <h5 class="mb-0">Alarm List</h5>
            </div>
            <div class="alarm-body card-body p-3 pb-0">
              <div v-if="alarmList.length === 0" class="alert alert-dark alert-dismissible text-white" role="alert">
                <span class="text-sm">알림이 존재하지 않습니다.</span>
              </div>
              <div v-else v-for="alarm in alarmList" class="alarm-info">
                <div v-if="alarm.alarmType === 'NEW_LIKE_ON_POST'" class="alert alert-info alert-dismissible text-white" role="alert">
                  <span class="text-sm">질문 게시글인 {{ truncateText(alarm.data, 10) }}에 {{ alarm.alarmCount }}개의 좋아요를 눌렸습니다.</span>
                  <router-link :to="`/main/question_board/${alarm.targetId}`" type="button" class="btn-base me-5 text-lg mt-3 opacity-10">
                    <i class="material-icons opacity-10">play_arrow</i>
                  </router-link>
                  <button type="button" class="btn-close text-lg opacity-10" data-bs-dismiss="alert" aria-label="Close">
                    <i class="material-icons opacity-10">close</i>
                  </button>
                </div>
                <div v-if="alarm.alarmType === 'NEW_COMMENT_ON_POST'" class="alert alert-info alert-dismissible text-white" role="alert">
                  <span class="text-sm">질문 게시글인 {{ truncateText(alarm.data, 10) }}에 {{ alarm.alarmCount }}개의 댓글이 달렸습니다.</span>
                  <router-link :to="`/main/question_board/${alarm.targetId}`" type="button" class="btn-base me-5 text-lg mt-3 opacity-10">
                    <i class="material-icons opacity-10">play_arrow</i>
                  </router-link>
                  <button type="button" class="btn-close text-lg opacity-10" data-bs-dismiss="alert" aria-label="Close">
                    <i class="material-icons opacity-10">close</i>
                  </button>
                </div>
                <div v-if="alarm.alarmType === 'INVITE_STUDY_GROUP'" class="alert alert-info alert-dismissible text-white" role="alert">
                  <span class="text-sm">{{ truncateText(alarm.data, 10) }}가 스터디 그룹에에 초대했습니다.</span>
                  <a @click="joinStudyGroup(alarm.data)" type="button" class="btn-base me-5 text-lg mt-3 opacity-10">
                    <i class="material-icons opacity-10">check</i>
                  </a>
                  <button type="button" class="btn-close text-lg opacity-10" data-bs-dismiss="alert" aria-label="Close">
                    <i class="material-icons opacity-10">close</i>
                  </button>
                </div>
                <div v-if="alarm.alarmType === 'NEW_COMMENT_ON_STUDY_GROUP_POST'" class="alert alert-info alert-dismissible text-white" role="alert">
                  <span class="text-sm">스터디 그룹 게시글인 {{ truncateText(alarm.data, 10) }}에 {{ alarm.alarmCount }}개의 댓글이 달렸습니다.</span>
                  <router-link :to="`study_group/board/${alarm.targetId}`" type="button" class="btn-base me-5 text-lg mt-3 opacity-10">
                    <i class="material-icons opacity-10">play_arrow</i>
                  </router-link>
                  <button type="button" class="btn-close text-lg opacity-10" data-bs-dismiss="alert" aria-label="Close">
                    <i class="material-icons opacity-10">close</i>
                  </button>
                </div>
                <div v-if="alarm.alarmType === 'NEW_CHAT_MESSAGE'" class="alert alert-info alert-dismissible text-white" role="alert">
                  <span class="text-sm">{{ truncateText(alarm.data, 10) }}에게 {{ alarm.alarmCount }}개의 새로운 메시지가 왔습니다.</span>
                  <a @click="goToChatPage(alarm.data)" type="button" class="btn-base me-5 text-lg mt-3 opacity-10">
                    <i class="material-icons opacity-10">play_arrow</i>
                  </a>
                  <button type="button" class="btn-close text-lg opacity-10" data-bs-dismiss="alert" aria-label="Close">
                    <i class="material-icons opacity-10">close</i>
                  </button>
                </div>
                <div v-if="alarm.alarmType === 'CHANGE_LEADER'" class="alert alert-info alert-dismissible text-white" role="alert">
                  <span class="text-sm">스터디 그룹의 리더가 당신으로 변경되었습니다.</span>
                  <button type="button" class="btn-close text-lg opacity-10" data-bs-dismiss="alert" aria-label="Close">
                    <i class="material-icons opacity-10">close</i>
                  </button>
                </div>
              </div>
            </div>
            <!-- 페이징 -->
            <nav class="mt-4 nav nav-bar d-flex justify-content-end" aria-label="Page navigation">
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
          </div>
        </div>
      </div>
</template>
<script setup>
   import apiClient from '../../../config/authConfig';
   import { ref, onMounted } from 'vue';
   import { usePagination } from '../../../utils/pagination';
   import router from '../../../router/router.js';

   const { 
    page, 
    totalPages, 
    nextPage, 
    prevPage, 
    goToPage, 
    paginatedPageNumbers,
    pageScrollTop
  } = usePagination();

   const alarmList = ref([]);

   const getAlarmList = async () => {
    try {
      const response = await apiClient.get('/main/alarm', {
        params: {
          page: page.value,
        },
      });

      console.log(response.data.result);

      alarmList.value = response.data.result.content;
      totalPages.value = response.data.result.totalPages;

    } catch (error) {
      console.log('알람 리스트를 가져오는데 에러 발생', error);
      alert('알람 리스트를 가져오지 못했습니다.');
    }
   };

   const joinStudyGroup = async (userId) => {
      try {
        const response = await apiClient.post('/main/study_group/join', null ,{
          params: {
            leaderUserId: userId,
          }
        });

        console.log(response);

        alert('스터디 그룹에 가입이 되었습니다.');

        router.push('/main/study_group');

      } catch (error) {
        console.log('에러가 발생했습니다.', error);
      alert('스터디 그룹 관련 에러가 발생했습니다.');
      }
   };

  const goToChatPage = async (userId) => {
    try {
      const response = await apiClient.get(`/main/chat/room/user/${userId}`);
      const chatRoomData = response.data.result;

      router.push({
        path: '/main/chat',
        state: { chatRoomData: chatRoomData } 
      });
    } catch (error) {
      console.log('에러가 발생했습니다.', error);
      alert('채팅관련 에러가 발생했습니다.');
    }
  };

  const truncateText = (text, len) => {
    // 전체 길이가 maxLength보다 작으면 그대로 반환
    if (text.length <= len) {
      return text;
    }

    return text.slice(0, len) + '...';
  };

  const nextPageAndFetch = () => {
    nextPage();
    getAlarmList();
    pageScrollTop();
  };

  const prevPageAndFetch = () => {
    prevPage();
    getAlarmList();
    pageScrollTop();
  };

  const goToPageAndFetch = (pageNumber) => {
    goToPage(pageNumber);
    getAlarmList();
    pageScrollTop();
  };

  onMounted(() => {
    getAlarmList();
  });
</script>
<style scoped>
  .active a {
    color: white;
  }

  .alarm-body {
    height: 755px;
  }

  .btn-base {
    background-image: none;
    position: absolute;
    top: 0;
    right: 0;
    z-index: 2;
    box-sizing: content-box;
    width: 1em;
    height: 1em;
    padding: 0.25em 0.25em;
    color: #fff;
    border: 0;
    border-radius: 0.375rem;
  }
</style>