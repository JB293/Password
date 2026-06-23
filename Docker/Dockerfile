FROM node:22-alpine

RUN npm install -g zxcvbn

WORKDIR /app

COPY Docker/check-password.sh .

RUN chmod +x check-password.sh

ENTRYPOINT ["/app/check-password.sh"]
