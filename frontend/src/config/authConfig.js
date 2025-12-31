import axios from "axios";
import { useAuthStore } from "../store/authStore";
import router from "../router/router";

const apiClient = axios.create({
  baseURL: '/api',
  withCredentials: true, // Refresh Token 쿠키 전송을 위해 필수
  headers: {
    'Content-Type': 'application/json',
  },
});

// 토큰 재발급을 위한 전역 상태 관리 변수
let isRefreshing = false;
let failedQueue = [];

/**
 *  대기열에 쌓인 요청들을 처리합니다.
 *  새 토큰을 받으면 resolve, 재발급 실패 시 reject 처리합니다.
 */
const processQueue = (error, token = null) => {
    failedQueue.forEach(prom => {
        if (error) {
            prom.reject(error);
        } else {
            // 새 토큰으로 Authorization 헤더를 업데이트하여 요청 재실행
            prom.resolve(token);
        }
    });
    failedQueue = [];
};
// 요청 인터셉터
apiClient.interceptors.request.use(
  (config) => {
    // 요청시마다 최신 토큰 상태를 가져옵니다.
    const authStore = useAuthStore();
    const token = authStore.token;

    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
        console.log(`[Axios Interceptor] JWT Token : ${token.substring(0, 10)}...`);
    } else {
        console.log('[Axios Interceptor] No JWT token');
    }
    return config;
  },
  (error) => Promise.reject(error)
);
// 응답 인터셉터 (핵심: 401 감지 및 재발급 로직 추가)
apiClient.interceptors.response.use(
    (response) => {
        // 성공 응답 로그 (2xx)
        console.log('[Axios Interceptor] Response Success:', response.status, response.config.url);
        return response;
    },
    async (error) => {
        const authStore = useAuthStore();
        const originalRequest = error.config;
        // 401 에러인지 확인 (Access Token 만료)
        // 이미 재시도한 요청이 아닌지 확인 (!originalRequest._retry)
        if (error.response?.status === 401 && !originalRequest._retry && originalRequest.url !== '/login') {
            console.log('[Axios Interceptor] 401 Unauthorized 확인. 로그인으로 리다이렉트');
            // 반복 막기위해 true로 변경
            originalRequest._retry = true;
            if (isRefreshing) {
                // 재발급 진행 중: 현재 요청을 대기열에 추가하고 기다립니다.
                return new Promise((resolve, reject) => {
                    failedQueue.push({ resolve, reject });
                }).then((token) => {
                    // 큐 처리 완료 후, 새 토큰으로 요청을 다시 보냅니다.
                    originalRequest.headers.Authorization = `Bearer ${token}`;
                    return apiClient(originalRequest);
                }).catch(err => {
                    return Promise.reject(err);
                });
            } else if (error.response?.status === 401 && originalRequest.url === '/login') {
                // 로그인 요청에서 401이 발생한 경우 (비밀번호 틀림 등)
                console.log('[Axios] 로그인 실패: 아이디 또는 비밀번호 확인 필요');
                return Promise.reject(new Error('아이디 또는 비밀번호가 일치하지 않습니다.'));
            }

            // 재발급 시작
            isRefreshing = true;

            try {
                // Refresh Token을 사용하여 재발급 요청
                // 이 요청은 Access Token이 아닌 Refresh Token(HttpOnly Cookie)만 사용합니다.
                const response = await axios.post('/reissue', {}, {
                    baseURL: '/api',
                    withCredentials: true,
                });
                // 재발급 받은 access token
                const newToken = response.data.result.accessToken;

                // Pinia Store 및 localStorage 업데이트
                // useAuthStore()에 setToken 액션/메서드가 있다고 가정합니다.
                authStore.setToken(newToken);

                // 대기열 처리 (성공)
                isRefreshing = false;
                processQueue(null, newToken);

                // 원래 요청 재시도
                originalRequest.headers.Authorization = `Bearer ${newToken}`;
                return apiClient(originalRequest);

            } catch (refreshError) {
                // Refresh Token까지 만료되어 재발급 실패
                isRefreshing = false;
                // 에러를 넣어서 reject
                processQueue(refreshError);

                alert('토큰이 만료되었습니다. 다시 로그인해 주십시오.');

                // 사용자 로그아웃 처리 및 로그인 페이지로 리다이렉트
                authStore.logout(); // 스토어의 로그아웃 액션 (토큰 제거)
                router.push('/login'); // Vue Router를 사용하여 로그인 페이지로 이동

                return Promise.reject(refreshError);
            }
        }
        // 에러 응답 로그 (4xx, 5xx)
        console.error('[Axios Interceptor] Response Error:', error);
        // 401 외의 에러는 그대로 반환
        return Promise.reject(error);
    }
);

export default apiClient;