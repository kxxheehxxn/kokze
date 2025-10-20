<template>
  <div class="quiz-card">
    <h2 class="quiz-title">!?퀴즈!?</h2>
    <div class="quiz-image-container">
      <img src="@/assets/images/quizImage.svg" alt="퀴즈 이미지" class="quiz-image" />
    </div>
  </div>
</template>
<script setup></script>
<style scoped>
.quiz-card {
  background: #fff6db;
  border-radius: 20px;
  width: 100%;
  height: 100%;
  text-align: center;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}
.quiz-title {
  font-size: 28px;
  font-weight: 700;
  color: #1e293b;
  margin-top: 15%;
  line-height: 1.3;
}
.quiz-image-container {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}
.quiz-image {
  width: 120px;
  height: auto;
  background-color: transparent;
}
@media (max-width: 1024px) {
  .quiz-card {
    padding: 20px;
  }
  .quiz-title {
    font-size: 24px;
  }
  .quiz-image {
    width: 100px;
  }
}
@media (max-width: 768px) {
  .quiz-card {
    padding: 15px;
  }
  .quiz-title {
    font-size: 20px;
  }
  .quiz-image {
    width: 80px;
  }
}
</style>
