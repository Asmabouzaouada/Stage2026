FROM node:20-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
ARG API_URL=http://localhost:8080/api
RUN sed -i "s|API_URL_PLACEHOLDER|${API_URL}|g" src/environments/environment.prod.ts
RUN npm run build -- --configuration=production

FROM nginx:alpine
COPY --from=builder /app/dist/smart-rh-front/browser /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]