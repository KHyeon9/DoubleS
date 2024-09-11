<template>
  <div class="page-header min-height-300 border-radius-xl mt-4" style="background-image: url('https://images.unsplash.com/photo-1531512073830-ba890ca4eba2?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&amp;ixlib=rb-1.2.1&amp;auto=format&amp;fit=crop&amp;w=1920&amp;q=80');">
    <span class="mask  bg-gradient-primary  opacity-6"></span>
  </div>
  <div class="card card-body mx-3 mx-md-4 mt-n6">
    <div class="row gx-4 mb-2">
      <div class="col-auto my-auto">
        <div class="h-100">
          <h5 class="mb-1">
            {{ nickname }}
          </h5>
          <p class="mb-0 font-weight-normal text-sm">
            {{ userId }}
          </p>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="row mt-4">
        <div class="col-md-5 col-lg-4">
          <div class="card blur shadow-blur max-height-vh-70 overflow-auto overflow-x-hidden mb-5 mb-lg-0">
            <div class="card-header p-3">
              <h6>Friends</h6>
              <div class="input-group input-group-outline">
                <label class="form-label">Search contact</label>
                <input type="text" class="form-control" onfocus="focused(this)" onfocusout="defocused(this)">
              </div>
            </div>
            <div class="chatRoomList card-body p-2">
              <a v-if="chatRoomList.length == 0" class="d-block m-2 border-radius-lg bg-light">
                <div class="d-flex p-3">
                  <div class="ms-3">
                    <div class="justify-content-between align-items-center">
                      <h6 class="text-muted mb-0">채팅방이 없습니다.
                        <span class="badge badge-success"></span>
                      </h6>
                    </div>
                  </div>
                </div>
              </a>
              <a v-else v-for="chatRoom in chatRoomList" 
                :key="chatRoom.id" 
                @click="connect(chatRoom.id, chatRoom.user1, chatRoom.user2)"
                class="chatRoom d-block m-2 border-radius-lg"
                :class="[activeLink === chatRoom.id ? 'bg-gradient-primary' : 'bg-light']"
              >
                <div class="d-flex p-2">
                  <div class="ms-3">
                    <h6 v-if="userId === chatRoom.user1.userId" class="mb-0" :class="[activeLink === chatRoom.id ? 'text-white' : '']">{{ chatRoom.user2.userId }}</h6>
                    <h6 v-if="userId !== chatRoom.user1.userId" class="mb-0" :class="[activeLink === chatRoom.id ? 'text-white' : '']">{{ chatRoom.user1.userId }}</h6>
                    <p class="text-xs mb-2" :class="[activeLink === chatRoom.id ? 'text-white' : 'text-muted']">1 hour ago</p>
                    <span v-if="chatRoom.lastMessage !== null" class="text-sm col-12 p-0 text-truncate d-block" :class="[activeLink === chatRoom.id ? 'text-white' : 'text-muted']">{{ chatRoom.lastMessage }}</span>
                    <span v-else class="text-sm col-12 p-0 d-block" :class="[activeLink === chatRoom.id ? 'text-white' : 'text-muted']">전송된 메세지가 없습니다.</span>
                  </div>
                </div>
              </a>
            </div>
          </div>
        </div>
        <div class="col-md-7 col-lg-8">
          <div class="card blur shadow-blur max-height-vh-70">
            <div class="card-header p-0 position-relative mt-n4 mx-3 z-index-2 bg-transparent">
              <div class="bg-gradient-primary shadow-primary border-radius-lg p-3">
                <div class="row">
                  <div class="col-md-9 col-lg-11">
                    <div class="d-flex align-items-center">
                      <div class="ms-3">
                        <h6 class="mb-0 d-block text-white">{{ nowChatUserUserNickname }}</h6>
                        <span class="text-sm text-white opacity-8">{{ nowChatUserUserId }}</span>
                      </div>
                    </div>
                  </div>
                  <div class="col-1 my-auto">
                    <div class="dropdown">
                      <button class="btn btn-icon-only text-white mb-0" type="button" data-bs-toggle="dropdown">
                        <i class="material-icons">settings</i>
                      </button>
                      <ul class="dropdown-menu dropdown-menu-end me-sm-n2 p-2" aria-labelledby="chatmsg">
                        <li>
                          <a class="dropdown-item border-radius-md" href="javascript:;">
                            Profile
                          </a>
                        </li>
                        <li>
                          <a class="dropdown-item border-radius-md" href="javascript:;">
                            Mute conversation
                          </a>
                        </li>
                        <li>
                          <a class="dropdown-item border-radius-md" href="javascript:;">
                            Block
                          </a>
                        </li>
                        <li>
                          <a class="dropdown-item border-radius-md" href="javascript:;">
                            Clear chat
                          </a>
                        </li>
                        <li>
                          <hr class="dropdown-divider">
                        </li>
                        <li>
                          <a class="dropdown-item border-radius-md text-danger" href="javascript:;">
                            Delete chat
                          </a>
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="chat card-body overflow-auto overflow-x-hidden">
              <div v-if="websocketClient === ''" class="mb-4">
                <div class="col-auto">
                  <div class="card ">
                    <div class="card-body py-2 px-3">
                      <p class="mb-1 text-center">
                        채팅방을 선택해주세요!
                      </p>
                    </div>
                  </div>
                </div>
              </div>
              <div v-else-if="chatMessageList && chatMessageList.length === 0" class="mb-4">
                <div class="col-auto">
                  <div class="card ">
                    <div class="card-body py-2 px-3">
                      <p class="mb-1 text-center">
                        채팅창이 연결되었습니다!
                      </p>
                    </div>
                  </div>
                </div>
              </div>
              <div v-else v-for="message in chatMessageList" class="message">
                <div v-if="message.userId !== userId" class="row justify-content-start mb-4">
                  <div class="col-auto">
                    <div class="card ">
                      <div class="card-body py-2 px-3">
                        <p class="mb-1">
                          {{ message.message }}
                        </p>
                        <div class="d-flex align-items-center text-sm opacity-6">
                          <small>{{ formatDateTime(message.createdAt) }}</small>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div v-if="message.userId === userId" class="row justify-content-end text-right mb-4">
                  <div class="col-auto">
                    <div class="card bg-gradient-primary">
                      <div class="card-body py-2 px-3 text-white">
                        <p class="mb-1">
                          {{ message.message }}
                        </p>
                        <div class="d-flex align-items-center justify-content-end text-sm opacity-6">
                          <small>{{ formatDateTime(message.createdAt) }}</small>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="card-footer d-block">
              <form class="align-items-center" @submit.prevent="sendMessage">
                <div class="input-group input-group-outline d-flex">
                  <input v-model="message" type="text" placeholder="메세지를 입력해주세요." class="form-control form-control-lg">
                  <button type="submit" class="btn bg-gradient-primary mb-0">
                    <i class="material-icons">send</i>
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
  import { nextTick, onMounted, onUnmounted, computed, ref } from 'vue';
  import { Client } from '@stomp/stompjs';
  import apiClient from '../../../config/authConfig';
  import { useAuthStore } from '../../../store/authStore';
  import { useFormat } from '../../../utils/format'

  const { formatDateTime } = useFormat();

  const activeLink = ref(0);

  const authStore = useAuthStore();
  const nickname = computed(() => authStore.nickname);
  const userId = computed(() => authStore.userId);
  
  const chatRoomList = ref([]);
  const chatMessageList = ref([]);

  const nowChatUserUserId = ref('');
  const nowChatUserUserNickname = ref('');
  const nowChatRoomId = ref(0);

  const message = ref('');
  let websocketClient = '';

  const sendMessage = async () => {
    if (!websocketClient) {
      alert('채팅방을 선택해 주세요.');
      return;
    }

    console.log('userId : ', userId.value);
    console.log('chatRoomId : ', nowChatRoomId.value);
    console.log('message : ', message.value);

    await websocketClient.publish({
      destination: `/pub/chat/room/${nowChatRoomId.value}`,
      body: JSON.stringify({
        sendUserId: userId.value,
        chatRoomId: nowChatRoomId.value,
        message: message.value
      })
    });

    message.value = '';
  }

  const connect = (chatRoomId, user1, user2) => {
    setActive(chatRoomId);

    nowChatRoomId.value = chatRoomId;

    if (user1.userId === userId) {
      nowChatUserUserId.value = user2.userId;
      nowChatUserUserNickname.value = user2.nickname;
    } else {
      nowChatUserUserId.value = user1.userId;
      nowChatUserUserNickname.value = user1.nickname;
    }

    // 다른 채팅을 눌렀을 때, 이전에 연결된 웹소켓 종료
    if (websocketClient && websocketClient.active) {
      websocketClient.deactivate();  // 연결을 종료
      console.log('연결 종료');
    }

    chatMessageList.value = [];

    const url = 'ws://localhost:8080/ws/init';
    const token = authStore.token;

    console.log('chatRoomId', chatRoomId);

    websocketClient = new Client({
      brokerURL: url,
      connectHeaders: {
        Authorization: `Bearer ${token}`
      },
      onConnect: async () => {
        console.log('onConnect 실행')
        
        await websocketClient.subscribe(`/sub/chat/room/${chatRoomId}`, async msg => {
          try {
            const messageBody = JSON.parse(msg.body);
            chatMessageList.value.push(messageBody);
            console.log(messageBody);
          } catch (error) {
            console.log('메세지를 가져오는데 에러가 발생했습니다. ', error);
            alert('메세지를 가져오는데 에러가 발생했습니다.');
          }

          await nextTick();
          scrollToBottom();
        });
        
        await websocketClient.publish({
          destination: `/pub/chat/room/${chatRoomId}/entered`,
          body: JSON.stringify({ chatRoomId: chatRoomId })
        });

      },
      onStompError: (frame) => {
        console.error('STOMP error:', frame);
      },
      onWebSocketError: (error) => {
        console.error('웹 소켓 error:', error);
      }
    });

    websocketClient.activate();
  };

  const getChatRoomList = async () => {
    try {
      const response = await apiClient.get(`/main/chat/room`);

      console.log(response.data.result);

      chatRoomList.value = response.data.result;

    } catch (error) {
      onsole.log('채팅방을 가져오지 못했습니다.', error);
      alert('채팅방을 가져오는데 오류가 생겼습니다.');
    }
  };

  const setActive = (chatRoomId) => {
    activeLink.value = chatRoomId;
  };

  const scrollToBottom = () => {
    const chatContainer = document.querySelector('.chat');
    chatContainer.scrollTop = chatContainer.scrollHeight;
  };

  onMounted(() => {
    getChatRoomList();
  });

  onUnmounted(() => {
    if (websocketClient && websocketClient.active) {
      websocketClient.deactivate();  // 연결을 종료
      console.log('연결 종료');
    }
  });

</script>
<style scoped>
  .chat {
    height: 600px;
  }

  .chatRoomList {
    height: 600px;
  }
</style>