<template>
  <div v-if="studyGroupData === null" class="d-sm-flex justify-content-between mb-3">
    <div>
      <router-link to="/main/study_group/new" class="btn btn-icon bg-gradient-primary">
        New Study Group
      </router-link>
    </div>
  </div>
  <div class="row">
    <div class="col-12">
      <div v-if="studyGroupData !== null" class="card card-body mb-5">
        <div class="row align-items-center">
          <div class="col-12 my-auto">
            <div class="h-100">
              <div class="d-flex justify-content-between align-items-center">
                <h5 class="mb-1 font-weight-bolder">
                  {{ studyGroupData.studyGroupName }}
                </h5>
                <div>
                  <a
                    v-if="myPosition === 'Member'"
                    class="me-2 mt-2"
                    role="button"
                  >
                    <i class="material-icons text-lx position-relative">logout</i>
                  </a>
                  <router-link 
                    v-if="myPosition === 'Leader'"
                    :to="`/main/study_group/modify`"
                    class="me-3 mt-2"
                  >
                    <i class="material-icons text-lx position-relative">settings</i>
                  </router-link>
                  <a
                    v-if="myPosition === 'Leader'"
                    @click="deleteStudyGroup"
                    class="me-2 mt-2"
                    role="button"
                  >
                    <i class="material-icons text-lx position-relative text-danger">delete</i>
                  </a>
                </div>
              </div>
              <p class="mb-0 font-weight-normal text-sm">
                {{ studyGroupData.description }}
              </p>
            </div>
          </div>
        </div>
      </div>
      <div class="card my-4">
        <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
          <div class="bg-gradient-primary shadow-primary border-radius-lg pt-4 pb-3">
            <h6 class="text-white text-capitalize ps-3">스터디 그룹원</h6>
          </div>
        </div>
        <div class="card-body px-0 pb-2">
          <div class="table-responsive p-0">
            <!-- 스터디 그룹 정보가 없을 때 -->
            <div v-if="studyGroupData === null" class="m-5">
              <p class="text-center">스터디 그룹에 가입되어 있지 않습니다.</p>
            </div>

            <!-- 스터디 그룹 정보가 있을 때 -->
            <table v-else class="table align-items-center mb-0">
              <thead>
                <tr>
                  <th class="text-uppercase text-secondary text-xs font-weight-bolder opacity-7">유저 정보</th>
                  <th class="text-uppercase text-secondary text-xs font-weight-bolder opacity-7 ps-2">직책</th>
                  <th class="text-center text-uppercase text-secondary text-xs font-weight-bolder opacity-7">가입 날짜</th>
                  <th class="text-secondary opacity-7"></th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="userInfo in studyGroupMemberList" :key="userInfo.userId">
                  <td>
                    <div class="d-flex px-2 py-1">
                      <div class="d-flex flex-column justify-content-center">
                        <h6 class="mb-0 text-sm">{{ userInfo.nickname }} ({{ userInfo.userId }})</h6>
                        <p class="text-xs text-secondary mb-0">{{ userInfo.email }}</p>
                      </div>
                    </div>
                  </td>
                  <td>
                    <p class="text-sm font-weight-bold mb-0">{{ userInfo.position }}</p>
                  </td>
                  <td class="align-middle text-center">
                    <span class="text-secondary text-sm font-weight-bold">{{ formatDate(userInfo.joinedAt) }}</span>
                  </td>
                  <td class="align-middle">
                    <a v-if="myPosition === 'Leader' && userInfo.userId !== userId"
                      @click="exitStudyGroup(userInfo.userId)" 
                      class="text-secondary font-weight-bold text-sm" role="button"
                    >
                      <i class="material-icons text-lg position-relative pointer">logout</i>
                    </a>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="row">
    <div class="col-12">
      <div class="card my-4">
        <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2">
          <div class="bg-gradient-primary shadow-primary border-radius-lg pt-4 pb-3">
            <h6 class="text-white text-capitalize ps-3">스터디 그룹 게시판</h6>
          </div>
        </div>
        <div class="card-body px-0 pb-2">
          <div class="table-responsive p-0">
            <!-- 스터디 그룹 정보가 없을 때 -->
            <div v-if="studyGroupData === null" class="m-5">
              <p class="text-center">스터디 그룹에 가입되어 있지 않습니다.</p>
            </div>

            <!-- 스터디 그룹 게시글이 없을 때 -->
            <div v-else-if="studyGroupBoardList.length == 0" class="m-5">
              <p class="text-center">게시글이 없습니다.</p>
            </div>

            <!-- 스터디 그룹 정보가 있을 때 -->
            <table v-else class="table align-items-center justify-content-center mb-0">
              <thead>
                <tr>
                  <th class="col-1 text-center text-uppercase text-secondary text-xs font-weight-bolder opacity-7">번호</th>
                  <th class="col-6 text-center text-uppercase text-secondary text-xs font-weight-bolder opacity-7">제목</th>
                  <th class="col-3 text-center text-uppercase text-secondary text-xs font-weight-bolder opacity-7">작성자</th>
                  <th class="col-2 text-center text-uppercase text-secondary text-xs font-weight-bolder text-center opacity-7">작성일</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="studyGroupBoard in studyGroupBoardList" :key="studyGroupBoard.id">
                  <td>
                    <div class="text-center px-2">
                      <div class="my-auto">
                        <h6 class="mb-0 text-sm text-center">{{ studyGroupBoard.id }}</h6>
                      </div>
                    </div>
                  </td>
                  <td>
                    <router-link :to="`/main/study_group/board/${studyGroupBoard.id}`">
                      <div class="text-center px-2">
                        <div class="my-auto">
                          <h6 class="mb-0 text-sm">{{ studyGroupBoard.title }}</h6>
                        </div>
                      </div>
                    </router-link>
                  </td>
                  <td>
                    <p class="text-sm text-center font-weight-bold mb-0">{{ studyGroupBoard.user.userId }}</p>
                  </td>
                  <td>
                    <p class="text-xs text-center font-weight-bold mb-0">{{ formatDate(studyGroupBoard.createdAt) }}</p>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <nav  v-if="studyGroupData !== null" class="mt-4 me-3 nav nav-bar d-flex justify-content-between" aria-label="Page navigation">
            <!-- Create Board 버튼 -->
            <router-link to="/main/study_group/board/new" class="btn btn-icon bg-gradient-primary ms-3">
              Create Board
            </router-link>
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
  </div>
</template>
<script setup>
  import apiClient from '../../../config/authConfig';
  import { ref, onMounted } from 'vue';
  import { useFormat } from '../../../utils/format';
  import { useAuthStore } from '../../../store/authStore.js';
  import { usePagination } from '../../../utils/pagination';
  import { useRouter } from 'vue-router';

  const authStore = useAuthStore();
  const router = useRouter();
  const userId = authStore.userId;
  const studyGroupData = ref({});
  const myPosition = ref('');
  const studyGroupMemberList = ref([]);
  const studyGroupBoardList = ref([]);

  const { formatDate } = useFormat();

  const { 
    page, 
    totalPages, 
    nextPage, 
    prevPage, 
    goToPage, 
    paginatedPageNumbers,
  } = usePagination();


  const getStudyGroupInfo = async () => {
    try {
      const response = await apiClient.get('/main/study_group');

      console.log(response.data.result);
      studyGroupData.value = response.data.result;

      getStudyGroupMemberList();
      getStudyGroupBoardList();

    } catch (error) {
      if (error.response.data.resultCode == 'USER_STUDY_GROUP_NOT_FOUND') {
        console.log('가입된 스터디 그룹이 없습니다.', error);
        studyGroupData.value = null;
      } else {
        console.log('스터디 정보를 가져오지 못했습니다.', error);
        alert('스터디 정보를 가져오는데 오류가 생겼습니다.');
      }
    }
  };

  const getStudyGroupMemberList = async () => {
    try{
      const response = await apiClient.get('/main/study_group/member');
      
      const memberList = response.data.result;

      studyGroupMemberList.value = memberList;

      for(let member of memberList) {
        if (member.userId === userId) {
          myPosition.value = member.position;
          break;
        }
      }
      console.log(myPosition.value);

    } catch (error) {
      console.log('스터디 그룹원을 가져오지 못했습니다.', error);
      alert('스터디 그룹원 정보를 가져오는데 오류가 생겼습니다.');
    }
  };

  const getStudyGroupBoardList = async () => {
    try {
      const response = await apiClient.get('/main/study_group/board', {
        params: {
          page: page.value,
        },
      });

      console.log(response.data.result);

      studyGroupBoardList.value = response.data.result.content;
      totalPages.value = response.data.result.totalPages;

    } catch (error) {
      console.log('스터디 그룹 게시글을 가져오지 못했습니다.', error);
      alert('스터디 그룹 게시글 리스트를 가져오는데 오류가 생겼습니다.');
    }
  };

  const exitStudyGroup = async (userId) => {
    try {
      const response = await apiClient.delete(`/main/study_group/exit`, {
        params: {
          deleteUserId: userId,
        },
      });

      console.log(response.data.result);

      alert(`ID가 ${userId}인 유저가 스터디 그룹에서 삭제되었습니다.`);

      getStudyGroupMemberList();

    } catch (error) {
      console.log('스터디 그룹에서 해당 유저를 삭제하지 못했습니다.', error);
      alert('스터디 그룹에서 해당 유저를 삭제하는데 오류가 생겼습니다.');
    }
  };

  const deleteStudyGroup = async () => {
    if (confirm('스터디 그룹을 삭제하시겠습니까?')) {
      try {
        const response = await apiClient.delete('/main/study_group');

        console.log(response.data.result);

        alert('스터디 그룹이 삭제되었습니다.');

        getStudyGroupInfo();
      } catch (error) {
        console.log('스터디 그룹을 삭제하지 못했습니다.', error);
        alert('스터디 그룹을 삭제하는데 오류가 생겼습니다.');
      }
    }
  };

  const nextPageAndFetch = () => {
    nextPage();
    getStudyGroupBoardList();
  };

  const prevPageAndFetch = () => {
    prevPage();
    getStudyGroupBoardList();
  };

  const goToPageAndFetch = (pageNumber) => {
    goToPage(pageNumber);
    getStudyGroupBoardList();
  };
  
  onMounted(() => {
    getStudyGroupInfo();
  });
</script>
<style scoped>
  .active a {
    color: white;
  }
</style>