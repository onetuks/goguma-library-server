import http from "k6/http";

export let options = {
  vus: 1,
  duration: '30s',
};

export default function() {
  let getUrl = "http://localhost:8080/reviews/1/picks"
  http.post(getUrl);
}