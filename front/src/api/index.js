import axios from 'axios';
import router from '@/router';

const instance = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 10000,
    withCredentials: false,
});

const refreshToken = async () => {
    try {
        const auth = localStorage.getItem('auth');
        if (auth) {
            const authData = JSON.parse(auth);
            if (authData.token) {
                const response = await axios.post(
                    'http://localhost:8080/api/auth/refresh-token',
                    {},
                    {
                        headers: {
                            Authorization: `Bearer ${authData.token}`,
                        },
                    }
                );

                if (response.data.success) {
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

instance.interceptors.response.use(
    (response) => {
        return response;
    },
    async (error) => {
        const originalRequest = error.config;

        if (
            error.response &&
            error.response.status === 401 &&
            !originalRequest._retry
        ) {
            originalRequest._retry = true;

            const newToken = await refreshToken();

            if (newToken) {
                originalRequest.headers.Authorization = `Bearer ${newToken}`;
                return instance(originalRequest);
            } else {
                localStorage.removeItem('auth');
                window.location.href = '/auth/login';
            }
        }

        return Promise.reject(error);
    }
);

export default instance;
