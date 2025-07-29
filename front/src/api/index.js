import axios from 'axios';
// import { useAuthStore } from '@/stores/auth';
import router from '@/router';

const instance = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 10000,
    withCredentials: false, // CORS 요청 시 credentials 포함
});

// 토큰 갱신 함수
const refreshToken = async () => {
    try {
        const auth = localStorage.getItem('auth');
        if (auth) {
            const authData = JSON.parse(auth);
            if (authData.token) {
                const response = await axios.post('http://localhost:8080/api/auth/refresh-token', {}, {
                    headers: {
                        'Authorization': `Bearer ${authData.token}`
                    }
                });
                
                if (response.data.success) {
                    // 새로운 토큰으로 업데이트
                    authData.token = response.data.token;
                    localStorage.setItem('auth', JSON.stringify(authData));
                    return response.data.token;
                }
            }
        }
        return null;
    } catch (error) {
        console.error('토큰 갱신 실패:', error);
        return null;
    }
};

// 요청 인터셉터 - JWT 토큰 자동 추가
instance.interceptors.request.use(
    (config) => {
        const auth = localStorage.getItem('auth');
        if (auth) {
            const authData = JSON.parse(auth);
            if (authData.token) {
                config.headers.Authorization = `Bearer ${authData.token}`;
            }
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 응답 인터셉터 - 토큰 만료 처리 및 자동 갱신
instance.interceptors.response.use(
    (response) => {
        return response;
    },
    async (error) => {
        const originalRequest = error.config;
        
        if (error.response && error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;
            
            // 토큰 갱신 시도
            const newToken = await refreshToken();
            
            if (newToken) {
                // 새로운 토큰으로 재요청
                originalRequest.headers.Authorization = `Bearer ${newToken}`;
                return instance(originalRequest);
            } else {
                // 토큰 갱신 실패 시 로그아웃
                localStorage.removeItem('auth');
                window.location.href = '/auth/login';
            }
        }
        
        return Promise.reject(error);
    }
);

export default instance;
