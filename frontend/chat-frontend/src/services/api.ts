import axios from 'axios';
import Cookies from 'js-cookie';

const API_URL = import.meta.env.VITE_API_URL + "/api";

const api = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true,
});

// 리프레시 로직에서 제외할 '인증 예외' 경로 정의
const AUTH_WHITE_LIST = [
    '/users/login',
    '/users/register',
    '/users/social-complete',
    '/email/send',
    '/email/verify'
];


// Add a request interceptor to add the auth token to requests
api.interceptors.request.use(
    (config) => {
        // GET 요청은 CSRF 검사 안 하니까(서버 필터 로직상), 데이터 변경 요청(POST, PUT, DELETE)에만 csrf 토큰 추가
        const method = config.method?.toLowerCase();

        if (method && ['post', 'put', 'delete', 'patch'].includes(method)) {
            // 1. 쿠키에서 csrf 토큰 꺼내기
            const csrfToken = Cookies.get('XSRF-TOKEN');

            // 2. 헤더에 토큰 설정
            if (csrfToken) {
                config.headers['X-XSRF-TOKEN'] = csrfToken;
            }
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

let isRefreshing = false;
let failedQueue: any[] = [];

const processQueue = (error: any) => {
    failedQueue.forEach(prom => {
        if (error) {
            prom.reject(error);
        } else {
            prom.resolve(); // 그냥 해결만 해주면 됨
        }
    });
    failedQueue = [];
};


// Add a response interceptor to handle token refresh
api.interceptors.response.use(
    (response) => {
        return response;
    },
    async (error) => {
        const originalRequest = error.config;

        // 상단에 정의한 화이트리스트에 포함되는지 확인
        const isWhiteList = AUTH_WHITE_LIST.some(path => originalRequest.url.includes(path));

        // If error is 401 and we haven't tried to refresh yet
        if (error.response?.status === 401 && !originalRequest._retry && !isWhiteList) {
            if (isRefreshing) {
                return new Promise((resolve, reject) => {
                    failedQueue.push({ resolve, reject });
                }).then(() => {
                    return api(originalRequest);
                }).catch(err => {
                    return Promise.reject(err);
                });
            }

            originalRequest._retry = true;
            isRefreshing = true;

            try {
                const csrfToken = Cookies.get('XSRF-TOKEN'); // 쿠키 읽기

                await axios.post(`${API_URL}/users/refresh`, {}, {
                    withCredentials: true,
                    headers: {
                        'X-XSRF-TOKEN': csrfToken
                    }
                });

                // 성공하면 큐 처리하고 원래 요청 재시도
                processQueue(null);
                return api(originalRequest);

            } catch (refreshError) {
                processQueue(refreshError);
                console.error('Token refresh failed:', refreshError);

                // 리프레시 실패하면 로그인 페이지로 이동
                window.location.href = '/login';
                return Promise.reject(refreshError);
            } finally {
                isRefreshing = false;
            }
        }

        return Promise.reject(error);
    }
);

export default api;