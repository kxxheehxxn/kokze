# build
FROM node:20-bullseye-slim AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# serve
FROM nginx:1.27
COPY --from=build /app/dist /usr/share/nginx/html
# 필요 시 커스텀 nginx.conf를 이미지에 포함
# COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx","-g","daemon off;"]