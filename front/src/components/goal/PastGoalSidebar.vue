<template>
  <div class="sidebar-wrapper">
    <div class="overlay" @click="$emit('close')"></div>
    <div class="sidebar">
      <div class="sidebar-header">
        <h3>✨ {{ userName }}님의 지난 목표 리스트 ✨</h3>
      </div>
      <div class="past-goals">
        <div v-if="pastGoals.length === 0" class="empty-message">
          지난 목표가 없습니다.
        </div>

        <div
          v-for="(goal, index) in pastGoals"
          :key="index"
          class="past-goal-card"
        >
          <div class="title" :title="goal.title">
            <span class="label">목표 : </span>
            <span class="text">{{ goal.title }}</span>
          </div>
          <hr />
          <div class="period">
            {{ formatDate(goal.startDate) }} ~<br />
            {{ formatDate(goal.endDate) }}<br />
            ( 약 {{ getPeriodDiff(goal.startDate, goal.endDate) }} )
          </div>
          <div class="amount">
            {{ formatAmount(goal.targetAmount) }}
          </div>

          <div :class="['status', goal.success ? 'success' : 'fail']">
            {{ goal.success ? '성공' : '실패' }}
          </div>
          <button
            class="btn-reward"
            :class="rewardVariant(goal)"            
            :disabled="isRewardDisabled(goal)"      
            :aria-disabled="isRewardDisabled(goal)"
            @click="claim(goal)"
          >
            {{ rewardLabel(goal) }}
          </button>
          <button class="btn btn-danger" @click="handleDeleteGoal(goal.goalId)">
            삭제
          </button>
        </div>
      </div>
      <div class="close-bottom-wrapper">
        <button class="close-btn" @click="$emit('close')">닫기 ✕</button>
      </div>
    </div>
  </div>
  <BaseModal
    :visible="modalVisible"
    :message="modalMessage"
    :buttons="modalButtons"
  />
</template>
<script>
import { fetchPastGoals, deleteGoalById, claimGoalReward, isGoalRewarded } from '@/api/goalApi';
import { userAuthStore } from '@/stores/auth';
import BaseModal from '@/components/BaseModal.vue';
export default {
  name: 'PastGoalSidebar',
  components: { BaseModal },
  data() {
    const auth = userAuthStore();

    return {
      pastGoals: [],
      userName: auth.state.user.userName || '김콕재',
      userId: auth.state.user.userId,
      modalVisible: false,
      modalMessage: '',
      modalButtons: [],
      claimingMap: {},
    };
  },
  methods: {
    async loadPastGoals() {
      try {
        const goals = await fetchPastGoals(this.userId);
        this.pastGoals = goals.map(g => ({ ...g, rewarded: false }));
        await this.checkRewardedStatuses();
      } catch (e) {
        console.error('지난 목표 조회 실패:', e);
      }
    },
    showModal(message, buttons) {
      this.modalMessage = message;
      this.modalButtons = buttons;
      this.modalVisible = true;
    },
    async handleDeleteGoal(goalId) {
      this.showModal('정말로 이 목표를 삭제하시겠습니까?', [
        { text: '취소', onClick: () => (this.modalVisible = false) },
        {
          text: '삭제',
          onClick: async () => {
            this.modalVisible = false;
            const auth = userAuthStore();
            const token = auth.getToken();
            try {
              await deleteGoalById(goalId, token);
              this.showModal('목표가 삭제되었습니다.', [
                { text: '확인', onClick: () => (this.modalVisible = false) },
              ]);
              this.loadPastGoals();
            } catch (error) {
              console.error('Failed to delete goal:', error);
              this.showModal('삭제 중 오류가 발생했습니다.', [
                { text: '확인', onClick: () => (this.modalVisible = false) },
              ]);
            }
          },
        },
      ]);
    },

    async checkRewardedStatuses() {
      const auth = userAuthStore();
      const token = auth.getToken();
      await Promise.all(
        this.pastGoals
          .filter(g => g.success)
          .map(async g => {
            try {
              const res = await isGoalRewarded(g.goalId, this.userId, token);
              g.rewarded = !!res.data;
            } catch (e) {
              console.warn('보상 지급 여부 확인 실패:', g.goalId, e);
            }
          })
      );
    },

    isRewardDisabled(goal) {
      return !goal.success || goal.rewarded || this.claimingMap[goal.goalId];
    },
    rewardVariant(goal) {
      if (this.claimingMap[goal.goalId]) return 'loading';
      if (goal.rewarded) return 'done';                    
      if (!goal.success) return 'blocked';                
      return 'ready';                                      
    },
    rewardLabel(goal) {
      if (this.claimingMap[goal.goalId]) return '지급 중...';
      if (goal.rewarded) return '보상 지급완료';
      if (!goal.success) return '보상 지급불가';
      return '보상 받기';
    },

    async claim(goal) {
      if (!goal.success || goal.rewarded || this.claimingMap[goal.goalId]) return;
      this.claimingMap[goal.goalId] = true;

      const auth = userAuthStore();
      const token = auth.getToken();
      try {
        const res = await claimGoalReward(goal.goalId, this.userId, token);
        const { status, points } = res.data || {};
        if (status === 'REWARDED') {
          goal.rewarded = true;
          this.showModal(`축하해요! ${Number(points).toLocaleString()}P 지급 완료 🎉`, [
            { text: '확인', onClick: () => (this.modalVisible = false) },
          ]);
        } else if (status === 'ALREADY_REWARDED') {
          goal.rewarded = true;
          this.showModal('이미 지급된 보상입니다.', [
            { text: '확인', onClick: () => (this.modalVisible = false) },
          ]);
        } else {
          this.showModal('보상 조건을 충족하지 않았어요.', [
            { text: '확인', onClick: () => (this.modalVisible = false) },
          ]);
        }
      } catch (e) {
        console.error('claimGoalReward failed', e);
        this.showModal('보상 지급 중 오류가 발생했습니다.', [
          { text: '확인', onClick: () => (this.modalVisible = false) },
        ]);
      } finally {
        this.claimingMap[goal.goalId] = false;
      }
    },

    toDate(val) {
      if (!val) return null;
      if (val instanceof Date) return val;
      if (Array.isArray(val) && val.length === 3) {
        const [y, m, d] = val;
        return new Date(Number(y), Number(m) - 1, Number(d));
      }
      if (typeof val === 'string') {
        const parts = val.split(/[-/.]/);
        if (parts.length >= 3) {
          return new Date(Number(parts[0]), Number(parts[1]) - 1, Number(parts[2]));
        }
        const d = new Date(val);
        return isNaN(d) ? null : d;
      }
      return null;
    },

    formatDate(input) {
      const d = this.toDate(input);
      if (!d) return '';
      const y = d.getFullYear();
      const m = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      return `${y}년 ${m}월 ${day}일`;
    },
    formatAmount(amount) {
      return `${amount.toLocaleString()}원`;
    },
    getPeriodDiff(start, end) {
      const s = this.toDate(start);
      const e = this.toDate(end);
      if (!s || !e) return '';
      let diffMonths =
        (e.getFullYear() - s.getFullYear()) * 12 +
        (e.getMonth() - s.getMonth());
      if (e.getDate() > s.getDate()) diffMonths += 1;
      return diffMonths < 24
        ? `${diffMonths}개월`
        : `${Math.round(diffMonths / 12)}년`;
    },
  },
  mounted() {
    this.loadPastGoals();
  },
};
</script>
<style scoped>
.sidebar-wrapper {
  position: fixed;
  top: 0;
  right: 0;
  height: 100vh;
  display: flex;
  justify-content: flex-end;
  pointer-events: none;
  z-index: 1000;
}
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(1px);
  pointer-events: auto;
  z-index: 999;
}
.sidebar {
  width: 500px;
  height: 100%;
  padding: 1.5rem 1rem 4rem;
  background: #f9f9f9;
  border-radius: 12px;
  box-shadow: -3px 0 6px rgba(0, 0, 0, 0.1);
  overflow-y: auto;
  z-index: 1001;
  pointer-events: auto;
  display: flex;
  flex-direction: column;
}
.sidebar-header {
  margin-bottom: 1rem;
  text-align: center;
  font-size: 1.3rem;
  font-weight: bold;
}
.past-goals {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 1rem;
}

.empty-message {
  grid-column: span 2;
  text-align: center;
  color: #999;
  margin-top: 2rem;
}

.past-goal-card {
  width: 100%;
  background: #fff;
  border: 1px solid #a2c3ff;
  border-radius: 12px;
  padding: 1.5rem;
  font-size: 1rem;
  box-shadow: 0 0 6px rgba(0, 120, 255, 0.15);
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}
.title {
  display: flex;
  align-items: center;
  gap: 0.3rem;
}

.label {
  flex-shrink: 0;
  font-weight: bold;
}

.text {
  min-width: 0;
  flex-grow: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.period,
.amount {
  text-align: left;
}
.period {
  margin: 0.5rem 0;
  line-height: 1.4;
}

.amount {
  margin-bottom: 0.3rem;
}

.status {
  font-weight: bold;
  text-align: right;
}

.success {
  color: green;
}

.fail {
  color: red;
  font-weight: bold;
  font-size: 1.1rem;
}

.btn {
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
  border-radius: 18px;
}

.close-bottom-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: auto;
  padding-top: 1.5rem;
}

.close-btn {
  background: #666;
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 18px;
  border: none;
  font-size: 0.85rem;
  font-weight: bold;
  cursor: pointer;
}

.past-goal-card .btn-reward {
  width: 100%;
  height: 44px;
  border-radius: 18px;
  font-weight: 700;
  border: 0;
  transition: filter .15s ease, transform .06s ease, box-shadow .12s ease;
}

.past-goal-card .btn-reward.ready {
  background: #3573ee;
  color: #fff;
  box-shadow: 0 2px 6px rgba(53, 115, 238, 0.28);
}
.past-goal-card .btn-reward.ready:hover {
  filter: brightness(1.05);
  transform: translateY(-1px);
}

.past-goal-card .btn-reward.blocked {
  background: #e9ecef;
  color: #9aa0a6;
  box-shadow: none;
  cursor: not-allowed;
}

.past-goal-card .btn-reward.done {
  background: #dbeafe;
  color: #1d4ed8;
  box-shadow: 0 2px 6px rgba(29, 78, 216, 0.18);
}

.past-goal-card .btn-reward.loading {
  background: #eef2ff;
  color: #4f46e5;
  cursor: progress;
  box-shadow: none;
}

.past-goal-card .btn-reward:disabled {
  pointer-events: none;
}

</style>