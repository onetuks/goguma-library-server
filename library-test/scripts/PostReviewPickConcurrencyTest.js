import http from "k6/http";
import {check} from "k6";

export let options = {
  vus: 100,
  iterations: 100,
};

// 토큰 발급을 위한 함수
function getToken() {
  const authUrl = 'http://localhost:8080/api/test/login';
  const authRes = http.post(authUrl);
  return JSON.parse(authRes.body).appToken;
}

export default function() {
  const reviewId = 4;

  const url = `http://localhost:8080/api/reviews/${reviewId}/picks`;
  const params = {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${getToken()}`,
    }
  }

  const res = http.post(url, null, params);
  check(res, {
    "status is 201": (r) => r.status === 201
  });
}