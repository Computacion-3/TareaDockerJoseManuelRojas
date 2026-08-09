FROM node:22-alpine

WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .

# Variables inyectadas desde .env a través de docker-compose (build args)
ARG VITE_API_URL=http://localhost:8080/api
ARG VITE_WS_URL=http://localhost:8080/ws
ARG VITE_BASE=/
ENV VITE_API_URL=$VITE_API_URL \
    VITE_WS_URL=$VITE_WS_URL \
    VITE_BASE=$VITE_BASE

RUN npm run build

EXPOSE 5173

CMD ["npm", "run", "preview", "--", "--host", "0.0.0.0", "--port", "5173"]
