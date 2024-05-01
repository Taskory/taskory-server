# Taskflower

## 🌟 About the project

### 📷 Screenshots

### 🛠️ Tech Stack

Client

<img src="https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB" />

<img src="https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white" />

<img src="https://img.shields.io/badge/ts--node-3178C6?style=for-the-badge&logo=ts-node&logoColor=white"/>

<img src="https://img.shields.io/badge/Node%20js-339933?style=for-the-badge&logo=nodedotjs&logoColor=white"/>

<img src="https://img.shields.io/badge/Yarn-2C8EBB?style=for-the-badge&logo=yarn&logoColor=white"/>

<img src="https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white"/>

<img src="https://img.shields.io/badge/daisyUI-1ad1a5?style=for-the-badge&logo=daisyui&logoColor=white"/>

<img src="https://img.shields.io/badge/json-5E5C5C?style=for-the-badge&logo=json&logoColor=white"/>

<img src="https://img.shields.io/badge/eslint-3A33D1?style=for-the-badge&logo=eslint&logoColor=white"/>

Server

<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>

<img src="https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot"/>

<img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white"/>

<img src="https://img.shields.io/badge/Google_Cloud-4285F4?style=for-the-badge&logo=google-cloud&logoColor=white"/>

<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"/>

<img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white"/>

<img src="https://img.shields.io/badge/json-5E5C5C?style=for-the-badge&logo=json&logoColor=white"/>

<img src="https://img.shields.io/badge/OpenJDK-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>

<img src="https://img.shields.io/badge/SonarLint-CB2029?style=for-the-badge&logo=sonarlint&logoColor=white"/>

<img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white"/>



DB

<img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white"/>

Tool

<img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white"/>

<img src="https://img.shields.io/badge/NeoVim-%2357A143.svg?&style=for-the-badge&logo=neovim&logoColor=white"/>

<img src="https://img.shields.io/badge/WebStorm-000000?style=for-the-badge&logo=WebStorm&logoColor=white"/>



### 🎯 Features

### 🔑 Environment Variables

To run this project, you will need to add the following environment variables to your "application-secrets.yml" file.

```
spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/taskflower?useSSL=false%serverTimeZone=UTC&useLegacyDatetimeCode=false
    username: [db-username]
    password: [db-passowrd]
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: "[client id]"
            client-secret: "[client secret]"
            redirect-uri: "{baseUrl}/oauth2/code/{registrationId}"
            scope: email, profile, https://www.googleapis.com/auth/calendar

app:
  token:
    expire-ms: 86400000
    secret-key: [token secret key]
  cors:
    allowed-origins:
      http://localhost:3000,
      http://localhost:8080
  oauth2:
    jwt-set-uri: https://www.googleapis.com/oauth2/v3/certs
    userinfo-uri: https://www.googleapis.com/oauth2/v3/userinfo
    token-uri: https://www.googleapis.com/oauth2/v4/token
    authorization-uri: https://accounts.google.com/o/oauth2/v2/auth
    authorized-redirect-uris: http://localhost:3000/oauth2/redirect
```



## 🧰  Getting started

### ‼️ Prerequisites

This client project uses Yarn as package manager

```
npm install --global yarn
```

### ⚙️ Installation

Clone the project

```
git clone https://github.com/codeartitect/taskflower.git
```

Go to the project directory

```
cd taskflower
```

1. client

   Go to the client project directory

   ```
   cd client
   ```

   Install dependency

   ```
   yarn install
   ```

   Start the server

   ```
   yarn start
   ```

   

2. server

### 🏃 Running

## 👋 Contributing

## ⚠️ License

## 🤝 Contact

Seongwon Yang

- https://github.com/codeartitect
- https://codeartitect.github.io/resume
- ysw991106@gmail.com

