<template>
  <div class="container-fluid py-4">
    <div class="row d-flex justify-content-center align-items-center">
      <div class="col-md-12">
        <div class="card">
          <div class="card-header p-3">
            <h4 class="mb-0">Todo List</h4>
            <p class="text-sm mb-0">
              일정들을 추가해보세요.
            </p>
          </div>
          <div class="card-body">
            <form @submit.prevent="createTodo" class="d-flex justify-content-center align-items-center mb-4">
              <div :class="['input-group input-group-outline my-3', { 'is-focused': isTaskInputFocused || taskInput }]">
                <label class="form-label">새로운 할 일을 입력해주세요.</label>
                <input 
                  id="taskInput" 
                  v-model="taskInput" 
                  type="text" 
                  class="form-control"
                  @focus="handleTaskInputFocus"
                  @blur="handleTaskInputBlur"
                >
              </div>
              <div class="ms-3 mt-3 col-4">
                <button class="btn bg-gradient-dark dropdown-toggle" data-bs-toggle="dropdown" id="navbarDropdownMenuLink2" aria-expanded="false">
                  {{ editingImportanceType || '중요도' }}
                </button>
                <ul class="dropdown-menu dropdown-menu-lg-start px-2 py-3" aria-labelledby="navbarDropdownMenuLink2" style="">
                  <li v-for="type in importanceTypes" :key="type.key">
                    <a class="dropdown-item border-radius-md" @click="selectType(type)">
                      {{ type.value }}
                    </a>
                  </li>
                </ul>
                <button type="submit" class="btn bg-gradient-dark ms-2">일정 추가</button>
              </div>
            </form>
            <div class="mt-3 mb-6 position-relative">
              <h6>Todo 진행률</h6>
              <div class="mt-3 progress-container">
                <span class="progress-text me-3">
                  {{ percentage }}%
                </span>
                <div class="progress rounded-pill">
                  <div 
                    class="progress-bar rounded-pill bg-gradient-info" 
                    role="progressbar" 
                    :aria-valuenow="percentage" 
                    aria-valuemin="0" 
                    aria-valuemax="100" 
                    :style="{ width: `${percentage}%` }">
                  </div>
                </div>
              </div>
            </div>
            <table class="table mb-4">
              <thead>
                <tr>
                  <th scope="col">내용</th>
                  <th scope="col">중요도</th>
                  <th scope="col">날짜</th>
                  <th scope="col"></th>
                  <th scope="col"></th>
                </tr>
              </thead>
              <tbody v-if="todos.length > 0" class="text-md" v-for="todo in todos" :key="todo.id">
                <tr class="fw-normal">
                  <td v-if="editingTodoId === todo.id" class="align-middle">
                    <div class="input-group input-group-outline my-2">
                      <input v-model="editingTodoText" placeholder="할 일을 입력해주세요." type="text" class="form-control">
                    </div>
                  </td>
                  <td v-else class="align-middle">
                    <span v-if="todo.completed"><del>{{ todo.content }}</del></span>
                    <span v-else>{{ todo.content }}</span>
                  </td>
                  <td v-if="editingTodoId === todo.id" class="align-middle">
                    <button class="btn btn-sm bg-gradient-dark dropdown-toggle ms-2 mb-0" data-bs-toggle="dropdown" id="navbarDropdownMenuLink2" aria-expanded="false">
                      {{ editingImportanceTypeName || '중요도' }}
                    </button>
                    <ul class="dropdown-menu dropdown-menu-lg-start px-2 py-3" aria-labelledby="navbarDropdownMenuLink2" style="">
                      <li v-for="type in importanceTypes" :key="type.key">
                        <a class="dropdown-item border-radius-md" @click="selectEditType(type)">
                          {{ type.value }}
                        </a>
                      </li>
                    </ul>
                  </td>
                  <td v-else class="align-middle w-20">
                    <h6 class="mb-0">
                      <span class="ms-3 badge" :class="importanceTypeColor[todo.importanceType.key]">{{ todo.importanceType.value }}</span>
                    </h6>
                  </td>
                  <td class="align-middle w-15">
                    <span>{{ formatDate(todo.createdAt) }}</span>
                  </td>
                  <td class="align-middle w-10">
                    <a  @click="changeCompletedState(todo.id)" role="button">
                      <i v-if="todo.completed" class="material-icons text-success me-4">restart_alt</i>
                      <i v-else class="material-icons text-success me-4">check</i>
                    </a>
                    <a @click="deleteTodo(todo)" role="button"><i class="material-icons text-danger">delete</i></a>
                  </td>
                  <td class="align-middle w-1">
                    <div v-if="editingTodoId === todo.id">
                      <a @click="modifyTodo(todo.id)" role="button"><i class="material-icons text-success me-4">save</i></a>
                      <a @click="cancelEdit" role="button"><i class="material-icons text-danger">cancel</i></a>
                    </div>
                    <a v-else @click="editTodo(todo)" role="button"><i class="material-icons text-secondary">settings</i></a>
                  </td>
                </tr>
              </tbody>
              <tbody v-else>
                <tr>
                  <td colspan="5" class="text-center">할 일을 생성해 보세요!</td>
                </tr>
              </tbody>
            </table>
          </div>
          <div class="card-footer text-end p-3">
            <router-link to="/main"  class="btn bg-gradient-dark">홈으로</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted, computed } from 'vue';
import apiClient from '../../../config/authConfig';
import { useFormat } from '../../../utils/format';

const isTaskInputFocused = ref(false);
const {
    formatDate
  } = useFormat();

const importanceTypes = ref([]);
const todos = ref([]);
const totalTodoCount = ref(0);
const totalCompletedCount = ref(0);
const taskInput =  ref('');
const importanceType = ref('');
const importanceTypeName = ref('');
const editingTodoId = ref(null);
const editingTodoText = ref('');
const editingImportanceType = ref('');
const editingImportanceTypeName = ref('');

const createTodo = async () => {
  if (!createValidateForm()) {
    return;
  }

  try {
    const response = await apiClient.post('/main/todo', {
      content: taskInput.value,
      importanceType: importanceType.value,
    });

    console.log(response);
    taskInput.value = '';
    getTodos();
  } catch (error) {
    console.log('에러가 발생했습니다. ', error);
    alert('todo를 생성하는데 오류가 발생했습니다.');
  }
};

const modifyTodo = async (todoId) => {
  try {
    const response = await apiClient.put(`/main/todo/${todoId}`, {
      content: editingTodoText.value,
      importanceType: editingImportanceType.value,
    });

    console.log(response.data.result);
    
    cancelEdit();
    getTodos();
  } catch (error) {
    console.log('에러가 발생했습니다. ', error);
    alert('todo 수정하는데 오류가 발생했습니다.');
  }
};

const changeCompletedState = async (todoId) => {
  if (editingTodoId.value) {
    alert('수정 중이므로 상태 변환이 불가능합니다.');
    return;
  } 

  try {
    const response = await apiClient.put(`/main/todo/${todoId}/change_status`);

    console.log(response.data.result);
    const isCompleted = response.data.result.completed;

    if (isCompleted) {
      totalCompletedCount.value += 1;
    } else {
      totalCompletedCount.value -= 1;
    }
    
    getTodos();
  } catch (error) {
    console.log('에러가 발생했습니다. ', error);
    alert('todo 완료 상태를 변경하는데 오류가 발생했습니다.');
  }
};

const deleteTodo = async (todo) => {
  if (editingTodoId.value) {
    alert('수정 중이므로 삭제가 불가능합니다.');
    return;
  } 

  try {
    const response = await apiClient.delete(`/main/todo/${todo.id}`);
    
    if (todo.completed) {
      totalCompletedCount.value -= 1;
    }

    totalTodoCount.value -= 1;

    console.log(response);
    
    getTodos();
  } catch (error) {
    console.log('에러가 발생했습니다. ', error);
    alert('todo를 삭제하는데 오류가 발생했습니다.');
  }
};

const getTodos = async () => {
  try {
    const response = await apiClient.get('/main/todo');
    
    todos.value = response.data.result;
    totalTodoCount.value = todos.value.length;
    
    console.log('todos data: ', todos.value)
    console.log('todos length: ', totalTodoCount.value)

  } catch (error) {
    console.log('에러가 발생했습니다. ', error);
    alert('todo 목록을 가져오는데 오류가 발생했습니다.');
  }
};

const getImportanceTypes = async () => {
  try {
    const response = await apiClient.get('/main/todo/importance_types');

    importanceTypes.value = response.data.result;

    console.log('importance types data: ', importanceTypes.value);

    
  } catch (error) {
    console.log('에러가 발생했습니다. ', error);
    alert('중요도 목록을 가져오는데 오류가 발생했습니다.');
  }
};


const getTotalCompletedCount = async () => {
  try {
    const response = await apiClient.get('/main/todo/totalCompletedCount');

    totalCompletedCount.value = response.data.result;

    console.log('total completed count data: ', totalCompletedCount.value);

  } catch (error) {
    console.log('에러가 발생했습니다. ', error);
    alert('todo 완료 총 갯수를 가져오는데 오류가 발생했습니다.');
  }
};

const percentage = computed(() => {
  // 할 일이 없을 경우(0일 경우) 대비하여 0으로 설정
  return totalTodoCount.value > 0 ? Math.round((totalCompletedCount.value / totalTodoCount.value) * 100) : 0;
});

const editTodo = (todo) => {
  editingTodoId.value = todo.id;
  editingTodoText.value = todo.content;
  editingImportanceType.value = todo.importanceType.key;
  editingImportanceTypeName.value = todo.importanceType.value;
};

const cancelEdit = () => {
  editingTodoId.value = null;
  editingTodoText.value = '';
  editingImportanceType.value = '';
  editingImportanceTypeName.value = '';
};

const selectEditType = (type) => {
  editingImportanceType.value = type.key;
  editingImportanceTypeName.value = type.value;
  console.log(editingImportanceType.value, editingImportanceTypeName.value, ' 타입');
};

const selectType = (type) => {
  importanceType.value = type.key;
  importanceTypeName.value = type.value;
};

const createValidateForm = () => {
  if (!taskInput.value) {
    alert('새로운 할 일이 입력되지 않았습니다.');
    return false;
  } 

  if (!importanceType.value) {
    alert('중요도가 입력되지 않았습니다.');
    return false;
  } 

  return true;
};

const importanceTypeColor = {
  High: 'bg-danger',
  Middle: 'bg-warning',
  Low: 'bg-success'
};

const handleTaskInputFocus = () => {
  isTaskInputFocused.value = true;
};

const handleTaskInputBlur = () => {
  isTaskInputFocused.value = false;
};

onMounted(() => {
  getImportanceTypes();
  getTotalCompletedCount();
  getTodos();
});
</script>
<style scoped>
  .todoTitle {
    font-size: 1em;
  }

  .progress {
    height: 20px;
  }

  .progress-bar {
    height: 20px;
  }
  
  .progress-container {
    display: flex;
    align-items: center;
  }

  .progress-container span {
    margin-right: 10px; /* 텍스트와 프로그레스 바 사이의 간격 */
    font-weight: bold;
  }

  .progress {
    flex-grow: 1;
  }

  .dropdown-menu {
    box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.3);
  }
</style>