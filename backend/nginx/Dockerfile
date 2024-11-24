FROM nginx:alpine

# 기존 nginx 설정 파일 제거
RUN rm /etc/nginx/conf.d/default.conf

# React 빌드 파일 복사
COPY build/ /usr/share/nginx/html/

# nginx 설정 파일 복사
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]